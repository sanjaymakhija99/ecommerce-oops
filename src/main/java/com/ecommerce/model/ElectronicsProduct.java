package com.ecommerce.model;

/**
 * =====================================================================
 * OOP CONCEPT: INHERITANCE — Child Class 1
 * =====================================================================
 * ElectronicsProduct inherits all common Product fields (name, price,
 * stock) and ADDS electronics-specific fields: brand, warranty, specs.
 *
 * getProductDetails() is OVERRIDDEN to display tech specs.
 *
 * Real-world: Croma/Vijay Sales product listing — a MacBook Pro shows
 * RAM, processor, display specs. A shirt does not.
 * =====================================================================
 */
public class ElectronicsProduct extends Product {

    private String brand;
    private int warrantyMonths;
    private String processorType;
    private int ramGB;
    private int storageGB;

    public ElectronicsProduct(String productId, String name, double price,
                               int stockQuantity, String sellerId,
                               String brand, int warrantyMonths,
                               String processorType, int ramGB, int storageGB) {
        super(productId, name, price, stockQuantity, sellerId); // Call parent constructor
        this.brand = brand;
        this.warrantyMonths = warrantyMonths;
        this.processorType = processorType;
        this.ramGB = ramGB;
        this.storageGB = storageGB;
    }




    @Override
    public String getCategory() {
        return "ELECTRONICS";
    }

    @Override
    public String getProductDetails() {
        return String.format(
            "📱 %s by %s\n" +
            "   Price     : ₹%.2f\n" +
            "   Processor : %s\n" +
            "   RAM       : %d GB\n" +
            "   Storage   : %d GB\n" +
            "   Warranty  : %d months\n" +
            "   Rating    : ⭐ %.1f",
            getName(), brand, getPrice(), processorType, ramGB, storageGB, warrantyMonths, getRating()
        );
    }

    public String getBrand()        { return brand; }
    public int getWarrantyMonths()  { return warrantyMonths; }
}
