package com.project.playhub.utils;


import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class SharedPrefsManager {

    private static final String PREF_NAME = "app_shared_prefs";
    private static SharedPrefsManager instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    // Private constructor to enforce Singleton pattern
    private SharedPrefsManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    // Initialize the SharedPrefsManager in MainActivity
    public static void init(Context context) {
        if (instance == null) {
            instance = new SharedPrefsManager(context);
        }
    }

    // Retrieve the singleton instance
    public static SharedPrefsManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SharedPrefsManager is not initialized. Call init(context) in your MainActivity.");
        }
        return instance;
    }

    // Save a String value
    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    // Retrieve a String value
    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    // Save a custom object
    public <T> void saveObject(String key, T object) {
        String json = gson.toJson(object);
        editor.putString(key, json);
        editor.apply();
    }

    // Retrieve a custom object
    public <T> T getObject(String key, Class<T> classOfT) {
        String json = sharedPreferences.getString(key, null);
        return (json != null) ? gson.fromJson(json, classOfT) : null;
    }
    public void saveMap(String key, HashMap<String, String> map) {
        String json = gson.toJson(map);
        editor.putString(key, json);
        editor.apply();
    }

    // Retrieve HashMap<String, String> from SharedPreferences
    public HashMap<String, String> getMap(String key) {
        String json = sharedPreferences.getString(key, null);
        if (json == null) {
            return new HashMap<>();
        }
        Type type = new TypeToken<HashMap<String, String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Remove a specific key
    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }

    // Clear all preferences
    public void clear() {
        editor.clear();
        editor.apply();
    }
}

/*
SharedPrefsManager prefs = SharedPrefsManager.getInstance();
prefs.saveString("username", "JohnDoe");
String username = prefs.getString("username");

UserData user = new UserData("John", "Doe", 30);
prefs.saveObject("user_data", user);
UserData retrievedUser = prefs.getObject("user_data", UserData.class);
*/