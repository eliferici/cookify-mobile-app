package com.example.cookify.ui.shopping;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cookify.adapter.ShoppingListAdapter;
import com.example.cookify.data.CookifyRepository;
import com.example.cookify.databinding.FragmentShoppingListBinding;
import com.example.cookify.model.ShoppingItem;

import java.util.ArrayList;
import android.content.Intent;
public class ShoppingListFragment extends Fragment {

    private FragmentShoppingListBinding binding;
    private CookifyRepository repository;
    private ShoppingListAdapter adapter;
    private ArrayList<ShoppingItem> currentList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentShoppingListBinding.inflate(inflater, container, false);

        repository = new CookifyRepository();

        binding.rvShoppingList.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadShoppingList();
        binding.btnShareList.setOnClickListener(v -> shareShoppingList());

        binding.btnDeleteBought.setOnClickListener(v -> {

            if (adapter == null || adapter.getCheckedIds().isEmpty()) {
                Toast.makeText(requireContext(),
                        "Silmek için alınan malzemeleri işaretle.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<String> ids = new ArrayList<>(adapter.getCheckedIds());

            repository.deleteShoppingItems(ids, () -> {
                Toast.makeText(requireContext(),
                        "Alınan malzemeler silindi.",
                        Toast.LENGTH_SHORT).show();

                loadShoppingList();
            });
        });

        binding.btnClearAll.setOnClickListener(v -> {

            new AlertDialog.Builder(requireContext())
                    .setTitle("Listeyi temizle")
                    .setMessage("Tüm alışveriş listesini silmek istiyor musun?")
                    .setPositiveButton("Evet", (dialog, which) -> {
                        repository.clearShoppingList(() -> {
                            Toast.makeText(requireContext(),
                                    "Alışveriş listesi temizlendi.",
                                    Toast.LENGTH_SHORT).show();

                            loadShoppingList();
                        });
                    })
                    .setNegativeButton("Hayır", null)
                    .show();
        });

        return binding.getRoot();
    }

    private void loadShoppingList() {

        repository.getShoppingList(list -> {


            adapter = new ShoppingListAdapter(list);

            binding.rvShoppingList.setAdapter(adapter);
            currentList = list;
            if (list.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Alışveriş listen boş.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void shareShoppingList() {

        if (currentList == null || currentList.isEmpty()) {
            Toast.makeText(requireContext(),
                    "Paylaşılacak alışveriş listesi yok.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Cookify Alışveriş Listem:\n\n");

        for (ShoppingItem item : currentList) {
            builder.append("- ").append(item.getName()).append("\n");
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, builder.toString());

        startActivity(Intent.createChooser(intent, "Listeyi paylaş"));
    }
}