package org.petya8bachey.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.petya8bachey.model.Product;
import org.petya8bachey.repository.ProductRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Интеграция Mockito с JUnit 5
class ProductServiceTest {

    @Mock // Создает мок-объект для ProductRepository
    private ProductRepository productRepository;

    @InjectMocks // Внедряет моки в ProductService. Создает экземпляр ProductService и внедряет в него productRepository
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Инициализация тестового продукта перед каждым тестом
        testProduct = new Product("P001", "Test Product", 100.0, 10);
    }

    // --- Тесты для isProductAvailable ---

    @Test
    @DisplayName("Должен вернуть true, если продукт существует и запас достаточен")
    void isProductAvailable_ShouldReturnTrue_WhenProductExistsAndStockIsSufficient() {
        // Настройка поведения мока: когда findById("P001") вызывается, вернуть Optional с testProduct
        when(productRepository.findById("P001")).thenReturn(Optional.of(testProduct));

        // Вызов тестируемого метода
        boolean isAvailable = productService.isProductAvailable("P001", 5);

        // Проверка результата
        assertTrue(isAvailable);

        // Проверка взаимодействия с моком: убедиться, что findById был вызван ровно 1 раз с "P001"
        verify(productRepository, times(1)).findById("P001");
    }

    @Test
    @DisplayName("Должен вернуть false, если продукт существует, но запас недостаточен")
    void isProductAvailable_ShouldReturnFalse_WhenProductExistsAndStockIsInsufficient() {
        when(productRepository.findById("P001")).thenReturn(Optional.of(testProduct));

        boolean isAvailable = productService.isProductAvailable("P001", 15); // Запрос больше, чем есть в наличии

        assertFalse(isAvailable);
        verify(productRepository, times(1)).findById("P001");
    }

    @Test
    @DisplayName("Должен вернуть false, если продукт не существует")
    void isProductAvailable_ShouldReturnFalse_WhenProductDoesNotExist() {
        // Настройка поведения мока: когда findById("P002") вызывается, вернуть пустой Optional
        when(productRepository.findById("P002")).thenReturn(Optional.empty());

        boolean isAvailable = productService.isProductAvailable("P002", 1);

        assertFalse(isAvailable);
        verify(productRepository, times(1)).findById("P002");
    }

    @Test
    @DisplayName("Должен выбросить IllegalArgumentException, если количество для isProductAvailable равно 0 или отрицательное")
    void isProductAvailable_ShouldThrowException_WhenQuantityIsZeroOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> productService.isProductAvailable("P001", 0));
        assertThrows(IllegalArgumentException.class, () -> productService.isProductAvailable("P001", -5));

        // Убедимся, что репозиторий не был вызван в этих случаях
        verify(productRepository, never()).findById(anyString());
    }

    // --- Тесты для purchaseProduct ---

    @Test
    @DisplayName("Должен вернуть true и обновить запас, если покупка успешна")
    void purchaseProduct_ShouldReturnTrueAndDecreaseStock_WhenPurchaseIsSuccessful() {
        when(productRepository.findById("P001")).thenReturn(Optional.of(testProduct));

        boolean purchased = productService.purchaseProduct("P001", 3);

        assertTrue(purchased);
        // Проверка, что findById был вызван
        verify(productRepository, times(1)).findById("P001");
        // Проверка, что updateStock был вызван с правильными аргументами (10 - 3 = 7)
        verify(productRepository, times(1)).updateStock("P001", 7);
    }

    @Test
    @DisplayName("Должен вернуть false и не обновлять запас, если запаса недостаточно")
    void purchaseProduct_ShouldReturnFalseAndNotDecreaseStock_WhenStockIsInsufficient() {
        when(productRepository.findById("P001")).thenReturn(Optional.of(testProduct));

        boolean purchased = productService.purchaseProduct("P001", 15); // Запрос больше, чем есть

        assertFalse(purchased);
        verify(productRepository, times(1)).findById("P001");
        // Проверка, что updateStock НЕ был вызван
        verify(productRepository, never()).updateStock(anyString(), anyInt());
    }

    @Test
    @DisplayName("Должен вернуть false и не обновлять запас, если продукт не существует")
    void purchaseProduct_ShouldReturnFalseAndNotDecreaseStock_WhenProductDoesNotExist() {
        when(productRepository.findById("P002")).thenReturn(Optional.empty());

        boolean purchased = productService.purchaseProduct("P002", 1);

        assertFalse(purchased);
        verify(productRepository, times(1)).findById("P002");
        verify(productRepository, never()).updateStock(anyString(), anyInt());
    }

    @Test
    @DisplayName("Должен выбросить IllegalArgumentException, если количество для purchaseProduct равно 0 или отрицательное")
    void purchaseProduct_ShouldThrowException_WhenQuantityIsZeroOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> productService.purchaseProduct("P001", 0));
        assertThrows(IllegalArgumentException.class, () -> productService.purchaseProduct("P001", -5));

        // Убедимся, что репозиторий не был вызван в этих случаях
        verify(productRepository, never()).findById(anyString());
        verify(productRepository, never()).updateStock(anyString(), anyInt());
    }
}
