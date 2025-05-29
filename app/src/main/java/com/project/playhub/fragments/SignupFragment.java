package com.project.playhub.fragments;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.project.playhub.R;
import com.project.playhub.activities.AuthActivity;
import com.project.playhub.controller.FirebaseController;
import com.project.playhub.databinding.DoubleCardLayoutBinding;
import com.project.playhub.models.UserData;


public class SignupFragment extends Fragment {

    private static final String TAG = SignupFragment.class.getSimpleName();
    private DoubleCardLayoutBinding b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DoubleCardLayoutBinding.inflate(inflater, container, false);

        Log.i(TAG, "onCreateView");

        b.titleTxt.setText("Sign Up");
        b.sloganTxt.setText("Please signup to create new account");
        b.tvType1.setText("Sign Up");
        b.signupOptnArea.setVisibility(View.GONE);
        b.operationArea.setVisibility(View.GONE);
        b.backImgView.setVisibility(View.VISIBLE);
        b.field1Area.setVisibility(View.VISIBLE);
        b.field2Area.setVisibility(View.VISIBLE);
        b.field3Area.setVisibility(View.VISIBLE);
        b.field4Area.setVisibility(View.VISIBLE);
        b.otpArea.rootArea.setVisibility(View.GONE);

        b.backImgView.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).showSigninFragment();
            }
        });

        b.tvType1.setOnClickListener(v -> {
            String name = b.etField1.getText().toString().trim();
            String email = b.etField2.getText().toString().trim();
            String password = b.etField3.getText().toString().trim();
            String confirmPassword = b.etField4.getText().toString().trim();

            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            b.pbar.setVisibility(View.VISIBLE);
            b.tvType1.setVisibility(View.GONE);
            FirebaseController.getInstance().registerUser(email, password, (isSuccess, data) -> {
                if (isSuccess) {
                    FirebaseUser user = (FirebaseUser) data;
                    UserData userData = new UserData(user.getUid(), "", name, email, "No");

                    // Store user data in Realtime Database under "Users/userId"
                    FirebaseController.getInstance().createData("Users/" + user.getUid(), userData, (dbSuccess, dbData) -> {
                        if (dbSuccess) {
                            Toast.makeText(getContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                            if (getActivity() instanceof AuthActivity) {
                                ((AuthActivity) getActivity()).showSigninFragment();
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to save user data", Toast.LENGTH_SHORT).show();
                        }
                    });
                    b.pbar.setVisibility(View.GONE);
                    b.tvType1.setVisibility(View.VISIBLE);
                } else {
                    b.pbar.setVisibility(View.GONE);
                    b.tvType1.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });


        /*b.tvType1.setOnClickListener(v -> {
            if (!b.etField3.getText().toString().equals(b.etField4.getText().toString())) {
//                myApp.showMessage(v, "Passwords don't match!");
                return;
            }
            if (!b.etField1.getText().toString().isEmpty() && !b.etField2.getText().toString().isEmpty()
                    && !b.etField3.getText().toString().isEmpty() && !b.etField4.getText().toString().isEmpty()) {
//                viewModel.registerUser(b.etField2.getText().toString(), b.etField3.getText().toString());
            } else {
//                myApp.showMessage(v, "Please enter all fields");
            }
        });*/

        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//
//                Log.e(TAGS.API_SUCCESS, "Response: " + response);
//                String msg = ((Resource.Success<String>) response).getData();
//                myApp.showToast(msg);
//                if (getActivity() instanceof AuthActivity) {
//                    ((AuthActivity) getActivity()).showSigninFragment();
//                }
    }

}
