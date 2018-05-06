package com.example.pranav.splitdo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class AddTaskActivity extends AppCompatActivity {

    public static final String TAG = AddTaskActivity.class.getSimpleName();

    private EditText mEditTextView;
    private Button mButton;
    private String mPlaceId;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUserDatabaseRef;

    private String mGroupName;
    boolean isGroupTask = false;

    private TextView mLocationNameTextView;
    private TextView mLocationAddressTextView;


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);



        mLocationNameTextView = (TextView) findViewById(R.id.tv_location_name);
        mLocationAddressTextView = (TextView) findViewById(R.id.tv_address);


        Intent intent = getIntent();


        mFirebaseDatabase = FirebaseDatabase.getInstance();

        if(intent!=null && intent.hasExtra("groupName")){

            isGroupTask = true;
            mGroupName = intent.getStringExtra("groupName");
            mDatabaseReference = mFirebaseDatabase.getReference().child("groups").child(mGroupName).child("tasks");
        }else{
            isGroupTask = false;
            mDatabaseReference = mFirebaseDatabase.getReference().child("users").child("tasks");
        }


        mEditTextView = (EditText) findViewById(R.id.et_task_description);
        mButton = (Button) findViewById((R.id.add_button));



        mEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length()>0) {
                    mButton.setEnabled(true);
                } else {
                    mButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


    }
}
