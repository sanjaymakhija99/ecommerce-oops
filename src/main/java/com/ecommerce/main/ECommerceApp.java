package com.ecommerce.main;

import com.ecommerce.discount.DiscountEngine;
import com.ecommerce.model.*;
import com.ecommerce.payment.PaymentFactory;
import com.ecommerce.payment.PaymentGateway;
import com.ecommerce.repository.InMemoryProductRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.OrderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * =====================================================================
 *  🛍️  E-COMMERCE OOP DEMO — 2026
 * =====================================================================
 *  Concepts demonstrated:
 *
 *  1. ENCAPSULATION    → User (wallet/points), Order (state machine)
 *  2. INHERITANCE      → Product → ElectronicsProduct, FashionProduct,
 *                                  GroceryProduct
 *  3. ABSTRACTION      → PaymentGateway (interface), ProductRepository
 *                        (interface), DiscountStrategy (interface)
 *  4. POLYMORPHISM     → 4 payment gateways, 4 notification channels,
 *                        3 discount types — same method, diff behavior
 *  5. METHOD OVERLOADING → DiscountEngine.calculate(), OrderService.placeOrder(),
 *                          ProductRepository.search()
 *
 *  Real-world context: A simplified Flipkart/Amazon India checkout flow
 * =====================================================================
 */
public class ECommerceApp {

    public static void main(String[] args) {


        String header = "my name is ";
        String name = "sanjay";



        System.out.println("-->" + header + name);






        printBanner();

        // ----------------------------------------------------------------
        // SETUP: Repository and Services
        // ----------------------------------------------------------------
        ProductRepository productRepo = new InMemoryProductRepository();
        OrderService orderService = new OrderService(productRepo);

        // ----------------------------------------------------------------
        // STEP 1: Seed Products (INHERITANCE in action)
        // ----------------------------------------------------------------
        printSection("STEP 1: Product Catalog (Inheritance Demo)");

        // Electronics — child of Product
        ElectronicsProduct macbook = new ElectronicsProduct(
            "P001", "Apple MacBook Air M3", 114900.0, 15, "SELLER-APPLE",
            "Apple", 12, "Apple M3 Chip", 16, 512
        );
        macbook.setRating(4.8);

        ElectronicsProduct samsung = new ElectronicsProduct(
            "P002", "Samsung Galaxy S25 Ultra", 134999.0, 30, "SELLER-SAMSUNG",
            "Samsung", 12, "Snapdragon 8 Elite", 12, 256
        );
        samsung.setRating(4.7);

        // Fashion — child of Product
        FashionProduct levis = new FashionProduct(
            "P003", "Levi's 511 Slim Fit Jeans", 3499.0, 100, "SELLER-LEVIS",
            "Levi's", "32", "Blue", "Denim", "Men"
        );
        levis.setRating(4.5);

        FashionProduct zara = new FashionProduct(
            "P004", "Zara Women's Blazer", 5990.0, 50, "SELLER-ZARA",
            "Zara", "M", "Black", "Polyester", "Women"
        );
        zara.setRating(4.3);

        // Grocery — child of Product (overrides isInStock() to check expiry too)
        GroceryProduct milk = new GroceryProduct(
            "P005", "Amul Full Cream Milk 1L", 68.0, 200, "SELLER-AMUL",
            "Amul", LocalDate.now().plusDays(5), 1.0, false
        );

        GroceryProduct toor = new GroceryProduct(
            "P006", "Organic Toor Dal 1kg", 189.0, 80, "SELLER-ORGANIC",
            "24 Mantra Organic", LocalDate.now().plusDays(365), 1.0, true
        );
        toor.setRating(4.6);






        /*// Save all products
        List.of(macbook, samsung, levis, zara, milk, toor)
            .forEach(productRepo::save);*/

        // Display product details (POLYMORPHISM: getProductDetails() on each type)
        System.out.println("\n📋 POLYMORPHISM DEMO — getProductDetails() on different types:\n");
        System.out.println(macbook.getProductDetails());
        System.out.println();
        System.out.println(levis.getProductDetails());
        System.out.println();
        System.out.println(toor.getProductDetails());

        // ----------------------------------------------------------------
        // STEP 2: User Setup (ENCAPSULATION demo)
        // ----------------------------------------------------------------
        printSection("STEP 2: User Setup (Encapsulation Demo)");

        User sanjay = new User("U001", "Sanjay Sharma", "sanjay@gmail.com", "+91-9876543210");
        sanjay.topUpWallet(5000.0);     // Controlled — validates positive amount
        sanjay.earnPoints(500);          // Points only via earnPoints()
        System.out.println("👤 " + sanjay);

        User priya = new User("U002", "Priya Mehta", "priya@outlook.com", "+91-9123456789");
        priya.topUpWallet(2000.0);
        priya.earnPoints(200);
        System.out.println("👤 " + priya);

        // Show encapsulation violation is prevented
        System.out.println("\n🔒 Encapsulation enforced:");
        System.out.println("   Wallet balance only via getWalletBalance(): ₹" + sanjay.getWalletBalance());
        System.out.println("   Cannot set sanjay.walletBalance = 99999 directly → private field!");

        // ----------------------------------------------------------------
        // STEP 3: Product Search (METHOD OVERLOADING demo)
        // ----------------------------------------------------------------
        printSection("STEP 3: Product Search (Method Overloading Demo)");

        System.out.println("🔍 search(\"Samsung\"):");
        productRepo.search("Samsung")
                .forEach(p -> System.out.println("   " + p));

        System.out.println("\n🔍 search(\"MacBook\", \"ELECTRONICS\"):");
        productRepo.search("MacBook", "ELECTRONICS")
                .forEach(p -> System.out.println("   " + p));

        System.out.println("\n🔍 search(\"Jeans\", 5000.0) — under ₹5000:");
        productRepo.search("Jeans", 5000.0)
                .forEach(p -> System.out.println("   " + p));

        System.out.println("\n🔍 search(\"Dal\", \"GROCERY\", 250.0):");
        productRepo.search("Dal", "GROCERY", 250.0)
                .forEach(p -> System.out.println("   " + p));

        // ----------------------------------------------------------------
        // STEP 4: Place Order — UPI, no discount (Overload 1)
        // ----------------------------------------------------------------
        printSection("STEP 4: Order 1 — UPI Payment, No Discount");

        Order order1 = orderService.placeOrder(
            sanjay,
            List.of("P003", "P006"),    // Jeans + Toor Dal
            List.of(1, 2),
            "sanjay@oksbi",             // UPI ID
            "42, MG Road, Nagpur - 440001"
        );

        // ----------------------------------------------------------------
        // STEP 5: Place Order — Card + Coupon Code (Overload 3)
        // ----------------------------------------------------------------
        printSection("STEP 5: Order 2 — HDFC Card + SAVE500 Coupon (Overloading + Polymorphism)");

        PaymentGateway hdfcCard = PaymentFactory.create("CARD", "4532015112830366", "Priya Mehta");

        Order order2 = orderService.placeOrder(
            priya,
            List.of("P004"),            // Zara Blazer
            List.of(1),
            hdfcCard,
            "SAVE500", 500.0,           // Flat ₹500 coupon
            "15, Bandra West, Mumbai - 400050"
        );

        // ----------------------------------------------------------------
        // STEP 6: Place Order — Wallet + Loyalty Points + % Discount (Overload 2)
        // ----------------------------------------------------------------
        printSection("STEP 6: Order 3 — Wallet Payment + Loyalty Redemption (Polymorphism)");

        // Sanjay has 500 + points earned from order1
        System.out.println("Sanjay's current points: " + sanjay.getLoyaltyPoints());

        PaymentGateway wallet = PaymentFactory.create("WALLET", sanjay);

        Order order3 = orderService.placeOrder(
            sanjay,
            List.of("P005", "P006"),    // Milk + Dal
            List.of(3, 1),
            wallet,
            DiscountEngine.loyaltyDiscount(100),  // Redeem 100 points
            "42, MG Road, Nagpur - 440001"
        );

        // ----------------------------------------------------------------
        // STEP 7: Simulate Delivery Lifecycle
        // ----------------------------------------------------------------
        printSection("STEP 7: Delivery Lifecycle Simulation (Polymorphism — 4 notification channels)");

        orderService.simulateDelivery(order1, sanjay);

        // ----------------------------------------------------------------
        // STEP 8: Grocery Expiry Check (Inherited isInStock() override)
        // ----------------------------------------------------------------
        printSection("STEP 8: Grocery Expiry Check (Inheritance — Overriding isInStock())");

        GroceryProduct expiredCurd = new GroceryProduct(
            "P999", "Expired Curd 500g", 40.0, 50, "SELLER-DAIRY",
            "Mother Dairy", LocalDate.now().minusDays(1), 0.5, false
        );
        System.out.println("Expired Curd in stock? → " + expiredCurd.isInStock()
                + " (stock=" + expiredCurd.getStockQuantity() + " but expiry overrides!)");

        GroceryProduct freshPaneer = new GroceryProduct(
            "P998", "Fresh Paneer 200g", 89.0, 30, "SELLER-DAIRY",
            "Amul", LocalDate.now().plusDays(7), 0.2, false
        );
        System.out.println("Fresh Paneer in stock?  → " + freshPaneer.isInStock());

        // ----------------------------------------------------------------
        // FINAL SUMMARY
        // ----------------------------------------------------------------
        printSection("FINAL: OOP Concepts Summary");
        System.out.println("""
            ✅ ENCAPSULATION     → User.walletBalance & loyaltyPoints private; Order status via methods
            ✅ ABSTRACTION       → PaymentGateway, NotificationService, DiscountStrategy interfaces
            ✅ INHERITANCE       → Product ← ElectronicsProduct, FashionProduct, GroceryProduct
            ✅ POLYMORPHISM      → 3 payment gateways, 4 notification channels, 3 discount types
            ✅ METHOD OVERLOADING → search() × 4, placeOrder() × 3, calculate() × 5
            ✅ METHOD OVERRIDING  → getProductDetails(), getCategory(), isInStock() per product type
            """);

        System.out.println("👤 " + sanjay);
        System.out.println("👤 " + priya);
    }

    private static void printBanner() {
        System.out.println("""
            ╔══════════════════════════════════════════════════════════╗
            ║        🛍️  JAVA OOP — E-COMMERCE DEMO (2026)            ║
            ║   Flipkart / Amazon / Meesho Style Checkout Flow        ║
            ╚══════════════════════════════════════════════════════════╝
            """);
    }

    private static void printSection(String title) {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  " + title);
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }
}
