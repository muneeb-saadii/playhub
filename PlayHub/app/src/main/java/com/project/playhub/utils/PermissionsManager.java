package com.project.playhub.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import java.util.HashMap;
import java.util.Map;

public class PermissionsManager {

    public interface PermissionCallback {
        void onResult(boolean isGranted);
    }

    private static PermissionsManager instance;
    private final Context context;
    private final Map<String, ActivityResultLauncher<String>> launchers = new HashMap<>();
    private final Map<String, PermissionCallback> callbacks = new HashMap<>();

    private PermissionsManager(Context context, ActivityResultCaller caller) {
        this.context = context.getApplicationContext();
        initializeLaunchers(caller);
    }

    public static void init(Context context, ActivityResultCaller caller) {
        if (instance == null) {
            instance = new PermissionsManager(context, caller);
        }
    }

    public static PermissionsManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PermissionManager is not initialized. Call init(context, caller) in your MainActivity.");
        }
        return instance;
    }

    private void initializeLaunchers(ActivityResultCaller caller) {
        registerLauncher(caller, Manifest.permission.CAMERA);
        registerLauncher(caller, Manifest.permission.READ_EXTERNAL_STORAGE);
        registerLauncher(caller, Manifest.permission.POST_NOTIFICATIONS);
        registerLauncher(caller, Manifest.permission.ACCESS_FINE_LOCATION);
        registerLauncher(caller, Manifest.permission.CALL_PHONE);
        registerLauncher(caller, Manifest.permission.RECORD_AUDIO);
    }

    private void registerLauncher(ActivityResultCaller caller, String permission) {
        ActivityResultLauncher<String> launcher = caller.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    PermissionCallback callback = callbacks.get(permission);
                    if (callback != null) {
                        callback.onResult(isGranted);
                        callbacks.remove(permission);
                    }
                }
        );
        launchers.put(permission, launcher);
    }

    private void requestPermission(String permission, PermissionCallback callback) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            callback.onResult(true);
        } else {
            callbacks.put(permission, callback);
            ActivityResultLauncher<String> launcher = launchers.get(permission);
            if (launcher != null) {
                launcher.launch(permission);
            } else {
                callback.onResult(false);
            }
        }
    }

    public void requestCameraPermission(PermissionCallback callback) {
        requestPermission(Manifest.permission.CAMERA, callback);
    }

    public void requestStoragePermission(PermissionCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermission(Manifest.permission.READ_MEDIA_IMAGES, callback);
        } else {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, callback);
        }
    }

    public void requestNotificationPermission(PermissionCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermission(Manifest.permission.POST_NOTIFICATIONS, callback);
        } else {
            // Notifications are enabled by default on earlier versions
            callback.onResult(true);
        }
    }

    public void requestLocationPermission(PermissionCallback callback) {
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, callback);
    }

    public void requestCallPermission(PermissionCallback callback) {
        requestPermission(Manifest.permission.CALL_PHONE, callback);
    }

    public void requestAudioPermission(PermissionCallback callback) {
        requestPermission(Manifest.permission.RECORD_AUDIO, callback);
    }

    public void checkInternetPermission(PermissionCallback callback) {
        // INTERNET permission is normal and granted at install time
        callback.onResult(true);
    }
}
/*
PermissionManager.getInstance().requestCameraPermission(isGranted -> {
        if (isGranted) {
        Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
                    });
*/