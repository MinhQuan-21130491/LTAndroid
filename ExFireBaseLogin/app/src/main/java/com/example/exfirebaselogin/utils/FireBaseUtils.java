package com.example.exfirebaselogin.utils;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

public class FireBaseUtils {
    //Phương thức này được sử dụng để lấy UUID (User ID) của người dùng hiện tại đang đăng nhập vào ứng dụng. Nó sử dụng FirebaseAuth.getInstance().getUid() để lấy UUID của người dùng đang đăng nhập thông qua Firebase Authentication.
    public static String getUUIDOfUserCurrent() {
        return FirebaseAuth.getInstance().getUid();
    }
    //Phương thức này kiểm tra xem người dùng có đang đăng nhập không. Nó kiểm tra xem UUID của người dùng hiện tại có khác null không. Nếu UUID không phải null, nghĩa là người dùng đã đăng nhập và trả về true, ngược lại trả về false.
    public static boolean isLoggedIn() {
        if(getUUIDOfUserCurrent() != null) {
//            Log.i("uuid", getUUIDOfUserCurrent());
            return true;
        }
        return false;
    }
    //Phương thức này được sử dụng để lấy tham chiếu tới tài liệu (document) trong Firestore chứa thông tin chi tiết của người dùng hiện tại. Nó sử dụng UUID của người dùng hiện tại để xác định vị trí của tài liệu trong Firestore. Phương thức này trả về một tham chiếu tới tài liệu đó.
    public static DocumentReference getCurrentUserDetail() {
        return FirebaseFirestore.getInstance().collection("Users").document(getUUIDOfUserCurrent());
    }
    public static CollectionReference getAllUserCollectionReference() {
        return FirebaseFirestore.getInstance().collection("Users");
    }
    public static DocumentReference getChatRoomReference(String chatRoomId) {
        return FirebaseFirestore.getInstance().collection("ChatRooms").document(chatRoomId);
    }
    public static CollectionReference getChatInRoomReference(String chatRoomId) {
       return getChatRoomReference(chatRoomId).collection("Chats");
    }
    public static CollectionReference getAllChatRoomReference() {
        return FirebaseFirestore.getInstance().collection("ChatRooms");
    }
    public static DocumentReference getUserInChatRoom(List<String> userIds) {
        if(userIds.get(0).equals(getUUIDOfUserCurrent())) {
            return getAllUserCollectionReference().document(userIds.get(1));
        }else {
            return getAllUserCollectionReference().document(userIds.get(0));
        }
    }
    public static String getChatRoomId(String userId1, String userId2) {
        if(userId1.hashCode() < userId2.hashCode()) {
            return userId1 + "_" + userId2;
        }
        return userId2 + "_" + userId1;
    }
    public static String formatTimeStamp(Timestamp timestamp) {
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }
}
