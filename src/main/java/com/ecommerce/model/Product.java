package com.ecommerce.model;

import com.ecommerce.exception.NegativePriceException;

/**
 * =====================================================================
 * OOP CONCEPT: INHERITANCE (Base Class)
 * =====================================================================
 * Product is the parent class. All product types on an e-commerce
 * platform share: id, name, price, stock, seller.
 *
 * Child classes (ElectronicsProduct, FashionProduct, GroceryProduct)
 * inherit this common structure and EXTEND with their own specific fields.
 *
 * Real-world: Amazon's product catalog — every item has a title, price,
 * and stock count, but a Laptop has RAM/storage specs that a T-Shirt
 * doesn't. Inheritance models this cleanly.
 * =====================================================================
 */
public abstract class Product {

    private String productId;
    private String name;
    private double price;
    private int stockQuantity;
    private String sellerId;
    private double rating;

    public Product(String productId, String name, double price, int stockQuantity, String sellerId) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.sellerId = sellerId;
        this.rating = 0.0;
    }

    protected Product() {
    }

    // ---- Stock Management (shared by all product types) ----

    public void reduceStock(int quantity) {
        if (quantity > stockQuantity)
            throw new IllegalStateException("Insufficient stock for: " + name);
        this.stockQuantity -= quantity;
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    // ---- Abstract method: each product type describes itself differently ----

    public abstract String getProductDetails();

    public abstract String getCategory();

    // ---- Getters / Setters ----

    public String getProductId()    { return productId; }
    public String getName()         { return name; }
    public double getPrice()        { return price; }

    public void setPrice(double p)  {
       validatePrice(p);
        this.price = p;
    }
    public int getStockQuantity()   { return stockQuantity; }
    public String getSellerId()     { return sellerId; }
    public double getRating()       { return rating; }
    public void setRating(double r) { this.rating = r; }

    @Override
    public String toString() {
        return String.format("[%s] %s | ₹%.2f | Stock: %d | ⭐ %.1f",
                getCategory(), name, price, stockQuantity, rating);
    }

    private  static  void validatePrice(double p){
        if(p < 0) {
            throw new NegativePriceException("price should be grater than 0");
        }
    }
}
