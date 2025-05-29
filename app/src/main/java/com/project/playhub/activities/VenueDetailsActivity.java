package com.project.playhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.playhub.activities.admin.HomeActivity;
import com.project.playhub.databinding.ActivityVenueDetailsBinding;

import java.util.HashMap;

public class VenueDetailsActivity extends AppCompatActivity {

    private String TAG = VenueDetailsActivity.class.getSimpleName();
    private ActivityVenueDetailsBinding binding;
    HashMap<String,Object> venue = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVenueDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
        Log.d(TAG, "onCreate: "+venue);

        if (venue != null && !venue.isEmpty()) {

            String title = (String) venue.get("name");
            String description = (String) venue.get("desc");
            String address = (String) venue.get("address");

            binding.titleText.setText(title);
            binding.descriptionTxt.setText(description);
            binding.addressTxt.setText(address);
            binding.price.setText("Rs: "+venue.get("price").toString());

        }
        if(HomeActivity.userData.get("isAdmin").toString().equalsIgnoreCase("Yes")){
            binding.continueArea.setVisibility(View.GONE);
        }else{
            binding.continueArea.setVisibility(View.VISIBLE);
        }
        binding.continueArea.setOnClickListener(v -> viewSlots());
    }

    private void viewSlots() {
        Bundle bn = new Bundle();
        bn.putSerializable("venueData", venue);
        Intent intent = new Intent(this, ViewSlotsActivity.class);
        intent.putExtras(bn);
        startActivity(intent);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("venueData")) {
            venue = (HashMap<String, Object>) bundle.getSerializable("venueData");
        }else{
            Toast.makeText(this, "Unable to fetch Venue's data.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


}