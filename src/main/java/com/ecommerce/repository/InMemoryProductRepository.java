package com.ecommerce.repository;

import com.ecommerce.model.Product;
import java.util.*;
import java.util.stream.Collectors;

/**
 * =====================================================================
 * OOP CONCEPT: ABSTRACTION — Concrete Implementation
 * =====================================================================
 * InMemoryProductRepository implements the contract. In production, this
 * would be JpaProductRepository talking to PostgreSQL.
 * Swapping is a one-line change in the service constructor.
 * =====================================================================
 */
public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> store = new HashMap<>();

    @Override
    public void save(Product product) {
        store.put(product.getProductId(), product);
    }

    @Override
    public Optional<Product> findById(String productId) {
        return Optional.ofNullable(store.get(productId));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Product> findByCategory(String category) {
        return store.values().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    // ---- METHOD OVERLOADING: search() with 4 signatures ----

    @Override
    public List<Product> search(String keyword) {
        return store.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> search(String keyword, String category) {
        return search(keyword).stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> search(String keyword, double maxPrice) {
        return search(keyword).stream()
                .filter(p -> p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> search(String keyword, String category, double maxPrice) {
        return search(keyword, category).stream()
                .filter(p -> p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
}
