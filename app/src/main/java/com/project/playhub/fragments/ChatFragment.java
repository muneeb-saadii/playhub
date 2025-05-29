package com.project.playhub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.project.playhub.R;
import com.project.playhub.activities.HomeActivity;
import com.project.playhub.adapters.ChatListAdapter;
import com.project.playhub.controller.FirebaseController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatFragment extends Fragment {

    private HomeActivity act;
    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private List<Map<String, Object>> chatList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.chats_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        act = ((HomeActivity) getActivity());
        loadChats();
    }

    private void loadChat() {

        act.mController.readData("chats", (isSuccess, data) -> {
            if (isSuccess) {
                DataSnapshot snapshot = (DataSnapshot) data;
                for (DataSnapshot chatNode : snapshot.getChildren()) {

                }

                Log.d("chats", ""+data.toString());
            }
        });
    }
    private void loadChats() {
        act.mController.fetchUserChats(true, new FirebaseController.FirebaseCallback() {
            @Override
            public void onResponse(boolean isSuccess, Object data) {
                if (isSuccess && data instanceof Map) {
                    chatList.clear();
                    Map<String, Object> result = (Map<String, Object>) data;

                    for (String key : result.keySet()) {
                        Object chatObj = result.get(key);
                        if (chatObj instanceof Map) {
                            chatList.add((Map<String, Object>) chatObj);
                        }
                    }

                    if (adapter == null) {
                        adapter = new ChatListAdapter(getContext(), chatList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getContext(), "No chats found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
