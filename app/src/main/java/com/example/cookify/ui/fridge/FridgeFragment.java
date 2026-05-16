package com.example.cookify.ui.fridge;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cookify.adapter.IngredientAdapter;
import com.example.cookify.databinding.FragmentFridgeBinding;
import com.example.cookify.model.Ingredient;
import com.example.cookify.viewmodel.FridgeViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FridgeFragment extends Fragment {

    private FragmentFridgeBinding binding;
    private FridgeViewModel viewModel;
    private IngredientAdapter adapter;

    private ArrayList<Ingredient> allIngredients;
    private ArrayList<Ingredient> currentCategoryIngredients;

    private final String[] categories = {
            "Sebzeler",
            "Meyveler",
            "Bakliyatlar",
            "Süt Ürünleri",
            "Et Ürünleri",
            "Sos ve Baharatlar",
            "Kuruyemiş",
            "Konserve",
            "Fırın ve Atıştırmalık"
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFridgeBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(FridgeViewModel.class);

        allIngredients = viewModel.getIngredients();
        currentCategoryIngredients = new ArrayList<>();

        binding.rvIngredients.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.getSelectedIngredientNames(selectedNames -> {
            for (Ingredient ingredient : allIngredients) {
                for (String selected : selectedNames) {
                    if (ingredient.getName().trim().equalsIgnoreCase(selected.trim())) {
                        ingredient.setSelected(true);
                        break;
                    }
                }
            }

            createCategoryButtons();
        });

        binding.btnSaveIngredients.setOnClickListener(v -> {
            ArrayList<Ingredient> selectedIngredients = new ArrayList<>();

            for (Ingredient ingredient : allIngredients) {
                if (ingredient.isSelected()) {
                    selectedIngredients.add(ingredient);
                }
            }

            if (selectedIngredients.isEmpty()) {
                Toast.makeText(requireContext(), "Lütfen en az bir malzeme seç.", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.saveSelectedIngredients(selectedIngredients);

            Toast.makeText(requireContext(), "Malzemeler kaydedildi.", Toast.LENGTH_SHORT).show();
        });

        binding.btnUpdateIngredients.setOnClickListener(v -> {SharedPreferences preferences = requireActivity()
                .getSharedPreferences("CookifyPrefs", Context.MODE_PRIVATE);

            preferences.edit()
                    .remove("selectedIngredients")
                    .putInt("selectedIngredientCount", 0)
                    .apply();
            for (Ingredient ingredient : allIngredients) {
                ingredient.setSelected(false);
            }

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            viewModel.clearSelectedIngredients();
            Toast.makeText(requireContext(),
                    "Tüm seçimler temizlendi.",
                    Toast.LENGTH_SHORT).show();
        });

        return binding.getRoot();
    }

    private void createCategoryButtons() {
        binding.categoryContainer.removeAllViews();

        for (String category : categories) {
            TextView categoryView = new TextView(requireContext());

            categoryView.setText(category);
            categoryView.setTextSize(14);
            categoryView.setTextColor(getResources().getColor(android.R.color.white));
            categoryView.setPadding(28, 18, 28, 18);

            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(0, 0, 18, 0);
            categoryView.setLayoutParams(params);

            categoryView.setBackgroundResource(com.example.cookify.R.drawable.bg_button_primary);

            categoryView.setOnClickListener(v -> showIngredientsByCategory(category));

            binding.categoryContainer.addView(categoryView);
        }
    }

    private void showIngredientsByCategory(String category) {
        binding.txtSelectedCategory.setText(category);

        currentCategoryIngredients.clear();

        for (Ingredient ingredient : allIngredients) {
            if (ingredient.getCategory().equals(category)) {
                currentCategoryIngredients.add(ingredient);
            }
        }

        adapter = new IngredientAdapter(currentCategoryIngredients);
        binding.rvIngredients.setAdapter(adapter);
    }

    private void saveIngredients(String message) {
        ArrayList<Ingredient> selectedIngredients = new ArrayList<>();

        for (Ingredient ingredient : allIngredients) {
            if (ingredient.isSelected()) {
                selectedIngredients.add(ingredient);
            }
        }

        if (selectedIngredients.isEmpty()) {
            Toast.makeText(requireContext(), "Lütfen en az bir malzeme seç.", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.saveSelectedIngredients(selectedIngredients);

        SharedPreferences preferences = requireActivity()
                .getSharedPreferences("CookifyPrefs", Context.MODE_PRIVATE);

        java.util.HashSet<String> selectedNames = new java.util.HashSet<>();

        for (Ingredient ingredient : selectedIngredients) {
            selectedNames.add(ingredient.getName());
        }

        preferences.edit()
                .putInt("selectedIngredientCount", selectedIngredients.size())
                .putStringSet("selectedIngredients", selectedNames)
                .apply();
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}