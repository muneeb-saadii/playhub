package com.project.playhub.fragments;

import static com.project.playhub.utils.Constants.FIREBASE_DB_PATH_BOOKINGS_NODE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.project.playhub.activities.admin.HomeActivity;
import com.project.playhub.adapters.BookingsAdapter;
import com.project.playhub.databinding.FragmentBookingBinding;

import java.util.ArrayList;
import java.util.HashMap;


public class BookingFragment extends Fragment {

    private FragmentBookingBinding binding;
    private HomeActivity act;
    private BookingsAdapter adapter;
    ArrayList<HashMap<String, Object>> bookingsList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookingBinding.inflate(getLayoutInflater(), container, false);
        act = (HomeActivity) getActivity();

        setAdapter();
        getBookings();

        return binding.getRoot();
    }

    private void setAdapter() {
        binding.bookingsRecycView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new BookingsAdapter(act, bookingsList, act.isAdmin, (posi, isConfirmed) -> {
            if(isConfirmed) approveBookingSlot(posi);
        });
        binding.bookingsRecycView.setAdapter(adapter);
    }

    private void approveBookingSlot(int pos) {
        bookingsList.get(pos).put("isConfirmed", "Yes");
        String path = FIREBASE_DB_PATH_BOOKINGS_NODE + "/" + bookingsList.get(pos).get("id");
        act.mController.updateData(path, bookingsList.get(pos), (isSuccess, data) -> {
            if(isSuccess){
                Toast.makeText(requireContext(), "Slot approved successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBookings() {
        binding.noBookingTxt.setVisibility(View.VISIBLE);
        act.mController.readData(FIREBASE_DB_PATH_BOOKINGS_NODE, (isSuccess, data) -> {
            if (isSuccess) {
                bookingsList.clear();
                DataSnapshot root = (DataSnapshot) data;

                for (DataSnapshot child : root.getChildren()) {
                    @SuppressWarnings("unchecked")
                    HashMap<String,Object> booking =
                            (HashMap<String,Object>) child.getValue(new GenericTypeIndicator<HashMap<String,Object>>() {});

                    if (booking == null) continue;

                    if(act.isAdmin) {
                        Object vci = booking.get("venueCreatorId");
                        if (vci != null &&
                                vci.toString().equalsIgnoreCase(HomeActivity.userData.get("id").toString())) {
                            bookingsList.add(booking);
                        }
                    }else {
                        Object vci = booking.get("created_by");
                        if (vci != null &&
                                vci.toString().equalsIgnoreCase(HomeActivity.userData.get("id").toString())) {
                            bookingsList.add(booking);
                        }
                    }
                }

                if (!bookingsList.isEmpty()) {
                    binding.noBookingTxt.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }

        });
    }
}