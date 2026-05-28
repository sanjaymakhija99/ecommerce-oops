package com.ecommerce.payment;

import com.ecommerce.model.Order;

/**
 * =====================================================================
 * OOP CONCEPT: ABSTRACTION + POLYMORPHISM (Interface)
 * =====================================================================
 * PaymentGateway defines the CONTRACT. All payment methods must implement
 * pay() and refund(). The OrderService doesn't care whether it's UPI,
 * Card, or Wallet — it just calls pay().
 *
 * Real-world: Razorpay/PayU integration — Swiggy, Zomato, Flipkart
 * route all payments through a single interface so they can onboard
 * new payment methods (BNPL, crypto) without changing order logic.
 * =====================================================================
 */
public interface PaymentGateway {

    /**
     * Process payment for an order.
     * @return transaction ID if successful
     */
    String pay(Order order, double amount);

    /**
     * Refund a previously made payment.
     */
    boolean refund(String transactionId, double amount);

    /**
     * Human-readable name of this payment method.
     */
    String getPaymentMethodName();
}
