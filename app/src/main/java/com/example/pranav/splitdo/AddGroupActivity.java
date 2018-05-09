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
    private static final int REQUEST_CONTACT = 1;

    private ArrayList<UserObject> mContacts = new ArrayList<>();
    private EditText mEditTextView;
    private Button mButton;

    private RecyclerView mRecyclerView;
    private ContactsAdapter mAdapter;

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
    private DatabaseReference mMemberDatabaseReference;

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
        mRecyclerView = findViewById(R.id.contacts_recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ContactsAdapter();

        mRecyclerView.setAdapter(mAdapter);


        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mGroupDatabaseReference = mFirebaseDatabase.getReference().child("groups").push();
        mGroupId = mGroupDatabaseReference.getKey();

        Log.d(TAG, "onCreate: " +  mGroupId);

        mGroupMembersReference = mFirebaseDatabase.getReference().child("groups").child(mGroupId).child("members");
        mGroupDatabaseReference =  mFirebaseDatabase.getReference().child("groups").child(mGroupId).child("groupInfo");
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("users").child(mUId).child("groups").child(mGroupId);


        mEditTextView = (EditText) findViewById(R.id.et_group_name);

        mButton = (Button) findViewById((R.id.add_group_button));

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

        mAddMembersButtonLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =  new Intent(AddGroupActivity.this, AddContactsActivity.class);

                startActivityForResult(intent, REQUEST_CONTACT);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CONTACT && resultCode == RESULT_OK){

        String name = data.getStringExtra(AddContactsActivity.NAME_TOKEN);
        String email = data.getStringExtra(AddContactsActivity.EMAIL_TOKEN);
        String uid = data.getStringExtra(AddContactsActivity.UID_TOKEN);

        UserObject obj = new UserObject(name, email, uid);

        mContacts.add(obj);
        mAdapter.addObject(obj);

        }
    }

    public void onClickAddGroup(View view) {
        String input = ((EditText) findViewById(R.id.et_group_name)).getText().toString();

        if (input.length() == 0) {
            return;
        }

        GroupObject groupObject = new GroupObject(input,mUserName, mUId, mGroupId, null, null);

        Map<String, Object> owner = new HashMap<>();
        owner.put(mUId, "owner");

        mGroupMembersReference.updateChildren(owner);

        for(UserObject contact : mContacts){

            String uid = contact.getUid();
            if(!uid.equals(mUId)) {
                Map<String, Object> member = new HashMap<>();
                member.put(uid, "member");

                mMemberDatabaseReference = mFirebaseDatabase.getReference().child("users").child(uid).child("groups").child(mGroupId);
                mMemberDatabaseReference.setValue(groupObject);
                mGroupMembersReference.updateChildren(member);
            }
        }


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

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.i(TAG, "onClickAddMember: " + "exception");
        }
    }
}
