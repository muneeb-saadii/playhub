package com.project.playhub.activities;

import static com.project.playhub.utils.Constants.FIREBASE_DB_PATH_USER_NODE;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.playhub.activities.admin.HomeActivity;
import com.project.playhub.controller.FirebaseController;
import com.project.playhub.databinding.ActivityPersonalDetailsBinding;

import java.util.HashMap;


public class PersonalDetailsActivity extends AppCompatActivity {

    private ActivityPersonalDetailsBinding binding;
    private FirebaseController mController = FirebaseController.getInstance();
    private HashMap<String, Object> userData = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setData();
    }

    private void setData() {
        userData = HomeActivity.userData;
        binding.backImgView.setOnClickListener(v -> finish());
        binding.submit.setOnClickListener(v -> updateUserdata());

        binding.name.setText(userData.get("name").toString());
        binding.email.setText(userData.get("email").toString());
        if(userData.containsKey("phone")) {
            binding.phone.setText(userData.get("phone").toString());
        }
        if(userData.containsKey("gender")) {
            binding.gender.setText(userData.get("gender").toString());
        }
    }

    private void updateUserdata() {
        if(binding.name.getText().toString().isEmpty()
           || binding.email.getText().toString().isEmpty()
           || binding.phone.getText().toString().isEmpty()
           || binding.gender.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        userData.put("name", binding.name.getText().toString());
        userData.put("email", binding.email.getText().toString());
        userData.put("phone", binding.phone.getText().toString());
        userData.put("gender", binding.gender.getText().toString());

        String path = FIREBASE_DB_PATH_USER_NODE + "/" + userData.get("id");
        mController.updateData(path, userData, (isSuccess, data) -> {
            if(isSuccess){
                Toast.makeText(this, "Data updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Something went wrong, Please try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}