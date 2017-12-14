package com.example.peter.a8mini.models;

/**
 * Created by Petertu on 18/11/2017.
 */

public class NotificationItem {
    String email, uid, img = null;

    @Override
    public String toString() {
        return "NotificationItem{" +
                "email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }

    public NotificationItem() {
    }

    public NotificationItem(String email, String uid, String img) {
        this.email = email;
        this.uid = uid;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
