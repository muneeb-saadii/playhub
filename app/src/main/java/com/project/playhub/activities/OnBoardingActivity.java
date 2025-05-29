package com.project.playhub.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.project.playhub.R;
import com.project.playhub.activities.admin.AuthActivity;
import com.project.playhub.adapters.OnBoardingAdapter;
import com.project.playhub.databinding.ActivityOnBoardingBinding;
import com.project.playhub.models.OnBoardingModel;
import java.util.ArrayList;

public class OnBoardingActivity extends AppCompatActivity {

    private ActivityOnBoardingBinding binding;
    private OnBoardingAdapter obAdapter;
    private ArrayList<OnBoardingModel> list;

    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;
    private Notification.Builder builder;
    private static final String CHANNEL_ID = "i.apps.notifications";
    private static final String CHANNEL_DESCRIPTION = "Test notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        list = new ArrayList<>();

        initViews();
        loadData();
    }

    private void initViews() {
        obAdapter = new OnBoardingAdapter(list);
        binding.onboarding.setAdapter(obAdapter);
        binding.onboarding.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicatingViews();
            }
        });

        binding.skipBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AuthActivity.class)) );

        binding.nextBtn.setOnClickListener(v -> {
            if (binding.onboarding.getCurrentItem() == list.size() - 1) {
                startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                finish();
            } else {
                binding.onboarding.setCurrentItem(binding.onboarding.getCurrentItem() + 1);
                obAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateIndicatingViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.view1.setBackgroundTintList(getResources().getColorStateList(R.color.appTheme_primary_light, getTheme()));
            binding.view2.setBackgroundTintList(getResources().getColorStateList(R.color.appTheme_primary_light, getTheme()));
            binding.view3.setBackgroundTintList(getResources().getColorStateList(R.color.appTheme_primary_light, getTheme()));

            switch (binding.onboarding.getCurrentItem()) {
                case 1:
                    binding.view2.setBackgroundTintList(getResources().getColorStateList(R.color.appTheme_primary, getTheme()));
                    break;
                case 2:
                    binding.view3.setBackgroundTintList(getResources().getColorStateList(R.color.appTheme_primary, getTheme()));
                    break;
                default:
                    binding.view1.setBackgroundTintList(getResources().getColorStateList(R.color.appTheme_primary, getTheme()));
                    break;
            }
        }
    }

    private void loadData() {
        list.clear();
        list.add(new OnBoardingModel(
                "https://dummyimage.com/600x400/000/fff",
                "All your favorites",
                "Get all your loved venues at one once place, just follow the directions."
        ));
        list.add(new OnBoardingModel(
                "https://resizing.flixster.com/pF8FZMke9ofUVrJwrrma3RAcpmA=/206x305/v2/https://resizing.flixster.com/-XZAfHZM39UwaGJIFWKAE8fS0ak=/v3/t/assets/p18253291_b_v10_ad.jpg",
                "All your favorites",
                "Get all your loved venues at one once place, just follow the directions."
        ));
        list.add(new OnBoardingModel(
                "https://5.imimg.com/data5/SELLER/Default/2023/4/299176288/JR/MF/PD/138005083/female-measurement-dummy-1000x1000.jpg",
                "All your favorites",
                "Get all your loved venues at one once place, just follow the directions."
        ));
        obAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int a = 100+65-12;
    }
}
