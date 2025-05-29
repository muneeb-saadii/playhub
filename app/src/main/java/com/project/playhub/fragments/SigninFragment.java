package com.project.playhub.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.project.playhub.R;
import com.project.playhub.activities.AuthActivity;
import com.project.playhub.activities.HomeActivity;
import com.project.playhub.activities.MainActivity;
import com.project.playhub.databinding.DoubleCardLayoutBinding;
import com.project.playhub.utils.Constants;
import com.project.playhub.utils.SharedPrefsManager;

import java.util.HashMap;


public class SigninFragment extends Fragment {

    private static final int RC_SIGN_IN = 100;
    private static final String TAG = SigninFragment.class.getSimpleName();
    private DoubleCardLayoutBinding b;
    private AuthActivity act = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DoubleCardLayoutBinding.inflate(inflater, container, false);

        Log.i(TAG, "onCreateView");

        b.otpArea.rootArea.setVisibility(View.GONE);

        b.gotoSignup.setOnClickListener(v -> {
            if (act != null) {
                act.showSignupFragment();
            }
        });

        b.forgetPassTxt.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                act.showForgetPassFragment();
            }
        });

        b.tvType1.setOnClickListener(v -> {
            if (!b.etField2.getText().toString().isEmpty() && !b.etField3.getText().toString().isEmpty()) {
                b.pbar.setVisibility(View.VISIBLE);
                b.tvType1.setVisibility(View.GONE);
                act.mController.loginUser(b.etField2.getText().toString(), b.etField3.getText().toString(), (isSuccess, data) -> {
                    if (isSuccess) {
                        DataSnapshot user = (DataSnapshot) data;
                        setUserData(user);
                        startActivity(new Intent(requireContext().getApplicationContext(), HomeActivity.class));
                        requireActivity().finish();
//                        Log.d("FirebaseAuth", "Registration successful: " + user.getEmail());
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.e("FirebaseAuth", "Registration failed.");
                    }
                    b.pbar.setVisibility(View.GONE);
                    b.tvType1.setVisibility(View.VISIBLE);
                });
//                viewModel.loginUser(b.etField2.getText().toString(), b.etField3.getText().toString());
            }
            else {
//                myApp.showMessage(v, "Please enter all fields");
            }
        });

        return b.getRoot();
    }

    private void setUserData(DataSnapshot user) {
        HashMap<String, String> data = (HashMap<String, String>) user.getValue();
        act.prefsManager.saveMap(Constants.PREFS_FIREBASE_USER, data);
        act.prefsManager.saveString(Constants.PREFS_IS_LOGIN, "yes");
        act.prefsManager.saveString(Constants.PREFS_IS_ADMIN, data.get("isAdmin"));
        act.prefsManager.saveString(Constants.PREFS_USER_ID, data.get("id"));
        /*act.mController.readData(FIREBASE_DB_PATH_USER+"/"+user.getUid(), b.etField3.getText().toString(), (isSuccess, data) -> {
            if (isSuccess) {
                FirebaseUser user = (FirebaseUser) data;
                setUserData(user);
                Log.d("FirebaseAuth", "Registration successful: " + user.getEmail());
            } else {
                Log.e("FirebaseAuth", "Registration failed.");
            }
        });*/
    }


    @Override
    public void onResume() {
        super.onResume();
        act = ((AuthActivity) getActivity());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObservers();


    }

    private void setupObservers() {

//                Log.e(TAGS.API_SUCCESS, "Response: " + response);
//                String msg = ((Resource.Success<String>) response).getData();
//                myApp.showToast(msg);
//                startActivity(new Intent(requireActivity(), MainActivity.class));
//                requireActivity().finishAffinity();



//                Log.e(TAGS.API_SUCCESS, "Response: " + response);
//                String msg = ((Resource.Success<String>) response).getData();
//                myApp.showToast(msg);
//                startActivity(new Intent(requireActivity(), MainActivity.class));
//                requireActivity().finishAffinity();

    }
}
