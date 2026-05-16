package com.example.cookify.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.cookify.R;
import com.example.cookify.databinding.ActivityMainBinding;
import com.example.cookify.ui.home.HomeFragment;
import com.example.cookify.ui.onboarding.OnboardingFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        preferences = getSharedPreferences("CookifyPrefs", Context.MODE_PRIVATE);

        boolean darkMode = preferences.getBoolean("darkMode", false);

        AppCompatDelegate.setDefaultNightMode(
                darkMode ?
                        AppCompatDelegate.MODE_NIGHT_YES :
                        AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        boolean onboardingSeen =
                preferences.getBoolean("onboardingSeen", false);

        if (savedInstanceState == null) {

            if (onboardingSeen) {
                openFragment(new HomeFragment(), false);
            } else {
                openFragment(new OnboardingFragment(), false);
            }
        }

        binding.toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {

                        if (getSupportFragmentManager()
                                .getBackStackEntryCount() > 0) {

                            getSupportFragmentManager().popBackStack();

                        } else {
                            finish();
                        }
                    }
                }
                );
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            requestPermissions(
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    100
            );
        }
    }

    public void openFragment(Fragment fragment, boolean addToBackStack) {

        androidx.fragment.app.FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();

        showBackButton(addToBackStack);
    }

    private void showBackButton(boolean show) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        }
    }
}