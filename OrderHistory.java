package com.shop;

import lombok.extern.java.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Log
public class OrderHistory {
    private final String fileName;
    private List<Order> orders = new ArrayList<>();

    public OrderHistory(String fileName) {
        this.fileName = fileName;
        load();
    }

    public void addOrder(Order order) {
        orders.add(order);
        save();
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public int nextId() {
        return orders.size() + 1;
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }

    private void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(orders);
        } catch (IOException e) {
            log.severe("Не вдалося зберегти історію замовлень: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        File file = new File(fileName);
        if (!file.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            orders = (List<Order>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.severe("Не вдалося завантажити історію замовлень: " + e.getMessage());
        }
    }
}
