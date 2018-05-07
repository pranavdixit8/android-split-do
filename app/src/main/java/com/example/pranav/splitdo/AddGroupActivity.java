package com.example.pranav.splitdo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.pranav.splitdo.MainActivity.getUid;

public class AddGroupActivity extends AppCompatActivity {

    public static final String TAG = AddGroupActivity.class.getSimpleName();

    private ArrayList<String> mEmails = new ArrayList<>();


    private EditText mEditTextView;
    private EditText mEmailEditText;
    private Button mButton;
    private Button mAddMemberButton;
    private RecyclerView mEmailRecyclerView;
    private EmailsAdapter mEmailAdapter;

    private LinearLayout mAddMembersLinearLayout;
    private LinearLayout mAddMembersButtonLinearLayout;

    private String mUserName;
    private String mUId;
    private static String mGroupId;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGroupDatabaseReference;
    private DatabaseReference mGroupMembersReference;
    private DatabaseReference mGroupMembersPendingReference;
    private DatabaseReference mUserDatabaseReference;
    private DatabaseReference mUserInfoDatabaseReference;

    private ChildEventListener mChildEventListener;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);


        ActionBar actionBar = getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        mUserName = MainActivity.getUser().getName();
        mUId = getUid();

        mAddMembersButtonLinearLayout = findViewById(R.id.ll_add_members_button);
        mAddMembersLinearLayout = findViewById(R.id.ll_add_members);

        mEmailRecyclerView = findViewById(R.id.email_recyclerview);

        mEmailAdapter = new EmailsAdapter();

        mEmailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEmailRecyclerView.setHasFixedSize(true);


        mEmailRecyclerView.setAdapter(mEmailAdapter);


        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserObject object = dataSnapshot.getValue(UserObject.class);
                mEmailAdapter.addObject(object);


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




        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mGroupDatabaseReference = mFirebaseDatabase.getReference().child("groups").push();
        mGroupId = mGroupDatabaseReference.getKey();

        Log.d(TAG, "onCreate: " +  mGroupId);

        mGroupMembersReference = mFirebaseDatabase.getReference().child("groups").child(mGroupId).child("members");
        mGroupDatabaseReference =  mFirebaseDatabase.getReference().child("groups").child(mGroupId).child("groupInfo");
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("users").child(mUId).child("groups").child(mGroupId);
        mGroupMembersPendingReference = mFirebaseDatabase.getReference().child("groups").child(mGroupId).child("pending");
        mUserInfoDatabaseReference =  mFirebaseDatabase.getReference().child("userInfo");

        mUserInfoDatabaseReference.addChildEventListener(mChildEventListener);


        mEditTextView = (EditText) findViewById(R.id.et_group_name);
        mEmailEditText = findViewById(R.id.et_enter_email);

        mButton = (Button) findViewById((R.id.add_group_button));
        mAddMemberButton = findViewById(R.id.add_button);




        mEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mButton.setEnabled(true);
                } else {
                    mButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mAddMemberButton.setEnabled(true);
                } else {
                    mAddMemberButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });




        mAddMembersButtonLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAddMembersButtonLinearLayout.setVisibility(View.INVISIBLE);
                mAddMembersLinearLayout.setVisibility(View.VISIBLE);

                mEmailEditText.requestFocus();




            }
        });

    }

    public void onClickAddGroup(View view) {
        String input = ((EditText) findViewById(R.id.et_group_name)).getText().toString();

        if (input.length() == 0) {
            return;
        }

        Map<String, String> owner = new HashMap<>();

        owner.put(mUId,"owner");

        mGroupMembersReference.setValue(owner);

        for(String email : mEmails){

            Log.d(TAG, "onClickAddGroup: " +email);
            Map<String, String> member = new HashMap<>();
            member.put(email,"member");
            mGroupMembersPendingReference.setValue(member);

        }


        GroupObject groupObject = new GroupObject(input,mUserName, mUId, mGroupId, null, null);

        mGroupDatabaseReference.setValue(groupObject);

        mUserDatabaseReference.setValue(groupObject);

        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickAddMember(View view) {

//        String email = mEmailEditText.getText().toString();
//
//        String str = email.split("@")[0];
//        mEmails.add(str);
//        mEmailAdapter.addEmail(email);
//
//        mEmailEditText.setText("");

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.i(TAG, "onClickAddMember: " + "exception");
        }
    }
}
