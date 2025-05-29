package com.project.playhub.activities.admin;

import static com.project.playhub.utils.Constants.FIREBASE_DB_PATH_VENUES_NODE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.playhub.adapters.DatesRecyclerAdapter;
import com.project.playhub.adapters.TimeSlotAdapter;
import com.project.playhub.controller.FirebaseController;
import com.project.playhub.databinding.ActivityManageSlotsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ManageSlotsActivity extends AppCompatActivity {

    private ActivityManageSlotsBinding binding;
    ArrayList<HashMap<String, Object>> dateList = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, String>> timeSlotList = new ArrayList<>();
    ArrayList<HashMap<String, String>> selectedSlotList = new ArrayList<>();
    long durationInMinutes = 60L;
    
    DatesRecyclerAdapter datesAdapter;
    TimeSlotAdapter timesAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageSlotsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        setDateTimeList();
        initViews();
    }

    private void setDateTimeList() {

        if(getIntent().hasExtra("duration")) {
            durationInMinutes = Long.parseLong(getIntent().getStringExtra("duration"));
        }
        generateDateList();
        generateTimeSlots(dateList.get(0).get("date").toString());
    }

    public void generateDateList() {
        dateList.clear();
        selectedSlotList.clear();
        /*ArrayList<HashMap<String, String>>*/ dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());

        for (int i = 0; i < 7; i++) {
            HashMap<String, Object/*String*/> dateMap = new HashMap<>();
            dateMap.put("date", /*dateFormat.format(*/calendar.getTime());
            dateMap.put("date_str", displayFormat.format(calendar.getTime()));
            dateMap.put("day_name", dayNameFormat.format(calendar.getTime()));
            dateMap.put("isSelected", (i==0)?"Yes":"No");
            dateList.add(dateMap);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
//        Collections.reverse(dateList);

    }
    public void generateTimeSlots(String dateString) {
        timeSlotList.clear();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        try {
            Date parsedDate = dateFormat.parse(dateString);
            Calendar slotStart = Calendar.getInstance();
            slotStart.setTime(parsedDate);
            slotStart.set(Calendar.HOUR_OF_DAY, 0);
            slotStart.set(Calendar.MINUTE, 0);
            slotStart.set(Calendar.SECOND, 0);
            slotStart.set(Calendar.MILLISECOND, 0);

            Calendar endOfDay = (Calendar) slotStart.clone();
            endOfDay.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
            endOfDay.set(Calendar.HOUR_OF_DAY, 0);
            endOfDay.set(Calendar.MINUTE, 0);
            endOfDay.set(Calendar.SECOND, 0);
            endOfDay.set(Calendar.MILLISECOND, 0);

            Calendar now = Calendar.getInstance();

            while (true) {
                Calendar slotEnd = (Calendar) slotStart.clone();
                slotEnd.add(Calendar.MINUTE, (int) durationInMinutes);

                if (slotEnd.after(endOfDay)) {
                    break;
                }

                HashMap<String, String> slotMap = new HashMap<>();
                slotMap.put("start_time", timeFormat.format(slotStart.getTime()));
                slotMap.put("end_time", timeFormat.format(slotEnd.getTime()));
                slotMap.put("date", dateString);
                slotMap.put("name", formatTimeSlotName(slotStart, slotEnd));
                slotMap.put("isAllowed", now.before(slotStart) ? "Yes" : "No");
                slotMap.put("isSelected", "No");
                slotMap.put("duration", ""+durationInMinutes);
                timeSlotList.add(slotMap);

                slotStart.add(Calendar.MINUTE, (int) durationInMinutes);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String formatTimeSlotName(Calendar startTime, Calendar endTime) {
        String pattern = (durationInMinutes % 60 == 0) ? "h a" : "h:mm a";
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
        binding.timeslotRecyclerView.setLayoutManager(new GridLayoutManager(this, (durationInMinutes % 60 == 0)?3:2));
        binding.timeslotRecyclerView.setAdapter(timesAdapter);
        timesAdapter.setOnClickList(position -> {
            selectedSlotList.add(timeSlotList.get(position));
        });


        binding.submit.setOnClickListener(v -> createNewVenue());
    }

    private void createNewVenue() {
        if(selectedSlotList.isEmpty()){
            Toast.makeText(this, "Select at least one slot to proceed!", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedSlotList.forEach(data -> {
            data.remove("isSelected");
            data.put("isAvailable", "Yes");
        });

        Map<String, Object> venue = new HashMap<>();
        venue.put("name", getIntent().getStringExtra("name"));
        venue.put("address", getIntent().getStringExtra("address"));
        venue.put("desc", getIntent().getStringExtra("desc"));
        venue.put("image", getIntent().getStringExtra("image"));
        venue.put("price", getIntent().getStringExtra("price"));
        venue.put("slots", selectedSlotList);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.ENGLISH);
        venue.put("created_at", dateFormat.format(Calendar.getInstance().getTime()));
        venue.put("created_by", HomeActivity.userData.get("id"));

        FirebaseController controller = FirebaseController.getInstance();
        controller.createData(FIREBASE_DB_PATH_VENUES_NODE, venue, true, (isSuccess, data) -> {
            if(isSuccess){
                Toast.makeText(this, "Venue is created successfully!", Toast.LENGTH_SHORT).show();
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