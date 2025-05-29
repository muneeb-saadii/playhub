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

public class MessagesRecycAdapter extends RecyclerView.Adapter<MessagesRecycAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HashMap<String, Object>> messageList = new ArrayList<>();

    public MessagesRecycAdapter(Context mContext, ArrayList<HashMap<String, Object>> messageList){
        this.mContext = mContext;
        this.messageList = messageList;
    }


    public interface OnItemClickListener{
        public void onItemClickListener(int posi);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_ui, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, Object> msgItem = messageList.get(position);
        if(msgItem == null && msgItem.isEmpty()){
            return;
        }

        /*dataMap.put("eUserType", "entrant");
        dataMap.put("Text", "hey there");
        dataMap.put("iGroupId", "48");
        dataMap.put("senderName", "myName");
        dataMap.put("senderImage", "");
        dataMap.put("senderId", ManageApp.getMemberId(getActContext()));
        dataMap.put("vTimeZone", GeneralFunctions.getCurrentDateHourMin());
        dataMap.put("vChatImg", "");
        dataMap.put("isChatImg", "No");*/

        holder.dateTxt.setText(msgItem.get("vDate").toString());

        if(msgItem.get("senderId").toString().equalsIgnoreCase("")){

            holder.rightMsgArea.setVisibility(View.VISIBLE);
            holder.leftMsgArea.setVisibility(View.GONE);

            holder.rightMsgTxt.setText(msgItem.get("Text").toString());
            holder.rightTimeTxt.setText(getHourMinOnly(msgItem.get("vTimeZone").toString()));
            String str = msgItem.get("senderImage").toString();

            str = "";
            if(!str.equalsIgnoreCase("null") && !str.trim().equals("")){
                /*Picasso.get().load(str)
                        .error(R.drawable.ic_no_pic_user)
                        .placeholder(R.drawable.ic_no_pic_user)
                        .into(holder.siv_rightUserImg);*/
            }
        }else{

            holder.leftMsgArea.setVisibility(View.VISIBLE);
            holder.rightMsgArea.setVisibility(View.GONE);

            holder.leftMsgTxt.setText(msgItem.get("Text").toString());
            holder.leftTimeTxt.setText(getHourMinOnly(msgItem.get("vTimeZone").toString()));
            String str = msgItem.get("senderImage").toString();

            if(!str.equalsIgnoreCase("null") && !str.trim().equals("")){
                /*Picasso.get().load(str)
                        .error(R.drawable.ic_no_pic_user)
                        .placeholder(R.drawable.ic_no_pic_user)
                        .into(holder.siv_leftUserImg);*/
            }
        }
    }

    private String getHourMinOnly(String inputTime) {

        // Parse the input string into a Date object
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = inputFormat.parse(inputTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return inputTime;
        }
        // Format the date to "hh:mm" format
        DateFormat outputFormat = new SimpleDateFormat("HH:mm");
        String formattedTime = outputFormat.format(date);

        return formattedTime;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout rightMsgArea, leftMsgArea;
        TextView rightMsgTxt, leftMsgTxt;
        TextView rightTimeTxt, leftTimeTxt;
        TextView dateTxt;
        ShapeableImageView siv_rightUserImg, siv_leftUserImg;

        public ViewHolder(@NonNull View item) {
            super(item);

            rightMsgArea = (LinearLayout) item.findViewById(R.id.rightMsgArea);
            leftMsgArea = (LinearLayout) item.findViewById(R.id.leftMsgArea);
            rightMsgTxt = (TextView) item.findViewById(R.id.rightMsgTxt);
            leftMsgTxt = (TextView) item.findViewById(R.id.leftMsgTxt);
            rightTimeTxt = (TextView) item.findViewById(R.id.rightTimeTxt);
            leftTimeTxt = (TextView) item.findViewById(R.id.leftTimeTxt);
            siv_rightUserImg = (ShapeableImageView) item.findViewById(R.id.siv_rightUserImg);
            siv_leftUserImg = (ShapeableImageView) item.findViewById(R.id.siv_leftUserImg);
            dateTxt = (TextView) item.findViewById(R.id.dateTxt);
        }
    }
}
