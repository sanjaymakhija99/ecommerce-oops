package com.ecommerce.repository;

import com.ecommerce.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * =====================================================================
 * OOP CONCEPT: ABSTRACTION (Repository Interface)
 * =====================================================================
 * ProductRepository defines storage operations as a contract.
 * The service layer doesn't know if products are in a PostgreSQL DB,
 * MongoDB, Redis cache, or an in-memory map.
 *
 * Real-world: Spring Data JPA in any e-commerce backend — swap MySQL
 * for Cassandra by changing the implementation class, not the service.
 * =====================================================================
 */
public interface ProductRepository {
    void save(Product product);
    Optional<Product> findById(String productId);
    List<Product> findAll();
    List<Product> findByCategory(String category);

    // =====================================================================
    // OOP CONCEPT: METHOD OVERLOADING — search() with different parameters
    // =====================================================================
    List<Product> search(String keyword);
    List<Product> search(String keyword, String category);
    List<Product> search(String keyword, double maxPrice);
    List<Product> search(String keyword, String category, double maxPrice);
}
