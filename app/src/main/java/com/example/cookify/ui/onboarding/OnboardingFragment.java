package com.example.cookify.ui.onboarding;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cookify.R;
import com.example.cookify.adapter.OnboardingAdapter;
import com.example.cookify.databinding.FragmentOnboardingBinding;
import com.example.cookify.ui.MainActivity;
import com.example.cookify.ui.home.HomeFragment;

public class OnboardingFragment extends Fragment {

    private FragmentOnboardingBinding binding;

    private final int[] onboardingImages = {
            R.drawable.onboarding_1aa,
            R.drawable.onboarding_2a,
            R.drawable.onboarding_3a,
            R.drawable.onboarding_4a
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOnboardingBinding.inflate(inflater, container, false);

        OnboardingAdapter adapter =
                new OnboardingAdapter(onboardingImages);

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.registerOnPageChangeCallback(
                new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);

                        if (position == 0) {
                            binding.txtIndicator.setText("● ○ ○ ○");
                        } else if (position == 1) {
                            binding.txtIndicator.setText("○ ● ○ ○");
                        } else if (position == 2) {
                            binding.txtIndicator.setText("○ ○ ● ○");
                        } else {
                            binding.txtIndicator.setText("○ ○ ○ ●");
                            binding.btnNext.setText("Başlayalım");
                        }

                        if (position != 3) {
                            binding.btnNext.setText("Devam Et");
                        }
                    }
                }
        );

        binding.btnNext.setOnClickListener(v -> {

            int current = binding.viewPager.getCurrentItem();

            if (current < onboardingImages.length - 1) {

                binding.viewPager.setCurrentItem(current + 1);

            } else {

                SharedPreferences preferences =
                        requireActivity().getSharedPreferences(
                                "CookifyPrefs",
                                Context.MODE_PRIVATE
                        );

                preferences.edit()
                        .putBoolean("onboardingSeen", true)
                        .apply();

                ((MainActivity) requireActivity())
                        .openFragment(new HomeFragment(), false);
            }
        });

        return binding.getRoot();
    }
}