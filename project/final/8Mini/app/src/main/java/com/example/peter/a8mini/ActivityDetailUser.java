package com.example.peter.a8mini;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.a8mini.models.RoundedBitmapDrawable;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ActivityDetailUser extends AppCompatActivity {
    final int RESULT_LOAD_IMAGE = 99;
    ImageView imgUser;
    EditText etNickName;
    private DatabaseReference listuser;
    private DatabaseReference datachat;
    StorageReference mStorageRef;
    FirebaseUser user;
    private MenuItem itemMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        imgUser = (ImageView) findViewById(R.id.imgUser);
        TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        etNickName = findViewById(R.id.etNickName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        listuser = database.getReference("listuser");
        datachat = database.getReference("datachat");
        mStorageRef = FirebaseStorage.getInstance().getReference(user.getUid());

        tvEmail.setText(user.getEmail()+"");

        listuser.child(user.getUid()).child("nickName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                etNickName.setText(dataSnapshot.getValue()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, RESULT_LOAD_IMAGE);
            }
        });

        listuser.child(user.getUid()).child("img").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    String strImg = dataSnapshot.getValue().toString();
                    /*byte[] bImg = Base64.decode(strImg, Base64.DEFAULT);
                    Bitmap bmp = BitmapFactory.decodeByteArray(bImg, 0, bImg.length);
                    imgUser.setImageBitmap(Bitmap.createScaledBitmap(bmp, 240, 240, false));*/
                    Picasso.with(getApplicationContext()).load(strImg).placeholder(R.drawable.loading).into(imgUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        etNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etNickName.getText().toString().equals("")){
                    if(itemMenu != null)
                        itemMenu.setEnabled(false);
                }else{
                    if(itemMenu != null)
                        itemMenu.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            Uri imgUri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imgUri);
                imgUser.setImageDrawable(RoundedBitmapDrawable.createRoundedBitmap(bm));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId() == android.R.id.home){

           /*if(Friend.imagViewToByte(imgUser) != null){
               String strImage = Base64.encodeToString(Friend.imagViewToByte(imgUser),Base64.DEFAULT);
               listuser.child(user.getUid()).child("image").setValue(strImage);
           }*/

           Intent intent = new Intent(ActivityDetailUser.this,MainActivity.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
           startActivity(intent);
           finish();
       }else if(item.getItemId() == R.id.menuEdit) {
           if(itemMenu.getTitle().toString().equals(getResources().getString(R.string.edit))){
               etNickName.setEnabled(true);
               etNickName.setFocusable(true);
               itemMenu.setIcon(android.R.drawable.ic_menu_save);
               itemMenu.setTitle(getResources().getString(R.string.save));
           }else{
               saveDataDetailUser();

               etNickName.setEnabled(false);
               itemMenu.setTitle(getResources().getString(R.string.edit));
               itemMenu.setIcon(android.R.drawable.ic_menu_edit);
           }
       }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_user,menu);
        itemMenu = menu.findItem(R.id.menuEdit);
        return true;
    }

    void saveDataDetailUser(){
        imgUser.setDrawingCacheEnabled(true);
        imgUser.buildDrawingCache();
        Bitmap bitmap = imgUser.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        final byte[] data = baos.toByteArray();
        UploadTask uploadTask = mStorageRef.child("imageUser").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActivityDetailUser.this, "Lưu thất bại!!!",Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Uri uri = task.getResult().getDownloadUrl();
                datachat.child(user.getUid()).child("nickName").setValue(etNickName.getText().toString());
                listuser.child(user.getUid()).child("nickName").setValue(etNickName.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ActivityDetailUser.this,"Lưu thành công!!!",Toast.LENGTH_SHORT).show();
                    }
                });
                listuser.child(user.getUid()).child("img").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ActivityDetailUser.this,"Lưu thành công!!!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
