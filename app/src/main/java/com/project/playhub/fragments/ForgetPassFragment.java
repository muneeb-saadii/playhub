package com.project.playhub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.playhub.activities.AuthActivity;
import com.project.playhub.databinding.DoubleCardLayoutBinding;
import com.project.playhub.databinding.LayoutOtpSixDigitsBinding;


import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;


public class ForgetPassFragment extends Fragment {

    private static final String TAG = ForgetPassFragment.class.getSimpleName();
    private DoubleCardLayoutBinding b;
    private boolean isOtpView = false;

    public ForgetPassFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = DoubleCardLayoutBinding.inflate(inflater, container, false);


        configViews(false);

        b.backImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOtpView) {
                    configViews(true);
                } else {
                    if (getActivity() instanceof AuthActivity) {
                        ((AuthActivity) getActivity()).showSigninFragment();
                    }
                }
            }
        });

        b.tvType1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOtpView) {
                    String email = b.etField2.getText().toString().trim();
                    if (!email.isEmpty()) {
//                        viewModel.resetLinkToMail(email);
                    } else {
                        Toast.makeText(getContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle OTP verification here
                    // For example, collect OTP digits and verify
                    String otp = b.otpArea.etOtp1.getText().toString() +
                            b.otpArea.etOtp2.getText().toString() +
                            b.otpArea.etOtp3.getText().toString() +
                            b.otpArea.etOtp4.getText().toString() +
                            b.otpArea.etOtp5.getText().toString() +
                            b.otpArea.etOtp6.getText().toString();
                    // Implement OTP verification logic
                }
            }
        });

        setupObservers();
        new SetupOtpSwitches(b.otpArea, requireActivity());

        return b.getRoot();
    }

    private void configViews(boolean isToggle) {
        if (isToggle) {
            isOtpView = !isOtpView;
        }

        if (!isOtpView) {
            b.titleTxt.setText("Reset Password");
            b.sloganTxt.setText("Enter email to get reset link");
            b.tvType1.setText("Reset Password");
            b.signupOptnArea.setVisibility(View.GONE);
            b.operationArea.setVisibility(View.GONE);
            b.field1Area.setVisibility(View.GONE);
            b.backImgView.setVisibility(View.VISIBLE);
            b.field2Area.setVisibility(View.VISIBLE);
            b.field3Area.setVisibility(View.GONE);
            b.field4Area.setVisibility(View.GONE);
            b.otpArea.rootArea.setVisibility(View.GONE);
            b.etField2.invalidate();
        } else {
            b.titleTxt.setText("Reset Password");
            b.sloganTxt.setText("Enter OTP we've sent to your email for verification");
            b.tvType1.setText("Verify");
            b.field2Area.setVisibility(View.GONE);
            b.otpArea.rootArea.setVisibility(View.VISIBLE);
            b.otpArea.etOtp1.invalidate();
        }
    }

    private void setupObservers() {

//                    Log.e(TAGS.API_SUCCESS, "Response: " + response.getData());
//                    Toast.makeText(getContext(), response.getData(), Toast.LENGTH_SHORT).show();
//                    if (getActivity() instanceof AuthActivity) {
//                        ((AuthActivity) getActivity()).showSigninFragment();
//                    }


//                    Log.e(TAGS.API_ERR, "An error occurred: " + response.getMessage());
//                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
//                    if (getActivity() instanceof AuthActivity) {
//                        ((AuthActivity) getActivity()).showSigninFragment();
//                    }

    }

    private static class SetupOtpSwitches {

        public SetupOtpSwitches(LayoutOtpSixDigitsBinding area, Activity context) {
            area.etOtp1.addTextChangedListener(new GenericTextWatcher(area.etOtp1, area.etOtp2));
            area.etOtp2.addTextChangedListener(new GenericTextWatcher(area.etOtp2, area.etOtp3));
            area.etOtp3.addTextChangedListener(new GenericTextWatcher(area.etOtp3, area.etOtp4));
            area.etOtp4.addTextChangedListener(new GenericTextWatcher(area.etOtp4, area.etOtp5));
            area.etOtp5.addTextChangedListener(new GenericTextWatcher(area.etOtp5, area.etOtp6));
            area.etOtp6.addTextChangedListener(new GenericTextWatcher(area.etOtp6, null));

            area.etOtp1.setOnKeyListener(new GenericKeyEvent(area.etOtp1, null));
            area.etOtp2.setOnKeyListener(new GenericKeyEvent(area.etOtp2, area.etOtp1));
            area.etOtp3.setOnKeyListener(new GenericKeyEvent(area.etOtp3, area.etOtp2));
            area.etOtp4.setOnKeyListener(new GenericKeyEvent(area.etOtp4, area.etOtp3));
            area.etOtp5.setOnKeyListener(new GenericKeyEvent(area.etOtp5, area.etOtp4));
            area.etOtp6.setOnKeyListener(new GenericKeyEvent(area.etOtp6, area.etOtp5));
        }

        private static class GenericTextWatcher implements TextWatcher {

            private final View currentView;
            private final View nextView;

            public GenericTextWatcher(View currentView, View nextView) {
                this.currentView = currentView;
                this.nextView = nextView;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && nextView != null) {
                    nextView.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        }

        private static class GenericKeyEvent implements View.OnKeyListener {

            private final View currentView;
            private final View previousView;

            public GenericKeyEvent(View currentView, View previousView) {
                this.currentView = currentView;
                this.previousView = previousView;
            }

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (previousView != null) {
                        previousView.requestFocus();
                    } else {
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                    return true;
                }
                return false;
            }
        }
    }
}
