package com.example.exfirebaselogin.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exfirebaselogin.ChatActivity;
import com.example.exfirebaselogin.R;
import com.example.exfirebaselogin.model.ChatRoomModel;
import com.example.exfirebaselogin.model.UserModel;
import com.example.exfirebaselogin.utils.AndroidUtils;
import com.example.exfirebaselogin.utils.FireBaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecentRecycleAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, RecentRecycleAdapter.ChatRoomModelViewHolder> {
    Context context;

    public RecentRecycleAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRoomModelViewHolder holder, int i, @NonNull ChatRoomModel model) {
        FireBaseUtils.getUserInChatRoom(model.getUserIds()).get().addOnCompleteListener(task ->  {
            if(task.isSuccessful()) {
                UserModel userModel = task.getResult().toObject(UserModel.class);
                holder.usernameText.setText(userModel.getUserName());
            }
        });
        holder.lastMessageTime.setText(FireBaseUtils.formatTimeStamp(model.getLastMessageTimestamp()));
        holder.lastMessageText.setText(model.getLastMessage());
    }

    @NonNull
    @Override
    public ChatRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_recent_chat_row,parent,false);
        return new ChatRoomModelViewHolder(view);
    }

    public class ChatRoomModelViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        TextView lastMessageText;
        TextView lastMessageTime;
        ImageView picProfile;
        public ChatRoomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            lastMessageTime = itemView.findViewById(R.id.time_last_text);
            picProfile = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
