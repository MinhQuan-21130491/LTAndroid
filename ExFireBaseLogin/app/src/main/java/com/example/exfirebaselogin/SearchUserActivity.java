package com.example.exfirebaselogin;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exfirebaselogin.adapter.SearchUserRecycleAdapter;
import com.example.exfirebaselogin.model.UserModel;
import com.example.exfirebaselogin.utils.FireBaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class SearchUserActivity extends AppCompatActivity {
    ImageButton btn_back;
    ImageButton btn_search;
    EditText inputUsername;
    RecyclerView recyclerView;
    SearchUserRecycleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        btn_back = findViewById(R.id.btn_back);
        inputUsername = findViewById(R.id.search_user_input);
        btn_search = findViewById(R.id.ic_search_user_btn);
        recyclerView = findViewById(R.id.search_user_recycle_view);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_search.setOnClickListener(v ->{
            String searchTerm = inputUsername.getText().toString();
            if(searchTerm.isEmpty() || searchTerm.length() < 2) {
                inputUsername.setError("Invalid UserName");
                return;
            }
            setUpSearchRecycleView(searchTerm);
        });
    }
     void setUpSearchRecycleView(String searchTerm) {
        Query query = FireBaseUtils.getAllUserCollectionReference().whereGreaterThanOrEqualTo("userName", searchTerm);
        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query, UserModel.class).build();
        adapter = new SearchUserRecycleAdapter(options, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}