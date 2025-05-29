package com.project.playhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.imageview.ShapeableImageView;
import com.project.playhub.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class VenuesAdapter extends RecyclerView.Adapter<VenuesAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HashMap<String, Object>> venuesList = new ArrayList<>();
    OnItemClickListener onItemClickListener;

    public VenuesAdapter(Context mContext, ArrayList<HashMap<String, Object>> venuesList, OnItemClickListener onItemClickListener){
        this.mContext = mContext;
        this.venuesList = venuesList;
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener{
        public void onItemClick(int posi);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_applications, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, Object> msgItem = venuesList.get(position);
        if(msgItem == null && msgItem.isEmpty()){
            return;
        }
        holder.name.setText(msgItem.get("name").toString());
        holder.loc.setText(msgItem.get("address").toString());
        holder.dateTxt.setText(msgItem.get("created_at").toString());
        holder.price.setText("Rs: "+msgItem.get("price").toString());

        holder.details.setOnClickListener(v -> {
            onItemClickListener.onItemClick(position);
        });
    }


    @Override
    public int getItemCount() {
        return venuesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, loc;
        TextView dateTxt;
        TextView details;
        TextView price;

        public ViewHolder(@NonNull View item) {
            super(item);

            dateTxt = (TextView) item.findViewById(R.id.date);
            name = (TextView) item.findViewById(R.id.name);
            price = (TextView) item.findViewById(R.id.price);
            details = (TextView) item.findViewById(R.id.details);
            loc = (TextView) item.findViewById(R.id.availability);
        }
    }
}
