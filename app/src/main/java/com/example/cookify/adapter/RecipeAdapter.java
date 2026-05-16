package com.example.cookify.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookify.databinding.ItemRecipeBinding;
import com.example.cookify.model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cookify.utils.NotificationHelper;
import com.example.cookify.data.CookifyRepository;
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final ArrayList<Recipe> recipeList;
    private final ArrayList<String> selectedIngredientNames;

    public RecipeAdapter(ArrayList<Recipe> recipeList, ArrayList<String> selectedIngredientNames) {
        this.recipeList = recipeList;
        this.selectedIngredientNames = selectedIngredientNames;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecipeBinding binding = ItemRecipeBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        ArrayList<String> missing = getMissingIngredients(recipe);

        holder.binding.txtRecipeTitle.setText(recipe.getTitle());
        holder.binding.txtRecipeDesc.setText(recipe.getDescription());

        if (missing.isEmpty()) {
            holder.binding.txtMissing.setText("Tüm malzemeler evinde var.");
        } else {
            holder.binding.txtMissing.setText("Eksik: " + String.join(", ", missing));
        }

        holder.itemView.setOnClickListener(v -> {

            if (missing.isEmpty()) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(recipe.getTitle())
                        .setMessage("Bu tarif için tüm malzemelere sahipsin.")
                        .setPositiveButton("Tamam", null)
                        .show();

            } else {

                LinearLayout layout = new LinearLayout(v.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(45, 30, 45, 10);

                TextView txtInfo = new TextView(v.getContext());
                txtInfo.setText("Bu tarif için eksik malzemeler:");
                txtInfo.setTextSize(16);
                layout.addView(txtInfo);

                TextView txtMissing = new TextView(v.getContext());
                txtMissing.setText(String.join("\n", missing));
                txtMissing.setTextSize(15);
                txtMissing.setPadding(0, 20, 0, 20);
                layout.addView(txtMissing);

                Button btnShopping = new Button(v.getContext());
                btnShopping.setText("Alışveriş Listesi Oluştur");
                layout.addView(btnShopping);

                AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                        .setTitle(recipe.getTitle())
                        .setView(layout)
                        .setNegativeButton("Kapat", null)
                        .create();

                btnShopping.setOnClickListener(btn -> {
                    CookifyRepository repository = new CookifyRepository();
                    repository.saveShoppingList(recipe.getTitle(), missing);

                    Toast.makeText(v.getContext(),
                            "Alışveriş listesi oluşturuldu.",
                            Toast.LENGTH_SHORT).show();
                    NotificationHelper.showNotification(
                            v.getContext(),
                            "Alışveriş listen hazır, listendeki malzemelerini unutma!",
                            "Eksik malzemeler alışveriş listene eklendi."
                    );
                    dialog.dismiss();
                });

                dialog.show();
            }
        });
    }

    private ArrayList<String> getMissingIngredients(Recipe recipe) {
        ArrayList<String> missing = new ArrayList<>();

        for (String item : recipe.getIngredients()) {
            if (!selectedIngredientNames.contains(item)) {
                missing.add(item);
            }
        }

        return missing;
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        ItemRecipeBinding binding;

        public RecipeViewHolder(ItemRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}