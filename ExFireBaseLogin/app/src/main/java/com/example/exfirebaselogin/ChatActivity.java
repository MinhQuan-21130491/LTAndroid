package com.example.exfirebaselogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exfirebaselogin.adapter.ChatMessageRecycleAdapter;
import com.example.exfirebaselogin.model.ChatModel;
import com.example.exfirebaselogin.model.ChatRoomModel;
import com.example.exfirebaselogin.model.UserModel;
import com.example.exfirebaselogin.utils.AndroidUtils;
import com.example.exfirebaselogin.utils.FireBaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
    TextView usernameText;
    ImageButton btn_back;
    ImageButton btn_send;
    EditText inputMessage;
    RecyclerView recyclerView;
    UserModel userModel;
    String chatRoomId;
    ChatRoomModel chatRoomModel;
    ChatMessageRecycleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btn_back = findViewById(R.id.btn_back);
        btn_send = findViewById(R.id.message_btn_send);
        inputMessage = findViewById(R.id.message_input_text);
        usernameText = findViewById(R.id.user_name_text);
        recyclerView = findViewById(R.id.recycle_view_message) ;

        Intent intent = getIntent();
        userModel = AndroidUtils.getUserModelFromIntent(intent);
        usernameText.setText(userModel.getUserName());
        chatRoomId = FireBaseUtils.getChatRoomId(FireBaseUtils.getUUIDOfUserCurrent(), userModel.getUserId());

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getOrCreateChatRoomModel();
        setUpChatRecycleView();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = inputMessage.getText().toString().trim();
                if(message.isEmpty()) return;
                sendMessage(message);
            }
        });

    }
    void sendMessage(String message) {
        //set lại người gửi cuối cùng trong room chat
        chatRoomModel.setLastMessageSender(FireBaseUtils.getUUIDOfUserCurrent());
        //set lại tin nhắn cuối cùng
        chatRoomModel.setLastMessage(message);
        //set lại thời gian gửi cuối cùng trong room chat
        chatRoomModel.setLastMessageTimestamp(Timestamp.now());
        FireBaseUtils.getChatRoomReference(chatRoomId).set(chatRoomModel);

        ChatModel chatModel = new ChatModel(message,FireBaseUtils.getUUIDOfUserCurrent(), Timestamp.now());
        FireBaseUtils.getChatInRoomReference(chatRoomId).add(chatModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()) {
                    inputMessage.setText("");
                }
            }
        });
    }
    void getOrCreateChatRoomModel() {
        FireBaseUtils.getChatRoomReference(chatRoomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                    if(chatRoomModel == null) {
                        //Lần tạo chat đầu tiên
                        chatRoomModel = new ChatRoomModel(chatRoomId,
                                                          Arrays.asList(FireBaseUtils.getUUIDOfUserCurrent(), userModel.getUserId()),
                                                          Timestamp.now(), "", "");
                        FireBaseUtils.getChatRoomReference(chatRoomId).set(chatRoomModel);
                    }
                }
            }
        });
    }
    void setUpChatRecycleView() {
        Query query = FireBaseUtils.getChatInRoomReference(chatRoomId).orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatModel> options = new FirestoreRecyclerOptions.Builder<ChatModel>().setQuery(query, ChatModel.class).build();
        adapter = new ChatMessageRecycleAdapter(options, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setItemPrefetchEnabled(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        // khi nhắn tin nhắn mới tự động cuộn xuống
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }
}