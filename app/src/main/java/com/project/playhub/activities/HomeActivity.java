package com.project.playhub.activities;



import static com.project.playhub.utils.Constants.PREFS_FIREBASE_USER;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.project.playhub.R;
import com.project.playhub.controller.FirebaseController;
import com.project.playhub.fragments.BookingFragment;
import com.project.playhub.fragments.ChatFragment;
import com.project.playhub.fragments.HomeFragment;
import com.project.playhub.fragments.ProfileFragment;
import com.project.playhub.utils.SharedPrefsManager;

import java.util.HashMap;


public class HomeActivity extends AppCompatActivity {


    public SharedPrefsManager prefsManager = SharedPrefsManager.getInstance();
    public FirebaseController mController = FirebaseController.getInstance();

    private Fragment homeFragment = new HomeFragment();
    private Fragment bookingFragment = new BookingFragment();
    private Fragment chattFragment = new ChatFragment();
    private Fragment profileFragment = new ProfileFragment();
    HashMap<String, String> userData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userData = prefsManager.getMap(PREFS_FIREBASE_USER);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load default fragment
        replaceFragment(homeFragment);

        // Set click listeners
        findViewById(R.id.nav_home).setOnClickListener(v -> replaceFragment(homeFragment));
        findViewById(R.id.nav_booking).setOnClickListener(v -> replaceFragment(bookingFragment));
        findViewById(R.id.nav_chat).setOnClickListener(v -> replaceFragment(chattFragment));
        findViewById(R.id.nav_profile).setOnClickListener(v -> replaceFragment(profileFragment));
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_container, fragment)
                .commit();
    }
}


//myview.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,5f));