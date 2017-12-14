package com.example.peter.a8mini;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.peter.a8mini.fragments.FragmentAbtract;
import com.example.peter.a8mini.fragments.FragmentListFriend;
import com.example.peter.a8mini.fragments.FragmentPersion;
import com.example.peter.a8mini.fragments.FragmentTimeline;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MainActivity extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    BottomNavigationView navigation;

    int position = 1;
    String nickName = null, img = null;

    FirebaseUser user;
    DatabaseReference myref;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentAbtract fragmentAbtract;
            int tmp = position;
            switch (item.getItemId()) {
                case R.id.navi_listFriend:
                    position = 1;
                    if (fragmentManager.findFragmentByTag("List friend") == null) {
                        fragmentAbtract = new FragmentListFriend();
                    } else
                        fragmentAbtract = (FragmentListFriend) fragmentManager.findFragmentByTag("List friend");
                    startFragment(fragmentAbtract, tmp);

                    return true;
                case R.id.navi_timeLine:
                    position = 2;
                    if (fragmentManager.findFragmentByTag("Timeline") == null) {
                        fragmentAbtract = new FragmentTimeline();
                    } else
                        fragmentAbtract = (FragmentTimeline) fragmentManager.findFragmentByTag("Timeline");
                    startFragment(fragmentAbtract, tmp);

                    return true;
                case R.id.navi_persion:
                    position = 3;
                    if (fragmentManager.findFragmentByTag("Persion") == null) {
                        fragmentAbtract = new FragmentPersion();
                    } else
                        fragmentAbtract = (FragmentPersion) fragmentManager.findFragmentByTag("Persion");
                    startFragment(fragmentAbtract, tmp);
                    if (img != null) {
                        Picasso.with(MainActivity.this).load(img).placeholder(R.drawable.loading).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                getSupportActionBar().setIcon(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, false)));
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                    }
                    getSupportActionBar().setTitle(nickName);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myref = database.getReference("listuser").child(user.getUid());

        getDetailUser();

        init();

        FragmentListFriend fragmentListFriend = new FragmentListFriend();
        startFragment(fragmentListFriend, 1);

    }

    private void startFragment(FragmentAbtract fragment, int tmp) {
        getSupportActionBar().setTitle(fragment.name);
        fragmentTransaction = fragmentManager.beginTransaction();

        if (position > tmp) {
            fragmentTransaction.setCustomAnimations(R.anim.anim_right_to_left_in, R.anim.anim_right_to_left_out);
        } else if (position < tmp) {
            fragmentTransaction.setCustomAnimations(R.anim.anim_left_to_right_in, R.anim.anim_left_to_right_out);
        }

        fragmentTransaction.replace(R.id.container, fragment, fragment.name);
        if (fragmentManager.findFragmentByTag(fragment.name) == null) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    void getDetailUser(){
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nickName = dataSnapshot.child("nickName").getValue().toString();
                if (dataSnapshot.child("img").getValue() != null) {
                    img = dataSnapshot.child("img").getValue().toString();
                    Picasso.with(MainActivity.this).load(img).placeholder(R.drawable.loading).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            getSupportActionBar().setIcon(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, false)));
                            getSupportActionBar().setTitle(nickName);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetailUser();
        //Toast.makeText(MainActivity.this,nickName,Toast.LENGTH_LONG).show();
    }

    private void init() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
    }

}
