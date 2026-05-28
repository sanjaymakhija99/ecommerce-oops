package com.ecommerce.discount;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;

/**
 * =====================================================================
 * OOP CONCEPT: ABSTRACTION + POLYMORPHISM (Strategy Pattern)
 * =====================================================================
 * DiscountStrategy is an interface. Each coupon/offer type implements it.
 * OrderService just calls applyDiscount() — it doesn't know if it's a
 * flat discount, percentage, or loyalty redemption.
 *
 * Real-world: Flipkart Big Billion Days / Amazon Great Indian Festival —
 * dozens of discount types (bank offers, coupons, combo deals) all
 * processed through the same apply() pipeline.
 * =====================================================================
 */
public interface DiscountStrategy {
    double apply(Order order, User user);
    String getDiscountDescription();
}
