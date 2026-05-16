package com.example.cookify.model;

public class ShoppingItem {

    private String id;
    private String name;
    private String recipeName;

    public ShoppingItem() {
    }

    public ShoppingItem(String id, String name, String recipeName) {
        this.id = id;
        this.name = name;
        this.recipeName = recipeName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRecipeName() {
        return recipeName;
    }
}