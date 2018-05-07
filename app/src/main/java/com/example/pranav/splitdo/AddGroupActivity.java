package com.example.pranav.splitdo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddGroupActivity extends AppCompatActivity {

    public static final String TAG = AddGroupActivity.class.getSimpleName();


    private EditText mEditTextView;
    private Button mButton;

    private LinearLayout mAddMembersLinearLayout;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGroupDatabaseReference;
    private DatabaseReference mGroupMembersReference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        mAddMembersLinearLayout = findViewById(R.id.ll_add_members);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mGroupDatabaseReference = mFirebaseDatabase.getReference().child("groups").push();
        mGroupMembersReference = mFirebaseDatabase.getReference().child("groups").child("members");





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

        mAddMembersLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(AddGroupActivity.this, AddMembersActivity.class);

                startActivity(intent);

            }
        });

    }

    public void onClickAddGroup(View view) {
        String input = ((EditText) findViewById(R.id.et_group_name)).getText().toString();

        if (input.length() == 0) {
            return;
        }

        GroupObject groupObject = new GroupObject(input,null, null, null, null, null);

        mGroupDatabaseReference.setValue(groupObject);

        finish();

    }
}
