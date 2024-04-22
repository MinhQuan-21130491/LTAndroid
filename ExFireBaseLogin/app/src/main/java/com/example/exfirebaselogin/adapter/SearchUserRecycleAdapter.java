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
import com.example.exfirebaselogin.model.UserModel;
import com.example.exfirebaselogin.utils.AndroidUtils;
import com.example.exfirebaselogin.utils.FireBaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecycleAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecycleAdapter.UserModelViewHolder> {
    Context context;

    public SearchUserRecycleAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int i, @NonNull UserModel model) {
        holder.usernameText.setText(model.getUserName());
        holder.phonenumberText.setText(model.getPhoneNumber());
        if(model.getUserId().equals(FireBaseUtils.getUUIDOfUserCurrent())) {
            holder.usernameText.setText(model.getUserName()+"(Me)");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                AndroidUtils.setUserModelAsIntent(intent, model);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_search_user_cycle_row,parent,false);
        return new UserModelViewHolder(view);
    }

    public class UserModelViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        TextView phonenumberText;
        ImageView picProfile;
        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            phonenumberText = itemView.findViewById(R.id.phone_text);
            picProfile = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
