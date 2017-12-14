package com.example.peter.a8mini;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.peter.a8mini.adapters.AdapterListCmt;
import com.example.peter.a8mini.models.Cmt;
import com.example.peter.a8mini.models.NotifyPost;
import com.example.peter.a8mini.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ActivityDetailPost extends AppCompatActivity {

    ImageView imgUserPost, imgPost;
    TextView tvUser, tvDateTime, tvStt, tvCountLike, tvCountCmt;
    ToggleButton tgbtnLike;
    LinearLayout lvListCmt;
    Button btnSendCmt;
    EditText etContentCmt;

    ArrayList<String> finalListLike;
    ArrayList<Cmt> arrCmt;
    AdapterListCmt adapterListCmt;
    Post post = null;
    String uidPost = null, keypost = null, imgUser = null, nickName = null, nameUser;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uidPost = getIntent().getStringExtra("uidPost");
        keypost = getIntent().getStringExtra("keyPost");
        nickName = getIntent().getStringExtra("nickName");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference("listuser");

        arrCmt = new ArrayList<>();
        getDetailpost();
        getInfoUser();

        init();

        //adapterListCmt = new AdapterListCmt(ActivityDetailPost.this,R.layout.layout_item_comment,arrCmt);
        //lvCmt.setAdapter(adapterListCmt);
        //setListViewHeightBasedOnChildren(lvCmt);

        //adapterListCmt.notifyDataSetChanged();

        Log.d("intent", uidPost + ";" + keypost + ";" + nickName);



        tgbtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tgbtnLike.isChecked()){
                    finalListLike.add(user.getUid());
                    mRef.child(post.getUid()).child("posts").child(post.getKeyPost()).child("listLike").setValue(finalListLike).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            tgbtnLike.setChecked(false);
                        }
                    });
                }else{
                    finalListLike.remove(user.getUid());
                    mRef.child(post.getUid()).child("posts").child(post.getKeyPost()).child("listLike").setValue(finalListLike).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            tgbtnLike.setChecked(true);
                        }
                    });
                }
            }
        });

        btnSendCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                final String dateTime = sdf.format(Calendar.getInstance().getTime());
                final Cmt cmt = new Cmt(nameUser,etContentCmt.getText().toString(),dateTime,imgUser);
                ArrayList<Cmt> listCmt = post.getListCmt();
                int i = 0;
                if(listCmt != null)
                    i = listCmt.size();

                mRef.child(post.getUid()).child("posts").child(post.getKeyPost()).child("listCmt").child(i+"").setValue(cmt).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //arrCmt.add(cmt);
                        //adapterListCmt.notifyDataSetChanged();
                        //setListViewHeightBasedOnChildren(lvCmt);
                        NotifyPost notifyPost = new NotifyPost(post.getUid(),NotifyPost.NOTIFY_TYPE_NEW_CMT,nameUser,imgUser,post.getKeyPost(),dateTime);
                        ArrayList<String> arrCmtUser = post.getListCmtUser();
                        if(arrCmtUser == null){
                            arrCmtUser = new ArrayList<>();
                        }
                        if(!arrCmtUser.contains(user.getUid()) && !user.getUid().equals(post.getUid())){
                            arrCmtUser.add(user.getUid());
                            mRef.child(post.getUid()).child("posts").child(post.getKeyPost()).child("listCmtUser").setValue(arrCmtUser);
                        }
                        if(!post.getUid().equals(user.getUid())){
                            mRef.child(post.getUid()).child("notifyPost").push().setValue(notifyPost);
                        }

                        for(String uid: arrCmtUser){
                            if(!uid.equals(user.getUid()))
                                mRef.child(uid).child("notifyPost").push().setValue(notifyPost);
                        }
                        etContentCmt.setText("");
                        //Toast.makeText(ActivityDetailPost.this,"da gui", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        etContentCmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnSendCmt.setEnabled(!(etContentCmt.getText().toString().equals("")));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void getInfoUser() {
        mRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    imgUser = dataSnapshot.child("img").getValue().toString();
                    nameUser = dataSnapshot.child("nickName").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void setValuePost(){
        if(post != null){
            Log.d("POST", post.toString());
            Picasso.with(ActivityDetailPost.this).load(post.getImgUser()).placeholder(R.drawable.loading).into(imgUserPost);
            if(post.getUrlImg() != null)
                Picasso.with(ActivityDetailPost.this).load(post.getUrlImg()).placeholder(R.drawable.loading).into(imgPost);
            else
                imgPost.setVisibility(View.GONE);

            tvUser.setText(post.getNickName());
            tvDateTime.setText(post.getDateTime());
            if(post.getListLike() != null) {
                finalListLike = post.getListLike();
                tvCountLike.setText(post.getListLike().size() + "");
                tgbtnLike.setChecked(post.getListLike().contains(user.getUid()));
            }else {
                finalListLike = new ArrayList<>();
                tvCountLike.setText("0");
            }
            if(post.getArrCmt() != null)
                tvCountCmt.setText(post.getArrCmt().size()+"");
            else
                tvCountCmt.setText("0");

            tvStt.setText(post.getStt());
            arrCmt = post.getListCmt();
            if(arrCmt != null){
                Collections.sort(arrCmt);
                CreateListCmt();
                /*adapterListCmt.clear();
                adapterListCmt.addAll(arrCmt);
                adapterListCmt.notifyDataSetChanged();*/
                Log.d("DetailPost",arrCmt.toString());
            }
        }
    }

    private void init() {
        imgUserPost = findViewById(R.id.imgUserDetail);
        imgPost = findViewById(R.id.imgPostDetail);
        tvUser = findViewById(R.id.tvUserName);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvStt = findViewById(R.id.tvStt);
        tvCountLike = findViewById(R.id.tvCountLike);
        tvCountCmt = findViewById(R.id.tvCountCmt);
        tgbtnLike = findViewById(R.id.tgbtnLike);
        lvListCmt = findViewById(R.id.lvListCmt);

        btnSendCmt = findViewById(R.id.btnSendCmt);
        btnSendCmt.setEnabled(false);
        etContentCmt = findViewById(R.id.etContentCmt);
    }

    private void getDetailpost() {
        mRef.child(uidPost).child("posts").child(keypost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    post = dataSnapshot.getValue(Post.class);
                    Log.d("NotifyPost",post.toString());
                    setValuePost();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter myListAdapter = listView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (myListAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));

    }

    void CreateListCmt(){
        int size = arrCmt.size();
        lvListCmt.removeAllViews();
        for (int i = 0; i < size; i++) {
            /**
             * inflate items/ add items in linear layout instead of listview
             */
            LayoutInflater inflater = null;
            inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(ActivityDetailPost.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.layout_item_comment, null,false);
            /**
             * getting id of row.xml
             */
            TextView tvCmtUser = itemView.findViewById(R.id.tvCmtUser);
            TextView tvCmtContent = itemView.findViewById(R.id.tvCmtContent);
            TextView tvCmtDateTime = itemView.findViewById(R.id.tvCmtDateTime);
            ImageView imgCmtUser = itemView.findViewById(R.id.imgCmtUser);

            /**
             * set item into row
             */
            tvCmtUser.setText(arrCmt.get(i).getName());
            tvCmtDateTime.setText(arrCmt.get(i).getDateTime());
            tvCmtContent.setText(arrCmt.get(i).getContent());
            Picasso.with(ActivityDetailPost.this).load(arrCmt.get(i).getImgUser()).placeholder(R.drawable.loading).into(imgCmtUser);

            /**
             * add view in top linear
             */

            lvListCmt.addView(itemView);

            /**
             * get item row on click
             *
             */
            /*mLinearView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(MainActivity.this, "Clicked item;" + fName,
                            Toast.LENGTH_SHORT).show();
                }
            });*/
        }
    }
}
