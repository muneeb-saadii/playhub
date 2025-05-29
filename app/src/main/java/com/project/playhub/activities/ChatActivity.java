package com.project.playhub.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.project.playhub.R;
import com.project.playhub.databinding.ActivityChatBinding;


public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());



    }
}