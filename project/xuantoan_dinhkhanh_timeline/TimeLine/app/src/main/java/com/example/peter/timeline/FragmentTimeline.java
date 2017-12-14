package com.example.peter.timeline;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.peter.timeline.adapters.AdapterCardPost;
import com.example.peter.timeline.adapters.AdapterListNotifyPost;
import com.example.peter.timeline.models.NotifyPost;
import com.example.peter.timeline.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTimeline extends FragmentAbtract {

    RecyclerView rvListPost;
    MenuItem itemNoti;
    DrawerLayout drawerNotifyPost;
    FrameLayout flDrawer;
    ActionBarDrawerToggle toggle;

    FirebaseUser user;
    DatabaseReference drNotifi;

    AdapterCardPost adapterCardPost;
    AdapterListNotifyPost adapterListNotifyPost;
    ArrayList<NotifyPost> arrNotifyPost;
    ArrayList<Post> arrPost;
    private ListView lvNotifyPost;

    public FragmentTimeline() {
        // Required empty public constructor
        name = "Timeline";
        arrNotifyPost = new ArrayList<>();
        arrPost = new ArrayList<>();
        adapterCardPost = new AdapterCardPost(arrPost);
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        drNotifi = database.getReference("listuser");
        actionUpdateNotifi();
        actionGetAllPost();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        //view.setFitsSystemWindows(true);
        setHasOptionsMenu(true);

        rvListPost = view.findViewById(R.id.rvListPost);
        rvListPost.setHasFixedSize(true);
        lvNotifyPost = view.findViewById(R.id.lvNotifyPost);
        drawerNotifyPost = view.findViewById(R.id.drawerNotifyPost);
        flDrawer = view.findViewById(R.id.flDrawer);

        toggle = new ActionBarDrawerToggle(getActivity(), drawerNotifyPost, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerNotifyPost.setDrawerListener(toggle);
        toggle.syncState();

        adapterListNotifyPost = new AdapterListNotifyPost(getActivity(), R.layout.layout_item_notifypost, arrNotifyPost);
        lvNotifyPost.setAdapter(adapterListNotifyPost);
        adapterListNotifyPost.notifyDataSetChanged();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvListPost.setLayoutManager(linearLayoutManager);
        rvListPost.setAdapter(adapterCardPost);
        Collections.sort(arrPost);
        adapterCardPost.notifyDataSetChanged();

        lvNotifyPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*Intent intent = new Intent(getActivity(), ActivityDetailPost.class);
                intent.putExtra("uidPost", arrNotifyPost.get(i).getUid());
                intent.putExtra("keyPost", arrNotifyPost.get(i).getKey());
                intent.putExtra("nickName", arrNotifyPost.get(i).getNickName());
                intent.putExtra("img", arrNotifyPost.get(i).getImg());
                startActivity(intent);*/
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapterCardPost.notifyDataSetChanged();
    }

    private void actionUpdateNotifi() {
        drNotifi.child(user.getUid()).child("notifyPost").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    NotifyPost notifyPost = dataSnapshot.getValue(NotifyPost.class);

                    arrNotifyPost.add(notifyPost);
                    Collections.sort(arrNotifyPost);
                    if (adapterListNotifyPost != null) {
                        adapterListNotifyPost.notifyDataSetChanged();
                    }
                    if (itemNoti != null) {
                        itemNoti.setIcon(R.drawable.ic_add_alert_green_24dp);
                    }

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

    private void actionGetAllPost() {

        drNotifi.child(user.getUid()).child("friends").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String key = dataSnapshot.getKey();
                drNotifi.child(user.getUid()).child("friends").child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String friendUid = dataSnapshot.getValue().toString();
                        drNotifi.child(friendUid).child("posts").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.getValue() != null) {
                                    if (!dataSnapshot.getValue().toString().equals("null")) {
                                        Post post = dataSnapshot.getValue(Post.class);
                                        //Log.d("POST", post.toString());
                                        arrPost.add(post);
                                        Collections.sort(arrPost);
                                        //Log.d("SORT",arrPost.toString());
                                        adapterCardPost.notifyDataSetChanged();
                                    }
                                    Log.d("NEW POST TIMELINE", dataSnapshot.getValue().toString());
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.getValue() != null) {
                                    Post post = dataSnapshot.getValue(Post.class);
                                    for (int i = 0; i < arrPost.size(); i++) {
                                        if (post.getKeyPost().equals(arrPost.get(i).getKeyPost())) {
                                            arrPost.set(i, post);
                                            adapterCardPost.notifyDataSetChanged();
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
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

        drNotifi.child(user.getUid()).child("posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    if (!dataSnapshot.getValue().toString().equals("null")) {
                        Post post = dataSnapshot.getValue(Post.class);
                        //Log.d("POST", post.toString());
                        arrPost.add(post);
                        Collections.sort(arrPost);
                        //Log.d("SORT",arrPost.toString());
                        adapterCardPost.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    Post post = dataSnapshot.getValue(Post.class);

                    for (int i = 0; i < arrPost.size(); i++) {
                        if (post.getKeyPost().equals(arrPost.get(i).getKeyPost())) {
                            arrPost.set(i, post);
                            adapterCardPost.notifyDataSetChanged();
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
        inflater.inflate(R.menu.menu_timeline, menu);
        itemNoti = menu.findItem(R.id.menuNotify);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuNotify) {
            if (drawerNotifyPost.isDrawerOpen(flDrawer)) {
                drawerNotifyPost.closeDrawer(flDrawer);
            } else {
                drawerNotifyPost.openDrawer(flDrawer);
            }

            if (itemNoti != null)
                item.setIcon(R.drawable.ic_add_alert_24dp);
        }

        return true;
    }
}
