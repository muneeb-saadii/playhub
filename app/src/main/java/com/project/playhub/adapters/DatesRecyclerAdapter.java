package com.project.playhub.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.playhub.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class DatesRecyclerAdapter extends RecyclerView.Adapter<DatesRecyclerAdapter.ViewHolder> {

    ArrayList<HashMap<String, Object>> listData;
    Context mContext;
    OnDateSelectListener onDateSelectListener;

    Date selectedDate;


    public DatesRecyclerAdapter(ArrayList<HashMap<String, Object>> listData, Context mContext, Date selectedDate) {
        this.listData = listData;
        this.mContext = mContext;
        this.selectedDate = selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dates_design, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    public void setOnDateSelectListener(OnDateSelectListener onDateSelectListener) {
        this.onDateSelectListener = onDateSelectListener;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final HashMap<String, Object> item = listData.get(position);
        final Date currentDate = (Date) listData.get(position).get("date");

        if (item.get("day_name") instanceof String) {
            holder.dayTxtView.setText((String) item.get("day_name"));
        }

        if (item.get("date_str") instanceof String) {
            holder.dayNumTxtView.setText((String) item.get("date_str"));
        }


        if (selectedDate.equals(currentDate) || item.get("isSelected").toString().equalsIgnoreCase("yes")) {
//            new CreateRoundedView(mContext.getResources().getColor(R.color.appThemeColor_1), btnRadius, Utils.dipToPixels(mContext, 1),
//                    Color.parseColor("#CECECE"), holder.dayNumTxtView);
            item.put("isSelected", "No");
            holder.dayNumTxtView.setTextColor(Color.parseColor("#FFFFFF"));

            holder.dayNumTxtView.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.dayTxtView.setTextColor(mContext.getResources().getColor(R.color.white));

            holder.cardview.setCardBackgroundColor(mContext.getResources().getColor(R.color.appTheme_primary));


        } else {
            int color = Color.parseColor("#ffffff");
//            new CreateRoundedView(color, btnRadius, Utils.dipToPixels(mContext, 1), color, holder.dayNumTxtView);
//            holder.dayNumTxtView.setTextColor(Color.parseColor("#1C1C1C"));
            holder.dayNumTxtView.setTextColor(Color.parseColor("#202020"));
            holder.dayTxtView.setTextColor(Color.parseColor("#808080"));

            holder.cardview.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
        }


        holder.containerView.setOnClickListener(view -> {
            setSelectedDate(currentDate);

            if (onDateSelectListener != null) {
                onDateSelectListener.onDateSelect(position);
            }
        });

    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listData.size();
    }


    public interface OnDateSelectListener {
        void onDateSelect(int position);
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView dayTxtView;
        private TextView dayNumTxtView;
        private View containerView;
        private CardView cardview;

        public ViewHolder(View view) {
            super(view);

            containerView = view;
            dayTxtView = (TextView) view.findViewById(R.id.dayTxtView);
            dayNumTxtView = (TextView) view.findViewById(R.id.dayNumTxtView);
            cardview = (CardView) view.findViewById(R.id.cardview);

        }
    }
}
