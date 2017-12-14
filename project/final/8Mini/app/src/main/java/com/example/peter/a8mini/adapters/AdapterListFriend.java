package com.example.peter.a8mini.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.peter.a8mini.R;
import com.example.peter.a8mini.models.Friend;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Petertu on 18/11/2017.
 */

public class AdapterListFriend extends ArrayAdapter<Friend> {

    Context context;
    int layout;
    ArrayList<Friend> friends;

    public AdapterListFriend(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Friend> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.friends = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(layout,parent, false);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        ImageView imgUser = convertView.findViewById(R.id.img);

        if(friends.get(position).getImg() != null){
            //byte[] bImg = Base64.decode(friends.get(position).getImg(), Base64.DEFAULT);
            //Bitmap bmp = BitmapFactory.decodeByteArray(bImg, 0, bImg.length);
            //imgUser.setImageBitmap(Bitmap.createScaledBitmap(bmp, 240, 240, false));
            //imgUser.setImageDrawable(RoundedBitmapDrawable.createRoundedBitmap(Bitmap.createScaledBitmap(bmp, 240, 240, false)));
            Picasso.with(context).load(friends.get(position).getImg()).placeholder(R.drawable.loading).into(imgUser);
        }

        tvName.setText(friends.get(position).getNickName());

        Log.d("TAG friend", ": " + friends.get(position).getEmail());
        return  convertView;
    }
}
