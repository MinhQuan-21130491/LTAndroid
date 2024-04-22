package com.example.exfirebaselogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.exfirebaselogin.model.UserModel;
import com.example.exfirebaselogin.utils.FireBaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUserNameActivity extends AppCompatActivity {
    EditText inputUserName;
    Button letMeInBtn;
    ProgressBar progressBar;
    UserModel userModel;

    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_name);

        init();
        phoneNumber = getIntent().getExtras().getString("phone").toString();
        getUserName();
        letMeInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserName();
            }
        });

    }
    void init() {
        progressBar = findViewById(R.id.login_progress_bar);
        inputUserName = findViewById(R.id.login_username);
        letMeInBtn = findViewById(R.id.login_let_me_in_btn);
    }

    void setUserName() {
        setInProgress(true);
        String userName = inputUserName.getText().toString();
        if(userName.isEmpty() || userName.length() < 2) {
            inputUserName.setError("UserName length should be at least 2 chars");
            return;
        }
        if(userModel != null) {
            userModel.setUserName(userName);
        }else {
            userModel = new UserModel(userName, phoneNumber, Timestamp.now(), FireBaseUtils.getUUIDOfUserCurrent());
        }
        FireBaseUtils.getCurrentUserDetail().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Intent intent = new Intent(LoginUserNameActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }
    void getUserName() {
        FireBaseUtils.getCurrentUserDetail().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()) {
                    UserModel userModel =  task.getResult().toObject(UserModel.class);
                    if(userModel != null) {
                        inputUserName.setText(userModel.getUserName());
                    }
                }
            }
        });
    }
    public void setInProgress(boolean inProgress) {
        if(inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            letMeInBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            letMeInBtn.setVisibility(View.VISIBLE);
        }
    }
}