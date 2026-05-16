package com.example.cookify.ui.addrecipe;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cookify.databinding.FragmentAddRecipeBinding;
import com.example.cookify.ui.MainActivity;
import com.example.cookify.ui.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddRecipeFragment extends Fragment {

    private FragmentAddRecipeBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddRecipeBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        if (user == null || !user.isEmailVerified()) {
            Toast.makeText(requireContext(),
                    "Tarif eklemek için önce giriş yapınız.",
                    Toast.LENGTH_LONG).show();

            ((MainActivity) requireActivity())
                    .openFragment(new ProfileFragment(), true);

            return binding.getRoot();
        }

        binding.btnSaveRecipe.setOnClickListener(v -> saveRecipe());

        return binding.getRoot();
    }

    private void saveRecipe() {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(requireContext(),
                    "Önce giriş yapınız.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String title = binding.edtRecipeName.getText().toString().trim();
        String description = binding.edtRecipeDescription.getText().toString().trim();
        String ingredientsText = binding.edtRecipeIngredients.getText().toString().trim();

        if (TextUtils.isEmpty(title)
                || TextUtils.isEmpty(description)
                || TextUtils.isEmpty(ingredientsText)) {

            Toast.makeText(requireContext(),
                    "Lütfen tüm alanları doldur.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> ingredients =
                Arrays.asList(ingredientsText.split("\\n"));

        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getUid());
        data.put("title", title);
        data.put("description", description);
        data.put("ingredients", ingredients);
        data.put("createdAt", System.currentTimeMillis());

        db.collection("userRecipes")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(requireContext(),
                            "Tarif başarıyla kaydedildi.",
                            Toast.LENGTH_SHORT).show();

                    binding.edtRecipeName.setText("");
                    binding.edtRecipeDescription.setText("");
                    binding.edtRecipeIngredients.setText("");
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(),
                                "Tarif kaydedilemedi.",
                                Toast.LENGTH_SHORT).show());
    }
}