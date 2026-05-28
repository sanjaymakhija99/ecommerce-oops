package com.ecommerce.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a line item inside an order.
 */
class OrderItem {
    private Product product;
    private int quantity;
    private double priceAtPurchase; // Price captured at time of order (may differ from current)

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.priceAtPurchase = product.getPrice(); // Snapshot price
    }

    public double getSubtotal() {
        return priceAtPurchase * quantity;
    }

    public Product getProduct()         { return product; }
    public int getQuantity()            { return quantity; }
    public double getPriceAtPurchase()  { return priceAtPurchase; }

    @Override
    public String toString() {
        return String.format("  - %-35s x%d  @ ₹%.2f  = ₹%.2f",
                product.getName(), quantity, priceAtPurchase, getSubtotal());
    }
}

/**
 * =====================================================================
 * OOP CONCEPT: ENCAPSULATION (Order State Machine)
 * =====================================================================
 * Order status is tightly controlled. You cannot jump from PLACED to
 * DELIVERED directly — state transitions are validated.
 *
 * Real-world: Meesho/Flipkart order lifecycle — PLACED → CONFIRMED →
 * SHIPPED → OUT_FOR_DELIVERY → DELIVERED (or CANCELLED/RETURNED).
 * No external code can set status = "DELIVERED" if it was never shipped.
 * =====================================================================
 */
public class Order {

    public enum OrderStatus {
        PLACED, CONFIRMED, SHIPPED, OUT_FOR_DELIVERY, DELIVERED, CANCELLED, RETURNED
    }

    private String orderId;
    private User customer;
    private List<OrderItem> items;
    private OrderStatus status;
    private double totalAmount;
    private double discountApplied;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private String deliveryAddress;

    public Order(String orderId, User customer, String deliveryAddress) {
        this.orderId = orderId;
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PLACED;
        this.discountApplied = 0.0;
        this.createdAt = LocalDateTime.now();
    }

    public void addItem(Product product, int quantity) {
        if (!product.isInStock())
            throw new IllegalStateException("Product out of stock: " + product.getName());
        items.add(new OrderItem(product, quantity));
        recalculate();
    }

    private void recalculate() {
        this.totalAmount = items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum() - discountApplied;
    }

    public void applyDiscount(double discountAmount) {
        this.discountApplied = discountAmount;
        recalculate();
    }

    // ---- Controlled status transitions ----

    public void confirm()           { transition(OrderStatus.CONFIRMED); }
    public void ship()              { transition(OrderStatus.SHIPPED); }
    public void outForDelivery()    { transition(OrderStatus.OUT_FOR_DELIVERY); }
    public void deliver()           { transition(OrderStatus.DELIVERED); }
    public void cancel()            { transition(OrderStatus.CANCELLED); }
    public void returnOrder()       { transition(OrderStatus.RETURNED); }

    private void transition(OrderStatus newStatus) {
        System.out.println("📦 Order " + orderId + ": " + status + " → " + newStatus);
        this.status = newStatus;
    }

    // ---- Getters ----

    public String getOrderId()          { return orderId; }
    public User getCustomer()           { return customer; }
    public List<OrderItem> getItems()   { return items; }
    public OrderStatus getStatus()      { return status; }
    public double getTotalAmount()      { return totalAmount; }
    public double getDiscountApplied()  { return discountApplied; }
    public String getPaymentMethod()    { return paymentMethod; }
    public void setPaymentMethod(String m) { this.paymentMethod = m; }

    public void printReceipt() {
        System.out.println("\n════════════════════════════════════════════════════");
        System.out.println("               🛍️  ORDER RECEIPT");
        System.out.println("════════════════════════════════════════════════════");
        System.out.println("Order ID     : " + orderId);
        System.out.println("Customer     : " + customer.getName());
        System.out.println("Address      : " + deliveryAddress);
        System.out.println("Date         : " + createdAt.toLocalDate());
        System.out.println("Status       : " + status);
        System.out.println("----------------------------------------------------");
        System.out.println("Items:");
        items.forEach(System.out::println);
        System.out.println("----------------------------------------------------");
        if (discountApplied > 0)
            System.out.printf("Discount     : -₹%.2f%n", discountApplied);
        System.out.printf("TOTAL        : ₹%.2f%n", totalAmount);
        System.out.println("Payment      : " + paymentMethod);
        System.out.println("════════════════════════════════════════════════════\n");
    }
}
