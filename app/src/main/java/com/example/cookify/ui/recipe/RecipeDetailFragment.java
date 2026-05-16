package com.example.cookify.ui.recipe;

import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cookify.adapter.RecipeAdapter;
import com.example.cookify.data.CookifyRepository;
import com.example.cookify.databinding.FragmentRecipeDetailBinding;
import com.example.cookify.model.Recipe;

import java.util.ArrayList;


public class RecipeDetailFragment extends Fragment {

    public static final String MODE_ALL = "all";
    public static final String MODE_SUGGESTED = "suggested";
    public static final String MODE_MINIMUM = "minimum";

    private static final String ARG_MODE = "mode";

    private FragmentRecipeDetailBinding binding;
    private String mode = MODE_ALL;

    public static RecipeDetailFragment newInstance(String mode) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();

        Bundle args = new Bundle();
        args.putString(ARG_MODE, mode);
        fragment.setArguments(args);

        return fragment;
    }

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            mode = getArguments().getString(ARG_MODE, MODE_ALL);
        }

        CookifyRepository repository = new CookifyRepository();

        ArrayList<Recipe> allRecipes = repository.getRecipes();

        binding.rvRecipes.setLayoutManager(new LinearLayoutManager(requireContext()));

        repository.getSelectedIngredientNames(selectedIngredients -> {

            ArrayList<Recipe> filteredRecipes =
                    filterRecipes(allRecipes, selectedIngredients);

            if (filteredRecipes.isEmpty()) {
                if (MODE_SUGGESTED.equals(mode)) {
                    Toast.makeText(requireContext(),
                            "Seçtiğin malzemelerle tam eşleşen tarif bulunamadı.",
                            Toast.LENGTH_LONG).show();
                } else if (MODE_MINIMUM.equals(mode)) {
                    Toast.makeText(requireContext(),
                            "1-3 eksik malzemeli tarif bulunamadı.",
                            Toast.LENGTH_LONG).show();
                }
            }

            RecipeAdapter adapter =
                    new RecipeAdapter(filteredRecipes, selectedIngredients);

            binding.rvRecipes.setAdapter(adapter);
        });

        return binding.getRoot();
    }

    private ArrayList<Recipe> filterRecipes(ArrayList<Recipe> recipes,
                                            ArrayList<String> selectedIngredients) {

        ArrayList<Recipe> filtered = new ArrayList<>();

        for (Recipe recipe : recipes) {

            int missingCount = 0;

            for (String ingredient : recipe.getIngredients()) {

                boolean exists = false;

                for (String selected : selectedIngredients) {

                    if (selected.trim()
                            .equalsIgnoreCase(ingredient.trim())) {

                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    missingCount++;
                }
            }

            if (MODE_ALL.equals(mode)) {

                filtered.add(recipe);

            } else if (MODE_SUGGESTED.equals(mode)) {

                if (missingCount == 0) {
                    filtered.add(recipe);
                }

            } else if (MODE_MINIMUM.equals(mode)) {

                if (missingCount >= 1 && missingCount <= 3) {
                    filtered.add(recipe);
                }
            }
        }

        return filtered;
    }

    private int getMissingCount(Recipe recipe, ArrayList<String> selectedIngredients) {
        int missing = 0;

        for (String ingredient : recipe.getIngredients()) {
            boolean exists = false;

            for (String selected : selectedIngredients) {

                if (selected.trim().equalsIgnoreCase(ingredient.trim())) {
                    exists = true;
                    break;
                }
            }

            if (exists) {
                missing++;
            }
        }

        return missing;
    }

}