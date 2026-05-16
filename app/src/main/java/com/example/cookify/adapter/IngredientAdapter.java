package com.example.cookify.adapter;

import com.example.cookify.databinding.ItemIngredientBinding;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookify.databinding.ItemIngredientBinding;
import com.example.cookify.model.Ingredient;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final ArrayList<Ingredient> ingredientList;

    public IngredientAdapter(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemIngredientBinding binding = ItemIngredientBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new IngredientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);

        holder.binding.checkIngredient.setText(ingredient.getName());

        holder.binding.checkIngredient.setChecked(ingredient.isSelected());

        holder.binding.checkIngredient.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ingredient.setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public ArrayList<Ingredient> getSelectedIngredients() {
        ArrayList<Ingredient> selectedList = new ArrayList<>();

        for (Ingredient ingredient : ingredientList) {
            if (ingredient.isSelected()) {
                selectedList.add(ingredient);
            }
        }

        return selectedList;
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {

        ItemIngredientBinding binding;

        public IngredientViewHolder(ItemIngredientBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}