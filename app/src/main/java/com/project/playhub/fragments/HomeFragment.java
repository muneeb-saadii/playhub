package com.project.playhub.fragments;

import static com.project.playhub.utils.Constants.FIREBASE_DB_PATH_VENUES_NODE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.project.playhub.activities.admin.HomeActivity;
import com.project.playhub.activities.PersonalDetailsActivity;
import com.project.playhub.activities.VenueDetailsActivity;
import com.project.playhub.adapters.VenuesAdapter;
import com.project.playhub.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeActivity act;
    private ArrayList<HashMap<String, Object>> venues = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> tempVenues = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        act = (HomeActivity) getActivity();


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getVenues();
        setAdapter();
        setNoDataView(false);

    }

    private void setAdapter() {
        binding.btnSlider.setOnClickListener(v -> act.binding.navProfile.performClick());
        binding.userProfile.setOnClickListener(v -> startActivity(new Intent(requireContext(), PersonalDetailsActivity.class)));
        binding.welcomeTxt.setText(("Welcome "+HomeActivity.userData.get("name").toString()));
        binding.venuesRecycView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.venuesRecycView.setAdapter(new VenuesAdapter(requireContext(), venues, posi -> {
            HashMap<String, Object> item = venues.get(posi);
            Bundle bn = new Bundle();
            bn.putSerializable("venueData", item);
            Intent intent = new Intent(requireContext(), VenueDetailsActivity.class);
            intent.putExtras(bn);
            startActivity(intent);
        }));
    }

    private void getVenues() {
        act.mController.readData(FIREBASE_DB_PATH_VENUES_NODE, (isSuccess, data) -> {
            if (isSuccess) {
                DataSnapshot user = (DataSnapshot) data;
                setVenuesData(user);
            }
        });
    }

    private void setVenuesData(DataSnapshot data) {
        ArrayList<HashMap<String, Object>> venuesList = new ArrayList<>();
        GenericTypeIndicator<HashMap<String,Object>> t =
                new GenericTypeIndicator<HashMap<String,Object>>() {};

        for (DataSnapshot snap : data.getChildren()) {
            HashMap<String,Object> map = snap.getValue(t);
            if (map != null) {
                map.put("id", snap.getKey());
                // slots will already be in map as a List<Map<String,Object>>
                if(act.isAdmin && HomeActivity.userData.get("id").toString().equalsIgnoreCase(map.get("created_by").toString())) {
                    venuesList.add(map);
                }else if(!act.isAdmin){
                    venuesList.add(map);
                }
            }
        }
        venues.clear();
        venues.addAll(venuesList);

        if(!venues.isEmpty()){
            setNoDataView(true);
            binding.venuesRecycView.getAdapter().notifyDataSetChanged();
            setupSearchView();
        }
    }

    private void setupSearchView() {
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    if(tempVenues.isEmpty()) {
                        tempVenues.addAll(venues);
                    }
                    venues.clear();
                    tempVenues.forEach( data -> {
                        if(data.get("name").toString().toLowerCase().contains(s.toString().toLowerCase())){
                            venues.add(data);
                        }
                    });

                }else{
                    venues.clear();
                    venues.addAll(tempVenues);
                    tempVenues.clear();
                }
                binding.venuesRecycView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void setNoDataView(boolean isDataAvail){
        if(isDataAvail){
            binding.venuesRecycView.setVisibility(View.VISIBLE);
            binding.noDataTxt.setVisibility(View.GONE);
        }else {
            binding.venuesRecycView.setVisibility(View.GONE);
            binding.noDataTxt.setVisibility(View.VISIBLE);
        }
    }
}