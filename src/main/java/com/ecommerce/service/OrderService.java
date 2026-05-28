package com.ecommerce.service;

import com.ecommerce.discount.DiscountEngine;
import com.ecommerce.discount.DiscountStrategy;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.notification.NotificationDispatcher;
import com.ecommerce.payment.PaymentFactory;
import com.ecommerce.payment.PaymentGateway;
import com.ecommerce.repository.ProductRepository;

import java.util.List;

/**
 * =====================================================================
 * OOP CONCEPT: ALL CONCEPTS TOGETHER — Service Layer
 * =====================================================================
 * OrderService is the central orchestrator. It:
 *  - Uses ENCAPSULATION: never touches internals of Order/User directly
 *  - Uses ABSTRACTION: talks to PaymentGateway interface, not UPIPayment
 *  - Uses POLYMORPHISM: NotificationDispatcher fires all channels
 *  - Uses METHOD OVERLOADING: placeOrder() has multiple signatures
 *
 * Real-world: Checkout microservice in Flipkart/Meesho — it doesn't
 * know about Razorpay internals or FCM push tokens. It just calls
 * the right interfaces.
 * =====================================================================
 */
public class OrderService {

    private final ProductRepository productRepo;
    private final NotificationDispatcher notifier;
    private final DiscountEngine discountEngine;
    private int orderCounter = 1000;

    public OrderService(ProductRepository productRepo) {
        this.productRepo = productRepo;
        this.notifier = new NotificationDispatcher();
        this.discountEngine = new DiscountEngine();
    }

    // =====================================================================
    // METHOD OVERLOADING: placeOrder() — 3 ways to place an order
    // =====================================================================

    /**
     * Overload 1: Simple order — no discount, pay by UPI
     */
    public Order placeOrder(User user, List<String> productIds,
                            List<Integer> quantities, String upiId, String address) {
        PaymentGateway pg = PaymentFactory.create("UPI", upiId);
        return processOrder(user, productIds, quantities, pg, null, address);
    }

    /**
     * Overload 2: Order with a discount strategy
     */
    public Order placeOrder(User user, List<String> productIds,
                            List<Integer> quantities, PaymentGateway pg,
                            DiscountStrategy discount, String address) {
        return processOrder(user, productIds, quantities, pg, discount, address);
    }

    /**
     * Overload 3: Order with a coupon code (flat discount)
     */
    public Order placeOrder(User user, List<String> productIds,
                            List<Integer> quantities, PaymentGateway pg,
                            String couponCode, double discountAmount, String address) {
        DiscountStrategy discount = DiscountEngine.flatDiscount(couponCode, discountAmount);
        return processOrder(user, productIds, quantities, pg, discount, address);
    }

    // ---- Core processing logic (private) ----

    private Order processOrder(User user, List<String> productIds,
                                List<Integer> quantities, PaymentGateway pg,
                                DiscountStrategy discount, String address) {

        String orderId = "ORD-" + (++orderCounter);
        Order order = new Order(orderId, user, address);




        // Apply discount if present (Polymorphism — any DiscountStrategy works)
        if (discount != null) {
            double discountAmt = discountEngine.calculate(order, user, discount);
            order.applyDiscount(discountAmt);
        }

        // Process payment (Abstraction — same call regardless of UPI/Card/Wallet)
        System.out.println("\n💳 Payment via: " + pg.getPaymentMethodName());
        String txnId = pg.pay(order, order.getTotalAmount());
        order.setPaymentMethod(pg.getPaymentMethodName());

        // Award loyalty points (1 point per ₹10 spent)
        int pointsEarned = (int) (order.getTotalAmount() / 10);
        user.earnPoints(pointsEarned);
        System.out.printf("⭐ Earned %d loyalty points!%n", pointsEarned);

        // Confirm order
        order.confirm();

        // Print receipt
        order.printReceipt();

        // Notify via all channels (Polymorphism — NotificationDispatcher)
        notifier.dispatch(user, order, "Your order has been confirmed! 🎉 Track: flipkart.com/track/" + orderId);

        return order;
    }

    // Simulate order lifecycle progression
    public void simulateDelivery(Order order, User user) {
        System.out.println("\n🚚 Simulating delivery lifecycle for Order: " + order.getOrderId());

        order.ship();
        notifier.dispatch(user, order, "Your order has been shipped! Expected delivery in 3-5 days.");

        order.outForDelivery();
        notifier.dispatch(user, order, "Out for delivery! Your delivery partner is on the way. 🛵");

        order.deliver();
        notifier.dispatch(user, order, "Order delivered! Hope you love it. Rate your experience ⭐");
    }
}
