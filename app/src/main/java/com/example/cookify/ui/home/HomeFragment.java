package com.example.cookify.ui.home;
import com.example.cookify.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Random;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.cookify.databinding.FragmentHomeBinding;
import com.example.cookify.ui.MainActivity;
import com.example.cookify.ui.recipe.RecipeDetailFragment;
import com.example.cookify.ui.search.SearchFragment;
import com.example.cookify.ui.settings.SettingsFragment;
import com.example.cookify.ui.profile.ProfileFragment;
import com.example.cookify.ui.fridge.FridgeFragment;
import com.example.cookify.ui.shopping.ShoppingListFragment;

import com.example.cookify.data.CookifyRepository;
import com.example.cookify.model.Recipe;

import java.util.ArrayList;
import com.example.cookify.ui.addrecipe.AddRecipeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setupThemeToggle();
        SharedPreferences preferences = requireActivity()
                .getSharedPreferences("CookifyPrefs", Context.MODE_PRIVATE);

        String name = preferences.getString("userName", "");

        if (name.isEmpty()) {
            binding.txtWelcome.setText("Merhaba");
        } else {
            binding.txtWelcome.setText("Hoş geldin, " + name);
        }

        binding.btnSearchTop.setOnClickListener(v ->
                ((MainActivity) requireActivity()).openFragment(new SearchFragment(), true));

        binding.btnAllRecipes.setOnClickListener(v ->
                ((MainActivity) requireActivity()).openFragment(
                        RecipeDetailFragment.newInstance(RecipeDetailFragment.MODE_ALL), true));

        binding.btnSuggestedRecipes.setOnClickListener(v ->
                ((MainActivity) requireActivity()).openFragment(
                        RecipeDetailFragment.newInstance(RecipeDetailFragment.MODE_SUGGESTED), true));

        binding.btnMinimumRecipes.setOnClickListener(v ->
                ((MainActivity) requireActivity()).openFragment(
                        RecipeDetailFragment.newInstance(RecipeDetailFragment.MODE_MINIMUM), true));
        binding.btnShoppingList.setOnClickListener(v ->
                ((MainActivity) requireActivity())
                        .openFragment(new ShoppingListFragment(), true));
        binding.navRecipes.setOnClickListener(v ->
                ((MainActivity) requireActivity())
                        .openFragment(new AddRecipeFragment(), true));
        binding.navSearch.setOnClickListener(v ->
                ((MainActivity) requireActivity()).openFragment(new SearchFragment(), true));


        binding.navProfile.setOnClickListener(v ->
                ((MainActivity) requireActivity())
                        .openFragment(new ProfileFragment(), true));
        binding.btnFridge.setOnClickListener(v ->
                ((MainActivity) requireActivity())
                        .openFragment(new FridgeFragment(), true));
        setRandomDailyRecipe();
        return binding.getRoot();
    }
    private void setRandomDailyRecipe() {

        CookifyRepository repository = new CookifyRepository();
        ArrayList<Recipe> recipes = repository.getRecipes();

        if (recipes == null || recipes.isEmpty()) {
            return;
        }

        SharedPreferences preferences = requireActivity()
                .getSharedPreferences("CookifyPrefs", Context.MODE_PRIVATE);

        String today = new java.text.SimpleDateFormat(
                "yyyyMMdd",
                java.util.Locale.getDefault()
        ).format(new java.util.Date());

        String savedDate = preferences.getString("dailyRecipeDate", "");
        int savedIndex = preferences.getInt("dailyRecipeIndex", -1);

        int recipeIndex;

        if (today.equals(savedDate)
                && savedIndex >= 0
                && savedIndex < recipes.size()) {

            recipeIndex = savedIndex;

        } else {

            Random random = new Random();
            recipeIndex = random.nextInt(recipes.size());

            preferences.edit()
                    .putString("dailyRecipeDate", today)
                    .putInt("dailyRecipeIndex", recipeIndex)
                    .apply();
        }

        Recipe dailyRecipe = recipes.get(recipeIndex);

        binding.txtDailyRecipeTitle.setText(dailyRecipe.getTitle());
        binding.txtDailyRecipeDesc.setText(dailyRecipe.getDescription());
    }
    private void setupThemeToggle() {

        SharedPreferences preferences =
                requireActivity().getSharedPreferences(
                        "CookifyPrefs",
                        Context.MODE_PRIVATE
                );

        boolean darkMode =
                preferences.getBoolean("darkMode", false);

        updateThemeIcon(darkMode);

        binding.btnTheme.setOnClickListener(v -> {

            boolean current =
                    preferences.getBoolean("darkMode", false);

            boolean newMode = !current;

            preferences.edit()
                    .putBoolean("darkMode", newMode)
                    .apply();

            if (newMode) {

                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES
                );

            } else {

                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO
                );
            }

            updateThemeIcon(newMode);
        });

    }private void updateThemeIcon(boolean darkMode) {

        if (darkMode) {

            binding.btnTheme.setImageResource(
                    R.drawable.ic_light_mode
            );

        } else {

            binding.btnTheme.setImageResource(
                    R.drawable.moon

            );
        }
    }

}