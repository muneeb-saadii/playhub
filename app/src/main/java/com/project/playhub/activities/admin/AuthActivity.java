package com.project.playhub.activities.admin;



import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.project.playhub.R;
import com.project.playhub.controller.FirebaseController;
import com.project.playhub.databinding.ActivityAuthBinding;
import com.project.playhub.fragments.ForgetPassFragment;
import com.project.playhub.fragments.SigninFragment;
import com.project.playhub.fragments.SignupFragment;
import com.project.playhub.utils.SharedPrefsManager;

import androidx.fragment.app.Fragment;


public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    public SharedPrefsManager prefsManager = SharedPrefsManager.getInstance();
    public FirebaseController mController = FirebaseController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Load SigninFragment by default
        loadFragment(new SigninFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFragment(new SigninFragment());
    }

    // Method to load a fragment into the container
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Methods to switch between fragments
    public void showSigninFragment() {
        loadFragment(new SigninFragment());
    }

    public void showSignupFragment() {
        loadFragment(new SignupFragment());
    }

    public void showForgetPassFragment() {
        loadFragment(new ForgetPassFragment());
    }
}


/*
// Inside SigninFragment.java
binding.signupButton.setOnClickListener(v -> {
        if (getActivity() instanceof AuthActivity) {
        ((AuthActivity) getActivity()).showSignupFragment();
    }
            });
*/