package com.shop;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Category electronics = new Category(1, "Електроніка");
        Category smartphones = new Category(2, "Смартфони");
        Category accessories = new Category(3, "Аксесуари");

        Catalog catalog = new Catalog();
        catalog.add(new Product(1, "Ноутбук", 19999.99, "Високопродуктивний ноутбук для роботи та ігор", electronics));
        catalog.add(new Product(2, "Смартфон", 12999.50, "Смартфон з великим екраном та високою автономністю", smartphones));
        catalog.add(new Product(3, "Навушники", 2499.00, "Бездротові навушники з шумозаглушенням", accessories));

        Cart cart = new Cart();
        OrderHistory history = new OrderHistory("orders_history.dat");

        while (true) {
            System.out.println("\nВиберіть опцію:");
            System.out.println("1 - Переглянути список товарів");
            System.out.println("2 - Додати товар до кошика");
            System.out.println("3 - Переглянути кошик");
            System.out.println("4 - Зробити замовлення");
            System.out.println("5 - Видалити товар з кошика");
            System.out.println("6 - Історія замовлень");
            System.out.println("7 - Пошук товарів");
            System.out.println("0 - Вийти");

            int choice = readInt(scanner);
            switch (choice) {
                case 1:
                    printProducts(catalog.getAll());
                    break;

                case 2:
                    System.out.println("Введіть ID товару для додавання до кошика:");
                    Product toAdd = catalog.findById(readInt(scanner));
                    if (toAdd != null) {
                        cart.addProduct(toAdd);
                        System.out.println("Додано: " + toAdd.getName());
                    } else {
                        System.out.println("Товар з таким ID не знайдено");
                    }
                    break;

                case 3:
                    System.out.println(cart);
                    break;

                case 4:
                    if (cart.getProducts().isEmpty()) {
                        System.out.println("Кошик порожній. Додайте товари перед оформленням замовлення.");
                    } else {
                        Order order = new Order(history.nextId(), cart);
                        history.addOrder(order);
                        System.out.println("Замовлення оформлено:");
                        System.out.println(order);
                        cart.clear();
                    }
                    break;

                case 5:
                    if (cart.getProducts().isEmpty()) {
                        System.out.println("Кошик порожній.");
                    } else {
                        System.out.println(cart);
                        System.out.println("Введіть ID товару для видалення з кошика:");
                        int removeId = readInt(scanner);
                        if (cart.removeProductById(removeId)) {
                            System.out.println("Товар видалено з кошика.");
                        } else {
                            System.out.println("Товару з таким ID у кошику немає.");
                        }
                    }
                    break;

                case 6:
                    if (history.isEmpty()) {
                        System.out.println("Історія замовлень порожня.");
                    } else {
                        System.out.println("=== Історія замовлень ===");
                        for (Order o : history.getOrders()) {
                            System.out.println(o);
                            System.out.println("-----------------------");
                        }
                    }
                    break;

                case 7:
                    System.out.println("Шукати за: 1 - назвою, 2 - категорією");
                    int mode = readInt(scanner);
                    if (mode != 1 && mode != 2) {
                        System.out.println("Невідомий варіант пошуку.");
                        break;
                    }
                    System.out.println("Введіть пошуковий запит:");
                    String query = scanner.nextLine();
                    List<Product> found = (mode == 1)
                            ? catalog.searchByName(query)
                            : catalog.searchByCategory(query);
                    if (found.isEmpty()) {
                        System.out.println("Нічого не знайдено.");
                    } else {
                        System.out.println("Знайдено товарів: " + found.size());
                        printProducts(found);
                    }
                    break;

                case 0:
                    System.out.println("Дякуємо, що використовували наш магазин!");
                    return;

                default:
                    System.out.println("Невідома опція. Спробуйте ще раз.");
                    break;
            }
        }
    }

    private static void printProducts(List<Product> products) {
        for (Product p : products) {
            System.out.println(p);
        }
    }

    private static int readInt(Scanner scanner) {
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
