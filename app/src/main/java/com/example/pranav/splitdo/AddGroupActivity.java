package com.example.pranav.splitdo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddGroupActivity extends AppCompatActivity {

    public static final String TAG = AddGroupActivity.class.getSimpleName();


    private EditText mEditTextView;
    private Button mButton;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGroupDatabaseReference;
    private DatabaseReference mGroupMembersReference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);



        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mGroupDatabaseReference = mFirebaseDatabase.getReference().child("groups").push();
        mGroupMembersReference = mFirebaseDatabase.getReference().child("groups").child("members");
        mGroupDatabaseReference =  mFirebaseDatabase.getReference().child("groups").child("groupInfo");




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



    }
}
