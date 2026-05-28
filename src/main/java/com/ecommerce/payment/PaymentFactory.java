package com.ecommerce.payment;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import java.util.UUID;

/**
 * =====================================================================
 * OOP CONCEPT: POLYMORPHISM — UPI Implementation
 * =====================================================================
 * UPI payment via PhonePe/GPay — internal logic completely different
 * from Card or Wallet, but the caller sees the same interface.
 * =====================================================================
 */
class UPIPayment implements PaymentGateway {

    private String upiId;

    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public String pay(Order order, double amount) {
        System.out.println("📲 Initiating UPI Payment...");
        System.out.println("   UPI ID   : " + upiId);
        System.out.printf ("   Amount   : ₹%.2f%n", amount);
        System.out.println("   Gateway  : PhonePe/NPCI");
        // In reality: call Razorpay UPI API, await callback, verify VPA
        String txnId = "UPI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.println("   ✅ TXN ID : " + txnId);
        return txnId;
    }

    @Override
    public boolean refund(String transactionId, double amount) {
        System.out.printf("↩️  UPI Refund of ₹%.2f initiated for TXN: %s%n", amount, transactionId);
        return true;
    }

    @Override
    public String getPaymentMethodName() { return "UPI (" + upiId + ")"; }
}

/**
 * =====================================================================
 * OOP CONCEPT: POLYMORPHISM — Credit/Debit Card Implementation
 * =====================================================================
 * Card payment via Visa/Mastercard — involves tokenization, CVV check,
 * bank authorization. All hidden from the caller.
 * =====================================================================
 */
class CardPayment implements PaymentGateway {

    private String maskedCardNumber;
    private String cardHolderName;
    private String cardNetwork; // VISA, MASTERCARD, RUPAY

    public CardPayment(String cardNumber, String cardHolderName) {
        // Mask the card — show only last 4 digits (PCI compliance)
        this.maskedCardNumber = "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
        this.cardHolderName = cardHolderName;
        this.cardNetwork = cardNumber.startsWith("4") ? "VISA" : "MASTERCARD";
    }

    @Override
    public String pay(Order order, double amount) {
        System.out.println("💳 Processing Card Payment...");
        System.out.println("   Card     : " + maskedCardNumber + " (" + cardNetwork + ")");
        System.out.println("   Holder   : " + cardHolderName);
        System.out.printf ("   Amount   : ₹%.2f%n", amount);
        System.out.println("   3D Secure: OTP sent to registered mobile");
        // In reality: tokenize card, call issuing bank API, handle 3DS flow
        String txnId = "CARD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.println("   ✅ TXN ID : " + txnId);
        return txnId;
    }

    @Override
    public boolean refund(String transactionId, double amount) {
        System.out.printf("↩️  Card Refund of ₹%.2f initiated for TXN: %s (3-5 business days)%n",
                amount, transactionId);
        return true;
    }

    @Override
    public String getPaymentMethodName() { return cardNetwork + " Card (" + maskedCardNumber + ")"; }
}

/**
 * =====================================================================
 * OOP CONCEPT: POLYMORPHISM — Platform Wallet Implementation
 * =====================================================================
 * Flipkart Pay/Amazon Pay wallet — instant deduction from user balance.
 * Much simpler than card/UPI but same interface.
 * =====================================================================
 */
class WalletPayment implements PaymentGateway {

    private User user;

    public WalletPayment(User user) {
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

/**
 * =====================================================================
 * OOP CONCEPT: ABSTRACTION (Factory)
 * =====================================================================
 * PaymentFactory hides which class gets instantiated.
 * Callers just ask for "UPI" or "CARD" — they never use 'new' directly.
 *
 * Real-world: Checkout page — user selects "Pay with UPI", app internally
 * routes to the right gateway class without the UI layer knowing.
 * =====================================================================
 */
public class PaymentFactory {

    public static PaymentGateway create(String method, Object... args) {
        return switch (method.toUpperCase()) {
            case "UPI"    -> new UPIPayment((String) args[0]);
            case "CARD"   -> new CardPayment((String) args[0], (String) args[1]);
            case "WALLET" -> new WalletPayment((User) args[0]);
            default       -> throw new IllegalArgumentException("Unknown payment method: " + method);
        };
    }
}
