package com.example.cookify.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.cookify.data.CookifyRepository;
import com.example.cookify.model.Ingredient;

import java.util.ArrayList;

public class FridgeViewModel extends ViewModel {

    private final CookifyRepository repository;

    public FridgeViewModel() {
        repository = new CookifyRepository();
    }

    public ArrayList<Ingredient> getIngredients() {
        return repository.getAllIngredients();
    }

    public void saveSelectedIngredients(ArrayList<Ingredient> selectedIngredients) {
        repository.saveSelectedIngredients(selectedIngredients);
    }
    public void clearSelectedIngredients() {
        repository.clearSelectedIngredients();
    }
    public void getSelectedIngredientNames(CookifyRepository.OnIngredientsLoadedListener listener) {
        repository.getSelectedIngredientNames(listener);
    }
}