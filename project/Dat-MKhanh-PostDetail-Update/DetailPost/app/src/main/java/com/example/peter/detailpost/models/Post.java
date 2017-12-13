package com.example.peter.detailpost.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by peter on 9/12/2017.
 */

public class Post implements Comparable<Post> {
    String uid, stt, nickName, dateTime, urlImg = null, day, month, year, imgUser = null, keyPost;
    ArrayList<Cmt> listCmt;
    ArrayList<String> listLike;
    ArrayList<String> listCmtUser;

    public Post() {
        listCmt = null;
        nickName = null;
    }

    public ArrayList<Cmt> getListCmt() {
        return listCmt;
    }

    public ArrayList<String> getListCmtUser() {
        return listCmtUser;
    }

    public void setListCmtUser(ArrayList<String> listCmtUser) {
        this.listCmtUser = listCmtUser;
    }

    public void setListCmt(ArrayList<Cmt> listCmt) {
        this.listCmt = listCmt;
    }

    public Post(String stt, String nickName, String dateTime, String urlImg, ArrayList<Cmt> arrCmt) {
        this.stt = stt;
        this.nickName = nickName;
        this.dateTime = dateTime;
        this.urlImg = urlImg;
        this.listCmt = arrCmt;
    }

    public ArrayList<String> getListLike() {
        return listLike;
    }

    public void setListLike(ArrayList<String> listLike) {
        this.listLike = listLike;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKeyPost() {
        return keyPost;
    }

    public void setKeyPost(String keyPost) {
        this.keyPost = keyPost;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDateTime() {

        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
        try {
            day = dateTime.substring(0, 2);
            month = dateTime.substring(3, 5);
            year = dateTime.substring(6, dateTime.indexOf(" "));
        }catch (Exception e){

        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "uid='" + uid + '\'' +
                ", stt='" + stt + '\'' +
                ", nickName='" + nickName + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", urlImg='" + urlImg + '\'' +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", imgUser='" + imgUser + '\'' +
                ", keyPost='" + keyPost + '\'' +
                ", arrCmt=" + listCmt +
                ", listLike=" + listLike +
                '}';
    }


    public ArrayList<Cmt> getArrCmt() {
        return listCmt;
    }

    @Override
    public int compareTo(@NonNull Post post) {
        /*int i = 0;
        if (dateTime.compareTo(post.getDateTime()) > 0) {
            i = -1;
        } else if (dateTime.compareTo(post.getDateTime()) < 0) {
            i = 1;
        }*/

        return post.dateTime.compareTo(dateTime);
    }

}
