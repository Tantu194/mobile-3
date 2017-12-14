package com.example.peter.a8mini.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.peter.a8mini.ActivityDetailPost;
import com.example.peter.a8mini.R;
import com.example.peter.a8mini.models.Cmt;
import com.example.peter.a8mini.models.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by peter on 10/12/2017.
 */

public class AdapterListPostPersion extends ArrayAdapter<Post> {

    ArrayList<Post> posts;
    int layout;
    Context context;

    DatabaseReference myRef;

    public AdapterListPostPersion(@NonNull Context context, int resource, @NonNull ArrayList<Post> objects) {
        super(context, resource, objects);
        this.context = context;
        layout = resource;
        posts = objects;
        myRef = FirebaseDatabase.getInstance().getReference("listuser");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Post post = posts.get(position);
        final Holder holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
            holder = new Holder();
            holder.flTop = convertView.findViewById(R.id.flTop);
            holder.lnDate = convertView.findViewById(R.id.lnDate);
            holder.tvTitleDate = convertView.findViewById(R.id.tvTitleDate);
            holder.tvStt = convertView.findViewById(R.id.tvStt);
            holder.tvCountCmt = convertView.findViewById(R.id.tvCountCmt);
            holder.tvCountLike = convertView.findViewById(R.id.tvCountLike);
            holder.tvYear = convertView.findViewById(R.id.tvYear);
            holder.imgPost = convertView.findViewById(R.id.imgPost);
            holder.tgbtnLike = convertView.findViewById(R.id.tgbtnLike);
            holder.btnCmt = convertView.findViewById(R.id.btnCmt);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (post.getListLike() != null) {
            holder.tgbtnLike.setChecked(false);
            for(String s : post.getListLike())
                if(s.equals(post.getUid())){
                    holder.tgbtnLike.setChecked(true);
                    break;
                }
            Log.d("LIKED", post.getListLike().contains(post.getUid())+";" + post.getUid());
            holder.tvCountLike.setText(post.getListLike().size()+"");
        }else{
            holder.tgbtnLike.setChecked(false);
            holder.tvCountLike.setText("0");
        }

        if (position == 0) {
            holder.lnDate.setVisibility(View.VISIBLE);
            holder.tvTitleDate.setText(post.getDay() + " tháng " + post.getMonth());
        } else if (!post.getDay().equals(posts.get(position - 1).getDay())) {
            holder.lnDate.setVisibility(View.VISIBLE);
            holder.tvTitleDate.setText(post.getDay() + " tháng " + post.getMonth());
        }
        if (position != 0 && !post.getYear().equals(Calendar.getInstance().getTime().getYear() + "") && !post.getYear().equals(posts.get(position - 1).getYear())) {
            holder.flTop.setVisibility(View.VISIBLE);
            holder.tvYear.setText(post.getYear());
        }

        holder.tvStt.setText(post.getStt());

        if (post.getUrlImg() != null) {
            Picasso.with(context).load(post.getUrlImg()).placeholder(R.drawable.loading).into(holder.imgPost);
        } else {
            holder.imgPost.setVisibility(View.GONE);
        }
        ArrayList<String> listLike = post.getListLike();
        ArrayList<Cmt> listCmt = post.getArrCmt();


        if(listCmt != null){
            holder.tvCountCmt.setText(listCmt.size()+"");
        }else{
            holder.tvCountCmt.setText("0");
        }

        holder.tgbtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.tgbtnLike.isChecked()) {

                    myRef.child(post.getUid()).child("posts").child(post.getKeyPost()).child("listLike").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final ArrayList<String> listLike;
                            if (dataSnapshot.getValue() == null) {
                                listLike = new ArrayList<>();
                            } else {
                                listLike = (ArrayList<String>) dataSnapshot.getValue();
                            }
                            listLike.add(post.getUid());
                            myRef.child(post.getUid()).child("posts").child(post.getKeyPost()).child("listLike").setValue(listLike).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    holder.tgbtnLike.setChecked(false);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    myRef.child(post.getUid()).child("posts").child(post.getKeyPost()).child("listLike").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                final ArrayList<String> listLike = (ArrayList<String>) dataSnapshot.getValue();

                                listLike.remove(post.getUid());

                                myRef.child(post.getUid()).child("posts").child(post.getKeyPost()).child("listLike").setValue(listLike).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        holder.tgbtnLike.setChecked(true);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

        holder.btnCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("CLICK CMT", post.getKeyPost());
                Intent intent = new Intent(context, ActivityDetailPost.class);
                intent.putExtra("uidPost", post.getUid());
                intent.putExtra("keyPost", post.getKeyPost());
                intent.putExtra("nickName", post.getNickName());
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    public static class Holder {
        FrameLayout flTop;
        LinearLayout lnDate;
        TextView tvTitleDate, tvStt, tvCountLike, tvCountCmt, tvYear;
        ImageView imgPost;
        ToggleButton tgbtnLike;
        Button btnCmt;


    }
}
