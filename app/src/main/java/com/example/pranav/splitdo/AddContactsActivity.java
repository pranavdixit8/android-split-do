package com.example.pranav.splitdo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddContactsActivity extends AppCompatActivity  implements ContactsAdapter.OnClickContactSelect{


    private RecyclerView mRecyclerView;
    private ContactsAdapter mAdapter;

    private ArrayList<UserObject> mContacts = new ArrayList<>();

    private DatabaseReference mUserInfoDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;

    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);


        mRecyclerView = findViewById(R.id.contacts_recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ContactsAdapter(this);

        mRecyclerView.setAdapter(mAdapter);


        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mUserInfoDatabaseReference =  mFirebaseDatabase.getReference().child("userInfo");


        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserObject object = dataSnapshot.getValue(UserObject.class);
                mAdapter.addObject(object);
                mContacts.add(object);


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

        mUserInfoDatabaseReference.addChildEventListener(mChildEventListener);



    }

    @Override
    public void onContactClick(int postion) {

        UserObject obj = mContacts.get(postion);
        String name = obj.getName();
        String email = obj.getEmail();
        String uid = obj.getUid();

        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("uid", uid);
        setResult(RESULT_OK, intent);
        finish();

    }
}
