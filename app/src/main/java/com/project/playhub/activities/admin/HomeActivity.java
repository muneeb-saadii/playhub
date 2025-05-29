package com.project.playhub.activities.admin;



import static com.project.playhub.utils.Constants.PREFS_FIREBASE_USER;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.project.playhub.R;
import com.project.playhub.controller.FirebaseController;
import com.project.playhub.databinding.ActivityHomeBinding;
import com.project.playhub.fragments.BookingFragment;
import com.project.playhub.fragments.ChatFragment;
import com.project.playhub.fragments.HomeFragment;
import com.project.playhub.fragments.ProfileFragment;
import com.project.playhub.utils.SharedPrefsManager;

import java.util.HashMap;


public class HomeActivity extends AppCompatActivity {


    public ActivityHomeBinding binding;
    public boolean isAdmin = false;
    public SharedPrefsManager prefsManager = SharedPrefsManager.getInstance();
    public FirebaseController mController = FirebaseController.getInstance();

    private Fragment homeFragment = new HomeFragment();
    private Fragment bookingFragment = new BookingFragment();
    private Fragment chattFragment = new ChatFragment();
    private Fragment profileFragment = new ProfileFragment();
    public static HashMap<String, Object> userData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userData = prefsManager.getMap(PREFS_FIREBASE_USER);
        isAdmin = (userData.get("isAdmin").toString().equalsIgnoreCase("yes"))?true:false;

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        // Load default fragment
        replaceFragment(homeFragment);

        // Set click listeners
        findViewById(R.id.nav_home).setOnClickListener(v -> replaceFragment(homeFragment));
        findViewById(R.id.nav_home1).setOnClickListener(v -> replaceFragment(homeFragment));
        findViewById(R.id.nav_booking).setOnClickListener(v -> replaceFragment(bookingFragment));
        findViewById(R.id.nav_booking1).setOnClickListener(v -> replaceFragment(bookingFragment));
        findViewById(R.id.nav_chat).setOnClickListener(v -> replaceFragment(chattFragment));
        findViewById(R.id.nav_chat1).setOnClickListener(v -> replaceFragment(chattFragment));
        findViewById(R.id.nav_profile).setOnClickListener(v -> replaceFragment(profileFragment));
        findViewById(R.id.nav_profile1).setOnClickListener(v -> replaceFragment(profileFragment));
        findViewById(R.id.addOptionArea).setOnClickListener(v -> startActivity(new Intent(this, AddVenueActivity.class)));

        setAdminView();
    }

    private void setAdminView() {
        if(isAdmin){
            binding.navParentLayArea.setVisibility(View.VISIBLE);
            binding.navParentLayAreaUser.setVisibility(View.GONE);
        }else{
            binding.navParentLayArea.setVisibility(View.GONE);
            binding.navParentLayAreaUser.setVisibility(View.VISIBLE);
        }
    }

    private void replaceFragment(Fragment fragment) {
        selectTab(fragment);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_container, fragment)
                .commit();
    }

    private void selectTab(Fragment fragment) {
        binding.homeImg.setColorFilter(ContextCompat.getColor(this, R.color.unselectedMenuItem), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.homeImg1.setColorFilter(ContextCompat.getColor(this, R.color.unselectedMenuItem), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.chatImg.setColorFilter(ContextCompat.getColor(this, R.color.unselectedMenuItem), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.chatImg1.setColorFilter(ContextCompat.getColor(this, R.color.unselectedMenuItem), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.recordsImg.setColorFilter(ContextCompat.getColor(this, R.color.unselectedMenuItem), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.recordsImg1.setColorFilter(ContextCompat.getColor(this, R.color.unselectedMenuItem), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.profileImg.setColorFilter(ContextCompat.getColor(this, R.color.unselectedMenuItem), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.profileImg1.setColorFilter(ContextCompat.getColor(this, R.color.unselectedMenuItem), android.graphics.PorterDuff.Mode.MULTIPLY);

        if(fragment instanceof HomeFragment){
            binding.homeImg.setColorFilter(ContextCompat.getColor(this, R.color.appTheme_primary), android.graphics.PorterDuff.Mode.MULTIPLY);
            binding.homeImg1.setColorFilter(ContextCompat.getColor(this, R.color.appTheme_primary), android.graphics.PorterDuff.Mode.MULTIPLY);
        }else if(fragment instanceof ChatFragment){
            binding.chatImg.setColorFilter(ContextCompat.getColor(this, R.color.appTheme_primary), android.graphics.PorterDuff.Mode.MULTIPLY);
            binding.chatImg1.setColorFilter(ContextCompat.getColor(this, R.color.appTheme_primary), android.graphics.PorterDuff.Mode.MULTIPLY);
        }else if(fragment instanceof BookingFragment){
            binding.recordsImg.setColorFilter(ContextCompat.getColor(this, R.color.appTheme_primary), android.graphics.PorterDuff.Mode.MULTIPLY);
            binding.recordsImg1.setColorFilter(ContextCompat.getColor(this, R.color.appTheme_primary), android.graphics.PorterDuff.Mode.MULTIPLY);
        }else if(fragment instanceof ProfileFragment){
            binding.profileImg.setColorFilter(ContextCompat.getColor(this, R.color.appTheme_primary), android.graphics.PorterDuff.Mode.MULTIPLY);
            binding.profileImg1.setColorFilter(ContextCompat.getColor(this, R.color.appTheme_primary), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }
}