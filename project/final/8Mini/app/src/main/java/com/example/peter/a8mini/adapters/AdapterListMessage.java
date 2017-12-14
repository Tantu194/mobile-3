package com.example.peter.a8mini.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.peter.a8mini.R;
import com.example.peter.a8mini.models.Message;

import java.util.ArrayList;

/**
 * Created by IT on 11/9/2017.
 */

public class AdapterListMessage extends ArrayAdapter<Message> {

    private Context context;
    private int layout;
    private ArrayList<Message> arrMessage;
    Bitmap bmpFriend = null, myBmmp = null;

    public void setImage(Bitmap imgFriend, Bitmap myImg){
        /*byte[] bImgFriend = Base64.decode(imgFriend, Base64.DEFAULT);
        byte[] mybyte = Base64.decode(myImg, Base64.DEFAULT);
        bmpFriend = BitmapFactory.decodeByteArray(bImgFriend, 0, bImgFriend.length);
        myBmmp = BitmapFactory.decodeByteArray(mybyte, 0, mybyte.length);*/
        bmpFriend = imgFriend;
        myBmmp = myImg;
    }


    public AdapterListMessage(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Message> objects) {
        super(context, resource, objects);
        this.arrMessage = objects;
        this.layout = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(layout,parent,false);

        TextView tvContent;
        ImageView imgV;
        LinearLayout linearLayout;
        Log.d("TAG message",arrMessage.get(position).toString());
        if(arrMessage.get(position).isType()){
            tvContent = (TextView) convertView.findViewById(R.id.tvContentR);
            imgV = (ImageView) convertView.findViewById(R.id.imgvR);
            linearLayout = (LinearLayout) convertView.findViewById(R.id.linearR);
            //imgV.setImageBitmap(Bitmap.createScaledBitmap(myBmmp, 24, 24,false));
            if(myBmmp != null){
                //imgV.setImageDrawable(RoundedBitmapDrawable.createRoundedBitmap(Bitmap.createScaledBitmap(myBmmp, 120, 120,false)));
                imgV.setImageBitmap(myBmmp);
                Log.d("TAG my bm", myBmmp.toString());
            }

        }else{
            tvContent = (TextView) convertView.findViewById(R.id.tvContentL);
            imgV = (ImageView) convertView.findViewById(R.id.imgvL);
            linearLayout = (LinearLayout) convertView.findViewById(R.id.linearL);
            //imgV.setImageBitmap(Bitmap.createScaledBitmap(bmpFriend, 24, 24,false));
            if(bmpFriend != null){
                //imgV.setImageDrawable(RoundedBitmapDrawable.createRoundedBitmap(Bitmap.createScaledBitmap(bmpFriend, 120, 120,false)));
                imgV.setImageBitmap(bmpFriend);
                Log.d("TAG friend bm", bmpFriend.toString());
            }

        }
        tvContent.setText(arrMessage.get(position).getContent());
        linearLayout.setVisibility(View.VISIBLE);

        return convertView;
    }
}
