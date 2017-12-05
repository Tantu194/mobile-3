package com.example.android.bluetoothchat;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bluetoothchat.Activity.MainActivity;
import com.example.android.bluetoothchat.Model.Communicate;

import java.util.ArrayList;

/**
 * Created by kk on 11/22/2017.
 */

public class MyAdapter extends ArrayAdapter<Communicate> {
    private Activity context = null;
    private ArrayList<Communicate> list = null;
    private int layoutId;

    public MyAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull ArrayList<Communicate> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        this.layoutId = resource;
    }

    public static class ViewHolder
    {
        public ImageView image;
        public TextView tvMess;
        public LinearLayout layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView == null)
        {
            convertView = inflater.inflate(layoutId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView)convertView.findViewById(R.id.Image);
            viewHolder.tvMess = (TextView) convertView.findViewById(R.id.tvMess);
            viewHolder.layout =  (LinearLayout) convertView.findViewById(R.id.llLayout);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Communicate s = list.get(position);
        if(s.getPerson() == MainActivity.BOSS) {
            viewHolder.image.setImageResource(R.drawable.p1 );
            viewHolder.layout.setGravity(Gravity.RIGHT);
            viewHolder.layout.setBackgroundColor(Color.CYAN);
        }
        else {
            viewHolder.image.setImageResource(R.drawable.p2);
            viewHolder.layout.setGravity(Gravity.LEFT);
            viewHolder.layout.setBackgroundColor(Color.WHITE);
        }
        viewHolder.tvMess.setText(s.getMess());

        return convertView;
    }
}
