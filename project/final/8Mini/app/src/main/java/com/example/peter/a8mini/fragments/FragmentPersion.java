package com.example.peter.a8mini.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.peter.a8mini.ActivityDetailUser;
import com.example.peter.a8mini.ActivityNewPost;
import com.example.peter.a8mini.R;
import com.example.peter.a8mini.adapters.AdapterListPostPersion;
import com.example.peter.a8mini.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPersion extends FragmentAbtract {

    Button btnPost;
    FirebaseUser user;
    DatabaseReference mRef, drUser;
    ArrayList<Post> arrPost;
    AdapterListPostPersion adapterListPostPersion;
    ListView lvPostPersion;
    ChildEventListener listener;

    MenuItem itemNoti;

    public FragmentPersion() {
        // Required empty public constructor
        arrPost = new ArrayList<>();
        name = "Persion";
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference("listuser").child(user.getUid()).child("posts");
        drUser = database.getReference("listuser").child(user.getUid());
        getAllPosts();
        Log.d("Persion", "da tao");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_persion, container, false);
        //view.setFitsSystemWindows(true);
        setHasOptionsMenu(true);

        btnPost = view.findViewById(R.id.btnPost);
        lvPostPersion = view.findViewById(R.id.lvPostPersion);

        adapterListPostPersion = new AdapterListPostPersion(getContext(), R.layout.layout_item_post_persion, arrPost);
        lvPostPersion.setAdapter(adapterListPostPersion);
        adapterListPostPersion.notifyDataSetChanged();

        //getAllPosts();

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityNewPost.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        //mRef.removeEventListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getAllPosts() {

        listener = mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    Post post = dataSnapshot.getValue(Post.class);
                    arrPost.add(0,post);
                    Collections.sort(arrPost);
                    if (adapterListPostPersion != null)
                        adapterListPostPersion.notifyDataSetChanged();
                    Log.d("NEW POST", dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    Post post = dataSnapshot.getValue(Post.class);
                    for (int i = 0; i < arrPost.size(); i++) {
                        if(arrPost.get(i).getKeyPost() == null){
                            arrPost.set(i, post);
                            adapterListPostPersion.notifyDataSetChanged();
                        }
                        if (post.getKeyPost().equals(arrPost.get(i).getKeyPost())) {
                            arrPost.set(i, post);
                            Log.d("Child", post.toString());
                            adapterListPostPersion.notifyDataSetChanged();
                            break;
                        }
                    }
                }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_persion, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuSetting) {
            Intent intent = new Intent(getActivity(), ActivityDetailUser.class);
            startActivity(intent);
        }
        return true;
    }
}
