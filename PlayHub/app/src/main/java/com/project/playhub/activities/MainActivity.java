package com.project.playhub.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.playhub.utils.PermissionsManager;
import com.project.playhub.utils.SharedPrefsManager;
import com.project.playhub.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        SharedPrefsManager.init(this);
        PermissionsManager.init(this, this);
    }
}