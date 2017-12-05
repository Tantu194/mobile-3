package com.example.nguyenkim.appmini.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nguyenkim.appmini.DaTaModel.Profile;
import com.example.nguyenkim.appmini.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenKim on 02/12/2017.
 */

public class ProfileAdapter extends ArrayAdapter<Profile> {
    Context context;
    ArrayList<Profile> mprofile = new ArrayList<Profile>();


    public ProfileAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Profile> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mprofile = new ArrayList<Profile>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        ViewHoDer viewHoDer;
        if(rowView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_view_layout, null);

            viewHoDer = new ViewHoDer();
            viewHoDer.txtName = (TextView) rowView.findViewById(R.id.txtNameAcCount);
            viewHoDer.txtTime = (TextView) rowView.findViewById(R.id.time);
            viewHoDer.txtStatus = (TextView) rowView.findViewById(R.id.status);
            viewHoDer.imgAVT = (ImageView) rowView.findViewById(R.id.imgAVT);
            viewHoDer.imgChinh = (ImageView) rowView.findViewById(R.id.imgChinh);

            rowView.setTag(viewHoDer);



        }
        else {
            viewHoDer = (ViewHoDer) convertView.getTag();
        }
        Profile profile  = mprofile.get(position);

        viewHoDer.txtName.setText(profile.getName());
        viewHoDer.txtTime.setText(profile.getTime());
        viewHoDer.imgAVT.setImageResource(profile.getAvt());
        viewHoDer.txtStatus.setText(profile.getStatus());
        viewHoDer.imgChinh.setImageResource(profile.getImgLoad());
        return rowView;
    }

    static class ViewHoDer{
        TextView txtName;
        TextView txtTime;
        ImageView imgAVT;
        ImageView imgChinh;
        TextView txtStatus;
    }
}
