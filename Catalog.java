package com.shop;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
    private final List<Product> products = new ArrayList<>();

    public void add(Product product) {
        products.add(product);
    }

    public List<Product> getAll() {
        return new ArrayList<>(products);
    }

    public Product findById(int id) {
        for (Product p : products) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public List<Product> searchByName(String query) {
        String q = query.trim().toLowerCase();
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(q)) result.add(p);
        }
        return result;
    }

    public List<Product> searchByCategory(String query) {
        String q = query.trim().toLowerCase();
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory().getName().toLowerCase().contains(q)) result.add(p);
        }
        return result;
    }
}
