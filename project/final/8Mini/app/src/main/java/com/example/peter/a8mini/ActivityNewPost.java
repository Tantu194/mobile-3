package com.example.peter.a8mini;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.peter.a8mini.models.NotifyPost;
import com.example.peter.a8mini.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityNewPost extends AppCompatActivity {
    private static final int PICK_IMAGE = 99;
    public static final String KEY_NOTIFI_POST = "notifyPost";
    ImageView imageView;
    EditText etContent;
    MenuItem item;
    ToggleButton tgbtnPickImg;

    private StorageReference mStorageRef;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference myRef, drUser, drFriend;

    String img = null, nickName, keyPost = null, dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bài viết mới");

        imageView = findViewById(R.id.imgPost);
        etContent = findViewById(R.id.etContentPost);
        tgbtnPickImg = findViewById(R.id.tgbtnPickImg);


        mStorageRef = FirebaseStorage.getInstance().getReference(user.getUid());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        drUser = database.getReference("listuser");
        drFriend = database.getReference("listuser");
        myRef = database.getReference("listuser").child(user.getUid()).child("posts");

        tgbtnPickImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tgbtnPickImg.isChecked()){
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent,PICK_IMAGE);
                }else{
                    imageView.setVisibility(View.GONE);
                }

            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(etContent.getText().toString().equals("")){
                    item.setEnabled(false);
                    item.setIcon(R.drawable.ic_post_false_24dp);
                }else{
                    item.setEnabled(true);
                    item.setIcon(R.drawable.ic_post_24dp);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getDetailUser();
    }

    private void getDetailUser() {
        drUser.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("img").getValue() != null){
                    img = dataSnapshot.child("img").getValue().toString();
                }
                nickName = dataSnapshot.child("nickName").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            Uri uri = data.getData();
            imageView.setImageURI(uri);
            imageView.setVisibility(View.VISIBLE);
        }else{
            tgbtnPickImg.setChecked(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_post, menu);
        item = menu.findItem(R.id.menuPost);
        item.setEnabled(false);
        item.setIcon(R.drawable.ic_post_false_24dp);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuPost){
            final Date dtNow = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            dateTime = sdf.format(dtNow);
            //Log.d("TAG date",date);
            //Toast.makeText(ActivityNewPost.this, date + "\n" + user.getUid(),Toast.LENGTH_LONG).show();
            if(imageView.getVisibility() == View.VISIBLE){
                saveImageWithPost(dtNow);
            }else{
                saveOnlyPost(dtNow);
            }

        }else if(item.getItemId() == android.R.id.home){
            finish();
        }

        return true;
    }

    void sendNotifiPost(){

        drUser.child(user.getUid()).child("friends").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey().toString();
                setValueNotifiPost(key);
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

    void setValueNotifiPost(String key){
        drFriend.child(user.getUid()).child("friends").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    final String uid = dataSnapshot.getValue().toString();
                    NotifyPost notifyPost = new NotifyPost(user.getUid(),NotifyPost.NOTIFY_TYPE_NEW_POST,nickName,img,keyPost,dateTime);
                    if(keyPost != null){
                        drUser.child(uid).child(KEY_NOTIFI_POST).push().setValue(notifyPost);
                        Toast.makeText(ActivityNewPost.this,"Cập nhật bài viết thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void saveOnlyPost(Date dtNow){
        Post post = new Post();
        post.setUid(user.getUid());
        post.setStt(etContent.getText()+"");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        post.setDateTime(sdf.format(dtNow));
        post.setImgUser(img);
        post.setNickName(nickName);
        myRef.push().setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        keyPost = dataSnapshot.getKey();
                        myRef.child(keyPost).child("keyPost").setValue(keyPost);
                        sendNotifiPost();
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
        });

    }

    void saveImageWithPost(final Date dtNow){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        String date = sdf.format(dtNow);
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        final byte[] data = baos.toByteArray();
        UploadTask uploadTask = mStorageRef.child("image"+date).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActivityNewPost.this, "Lỗi!!!",Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Uri uri = task.getResult().getDownloadUrl();
                Log.d("TAG link", uri.toString());
                Post post = new Post();
                post.setUid(user.getUid());
                post.setUrlImg(uri.toString());
                post.setStt(etContent.getText()+"");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                post.setDateTime(sdf.format(dtNow));
                post.setImgUser(img);
                post.setNickName(nickName);
                myRef.push().setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        myRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                keyPost = dataSnapshot.getKey();
                                myRef.child(keyPost).child("keyPost").setValue(keyPost);
                                sendNotifiPost();
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
                });
            }
        });
    }
}
