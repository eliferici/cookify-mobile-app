package com.example.cookify.model;

import java.util.ArrayList;

public class Recipe {

    private String title;
    private String description;
    private ArrayList<String> ingredients;

    public Recipe(String title, String description, ArrayList<String> ingredients) {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }
}