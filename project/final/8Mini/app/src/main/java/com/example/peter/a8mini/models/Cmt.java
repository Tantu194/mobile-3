package com.example.peter.a8mini.models;

import android.support.annotation.NonNull;

/**
 * Created by peter on 9/12/2017.
 */

public class Cmt implements Comparable<Cmt>{
    String name, content, dateTime;
    String imgUser;

    public Cmt() {
    }

    public Cmt(String name, String content, String dateTime, String imgUser) {
        this.name = name;
        this.content = content;
        this.dateTime = dateTime;
        this.imgUser = imgUser;
    }

    @Override
    public String toString() {
        return "Cmt{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", imgUser='" + imgUser + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }

    @Override
    public int compareTo(@NonNull Cmt cmt) {
        return cmt.dateTime.compareTo(dateTime);
    }
}
