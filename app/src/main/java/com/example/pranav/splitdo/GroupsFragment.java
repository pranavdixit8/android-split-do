package com.example.pranav.splitdo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GroupsFragment extends Fragment {

    private ArrayList<GroupObject> mGroups = new ArrayList<>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGroupDatabaseReference;

    private RecyclerView mRecyclerView;
    private GroupsAdapter mAdapter;


    private ChildEventListener mChildEventListener;

    public GroupsFragment(){}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_groups,container,false);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mGroupDatabaseReference = mFirebaseDatabase.getReference().child("users").child("groups");



        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_groups);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new GroupsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GroupObject object = dataSnapshot.getValue(GroupObject.class);
                mAdapter.addObject(object);


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
        };

        mGroupDatabaseReference.addChildEventListener(mChildEventListener);


        return view;

    }
}
