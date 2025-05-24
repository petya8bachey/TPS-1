package org.petya8bachey.repository;

import org.petya8bachey.model.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryProductRepository implements ProductRepository {
    private final Map<String, Product> products = new HashMap<>();

    public InMemoryProductRepository() {
        // Инициализация некоторыми продуктами
        products.put("P001", new Product("P001", "Laptop", 1200.0, 10));
        products.put("P002", new Product("P002", "Mouse", 25.0, 50));
        products.put("P003", new Product("P003", "Keyboard", 75.0, 5));
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public void updateStock(String id, int newStock) {
        products.computeIfPresent(id, (k, v) -> {
            v.setStock(newStock);
            return v;
        });
    }
}
