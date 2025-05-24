package org.petya8bachey.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("Должен корректно создавать продукт и возвращать значения через геттеры")
    void constructorAndGetters_ShouldWorkCorrectly() {
        String id = "P001";
        String name = "Test Product";
        double price = 99.99;
        int stock = 10;

        Product product = new Product(id, name, price, stock);

        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(price, product.getPrice(), 0.001); // Используем дельту для сравнения double
        assertEquals(stock, product.getStock());
    }

    @Test
    @DisplayName("Должен корректно обновлять запас с помощью setStock")
    void setStock_ShouldUpdateStockCorrectly() {
        Product product = new Product("P001", "Test Product", 99.99, 10);
        int newStock = 5;

        product.setStock(newStock);

        assertEquals(newStock, product.getStock());
    }

    @Test
    @DisplayName("Equals и HashCode должны быть корректными для одинаковых объектов")
    void equalsAndHashCode_ShouldBeCorrectForEqualObjects() {
        Product product1 = new Product("P001", "Test Product", 99.99, 10);
        Product product2 = new Product("P001", "Test Product", 99.99, 10);

        // Рефлексивность
        assertTrue(product1.equals(product1));
        assertEquals(product1.hashCode(), product1.hashCode());

        // Симметричность и транзитивность (для product1 и product2)
        assertTrue(product1.equals(product2));
        assertTrue(product2.equals(product1));
        assertEquals(product1.hashCode(), product2.hashCode());

        // Проверка с другим объектом, но с тем же ID (если ID является ключом уникальности)
        Product product3 = new Product("P001", "Another Product", 10.0, 5);
        // В нашей реализации equals учитывает все поля, поэтому они не должны быть равны
        assertFalse(product1.equals(product3));
        assertNotEquals(product1.hashCode(), product3.hashCode());
    }

    @Test
    @DisplayName("Equals и HashCode должны быть корректными для разных объектов")
    void equalsAndHashCode_ShouldBeCorrectForDifferentObjects() {
        Product product1 = new Product("P001", "Test Product", 99.99, 10);
        Product product2 = new Product("P002", "Another Product", 10.0, 5);

        assertFalse(product1.equals(product2));
        assertFalse(product2.equals(product1));
        assertNotEquals(product1.hashCode(), product2.hashCode()); // Хэш-коды разных объектов могут быть одинаковыми, но это редкость и нежелательно
    }

    @Test
    @DisplayName("Equals должен возвращать false при сравнении с null или объектом другого типа")
    void equals_ShouldReturnFalse_WhenComparingWithNullOrDifferentType() {
        Product product = new Product("P001", "Test Product", 99.99, 10);

        assertFalse(product.equals(null));
        assertFalse(product.equals(new Object()));
    }

    @Test
    @DisplayName("ToString должен возвращать корректное строковое представление")
    void toString_ShouldReturnCorrectStringRepresentation() {
        Product product = new Product("P001", "Test Product", 99.99, 10);
        String expectedString = "Product{id='P001', name='Test Product', price=99.99, stock=10}";

        assertEquals(expectedString, product.toString());
    }
}
