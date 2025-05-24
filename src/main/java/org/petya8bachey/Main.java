package org.petya8bachey;

import org.petya8bachey.repository.InMemoryProductRepository;
import org.petya8bachey.repository.ProductRepository;
import org.petya8bachey.service.ProductService;


public class Main {
    public static void main(String[] args) {
        // Использование реальной (в данном случае, In-Memory) реализации репозитория
        ProductRepository productRepository = new InMemoryProductRepository();
        ProductService productService = new ProductService(productRepository);

        System.out.println("--- Демонстрация работы ProductService ---");

        // Проверка доступности
        System.out.println("\nПроверка доступности:");
        System.out.println("P001 (Laptop) доступен в количестве 5? " + productService.isProductAvailable("P001", 5)); // true
        System.out.println("P001 (Laptop) доступен в количестве 15? " + productService.isProductAvailable("P001", 15)); // false (stock is 10)
        System.out.println("P004 (NonExistent) доступен в количестве 1? " + productService.isProductAvailable("P004", 1)); // false

        // Покупка продукта
        System.out.println("\nПопытка покупки:");
        System.out.println("Текущий запас P001 (Laptop): " + productRepository.findById("P001").map(p -> p.getStock()).orElse(-1));

        System.out.println("Покупка 3 Laptops (P001)...");
        boolean purchased = productService.purchaseProduct("P001", 3);
        System.out.println("Покупка успешна: " + purchased); // true
        System.out.println("Новый запас P001 (Laptop): " + productRepository.findById("P001").map(p -> p.getStock()).orElse(-1)); // 7

        System.out.println("\nПокупка 10 Laptops (P001)...");
        purchased = productService.purchaseProduct("P001", 10);
        System.out.println("Покупка успешна: " + purchased); // false (stock is 7)
        System.out.println("Запас P001 (Laptop) остался: " + productRepository.findById("P001").map(p -> p.getStock()).orElse(-1)); // 7

        System.out.println("\nПокупка 1 NonExistent (P004)...");
        purchased = productService.purchaseProduct("P004", 1);
        System.out.println("Покупка успешна: " + purchased); // false

        // Демонстрация обработки некорректных входных данных
        System.out.println("\nДемонстрация обработки некорректных входных данных:");
        try {
            productService.isProductAvailable("P001", 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при проверке доступности с quantity = 0: " + e.getMessage());
        }
        try {
            productService.purchaseProduct("P001", -5);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при покупке с quantity = -5: " + e.getMessage());
        }
    }
}
