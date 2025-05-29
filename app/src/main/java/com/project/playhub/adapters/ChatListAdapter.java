package com.project.playhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.playhub.R;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    private final List<Map<String, Object>> chatList;
    private final Context context;

    public ChatListAdapter(Context context, List<Map<String, Object>> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_parant_chat_view, parent, false);
        return new ChatViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Map<String, Object> chatItem = chatList.get(position);

        String adminName = (String) chatItem.get("name");
        String adminImage = (String) chatItem.get("image");
        List<HashMap<String, Object>> messages = (List<HashMap<String, Object>>) chatItem.get("messages");

        // Sort messages by timestamp descending to get the latest
        if (messages != null && !messages.isEmpty()) {
            Collections.sort(messages, (m1, m2) -> {
                long t1 = ((Number) m1.get("timestamp")).longValue();
                long t2 = ((Number) m2.get("timestamp")).longValue();
                return Long.compare(t2, t1); // descending
            });

            HashMap<String, Object> lastMsg = messages.get(0);
            String messageType = (String) lastMsg.get("messageType");
            String previewMessage;

            if ("Image".equalsIgnoreCase(messageType)) {
                previewMessage = "[Image]";
            } else {
                previewMessage = (String) lastMsg.get("message");
            }

                holder.userName.setText(adminName + ": " + previewMessage);

            long timestamp = ((Number) lastMsg.get("timestamp")).longValue();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault());
            holder.dateText.setText(sdf.format(new Date(timestamp)));

        } else {
            holder.dateText.setText("No messages");
        }

        holder.userName.setText(adminName != null ? adminName : "Unknown");

        if (adminImage != null && !adminImage.isEmpty()) {
            Glide.with(context).load(adminImage).into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.ic_no_pic_user); // fallback image
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView userName, dateText;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            userName = itemView.findViewById(R.id.user_name);
            dateText = itemView.findViewById(R.id.date);
        }
    }
}
