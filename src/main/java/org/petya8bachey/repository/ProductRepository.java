package org.petya8bachey.repository;

import org.petya8bachey.model.Product;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(String id);
    void updateStock(String id, int newStock);
}
