package com.example.exfirebaselogin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.exfirebaselogin.utils.AndroidUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {
    String phoneNumber;
    EditText otpText;
    Button nextButton;
    ProgressBar progressBar;
    TextView resendOtp;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Long timeOut = 60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        phoneNumber = getIntent().getExtras().getString("phone");
        AndroidUtils.showToast(getApplicationContext(), phoneNumber);
        init();
        sendOtp(phoneNumber, false  );
        action();
    }

     void action() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredInput = otpText.getText().toString();
                //So sánh 2 mã xác thực với nhau trả về PhoneAuthCredential sau đó dùng FireBaseAuth để xử lý đăng nhập với signInWithCredential()
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredInput);
                signIn(credential);
            }
        });
        // khi ấn nhận lại mã
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtp(phoneNumber, true);
            }
        });
    }

    public void init() {
        otpText = findViewById(R.id.login_otp);
        nextButton = findViewById(R.id.login_next_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        resendOtp = findViewById(R.id.resend_otp_textview);
    }
    public void sendOtp(String phoneNumber, boolean isResend) {
        startResendTimer();
        setInProgress(true);
        //Time out là thời gian mã còn tác dụng Ex: 60s
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth).
                                            setPhoneNumber(phoneNumber).
                                            setTimeout(timeOut, TimeUnit.SECONDS).
                                            setActivity(this).
                                            setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            //Tự động xác thực thành công
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
                setInProgress(false);
            }

            @Override
            //Tự động xác thực thất bại
            public void onVerificationFailed(@NonNull FirebaseException e) {
                AndroidUtils.showToast(getApplicationContext(), "OTP verification failed!");
                setInProgress(false);
            }

            @Override
            //Nhận otp về số điện thoại nếu tự động xác thực thất bại
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                resendingToken = forceResendingToken;
                AndroidUtils.showToast(getApplicationContext(), "OTP sent successfully!");
                setInProgress(false);
            }
        });
        if(isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else {
            PhoneAuthProvider.verifyPhoneNumber((builder.build()));
        }
    }
    public void setInProgress(boolean inProgress) {
        if(inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
        }
    }
    public void signIn(PhoneAuthCredential phoneAuthCredential) {
        setInProgress(true);
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Nếu xác thực thành công thì intent
                if(task.isSuccessful()) {
                    Intent intent = new Intent(LoginOtpActivity.this, LoginUserNameActivity.class);
                    intent.putExtra("phone", phoneNumber);
                    startActivity(intent);
                }else { // thất bại thì thông báo lên màn hình = toast
                    AndroidUtils.showToast(getApplicationContext(), "OTP is incorrect!");
                    setInProgress(false);
                }
            }
        });
    }

    public void startResendTimer() {
        resendOtp.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeOut--;
                // Sử dụng Handler để cập nhật giao diện người dùng từ luồng khác
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resendOtp.setText("Resend OTP in " + timeOut + "  seconds");
                    }
                });
                if (timeOut <= 0) {
                    timeOut = 60L;
                    timer.cancel();
                    // Đảm bảo cập nhật UI khi kết thúc đếm ngược
                    Handler handler2 = new Handler(Looper.getMainLooper());
                    handler2.post(new Runnable() {
                        @Override
                        public void run() {
                            resendOtp.setEnabled(true);
                        }
                    });
                }
            }
        }, 0, 1000);
    }

}