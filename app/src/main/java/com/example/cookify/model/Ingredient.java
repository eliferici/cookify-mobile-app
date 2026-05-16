package com.example.cookify.model;

public class Ingredient {

    private String name;
    private String category;
    private boolean selected;

    public Ingredient() {
    }

    public Ingredient(String name, String category) {
        this.name = name;
        this.category = category;
        this.selected = false;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}