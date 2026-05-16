package com.example.cookify.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cookify.adapter.RecipeAdapter;
import com.example.cookify.data.CookifyRepository;
import com.example.cookify.databinding.FragmentSearchBinding;
import com.example.cookify.model.Recipe;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private ArrayList<Recipe> allRecipes;
    private ArrayList<String> selectedIngredientNames;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);

        CookifyRepository repository = new CookifyRepository();
        allRecipes = repository.getRecipes();
        selectedIngredientNames = new ArrayList<>();

        binding.rvSearchRecipes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSearchRecipes.setAdapter(new RecipeAdapter(allRecipes, selectedIngredientNames));

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRecipes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return binding.getRoot();
    }

    private void filterRecipes(String query) {
        ArrayList<Recipe> filteredList = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            if (recipe.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(recipe);
            }
        }

        binding.rvSearchRecipes.setAdapter(
                new RecipeAdapter(filteredList, selectedIngredientNames)
        );
    }
}