package com.example.pranav.splitdo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GroupTasksActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGroupDatabaseReference;
    private TextView mGroupNameTextView;

    private FloatingActionButton mTasksFab;
    private String mGroupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_tasks);

        mGroupNameTextView = (TextView) findViewById(R.id.tv_group_name);

        Intent intent = getIntent();

        if(intent!= null){

            mGroupName = intent.getStringExtra("name");
            mGroupNameTextView.setText(mGroupName);



        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mGroupDatabaseReference = mFirebaseDatabase.getReference().child("groups").child(mGroupName).child("tasks");

        PersonalTasksFragment fragment1 = new PersonalTasksFragment();

        fragment1.setFirebaseDatabase(mFirebaseDatabase);
        fragment1.setDatabaseReference(mGroupDatabaseReference);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.group_tasks_container, fragment1).commit();

        mTasksFab = (FloatingActionButton) findViewById(R.id.fab_add_task);

        mTasksFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(GroupTasksActivity.this, AddTaskActivity.class);
                addTaskIntent.putExtra("groupName", mGroupName);
                startActivity(addTaskIntent);
            }
        });
    }
}