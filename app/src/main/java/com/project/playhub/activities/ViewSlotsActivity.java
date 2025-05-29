package com.project.playhub.activities;

import static com.project.playhub.utils.Constants.FIREBASE_DB_PATH_BOOKINGS_NODE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.playhub.activities.admin.HomeActivity;
import com.project.playhub.adapters.DatesRecyclerAdapter;
import com.project.playhub.adapters.TimeSlotAdapter;
import com.project.playhub.controller.FirebaseController;
import com.project.playhub.databinding.ActivityViewSlotsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


public class ViewSlotsActivity extends AppCompatActivity {

    private ActivityViewSlotsBinding binding;
    ArrayList<HashMap<String, Object>> dateList = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, String>> timeSlotList = new ArrayList<>();
    HashMap<String, Object> selectedSlot = new HashMap<>();
    ArrayList<HashMap<String, String>> dates = new ArrayList<>();

    DatesRecyclerAdapter datesAdapter;
    TimeSlotAdapter timesAdapter;
    HashMap<String, Object> venue = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewSlotsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getData();
        initViews();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("venueData")) {
            venue = (HashMap<String, Object>) bundle.getSerializable("venueData");
        }else{
            Toast.makeText(this, "Unable to fetch Venue's data.", Toast.LENGTH_SHORT).show();
            finish();
        }
        setDateTimeList();
    }

    private void setDateTimeList() {


        generateDateList();
        if(!dateList.isEmpty()) {
            generateTimeSlots(dateList.get(0).get("date").toString());
        }
        int x = dateList.size();
    }

    public void generateDateList() {
        dateList.clear();
        selectedSlot.clear();

        dates = (ArrayList<HashMap<String, String>>) venue.get("slots");
        if (dates == null || dates.isEmpty()) return;

        Set<String> uniqueDates = new HashSet<>();

        for (int i = 0; i < dates.size(); i++) {
            HashMap<String, String> data = dates.get(i);
            String rawDate = data.get("date");
            if (rawDate == null) continue;

            try {
                // Fix the timezone by removing colon in GMT+05:00 → GMT+0500
                rawDate = rawDate.replaceAll("GMT\\+(\\d{2}):(\\d{2})", "GMT+$1$2");

                // Parse the fixed date
                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                Date parsedDate = inputFormat.parse(rawDate);

                // Format to date string for uniqueness (e.g., "2025-05-20")
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String dateStr = outputFormat.format(parsedDate);

                // Ensure uniqueness based on formatted date string
                if (uniqueDates.add(dateStr)) {
                    HashMap<String, Object> dataObj = new HashMap<>(/*data*/);
                    dataObj.put("date", parsedDate);
                    dataObj.put("isSelected", (dateList.isEmpty() ? "Yes" : "No")); // Select only first by default
                    dateList.add(dataObj);
                }

            } catch (ParseException e) {
                e.printStackTrace(); // You can also log this
            }
        }
    }

    public void generateTimeSlots(String dateString) {
        // 1) Clear out old list
        timeSlotList.clear();

        // 2) Grab the raw list of all slots
        @SuppressWarnings("unchecked")
        ArrayList<HashMap<String, String>> allSlots =
                (ArrayList<HashMap<String, String>>) venue.get("slots");
        if (allSlots == null) return;

        // 3) For each slot, if its "date" field equals our dateString, copy it in:
        Calendar now = Calendar.getInstance();
        SimpleDateFormat inputFormat =
                new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat timeFormat =
                new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        for (HashMap<String, String> slot : allSlots) {
            String slotDateRaw = slot.get("date");
            if (slotDateRaw == null) continue;

            if (!slotDateRaw.equals(dateString)) {
                // skip any slots that aren’t for the requested date
                continue;
            }

            // Copy the original map into a new one (so we don’t mutate the venue data)
            HashMap<String, String> copy = new HashMap<>(slot);

            // Optionally add isAllowed / isSelected
            try {
                // parse the raw date
                Date parsed = inputFormat.parse(slotDateRaw);
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(parsed);

                String startTime = copy.get("start_time");
                Date st = new SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                        .parse(startTime);

                Calendar stCal = Calendar.getInstance();
                stCal.setTime(parsed);
                Calendar timeOnly = Calendar.getInstance();
                timeOnly.setTime(st);
                stCal.set(Calendar.HOUR_OF_DAY, timeOnly.get(Calendar.HOUR_OF_DAY));
                stCal.set(Calendar.MINUTE, timeOnly.get(Calendar.MINUTE));

                boolean allowed = now.before(stCal);
                copy.put("isExpired", allowed ? "No" : "Yes");
            } catch (ParseException e) {
                // if parsing fails, default to allowed
                copy.put("isExpired", "No");
            }

            // First one selected by default?
            /*if (timeSlotList.isEmpty()) {
                copy.put("isSelected", "Yes");
            } else {
            }             */
            copy.put("isSelected", "No");

            // Finally add to your filtered list
            timeSlotList.add(copy);
        }
    }

    public String formatTimeSlotName(Calendar startTime, Calendar endTime) {
        String pattern = (60 % 60 == 0) ? "h a" : "h:mm a";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        String start = formatter.format(startTime.getTime());
        String end = formatter.format(endTime.getTime());
        return start + " - " + end;
    }

    private void initViews() {
        Calendar startDate = Calendar.getInstance(Locale.getDefault());
        startDate.add(Calendar.MONTH, 0);

        binding.backImgView.setOnClickListener(v -> finish());

        datesAdapter = new DatesRecyclerAdapter(dateList, this, startDate.getTime());
        datesAdapter.setOnDateSelectListener(position -> {
            generateTimeSlots(dateList.get(position).get("date").toString());
            timesAdapter.notifyDataSetChanged();

        });
        binding.datesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.datesRecyclerView.setAdapter(datesAdapter);
        binding.datesRecyclerView.scrollToPosition(0);
//        datesAdapter.notifyItemChanged(0);


        timesAdapter = new TimeSlotAdapter(this, timeSlotList);
        binding.timeslotRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.timeslotRecyclerView.setAdapter(timesAdapter);
        timesAdapter.setOnClickList(position -> {
            if(!selectedSlot.isEmpty()) {
                int index = timeSlotList.indexOf(selectedSlot);
                if(index >= 0) {
                    timeSlotList.get(index).put("isSelected", "No");
                    timesAdapter.notifyItemChanged(index);
                    selectedSlot.clear();
                }
            }
            selectedSlot.putAll(timeSlotList.get(position));
        });

        binding.submit.setOnClickListener(v -> boolSlot());
    }

    private void boolSlot() {
        if(selectedSlot.isEmpty()){
            Toast.makeText(this, "Select any one slot to proceed!", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedSlot.remove("isSelected");
        selectedSlot.put("isBooked", "Yes");
        selectedSlot.put("venueId", venue.get("id"));
        selectedSlot.put("venueCreatorId", venue.get("created_by"));
        selectedSlot.put("price", venue.get("price"));


        FirebaseController controller = FirebaseController.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.ENGLISH);
        selectedSlot.put("created_at", dateFormat.format(Calendar.getInstance().getTime()));
        selectedSlot.put("created_by", HomeActivity.userData.get("id").toString());

        controller.createData(FIREBASE_DB_PATH_BOOKINGS_NODE, selectedSlot, true, (isSuccess, data) -> {
            if(isSuccess){
                Toast.makeText(this, "Slot is booked successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finishAffinity();
            }else{
                Toast.makeText(this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}