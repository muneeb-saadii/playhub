package com.project.playhub.activities;

import static com.project.playhub.utils.Constants.PREFS_IS_FIRST_TIME;
import static com.project.playhub.utils.Constants.PREFS_IS_LOGIN;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.project.playhub.controller.FirebaseController;
import com.project.playhub.utils.PermissionsManager;
import com.project.playhub.utils.SharedPrefsManager;
import com.project.playhub.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPrefsManager.init(this);
        FirebaseController.init(this);
        PermissionsManager.init(this, this);

        prefsManager = SharedPrefsManager.getInstance();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(prefsManager.getString(PREFS_IS_LOGIN).equals("yes")){
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }else if(!prefsManager.getString(PREFS_IS_FIRST_TIME).equals("no")){
                    startActivity(new Intent(MainActivity.this, OnBoardingActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(MainActivity.this, AuthActivity.class));
                    finish();
                }
            }
        }, 2000);  // e.g., 2000 means 2 seconds

    }
}