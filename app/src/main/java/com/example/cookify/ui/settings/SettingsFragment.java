package com.example.cookify.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.cookify.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        preferences = requireActivity().getSharedPreferences("CookifyPrefs", Context.MODE_PRIVATE);

        boolean darkMode = preferences.getBoolean("darkMode", false);
        boolean notificationsOn = preferences.getBoolean("notificationsOn", true);

        binding.switchDarkMode.setChecked(darkMode);
        binding.switchNotifications.setChecked(notificationsOn);

        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("darkMode", isChecked).apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );

            Toast.makeText(requireContext(), "Tema tercihi kaydedildi.", Toast.LENGTH_SHORT).show();
        });

        binding.switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("notificationsOn", isChecked).apply();

            Toast.makeText(requireContext(), "Bildirim tercihi kaydedildi.", Toast.LENGTH_SHORT).show();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}