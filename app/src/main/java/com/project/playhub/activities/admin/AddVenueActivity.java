package com.project.playhub.activities.admin;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.playhub.databinding.ActivityAddVenueBinding;


public class AddVenueActivity extends AppCompatActivity {

    private ActivityAddVenueBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddVenueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
    }

    private void initViews() {
        binding.backImgView.setOnClickListener(v -> finish());

        binding.next.setOnClickListener(v -> {
            if(binding.name.getText().toString().isEmpty() ||
               binding.desc.getText().toString().isEmpty() ||
               binding.address.getText().toString().isEmpty() ||
               binding.price.getText().toString().isEmpty() ||
               binding.duration.getText().toString().isEmpty() ){
                Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
            }else{
                proceedNext();
            }
        });
    }

    private void proceedNext() {
        Bundle bn = new Bundle();
        bn.putString("name", binding.name.getText().toString());
        bn.putString("desc", binding.desc.getText().toString());
        bn.putString("image", "");
        bn.putString("address", binding.address.getText().toString());
        bn.putString("price", binding.price.getText().toString());
        if(!binding.duration.getText().toString().isEmpty()) {
            bn.putString("duration", binding.duration.getText().toString());
        }
//        bn.putString("lat", location.getLatitude());
//        bn.putString("lng", location.getLongitude());
        Intent intent = new Intent(this, ManageSlotsActivity.class);
        intent.putExtras(bn);
        startActivity(intent);
    }
}