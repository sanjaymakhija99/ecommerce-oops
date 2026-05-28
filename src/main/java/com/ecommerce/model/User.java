package com.ecommerce.model;

/**
 * =====================================================================
 * OOP CONCEPT: ENCAPSULATION
 * =====================================================================
 * All fields are private. External code CANNOT directly touch balance,
 * loyaltyPoints, or email. They must go through controlled methods.
 *
 * Real-world: Flipkart/Amazon user profiles — balance and reward points
 * are never exposed directly. Topped up or deducted via dedicated methods
 * that enforce validation and audit trails.
 * =====================================================================
 */
public class User {

    private String userId;
    private String name;
    private String email;           // Validated on set — no garbage emails
    private String phone;
    private double walletBalance;   // Never set directly — only topUp/deduct
    private int loyaltyPoints;      // Earned/redeemed through methods only

    public User(String userId, String name, String email, String phone) {
        this.userId = userId;
        this.name = name;
        setEmail(email);            // Validation happens here
        this.phone = phone;
        this.walletBalance = 0.0;
        this.loyaltyPoints = 0;
    }

    // ---- Wallet: controlled access ----

    public void topUpWallet(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Top-up amount must be positive.");
        this.walletBalance += amount;
        System.out.println("✅ Wallet topped up by ₹" + amount + ". New balance: ₹" + walletBalance);
    }

    public void deductFromWallet(double amount) {
        if (amount > walletBalance)
            throw new IllegalStateException("Insufficient wallet balance.");
        this.walletBalance -= amount;
    }

    public double getWalletBalance() { return walletBalance; }

    // ---- Loyalty Points: controlled access ----

    public void earnPoints(int points) {
        if (points > 0) this.loyaltyPoints += points;
    }

    public void redeemPoints(int points) {
        if (points > loyaltyPoints)
            throw new IllegalStateException("Not enough loyalty points.");
        this.loyaltyPoints -= points;
    }

    public int getLoyaltyPoints() { return loyaltyPoints; }

    // ---- Email: validated setter ----

    public void setEmail(String email) {
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email: " + email);
        this.email = email;
    }

    // ---- Standard Getters ----

    public String getUserId()   { return userId; }
    public String getName()     { return name; }
    public String getEmail()    { return email; }
    public String getPhone()    { return phone; }

    @Override
    public String toString() {
        return String.format("User[%s | %s | Wallet: ₹%.2f | Points: %d]",
                userId, name, walletBalance, loyaltyPoints);
    }
}
