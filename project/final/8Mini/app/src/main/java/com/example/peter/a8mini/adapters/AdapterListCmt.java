package com.example.peter.a8mini.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peter.a8mini.R;
import com.example.peter.a8mini.models.Cmt;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by peter on 13/12/2017.
 */

public class AdapterListCmt extends ArrayAdapter<Cmt> {

    Context context;
    int layout;
    ArrayList<Cmt> arrCmt;

    public AdapterListCmt(@NonNull Context context, int resource, @NonNull ArrayList<Cmt> objects) {
        super(context, resource, objects);
        this.context = context;
        layout = resource;
        arrCmt = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Holder holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        if(convertView == null){
            convertView = inflater.inflate(layout,parent,false);
            holder = new Holder();
            holder.tvCmtUser = convertView.findViewById(R.id.tvCmtUser);
            holder.tvCmtContent = convertView.findViewById(R.id.tvCmtContent);
            holder.tvCmtDateTime = convertView.findViewById(R.id.tvCmtDateTime);
            holder.imgCmtUser = convertView.findViewById(R.id.imgCmtUser);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        Cmt cmt = arrCmt.get(position);
        holder.tvCmtUser.setText(cmt.getName());
        holder.tvCmtDateTime.setText(cmt.getDateTime());
        holder.tvCmtContent.setText(cmt.getContent());
        if(cmt.getImgUser() != null)
            Picasso.with(context).load(cmt.getImgUser()).placeholder(R.drawable.loading).into(holder.imgCmtUser);
        return convertView;
    }

    public static class Holder {
        TextView tvCmtDateTime, tvCmtContent, tvCmtUser;
        ImageView imgCmtUser;
    }
}
