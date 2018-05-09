package com.example.pranav.splitdo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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
    private String mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_tasks);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mGroupNameTextView = (TextView) findViewById(R.id.tv_group_name);

        Intent intent = getIntent();

        if(intent!= null){

            mGroupName = intent.getStringExtra(GroupsAdapter.GROUP_NAME_TOKEN);
            mGroupNameTextView.setText(mGroupName);

            mGroupId = intent.getStringExtra(GroupsAdapter.GROUP_ID_TOKEN);


        }



        PersonalTasksFragment fragment1 = new PersonalTasksFragment();

        fragment1.setGroupFlag(true, mGroupId);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.group_tasks_container, fragment1).commit();

        mTasksFab = (FloatingActionButton) findViewById(R.id.fab_add_task);

        mTasksFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(GroupTasksActivity.this, AddTaskActivity.class);
                addTaskIntent.putExtra(GroupsAdapter.GROUP_ID_TOKEN, mGroupId);
                addTaskIntent.putExtra(GroupsAdapter.GROUP_NAME_TOKEN, mGroupName);
                startActivity(addTaskIntent);
            }
        });
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
}
