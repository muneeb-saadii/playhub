package com.project.playhub.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.playhub.activities.admin.AuthActivity;
import com.project.playhub.activities.BankDetailsActivity;
import com.project.playhub.activities.admin.HomeActivity;
import com.project.playhub.activities.PersonalDetailsActivity;
import com.project.playhub.databinding.FragmentProfileBinding;
import com.project.playhub.utils.Constants;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private HomeActivity act;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        act = (HomeActivity) getActivity();

        setViews();
        initListeners();

        return binding.getRoot();
    }

    private void setViews() {
        binding.userNameTxt.setText(HomeActivity.userData.get("name").toString());
        binding.userEmailTxt.setText(HomeActivity.userData.get("email").toString());
    }

    private void initListeners() {
        if(act.isAdmin){
            binding.bankInfoArea.setVisibility(View.VISIBLE);
        }else{
            binding.bankInfoArea.setVisibility(View.GONE);
        }
        binding.bankInfoArea.setOnClickListener(v -> {
            Bundle bn = new Bundle();
            bn.putString("id", HomeActivity.userData.get("id").toString());
            Intent intent = new Intent(requireActivity(), BankDetailsActivity.class);
            intent.putExtras(bn);
            startActivity(intent);
        });
        binding.personalInfoArea.setOnClickListener(v -> startActivity(new Intent(requireContext(), PersonalDetailsActivity.class)));
        binding.editProfile.setOnClickListener(v -> startActivity(new Intent(requireContext(), PersonalDetailsActivity.class)));

        binding.logoutArea.setOnClickListener(v -> {
            act.mController.logoutUser();
            act.prefsManager.remove(Constants.PREFS_FIREBASE_USER);
            act.prefsManager.saveString(Constants.PREFS_IS_LOGIN, "Yes");
            act.prefsManager.saveString(Constants.PREFS_IS_ADMIN, "No");
            act.prefsManager.remove(Constants.PREFS_USER_ID);
            startActivity(new Intent(requireContext(), AuthActivity.class));
            act.finishAffinity();
        });
    }
}