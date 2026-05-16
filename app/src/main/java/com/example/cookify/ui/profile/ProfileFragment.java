package com.example.cookify.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cookify.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        checkCurrentUser();

        binding.btnRegister.setOnClickListener(v -> registerUser());

        binding.btnLogin.setOnClickListener(v -> loginUser());

        return binding.getRoot();
    }

    private void registerUser() {

        String name =
                binding.edtName.getText().toString().trim();

        String surname =
                binding.edtSurname.getText().toString().trim();

        String email =
                binding.edtEmail.getText().toString().trim();

        String password =
                binding.edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)
                || TextUtils.isEmpty(surname)
                || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password)) {

            Toast.makeText(requireContext(),
                    "Tüm alanları doldur.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {

            Toast.makeText(requireContext(),
                    "Şifre en az 6 karakter olmalı.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    FirebaseUser user = auth.getCurrentUser();

                    if (user != null) {

                        Map<String, Object> userData =
                                new HashMap<>();

                        userData.put("name", name);
                        userData.put("surname", surname);
                        userData.put("email", email);

                        db.collection("users")
                                .document(user.getUid())
                                .set(userData);

                        user.sendEmailVerification()
                                .addOnSuccessListener(unused -> {

                                    Toast.makeText(
                                            requireContext(),
                                            "Mail doğrulama bağlantısı gönderildi.",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    auth.signOut();
                                });
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(),
                                e.getMessage(),
                                Toast.LENGTH_LONG).show());
    }

    private void loginUser() {

        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext(),
                    "E-posta ve şifre gir.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    FirebaseUser user = auth.getCurrentUser();

                    if (user != null) {
                        user.reload().addOnCompleteListener(task -> {

                            FirebaseUser updatedUser = auth.getCurrentUser();

                            if (updatedUser != null && updatedUser.isEmailVerified()) {

                                db.collection("users")
                                        .document(updatedUser.getUid())
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {

                                            String name = documentSnapshot.getString("name");

                                            if (name == null || name.isEmpty()) {
                                                name = "kullanıcı";
                                            }

                                            binding.txtUserInfo.setText("Hoş geldin " );

                                            Toast.makeText(requireContext(),
                                                    "Giriş başarılı.",
                                                    Toast.LENGTH_SHORT).show();
                                        });

                            } else {
                                Toast.makeText(requireContext(),
                                        "Önce mail adresini doğrula.",
                                        Toast.LENGTH_LONG).show();

                                auth.signOut();
                            }
                        });
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(),
                                "Giriş başarısız.",
                                Toast.LENGTH_SHORT).show());
    }

    private void checkCurrentUser() {

        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {

            db.collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {

                        String name =
                                documentSnapshot.getString("name");

                        if (name != null) {

                            binding.txtUserInfo.setText(
                                    "Hoş geldin, " + name
                            );
                        }
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}