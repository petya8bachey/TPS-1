package org.petya8bachey.service;

import org.petya8bachey.model.Product;
import org.petya8bachey.repository.ProductRepository;

import java.util.Optional;

public class ProductService {
    private final ProductRepository productRepository;

    // Конструктор для внедрения зависимости (Dependency Injection)
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Проверяет, доступен ли продукт в указанном количестве.
     * @param productId ID продукта
     * @param quantity Требуемое количество
     * @return true, если продукт существует и его запас достаточен; false в противном случае.
     */
    public boolean isProductAvailable(String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        Optional<Product> productOpt = productRepository.findById(productId);
        return productOpt.map(product -> product.getStock() >= quantity).orElse(false);
    }

    /**
     * Пытается "купить" продукт, уменьшая его запас.
     * @param productId ID продукта
     * @param quantity Количество для покупки
     * @return true, если покупка прошла успешно; false, если продукта нет или запаса недостаточно.
     */
    public boolean purchaseProduct(String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (product.getStock() >= quantity) {
                int newStock = product.getStock() - quantity;
                productRepository.updateStock(productId, newStock);
                return true;
            }
        }
        return false;
    }
}
