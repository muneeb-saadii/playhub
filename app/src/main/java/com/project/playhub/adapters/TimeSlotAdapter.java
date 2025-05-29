package com.project.playhub.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.playhub.R;

import java.util.ArrayList;
import java.util.HashMap;



public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

    Context mContext;
    setRecentTimeSlotClickList setRecentTimeSlotClickList;

    ArrayList<HashMap<String, String>> timeSlotList;

    public TimeSlotAdapter(Context context, ArrayList<HashMap<String, String>> timeSlotList) {
        this.mContext = context;
        this.timeSlotList = timeSlotList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_timeslot_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        HashMap<String, String> map=timeSlotList.get(position);
        String name = map.get("name");

        holder.stratTimeTxtView.setText(name);
        holder.stratselTimeTxtView.setText(name);
        holder.driverstratselTimeTxtView.setText(name);

        long diff = Long.parseLong(map.get("duration"));
        if (diff % 60 != 0) {
            ViewGroup.LayoutParams params = holder.driverstratselTimeTxtView.getLayoutParams();
//            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int widthInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, holder.driverstratselTimeTxtView.getContext().getResources().getDisplayMetrics());
            params.width = widthInPixels;
            holder.driverstratselTimeTxtView.setLayoutParams(params);
            holder.stratselTimeTxtView.setLayoutParams(params);
            holder.stratTimeTxtView.setLayoutParams(params);
        } else {

            ViewGroup.LayoutParams params = holder.driverstratselTimeTxtView.getLayoutParams();
            int widthInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, holder.driverstratselTimeTxtView.getContext().getResources().getDisplayMetrics());
            params.width = widthInPixels;
            holder.driverstratselTimeTxtView.setLayoutParams(params);
            holder.stratselTimeTxtView.setLayoutParams(params);
            holder.stratTimeTxtView.setLayoutParams(params);
        }


        String isAllowed = map.get("isAllowed");

        if (isAllowed != null && isAllowed.equalsIgnoreCase("No")) {
            holder.selmainarea.setVisibility(View.GONE);
            holder.mainarea.setVisibility(View.GONE);
            holder.driverUnselmainarea.setVisibility(View.VISIBLE);
            holder.mainarea.setClickable(false);
        } else {
            holder.driverUnselmainarea.setVisibility(View.GONE);
            holder.mainarea.setClickable(true);

            if (map.get("isSelected").toString().equalsIgnoreCase("yes")) {
                holder.selmainarea.setVisibility(View.VISIBLE);
                holder.mainarea.setVisibility(View.GONE);
            }else {
                holder.selmainarea.setVisibility(View.GONE);
                holder.mainarea.setVisibility(View.VISIBLE);
            }
        }

        holder.mainarea.setOnClickListener(v -> {

            if(map.get("isSelected").equalsIgnoreCase("yes")){
                map.put("isSelected", "No");
            }else{
                map.put("isSelected", "Yes");
            }

            if (setRecentTimeSlotClickList != null) {
                setRecentTimeSlotClickList.itemTimeSlotLocClick(position);
            }

            notifyItemChanged(position);
        });

        holder.selmainarea.setOnClickListener(v -> holder.mainarea.performClick());

//        holder.driverUnselmainarea.setOnClickListener(v -> generalFunctions.showMessage(holder.driverstratselTimeTxtView,LBL_PROVIDER_NOT_AVAIL_NOTE));
    }

    @Override
    public int getItemCount() {
        //  return recentList.size();
        // return 23;
        return timeSlotList.size();
    }

    public void setOnClickList(setRecentTimeSlotClickList setRecentTimeSlotClickList) {
        this.setRecentTimeSlotClickList = setRecentTimeSlotClickList;
    }

    public interface setRecentTimeSlotClickList {
        void itemTimeSlotLocClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView stratselTimeTxtView, driverstratselTimeTxtView,stratTimeTxtView;
        LinearLayout mainarea, selmainarea, driverUnselmainarea;


        public ViewHolder(View itemView) {
            super(itemView);

            stratTimeTxtView = (TextView) itemView.findViewById(R.id.stratTimeTxtView);
            mainarea = (LinearLayout) itemView.findViewById(R.id.mainarea);
            selmainarea = (LinearLayout) itemView.findViewById(R.id.selmainarea);
            driverUnselmainarea = (LinearLayout) itemView.findViewById(R.id.driverUnselmainarea);
            stratselTimeTxtView = (TextView) itemView.findViewById(R.id.stratselTimeTxtView);
            driverstratselTimeTxtView = (TextView) itemView.findViewById(R.id.driverstratselTimeTxtView);


        }
    }


}
