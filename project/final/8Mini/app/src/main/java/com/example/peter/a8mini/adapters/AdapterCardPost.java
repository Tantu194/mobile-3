package com.example.peter.a8mini.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.peter.a8mini.ActivityDetailPost;
import com.example.peter.a8mini.R;
import com.example.peter.a8mini.models.Cmt;
import com.example.peter.a8mini.models.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by peter on 9/12/2017.
 */

public class AdapterCardPost extends RecyclerView.Adapter<AdapterCardPost.CardPostHolder>{

    Context context;
    ArrayList<Post> arrPost;

    FirebaseUser user;
    DatabaseReference mRefPost;

    public AdapterCardPost(ArrayList<Post> arrPost) {
        this.arrPost = arrPost;
        this.context = context;
        user = FirebaseAuth.getInstance().getCurrentUser();
        mRefPost = FirebaseDatabase.getInstance().getReference("listuser");
    }

    @Override
    public CardPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cardview_post,parent,false);
        context = parent.getContext();
        return new CardPostHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CardPostHolder holder, int position) {
        final Post post = arrPost.get(position);
        holder.tvUserName.setText(post.getNickName());
        holder.tvStt.setText(post.getStt());
        holder.tvDateTime.setText(post.getDateTime());

        if(post.getImgUser() != null){
            Picasso.with(context).load(post.getImgUser()).placeholder(R.drawable.loading).into(holder.imgUser);
        }

        if(post.getUrlImg() != null){
            Picasso.with(context).load(post.getUrlImg()).placeholder(R.drawable.loading).into(holder.imgPost);
            holder.imgPost.setVisibility(View.VISIBLE);
        }else{
            holder.imgPost.setVisibility(View.GONE);
        }
        ArrayList<String> listLike = post.getListLike();
        ArrayList<Cmt> listCmt = post.getArrCmt();
        //Log.d("listlikeTimeLike",post.toString());
        if(listLike != null){
                holder.tvCountLike.setText(listLike.size()+"");
                holder.tgbtnLike.setChecked(listLike.contains(user.getUid()));
                //Log.d("listlikeTimeLike",post.toString());
                //Log.d("listlikeuid",user.getUid());
        }else{
            listLike = new ArrayList<>();
            holder.tvCountLike.setText("0");
        }

        if(listCmt != null){
            holder.tvCountCmt.setText(listCmt.size()+"");
        }else{
            listCmt = new ArrayList<>();
            holder.tvCountCmt.setText("0");
        }

        final ArrayList<String> finalListLike = listLike;
        holder.tgbtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.tgbtnLike.isChecked()){
                    finalListLike.add(user.getUid());
                    mRefPost.child(post.getUid()).child("posts").child(post.getKeyPost()).child("listLike").setValue(finalListLike).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            holder.tgbtnLike.setChecked(false);
                        }
                    });
                }else{
                    finalListLike.remove(user.getUid());
                    mRefPost.child(post.getUid()).child("posts").child(post.getKeyPost()).child("listLike").setValue(finalListLike).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            holder.tgbtnLike.setChecked(true);
                        }
                    });
                }
            }
        });

        holder.btnCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityDetailPost.class);
                intent.putExtra("uidPost", post.getUid());
                intent.putExtra("keyPost", post.getKeyPost());
                intent.putExtra("nickName", post.getNickName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrPost.size();
    }

    public static class CardPostHolder extends RecyclerView.ViewHolder{

        protected TextView tvUserName, tvCountLike, tvCountCmt, tvStt, tvDateTime;
        protected ImageView imgUser, imgPost;
        protected ToggleButton tgbtnLike;
        protected Button btnCmt;

        public CardPostHolder(View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvStt = itemView.findViewById(R.id.tvStt);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvCountCmt = itemView.findViewById(R.id.tvCountCmt);
            tvCountLike = itemView.findViewById(R.id.tvCountLike);
            imgUser = itemView.findViewById(R.id.imgUser);
            imgPost = itemView.findViewById(R.id.imgPost);
            tgbtnLike = itemView.findViewById(R.id.tgbtnLike);
            btnCmt = itemView.findViewById(R.id.btnCmt);

        }
    }
}
