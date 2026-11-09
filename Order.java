package com.shop;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final List<Product> products;
    private final double totalPrice;
    @Setter
    private String status;
    private final LocalDateTime createdAt;

    public Order(int id, Cart cart) {
        this.id = id;
        this.products = new ArrayList<>(cart.getProducts());
        this.totalPrice = cart.getTotalPrice();
        this.status = "Нове";
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        StringBuilder sb = new StringBuilder("Замовлення №" + id + " від " + createdAt.format(f) + ":\n");
        for (Product product : products) {
            sb.append("  ").append(product).append("\n");
        }
        sb.append("Загальна вартість: ").append(totalPrice).append("\n");
        sb.append("Статус: ").append(status);
        return sb.toString();
    }
}
