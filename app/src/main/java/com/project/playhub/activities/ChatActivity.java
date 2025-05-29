package com.project.playhub.activities;

import static com.project.playhub.utils.Constants.FIREBASE_DB_ADMIN_PREFIX;
import static com.project.playhub.utils.Constants.FIREBASE_DB_PATH_CHAT_NODE;
import static com.project.playhub.utils.Constants.FIREBASE_DB_USER_PREFIX;
import static com.project.playhub.utils.Constants.PREFS_IS_ADMIN;
import static com.project.playhub.utils.Constants.PREFS_USER_ID;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.FirebaseDatabase;
import com.project.playhub.R;
import com.project.playhub.adapters.ChatListAdapter;
import com.project.playhub.adapters.MessagesRecycAdapter;
import com.project.playhub.controller.FirebaseController;
import com.project.playhub.databinding.ActivityChatBinding;
import com.project.playhub.utils.Constants;
import com.project.playhub.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {

    private String TAG = ChatActivity.class.getSimpleName();
    private ActivityChatBinding binding;

    public FirebaseController mController = FirebaseController.getInstance();
    public SharedPrefsManager prefsManager = SharedPrefsManager.getInstance();


    private MessagesRecycAdapter adapter;
    private ArrayList<HashMap<String, Object>> chatList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadChats();
        sendChat();
    }

    private void sendChat() {
        binding.btnSend.setOnClickListener(v -> {
            boolean isAdmin = prefsManager.getString(Constants.PREFS_IS_ADMIN).equalsIgnoreCase("yes");
            String msg = binding.msgTxt.getText().toString().trim();

            if (!msg.isEmpty()) {
                String currentUserId = prefsManager.getString(PREFS_USER_ID);
                String receiverRawId = getIntent().getStringExtra("userId");

                if (receiverRawId == null || receiverRawId.isEmpty()) {
                    Toast.makeText(this, "No chat partner specified", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("sendMessage", "CurrentUserRawId: " + currentUserId);
                Log.d("sendMessage", "ChatPartnerRawId: " + receiverRawId);

                // Create proper current user key and receiver key
                String currentUserKey = isAdmin ? FIREBASE_DB_ADMIN_PREFIX + currentUserId : FIREBASE_DB_USER_PREFIX + currentUserId;
                String receiverKey = (receiverRawId.startsWith(FIREBASE_DB_ADMIN_PREFIX) || receiverRawId.startsWith(FIREBASE_DB_USER_PREFIX))
                        ? receiverRawId
                        : (isAdmin ? FIREBASE_DB_USER_PREFIX + receiverRawId : FIREBASE_DB_ADMIN_PREFIX + receiverRawId);

                // Ensure chatKey is always user|admin
                String userKey = currentUserKey.startsWith("user_") ? currentUserKey : receiverKey;
                String adminKey = currentUserKey.startsWith("admin_") ? currentUserKey : receiverKey;
                String chatKey = userKey + "|" + adminKey;

                String newMessageKey = mController.generatePushKey(FIREBASE_DB_PATH_CHAT_NODE + "/" + chatKey);

                Log.d("sendMessage", "currentUserKey: " + currentUserKey);
                Log.d("sendMessage", "receiverKey: " + receiverKey);
                Log.d("sendMessage", "chatKey: " + chatKey);

                Map<String, Object> msgMap = new HashMap<>();
                msgMap.put("message", msg);
                msgMap.put("timestamp", System.currentTimeMillis());
                msgMap.put("senderId", currentUserId); // Only raw UID (without prefix)
                msgMap.put("receiverId", receiverRawId); // Only raw UID (without prefix)
                msgMap.put("type", "Text");

                mController.createData(FIREBASE_DB_PATH_CHAT_NODE + "/" + chatKey + "/" + newMessageKey, msgMap, (success, data) -> {
                    if (success) {
                        Log.d(TAG, "Message sent");
                        binding.msgTxt.setText("");
                        loadChats();
                    } else {
                        Log.e(TAG, "Failed to send message");
                    }
                });
            }
        });
    }

    private void loadChats() {
        mController.fetchUserChats(false, new FirebaseController.FirebaseCallback() {
            @Override
            public void onResponse(boolean isSuccess, Object data) {
                if (isSuccess && data instanceof Map) {
                    chatList.clear();
                    Map<String, Object> result = (Map<String, Object>) data;

                    Log.d(TAG, "onResponse: "+result);

                    chatList.clear(); // Clear existing data if needed

                    for (String key : result.keySet()) {
                        Object chatObj = result.get(key);
                        if (chatObj instanceof Map) {
                            Map<String, Object> chatData = (Map<String, Object>) chatObj;

                            Object messagesObj = chatData.get("messages");
                            if (messagesObj instanceof List) {
                                List<?> messages = (List<?>) messagesObj;
                                for (Object message : messages) {
                                    if (message instanceof Map) {
                                        // Convert each Map to HashMap explicitly
                                        chatList.add(new HashMap<>((Map<String, Object>) message));
                                    }
                                }
                            }
                        }
                    }


                    if (adapter == null) {
                        adapter = new MessagesRecycAdapter(ChatActivity.this, chatList);
                        binding.messagesRecycView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(ChatActivity.this, "No chats found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}