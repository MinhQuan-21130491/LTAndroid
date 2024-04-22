package com.example.exfirebaselogin.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.exfirebaselogin.model.UserModel;

public class AndroidUtils {

        public static void showToast(Context context, String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
        public static void setUserModelAsIntent(Intent intent, UserModel userModel) {
            intent.putExtra("username", userModel.getUserName());
            intent.putExtra("phonenumber", userModel.getPhoneNumber());
            intent.putExtra("userId", userModel.getUserId());
        }
        public static UserModel getUserModelFromIntent(Intent intent) {
            UserModel userModel = new UserModel();
            userModel.setUserName(intent.getStringExtra("username"));
            userModel.setPhoneNumber(intent.getStringExtra("phonenumber"));
            userModel.setUserId(intent.getStringExtra("userId"));
            return userModel;
        }
}
