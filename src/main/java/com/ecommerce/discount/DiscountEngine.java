package com.ecommerce.discount;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;

/**
 * =====================================================================
 * OOP CONCEPT: POLYMORPHISM — Multiple Discount Types
 * =====================================================================
 */

/**
 * Flat ₹ discount — e.g., SAVE200 coupon on Meesho
 */
class FlatDiscount implements DiscountStrategy {
    private double discountAmount;
    private String couponCode;

    public FlatDiscount(String couponCode, double discountAmount) {
        this.couponCode = couponCode;
        this.discountAmount = discountAmount;
    }

    @Override
    public double apply(Order order, User user) {
        if (order.getTotalAmount() < discountAmount) return 0;
        System.out.printf("🏷️  Coupon %s applied: -₹%.2f%n", couponCode, discountAmount);
        return discountAmount;
    }

    @Override
    public String getDiscountDescription() {
        return "Flat ₹" + discountAmount + " off (Code: " + couponCode + ")";
    }
}

/**
 * Percentage discount — e.g., 10% off on HDFC Bank card
 */
class PercentageDiscount implements DiscountStrategy {
    private double percentage;
    private double maxCapAmount;
    private String offerName;

    public PercentageDiscount(String offerName, double percentage, double maxCapAmount) {
        this.offerName = offerName;
        this.percentage = percentage;
        this.maxCapAmount = maxCapAmount;
    }

    @Override
    public double apply(Order order, User user) {
        double discount = order.getTotalAmount() * (percentage / 100.0);
        double finalDiscount = Math.min(discount, maxCapAmount); // Cap enforced
        System.out.printf("💰 %s: %.0f%% off = -₹%.2f (capped at ₹%.2f)%n",
                offerName, percentage, finalDiscount, maxCapAmount);
        return finalDiscount;
    }

    @Override
    public String getDiscountDescription() {
        return percentage + "% off up to ₹" + maxCapAmount + " (" + offerName + ")";
    }
}

/**
 * Loyalty points redemption — e.g., Tata Neu Coins, Amazon Pay rewards
 */
class LoyaltyPointsDiscount implements DiscountStrategy {
    private int pointsToRedeem;
    private static final double POINTS_TO_RUPEE_RATIO = 0.25; // 1 point = ₹0.25

    public LoyaltyPointsDiscount(int pointsToRedeem) {
        this.pointsToRedeem = pointsToRedeem;
    }

    @Override
    public double apply(Order order, User user) {
        if (user.getLoyaltyPoints() < pointsToRedeem) {
            System.out.println("⚠️  Not enough loyalty points. Available: " + user.getLoyaltyPoints());
            return 0;
        }
        user.redeemPoints(pointsToRedeem);
        double discount = pointsToRedeem * POINTS_TO_RUPEE_RATIO;
        System.out.printf("⭐ Redeemed %d loyalty points = -₹%.2f%n", pointsToRedeem, discount);
        return discount;
    }

    @Override
    public String getDiscountDescription() {
        return "Loyalty Points: " + pointsToRedeem + " pts = ₹" + (pointsToRedeem * POINTS_TO_RUPEE_RATIO);
    }
}

/**
 * =====================================================================
 * OOP CONCEPT: METHOD OVERLOADING
 * =====================================================================
 * DiscountEngine.calculate() is overloaded — same action, different
 * parameter sets depending on what the caller has available.
 *
 * Real-world: Checkout engine — sometimes you have a coupon code,
 * sometimes a bank offer, sometimes both. Overloading handles all cases
 * with a clean API instead of 4 differently named methods.
 * =====================================================================
 */
public class DiscountEngine {

    // Overload 1: No discount
    public double calculate(Order order, User user) {
        System.out.println("ℹ️  No discount applied.");
        return 0.0;
    }

    // Overload 2: Single discount strategy
    public double calculate(Order order, User user, DiscountStrategy strategy) {
        return strategy.apply(order, user);
    }

    // Overload 3: Flat coupon by code (factory built-in)
    public double calculate(Order order, User user, String couponCode, double flatAmount) {
        return new FlatDiscount(couponCode, flatAmount).apply(order, user);
    }

    // Overload 4: Percentage offer by name
    public double calculate(Order order, User user, String offerName,
                             double percentage, double maxCap) {
        return new PercentageDiscount(offerName, percentage, maxCap).apply(order, user);
    }

    // Overload 5: Loyalty points redemption
    public double calculate(Order order, User user, int loyaltyPointsToRedeem) {
        return new LoyaltyPointsDiscount(loyaltyPointsToRedeem).apply(order, user);
    }

    // Factory methods for discount creation
    public static DiscountStrategy flatDiscount(String code, double amount) {
        return new FlatDiscount(code, amount);
    }

    public static DiscountStrategy percentageDiscount(String name, double pct, double cap) {
        return new PercentageDiscount(name, pct, cap);
    }

    public static DiscountStrategy loyaltyDiscount(int points) {
        return new LoyaltyPointsDiscount(points);
    }
}
