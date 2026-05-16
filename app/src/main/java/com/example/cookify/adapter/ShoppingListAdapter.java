package com.example.cookify.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookify.databinding.ItemShoppingBinding;
import com.example.cookify.model.ShoppingItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder> {

    private final ArrayList<ShoppingItem> list;
    private final Set<String> checkedIds = new HashSet<>();

    public ShoppingListAdapter(ArrayList<ShoppingItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemShoppingBinding binding = ItemShoppingBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ShoppingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        ShoppingItem item = list.get(position);

        holder.binding.txtShoppingName.setText(item.getName());

        holder.binding.checkBought.setOnCheckedChangeListener(null);
        holder.binding.checkBought.setChecked(checkedIds.contains(item.getId()));

        holder.binding.checkBought.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedIds.add(item.getId());
            } else {
                checkedIds.remove(item.getId());
            }
        });
    }

    public Set<String> getCheckedIds() {
        return checkedIds;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ShoppingViewHolder extends RecyclerView.ViewHolder {
        ItemShoppingBinding binding;

        public ShoppingViewHolder(ItemShoppingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}