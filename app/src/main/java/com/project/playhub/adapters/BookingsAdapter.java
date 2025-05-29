package com.project.playhub.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.imageview.ShapeableImageView;
import com.project.playhub.R;
import com.project.playhub.activities.BankDetailsActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.ViewHolder> {

    Context mContext;
    boolean isAdmin = false;
    ArrayList<HashMap<String, Object>> bookingsList = new ArrayList<>();
    OnItemClickListener onItemClickListener;

    public BookingsAdapter(Context mContext, ArrayList<HashMap<String, Object>> bookingsList, boolean isAdmin, OnItemClickListener onItemClickListener){
        this.mContext = mContext;
        this.bookingsList = bookingsList;
        this.isAdmin = isAdmin;
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener{
        public void onItemClick(int posi, boolean isConfirmed);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_booking_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, Object> msgItem = bookingsList.get(position);
        if(msgItem == null && msgItem.isEmpty()){
            return;
        }
        String status = "";
        holder.button.setVisibility(View.GONE);
        if(msgItem.containsKey("isExpired") && msgItem.get("isExpired").equals("Yes")){
            status = "Expired";
        }else if(msgItem.containsKey("isConfirmed") && msgItem.get("isConfirmed").equals("Yes")){
            status = "Confirmed";
        }else if(msgItem.containsKey("isBooked") && msgItem.get("isBooked").equals("Yes")){
            status = "Booked";
            if(isAdmin) {
                holder.button.setVisibility(View.VISIBLE);
            }
        }else /*if(msgItem.containsKey("isAvailable") && msgItem.get("isAvailable").equals("Yes"))*/{
            status = "Available";
        }
        holder.details.setText(status);
        holder.dateTxt.setText(msgItem.get("name").toString());
        holder.price.setText("Rs: "+msgItem.get("price").toString());

        holder.button.setOnClickListener(v -> {
            onItemClickListener.onItemClick(position, true);
        });
        holder.chat.setOnClickListener(v -> {
//            onItemClickListener.onItemClick(position, true);
        });
        holder.bank.setOnClickListener(v -> {
            Bundle bn = new Bundle();
            bn.putString("id", msgItem.get("venueCreatorId").toString());
            Intent intent = new Intent((Activity)mContext, BankDetailsActivity.class);
            intent.putExtras(bn);
            mContext.startActivity(intent);
        });
        if(isAdmin){
            holder.bank.setVisibility(View.GONE);
        }else{
            holder.bank.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return bookingsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView button;
        TextView dateTxt;
        TextView details;
        TextView price;
        ImageView chat, bank;

        public ViewHolder(@NonNull View item) {
            super(item);

            dateTxt = (TextView) item.findViewById(R.id.date);
            price = (TextView) item.findViewById(R.id.price);
            details = (TextView) item.findViewById(R.id.status);
            button = (TextView) item.findViewById(R.id.approve);
            chat = (ImageView) item.findViewById(R.id.chat);
            bank = (ImageView) item.findViewById(R.id.adminBank);
        }
    }
}
