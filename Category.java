package com.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;

    @Override
    public String toString() {
        return "Категорія{id=" + id + ", назва='" + name + "'}";
    }
}
