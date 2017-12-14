package com.example.peter.timeline.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peter.timeline.R;
import com.example.peter.timeline.models.NotifyPost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by peter on 10/12/2017.
 */

public class AdapterListNotifyPost extends ArrayAdapter<NotifyPost> {

    Context context;
    int layout;
    ArrayList<NotifyPost> arrNotifypost;

    public AdapterListNotifyPost(@NonNull Context context, int resource, @NonNull ArrayList<NotifyPost> objects) {
        super(context, resource, objects);
        this.context = context;
        layout = resource;
        arrNotifypost = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(layout,parent,false);
        ImageView imgUser = convertView.findViewById(R.id.imvUser);
        TextView tvContent = convertView.findViewById(R.id.tvContent);
        TextView tvNotidyPost = convertView.findViewById(R.id.tvDateTime);

        tvNotidyPost.setText(arrNotifypost.get(position).getDateTime());
        Picasso.with(context).load(arrNotifypost.get(position).getImg()).placeholder(R.drawable.loading).into(imgUser);
        if(arrNotifypost.get(position).getType().equals(NotifyPost.NOTIFY_TYPE_NEW_POST)){
            tvContent.setText(arrNotifypost.get(position).getNickName() + " đã đăng bài viết mới");
        }else{
            tvContent.setText(arrNotifypost.get(position).getNickName() + " bình luận về bài viết");
        }

        return convertView;
    }
}
