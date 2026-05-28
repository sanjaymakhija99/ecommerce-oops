package com.ecommerce.payment;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;

import java.util.UUID;

/**
 * =====================================================================
 * OOP CONCEPT: POLYMORPHISM — Platform Wallet Implementation
 * =====================================================================
 * Flipkart Pay/Amazon Pay wallet — instant deduction from user balance.
 * Much simpler than card/UPI but same interface.
 * =====================================================================
 */
class whatsappPayment implements PaymentGateway {

    private User user;

    public whatsappPayment(User user) {
        this.user = user;
    }

    @Override
    public String pay(Order order, double amount) {
        System.out.println("👜 Processing Wallet Payment...");
        System.out.printf ("   Wallet Balance  : ₹%.2f%n", user.getWalletBalance());
        System.out.printf ("   Amount to Deduct: ₹%.2f%n", amount);
        user.deductFromWallet(amount); // Encapsulation: validates before deducting
        String txnId = "WALLET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.println("   ✅ TXN ID : " + txnId);
        System.out.printf ("   Remaining Balance: ₹%.2f%n", user.getWalletBalance());
        return txnId;
    }

    @Override
    public boolean refund(String transactionId, double amount) {
        user.topUpWallet(amount);
        System.out.printf("↩️  Wallet Refund of ₹%.2f credited instantly for TXN: %s%n",
                amount, transactionId);
        return true;
    }

    @Override
    public String getPaymentMethodName() { return "Platform Wallet (" + user.getName() + ")"; }
}

