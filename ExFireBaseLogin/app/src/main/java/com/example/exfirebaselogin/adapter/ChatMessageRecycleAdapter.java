package com.example.exfirebaselogin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exfirebaselogin.R;
import com.example.exfirebaselogin.model.ChatModel;
import com.example.exfirebaselogin.utils.FireBaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatMessageRecycleAdapter extends FirestoreRecyclerAdapter<ChatModel, ChatMessageRecycleAdapter.ChatModelViewHolder> {
    Context context;

    public ChatMessageRecycleAdapter(@NonNull FirestoreRecyclerOptions<ChatModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int i, @NonNull ChatModel model) {

        if(model.getSenderId().equals(FireBaseUtils.getUUIDOfUserCurrent())) {
            holder.layout_left.setVisibility(View.GONE);
            holder.layout_right.setVisibility(View.VISIBLE);
            holder.text_right.setText(model.getMessage());
        }else {
            holder.layout_left.setVisibility(View.VISIBLE);
            holder.layout_right.setVisibility(View.GONE);
            holder.text_left.setText(model.getMessage());
        }
    }

    @NonNull
    @Override
    public ChatMessageRecycleAdapter.ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_message_cycle_row,parent,false);
        return new ChatModelViewHolder(view);
    }


     class ChatModelViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_left, layout_right;
        TextView text_left, text_right;
        public ChatModelViewHolder(@NonNull View view) {
            super(view);
            layout_left = view.findViewById(R.id.layout_left);
            layout_right = view.findViewById(R.id.layout_right);
            text_left = view.findViewById(R.id.text_message_left);
            text_right = view.findViewById(R.id.text_message_right);
        }
    }
}

