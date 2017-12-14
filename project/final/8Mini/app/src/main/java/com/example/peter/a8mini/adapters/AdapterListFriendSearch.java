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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peter.a8mini.R;
import com.example.peter.a8mini.models.Friend;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Petertu on 18/11/2017.
 */

public class AdapterListFriendSearch extends ArrayAdapter<Friend>{
    Context context;
    int layout;
    ArrayList<Friend> friends;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef;

    public AdapterListFriendSearch(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Friend> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.friends = objects;
        myRef =  database.getReference("listuser");
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(layout,parent,false);
        TextView tvName = convertView.findViewById(R.id.tvName);
        ImageView imgSearch = convertView.findViewById(R.id.imgSearch);
        final Button btnThem = convertView.findViewById(R.id.btnThem);

        tvName.setText(friends.get(position).getEmail());
        Log.d("Tag img",friends.get(position).getImg());
        if(friends.get(position).getImg() != null){
            Picasso.with(context).load(friends.get(position).getImg()).placeholder(R.drawable.loading).into(imgSearch);
        }

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(friends.get(position).getUid()).child("notification").child(user.getUid()).setValue("true");
                btnThem.setEnabled(false);
            }
        });
        return convertView;
    }
}
