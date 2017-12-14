package com.example.peter.a8mini;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.peter.a8mini.adapters.AdapterListMessage;
import com.example.peter.a8mini.models.Friend;
import com.example.peter.a8mini.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ArrayList<Message> arrMessages;
    AdapterListMessage adapter;
    private ListView lsv;
    private EditText etContent;
    private Button btnSend;
    private FirebaseUser user;

    FirebaseDatabase database;
    DatabaseReference refDataUser;
    DatabaseReference refDatachat;
    DatabaseReference refFriend;
    DatabaseReference refChatUid;

    Friend friend;

    Bitmap myBm, friendBm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        arrMessages = new ArrayList<>();
        adapter = new AdapterListMessage(ChatActivity.this, R.layout.layout_message_listview, arrMessages);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        friend = new Friend(bundle.getString("img"), bundle.getString("email"), bundle.getString("uid"));
        friend.setNickName(bundle.getString("nickName"));

        getSupportActionBar().setTitle(friend.getNickName());
        if(friend.getImg() != null){
            Picasso.with(getApplication()).load(friend.getImg()).placeholder(R.drawable.loading).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if (bitmap != null) {
                        //adapter.setBmpFriend(bitmap);
                        friendBm = bitmap;

                        getSupportActionBar().setLogo(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, false)));
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    getSupportActionBar().setIcon(R.drawable.ic_account_circle_24dp);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
            Picasso.with(getApplicationContext()).load(bundle.getString("myImg")).placeholder(R.drawable.loading).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    myBm = bitmap;
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
       adapter.setImage(friendBm,myBm);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        refDataUser = database.getReference("listuser");
        refDatachat = database.getReference("datachat");
        refFriend = refDatachat.child(friend.getUid()).child("chat").child(user.getUid());
        refChatUid = refDatachat.child(user.getUid()).child("chat").child(friend.getUid());

        //Toast.makeText(ChatActivity.this,user.getEmail()+"; " + user.getUid(), Toast.LENGTH_LONG).show();
        Log.d("TAG user uid", user.getUid());
        Log.d("TAG friend uid", friend.getUid());


        init();

        lsv.setAdapter(adapter);
        btnSend.setEnabled(false);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.setContent(etContent.getText().toString().trim());
                Log.d("Tag date", message.getDate());

                message.setType(false);
                refFriend.push().setValue(message);
                message.setType(true);
                refChatUid.push().setValue(message);
                etContent.setText("");
                //arrMessages.add(message);
                //adapter.notifyDataSetChanged();
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etContent.getText().toString().equals("")) {
                    btnSend.setEnabled(false);
                } else btnSend.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //TODO update list chat
        refChatUid.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {
                    String key = dataSnapshot.getKey();
                    refChatUid.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Log.d("TAG date",dataSnapshot.child("date").getValue().toString());
                            String date = dataSnapshot.child("date").getValue().toString();
                            String content = dataSnapshot.child("content").getValue().toString();
                            boolean type = (boolean) dataSnapshot.child("type").getValue();
                            arrMessages.add(new Message(content, type, date));
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        lsv = (ListView) findViewById(R.id.lvMessage);
        etContent = (EditText) findViewById(R.id.edContent);
        btnSend = (Button) findViewById(R.id.btnSend);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
