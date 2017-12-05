package com.example.nguyenkim.appmini;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.*;
import android.widget.ListView;

import com.example.nguyenkim.appmini.Adapter.ProfileAdapter;
import com.example.nguyenkim.appmini.DaTaModel.Profile;

import java.util.ArrayList;

public class MainActivity extends Activity{
    ArrayList<Profile> mprofile = new ArrayList<Profile>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datda();
        //lay listview
        ListView listView = (ListView) findViewById(R.id.lvMain);
        //Tao adapte
        ProfileAdapter adapter =  new ProfileAdapter(MainActivity.this ,R.layout.list_view_layout,mprofile );

        //do du lieu len ListView
        listView.setAdapter(adapter);

    }



    private void datda(){
        //status 1
        Profile p1 = new Profile();
        p1.setName("nguyen van a");
        p1.setTime("12:12");
        p1.setAvt(R.drawable.icon1);
        p1.setImgLoad(R.drawable.anhnen3);
        p1.setStatus("Liên Quân Mobile ---> Valhein");

        //status 2
        Profile p2 = new Profile();
        p2.setName("nguyen van b");
        p2.setTime("12:13");
        p2.setAvt(R.drawable.icon2);
        p2.setImgLoad(R.drawable.anhnen2);
        p2.setStatus("Chiếu Xe Kỉ Niệm");

        //status 3
        Profile p3 = new Profile();
        p3.setName("nguyen van c");
        p3.setTime("12:14");
        p3.setAvt(R.drawable.icon4);
        p3.setImgLoad(R.drawable.anhnen4);
        p3.setStatus("Thiên Nhiên Thật Là Kì Aỏ");


        //status 4
        Profile p4 = new Profile();
        p4.setName("nguyen van d");
        p4.setTime("12:15");
        p4.setAvt(R.drawable.icon4);
        p4.setImgLoad(R.drawable.anhnen5);
        p4.setStatus("Cần Bán Chiến Kê Như Hình" +
                "--->Gía 300k");


        //status 5
        Profile p5 = new Profile();
        p5.setName("nguyen van e");
        p5.setTime("12:16");
        p5.setAvt(R.drawable.icon3);
        p5.setImgLoad(R.drawable.anhnen6);
        p5.setStatus("Hôm Nay Tôi Cô Đơn quá");

        mprofile.add(p1);
        mprofile.add(p2);
        mprofile.add(p3);
        mprofile.add(p4);
        mprofile.add(p5);
    }
}
