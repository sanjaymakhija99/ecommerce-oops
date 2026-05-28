package com.ecommerce.model;

import java.time.LocalDate;

/**
 * =====================================================================
 * OOP CONCEPT: INHERITANCE — Child Class 3
 * =====================================================================
 * GroceryProduct adds expiry date, weight, and organic flag.
 * Also OVERRIDES isInStock() to also check expiry — expired products
 * should not be sold even if stock > 0.
 *
 * Real-world: BigBasket/Blinkit product listings — Amul Milk shows
 * expiry date, weight, organic certification. A laptop does not.
 * =====================================================================
 */
public class GroceryProduct extends Product {

    private LocalDate expiryDate;
    private double weightKg;
    private boolean isOrganic;
    private String brand;

    public GroceryProduct(String productId, String name, double price,
                          int stockQuantity, String sellerId,
                          String brand, LocalDate expiryDate,
                          double weightKg, boolean isOrganic) {
        super(productId, name, price, stockQuantity, sellerId);
        this.brand = brand;
        this.expiryDate = expiryDate;
        this.weightKg = weightKg;
        this.isOrganic = isOrganic;
    }

    @Override
    public String getCategory() {
        return "GROCERY";
    }

    // ---- OVERRIDING parent method with additional logic ----
    @Override
    public boolean isInStock() {
        boolean notExpired = LocalDate.now().isBefore(expiryDate);
        return super.isInStock() && notExpired;  // Stock AND not expired
    }

    @Override
    public String getProductDetails() {
        return String.format(
            "🛒 %s by %s\n" +
            "   Price    : ₹%.2f\n" +
            "   Weight   : %.2f kg\n" +
            "   Expiry   : %s\n" +
            "   Organic  : %s\n" +
            "   Rating   : ⭐ %.1f",
            getName(), brand, getPrice(), weightKg,
            expiryDate.toString(),
            isOrganic ? "Yes 🌿" : "No",
            getRating()
        );
    }

    public LocalDate getExpiryDate()    { return expiryDate; }
    public double getWeightKg()         { return weightKg; }
    public boolean isOrganic()          { return isOrganic; }
    public String getBrand()            { return brand; }
}
