package com.example.exfirebaselogin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exfirebaselogin.adapter.RecentRecycleAdapter;
import com.example.exfirebaselogin.adapter.SearchUserRecycleAdapter;
import com.example.exfirebaselogin.model.ChatRoomModel;
import com.example.exfirebaselogin.model.UserModel;
import com.example.exfirebaselogin.utils.FireBaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class FragmentChat extends Fragment {
    RecyclerView recyclerView;
    RecentRecycleAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recycle_view);
        setUpRecycleView();
        return view;
    }
    void setUpRecycleView() {
        Query query = FireBaseUtils.getAllChatRoomReference().
                whereArrayContains("userIds", FireBaseUtils.getUUIDOfUserCurrent()).
                orderBy("lastMessageTimestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatRoomModel> options = new FirestoreRecyclerOptions.Builder<ChatRoomModel>().setQuery(query, ChatRoomModel.class).build();
        adapter = new RecentRecycleAdapter(options, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}