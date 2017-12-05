package com.example.nguyenkim.appmini.DaTaModel;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by NguyenKim on 02/12/2017.
 */

public class Profile {
    String name;
    int avt;
    String time;
    String status;
    int imgLoad;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvt() {
        return avt;
    }

    public void setAvt(int avt) {
        this.avt = avt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getImgLoad() {
        return imgLoad;
    }

    public void setImgLoad(int imgLoad) {
        this.imgLoad = imgLoad;
    }
}
