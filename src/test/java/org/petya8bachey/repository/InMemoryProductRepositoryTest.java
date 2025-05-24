package org.petya8bachey.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.petya8bachey.model.Product;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryProductRepositoryTest {

    private InMemoryProductRepository repository;

    @BeforeEach
    void setUp() {
        // Создаем новый экземпляр репозитория перед каждым тестом
        // Это гарантирует, что каждый тест начинается с чистого состояния данных
        repository = new InMemoryProductRepository();
    }

    @Test
    @DisplayName("findById должен вернуть Optional с продуктом, если он существует")
    void findById_ShouldReturnProduct_WhenExists() {
        // Продукт "P001" существует по умолчанию в InMemoryProductRepository
        String existingProductId = "P001";
        Optional<Product> productOpt = repository.findById(existingProductId);

        assertTrue(productOpt.isPresent());
        Product product = productOpt.get();
        assertEquals(existingProductId, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(1200.0, product.getPrice(), 0.001);
        assertEquals(10, product.getStock());
    }

    @Test
    @DisplayName("findById должен вернуть Optional.empty, если продукт не существует")
    void findById_ShouldReturnEmpty_WhenNotExists() {
        String nonExistentProductId = "P999";
        Optional<Product> productOpt = repository.findById(nonExistentProductId);

        assertFalse(productOpt.isPresent());
        assertTrue(productOpt.isEmpty());
    }

    @Test
    @DisplayName("updateStock должен обновить запас существующего продукта")
    void updateStock_ShouldUpdateExistingProductStock() {
        String productId = "P002"; // Mouse, initial stock 50
        int initialStock = repository.findById(productId).get().getStock();
        assertEquals(50, initialStock);

        int newStock = 45;
        repository.updateStock(productId, newStock);

        Optional<Product> updatedProductOpt = repository.findById(productId);
        assertTrue(updatedProductOpt.isPresent());
        assertEquals(newStock, updatedProductOpt.get().getStock());
    }

    @Test
    @DisplayName("updateStock не должен изменять запас других продуктов")
    void updateStock_ShouldNotAffectOtherProducts() {
        String productIdToUpdate = "P001"; // Laptop
        String otherProductId = "P002"; // Mouse

        int initialStockOtherProduct = repository.findById(otherProductId).get().getStock();

        repository.updateStock(productIdToUpdate, 5); // Обновляем P001

        // Проверяем, что запас P002 не изменился
        assertEquals(initialStockOtherProduct, repository.findById(otherProductId).get().getStock());
    }

    @Test
    @DisplayName("updateStock не должен вызывать ошибку и не должен изменять ничего, если продукт не существует")
    void updateStock_ShouldDoNothingForNonExistentProduct() {
        String nonExistentProductId = "P999";
        int stockBeforeUpdate = repository.findById("P001").get().getStock(); // Проверяем, что существующий продукт не изменился

        // Вызываем updateStock для несуществующего продукта
        assertDoesNotThrow(() -> repository.updateStock(nonExistentProductId, 100));

        // Проверяем, что запас существующего продукта остался прежним
        assertEquals(stockBeforeUpdate, repository.findById("P001").get().getStock());
        // Убеждаемся, что несуществующий продукт так и не появился
        assertFalse(repository.findById(nonExistentProductId).isPresent());
    }
}
