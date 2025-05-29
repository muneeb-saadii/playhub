package com.project.playhub.activities;



import static com.project.playhub.utils.Constants.FIREBASE_DB_PATH_USER_NODE;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.project.playhub.activities.admin.HomeActivity;
import com.project.playhub.controller.FirebaseController;
import com.project.playhub.databinding.ActivityBankDetailsBinding;

import java.util.HashMap;


public class BankDetailsActivity extends AppCompatActivity {

    private ActivityBankDetailsBinding binding;
    private FirebaseController mController = FirebaseController.getInstance();
    private HashMap<String, Object> bankInfo = new HashMap<>();
    private HashMap<String, Object> userData = new HashMap<>();
    private String userId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBankDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setData();
    }

    private void setData() {
        binding.backImgView.setOnClickListener(v -> finish());
        binding.submit.setOnClickListener(v -> updateBankInfo());

        bankInfo.clear();
        userData = HomeActivity.userData;
        if (!userData.get("isAdmin").equals("Yes")){
            if(getIntent().hasExtra("id") && !getIntent().getStringExtra("id").isEmpty()){
                userId = getIntent().getStringExtra("id");
                getBankDetails();
            }else{
                finish();
            }
            return;
        }
        if(userData.containsKey("bank_details") && userData.get("bank_details") != null
                && !userData.get("bank_details").toString().isEmpty()){
            bankInfo = (HashMap<String, Object>) userData.get("bank_details");

            binding.bankName.setText(bankInfo.get("bankName").toString());
            binding.title.setText(bankInfo.get("title").toString());
            binding.accNumber.setText(bankInfo.get("accNumber").toString());
            binding.iban.setText(bankInfo.get("iban").toString());
            binding.desc.setText(bankInfo.get("desc").toString());
        }
    }

    private void getBankDetails() {
        String path = FIREBASE_DB_PATH_USER_NODE + "/" + userId + "/bank_details";
        mController.readData(path, (isSuccess, data) -> {
            if(isSuccess){
                HashMap<String, Object> bankInfo = (HashMap<String, Object>) (((DataSnapshot)data).getValue());
                if(bankInfo != null && !bankInfo.isEmpty()){

                    binding.bankName.setText(bankInfo.get("bankName").toString());
                    binding.title.setText(bankInfo.get("title").toString());
                    binding.accNumber.setText(bankInfo.get("accNumber").toString());
                    binding.iban.setText(bankInfo.get("iban").toString());
                    binding.desc.setText(bankInfo.get("desc").toString());
                    binding.submit.setVisibility(View.GONE);
                    return;
                }
            }
            Toast.makeText(this, "No Bank Details Available", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateBankInfo() {
        if(binding.bankName.getText().toString().isEmpty()
           || binding.title.getText().toString().isEmpty()
           || binding.accNumber.getText().toString().isEmpty()
           || binding.iban.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter all necessary details!", Toast.LENGTH_SHORT).show();
            return;
        }
        bankInfo.put("bankName", binding.bankName.getText().toString());
        bankInfo.put("title", binding.title.getText().toString());
        bankInfo.put("accNumber", binding.accNumber.getText().toString());
        bankInfo.put("iban", binding.iban.getText().toString());
        bankInfo.put("desc", binding.desc.getText().toString());
        String path = FIREBASE_DB_PATH_USER_NODE+"/"+userData.get("id")+"/bank_details";

        mController.createData(path, bankInfo, false, (isSuccess, data) -> {
            if(isSuccess){
                Toast.makeText(this, "Data updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}