package com.example.android.bluetoothchat.Model;

/**
 * Created by kk on 11/22/2017.
 */

public class Communicate {
    private int person;
    private String mess;



    public Communicate() {
    }

    public Communicate(int person, String mess) {
        this.person = person;
        this.mess = mess;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }
}
