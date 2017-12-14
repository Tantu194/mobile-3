package com.example.peter.a8mini.models;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by Petertu on 18/11/2017.
 */

public class Friend {
    private String img = null, nickName;
    private String email, uid;

    public Friend() {
    }
    public Friend(String img, String email) {
        this.img = img;
        this.email = email;
    }

    public Friend(String img, String email, String uid) {
        this.img = img;
        this.email = email;
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public static byte[] imagViewToByte(ImageView imgV){
        try {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgV.getDrawable();
            Bitmap bmp = bitmapDrawable.getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bytes = bos.toByteArray();
            return bytes;
        }catch (Exception e){
            return null;
        }
    }
}
