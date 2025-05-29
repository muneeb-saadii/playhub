package com.project.playhub.controller;


import static com.project.playhub.utils.Constants.FIREBASE_DB_ADMIN_PREFIX;
import static com.project.playhub.utils.Constants.FIREBASE_DB_PATH_CHAT_NODE;
import static com.project.playhub.utils.Constants.FIREBASE_DB_PATH_USER_NODE;
import static com.project.playhub.utils.Constants.FIREBASE_DB_USER_PREFIX;

import android.content.Context;

import androidx.annotation.NonNull;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.project.playhub.utils.Constants;
import com.project.playhub.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FirebaseController {

    private static FirebaseController instance;
    private final FirebaseAuth mAuth;
    private final DatabaseReference mDatabase;
    private final Context context;
    private final SharedPrefsManager prefsManager;

    // Callback Interface
    public interface FirebaseCallback {
        void onResponse(boolean isSuccess, Object data);
    }

    // Private constructor for singleton pattern
    private FirebaseController(Context context) {
        this.context = context.getApplicationContext();
        FirebaseApp.initializeApp(this.context);
        mAuth = FirebaseAuth.getInstance();
        prefsManager = SharedPrefsManager.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public String generatePushKey(String path) {
        return FirebaseDatabase.getInstance().getReference(path).push().getKey();
    }

    // Initialize the controller in MainActivity
    public static void init(Context context) {
        if (instance == null) {
            instance = new FirebaseController(context);
        }
    }

    // Get the singleton instance
    public static FirebaseController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("FirebaseController is not initialized. Call init(context) in MainActivity.");
        }
        return instance;
    }

    // Authentication Methods

    public void registerUser(String email, String password, final FirebaseCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        callback.onResponse(true, user);
                    } else {
                        callback.onResponse(false, null);
                    }
                });
    }

    public void loginUser(String email, String password, final FirebaseCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        getLoginData(user, callback);
//                        callback.onResponse(true, user);
                    } else {
                        callback.onResponse(false, null);
                    }
                });
    }

    private void getLoginData(FirebaseUser user, FirebaseCallback callback) {
        mDatabase.child(FIREBASE_DB_PATH_USER_NODE).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onResponse(true, snapshot);
                } else {
                    callback.onResponse(false, null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResponse(false, null);
            }
        });
    }

    public void logoutUser() {
        mAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }


    //                  Realtime Database Methods
    public <T> void createData(String path, T data, final FirebaseCallback callback) {
        mDatabase.child(path).setValue(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onResponse(true, data);
                    } else {
                        callback.onResponse(false, null);
                    }
                });
    }

    public void createData(String path, Map<String, Object> data, boolean addId, final FirebaseCallback callback) {
        if(addId) {
            DatabaseReference ref = mDatabase.child(path).push().getRef();
            data.put("id", ref.getKey());
            ref.setValue(data)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onResponse(true, data);
                        } else {
                            callback.onResponse(false, null);
                        }
                    });
        }else{
            mDatabase.child(path).setValue(data)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onResponse(true, data);
                        } else {
                            callback.onResponse(false, null);
                        }
                    });
        }
    }

    public <T> void readData(String path, final FirebaseCallback callback) {
        mDatabase.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                data = snapshot.getValue();
                if (snapshot.getValue() != null) {
                    callback.onResponse(true, snapshot/*.getChildren()*/);
                } else {
                    callback.onResponse(false, null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResponse(false, null);
            }
        });
    }

    public void readDataToSnapshot(String path, final FirebaseCallback callback) {
        mDatabase.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onResponse(true, snapshot);
                } else {
                    callback.onResponse(false, null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResponse(false, null);
            }
        });
    }

    public void updateData(String path, Map<String, Object> updates, final FirebaseCallback callback) {
        mDatabase.child(path).updateChildren(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onResponse(true, updates);
                    } else {
                        callback.onResponse(false, null);
                    }
                });
    }

    public void deleteData(String path, final FirebaseCallback callback) {
        mDatabase.child(path).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onResponse(true, null);
                    } else {
                        callback.onResponse(false, null);
                    }
                });
    }

    private String getCurrentUserId(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return prefsManager.getString(Constants.PREFS_USER_ID);
        }else{
            return currentUser.getUid();
        }
    }

    public void fetchUserChats(boolean onlyRoot, final FirebaseCallback callback) {

        String currentUserId = getCurrentUserId();
        String _userKeyPrefix = FIREBASE_DB_USER_PREFIX + currentUserId;
        boolean isAdmin = false;

        if(prefsManager.getString(Constants.PREFS_IS_ADMIN).equalsIgnoreCase("yes")){
            _userKeyPrefix = FIREBASE_DB_ADMIN_PREFIX + currentUserId;
            isAdmin = true;
        }
        String userKeyPrefix = _userKeyPrefix;

        boolean finalIsAdmin = isAdmin;
        mDatabase.child(FIREBASE_DB_PATH_CHAT_NODE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> chatResult = new HashMap<>();
                List<String> senderIdsToFetch = new ArrayList<>();
                Map<String, List<HashMap<String, Object>>> messagesMap = new HashMap<>();

                for (DataSnapshot chatNode : snapshot.getChildren()) {
                    String chatKey = chatNode.getKey(); // e.g. user_abc123|admin_xyz123

                    if (chatKey != null && chatKey.contains(userKeyPrefix)) {

                        if(!onlyRoot) {
                            List<HashMap<String, Object>> messagesList = new ArrayList<>();
                            for (DataSnapshot messageNode : chatNode.getChildren()) {
                                Object msgObj = messageNode.getValue();
                                if (msgObj instanceof Map) {
                                    HashMap<String, Object> msgMap = (HashMap<String, Object>) msgObj;
                                    messagesList.add(msgMap);
                                }
                            }
                            messagesMap.put(chatKey, messagesList);
                        }else{
                            List<HashMap<String, Object>> messagesList = new ArrayList<>();
                            Object msgObj = null;
                            while(chatNode.getChildren().iterator().hasNext()){
                                msgObj = chatNode.getChildren().iterator().next().getValue();
                            }
                            if (msgObj instanceof Map) {
                                HashMap<String, Object> msgMap = (HashMap<String, Object>) msgObj;
                                messagesList.add(msgMap);
                            }
                            messagesMap.put(chatKey, messagesList);
                        }

                        // Extract sender ids from key
                        String[] parts = chatKey.split("\\|");
                        if (parts.length == 2 ) {
                            String senderId;
                            if(finalIsAdmin){
                                senderId = parts[0].substring(parts[0].lastIndexOf(FIREBASE_DB_USER_PREFIX));
                            }else{
                                senderId = parts[1].substring(parts[1].lastIndexOf(FIREBASE_DB_ADMIN_PREFIX));
                            }
                            senderIdsToFetch.add(senderId);
                        }
                    }
                }

                if(!onlyRoot){
                    if (messagesMap.isEmpty()) {
                        callback.onResponse(false, null);
                        return;
                    }
                }

                // Fetch sender details in one go
                mDatabase.child(FIREBASE_DB_PATH_USER_NODE).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot usersSnapshot) {
                        for (String senderId : /*messagesMap.keySet()*/senderIdsToFetch) {

                            DataSnapshot senderSnapshot = usersSnapshot.child(senderId);
                            String senderName = "";
                            String senderImage = "";

                            if (senderSnapshot.exists()) {
                                Object nameObj = senderSnapshot.child("name").getValue();
                                Object imageObj = senderSnapshot.child("image").getValue();
                                senderName = nameObj != null ? nameObj.toString() : "";
                                senderImage = imageObj != null ? imageObj.toString() : "";
                            }

                            Map<String, Object> chatData = new HashMap<>();
                            chatData.put("id", senderId);
                            chatData.put("name", senderName);
                            chatData.put("image", senderImage);
                            String chatKey = (finalIsAdmin)?FIREBASE_DB_USER_PREFIX+senderId+"|"+userKeyPrefix:userKeyPrefix+"|"+FIREBASE_DB_ADMIN_PREFIX+senderId;
                            chatData.put("messages", (onlyRoot)?messagesMap.get(0):messagesMap.get(chatKey));

                            chatResult.put(senderId, chatData);
                        }

                        callback.onResponse(true, chatResult);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onResponse(false, null);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResponse(false, null);
            }
        });
    }
}