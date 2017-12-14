package com.example.peter.timeline.models;

import android.support.annotation.NonNull;

/**
 * Created by peter on 10/12/2017.
 */

public class NotifyPost implements Comparable<NotifyPost> {
    public static final String NOTIFY_TYPE_NEW_POST = "notify_new_post";
    public static final String NOTIFY_TYPE_NEW_CMT = "notify_new_cmt";

    String uid, type, nickName, img, key, dateTime;

    public NotifyPost() {
    }

    public NotifyPost(String uid, String type, String nickName, String img, String key, String dateTime) {
        this.uid = uid;
        this.type = type;
        this.nickName = nickName;
        this.img = img;
        this.key = key;
        this.dateTime = dateTime;
    }

    public NotifyPost(String key, String uid, String type, String nickName, String img) {
        this.uid = uid;
        this.type = type;
        this.nickName = nickName;
        this.img = img;
        this.key = key;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(@NonNull NotifyPost notifyPost) {
        int i = 0;
        if(dateTime.compareTo(notifyPost.getDateTime()) > 0){
            i = -1;
        }else if(dateTime.compareTo(notifyPost.getDateTime()) < 0){
            i = 1;
        }
        return i;
    }
}
