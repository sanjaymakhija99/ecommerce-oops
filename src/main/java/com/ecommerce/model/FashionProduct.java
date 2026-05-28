package com.ecommerce.model;

/**
 * =====================================================================
 * OOP CONCEPT: INHERITANCE — Child Class 2
 * =====================================================================
 * FashionProduct inherits Product and adds fashion-specific attributes:
 * size, color, fabric, gender.
 *
 * Real-world: Myntra/AJIO product catalog — a Levis jeans listing shows
 * size chart, color options, fabric type. An iPhone listing does not.
 * =====================================================================
 */
public class FashionProduct extends Product {

    private String brand;
    private String size;        // S, M, L, XL, XXL
    private String color;
    private String fabric;      // Cotton, Polyester, Denim, etc.
    private String gender;      // Men, Women, Unisex, Kids

    public FashionProduct(String productId, String name, double price,
                          int stockQuantity, String sellerId,
                          String brand, String size, String color,
                          String fabric, String gender) {
        super(productId, name, price, stockQuantity, sellerId);
        this.brand = brand;
        this.size = size;
        this.color = color;
        this.fabric = fabric;
        this.gender = gender;
    }

    @Override
    public String getCategory() {
        return "FASHION";
    }

    @Override
    public String getProductDetails() {
        return String.format(
            "👗 %s by %s\n" +
            "   Price  : ₹%.2f\n" +
            "   Size   : %s | Color: %s\n" +
            "   Fabric : %s\n" +
            "   For    : %s\n" +
            "   Rating : ⭐ %.1f",
            getName(), brand, getPrice(), size, color, fabric, gender, getRating()
        );
    }

    public String getBrand()    { return brand; }
    public String getSize()     { return size; }
    public String getColor()    { return color; }
    public String getFabric()   { return fabric; }
    public String getGender()   { return gender; }
}
