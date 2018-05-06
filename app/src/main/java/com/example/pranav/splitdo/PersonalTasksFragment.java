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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PersonalTasksFragment extends Fragment {
    ArrayList<TaskObject> mTasks;

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference mUserTasksDatabaseRef;

    private RecyclerView mRecyclerView;
    private PersonalTasksAdapter mAdapter;



    public void setTaskList( ArrayList<TaskObject> tasks){

        mTasks = tasks;

    }

    public void clearAdapterData(){

        if(mAdapter!=null){
            mAdapter.clearData();
        }

    }

    public void setFirebaseDatabase(FirebaseDatabase db){

        mFirebaseDatabase =db;
    }

    public void setDatabaseReference (DatabaseReference databaseRef){
        mUserTasksDatabaseRef = databaseRef;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_personal_tasks,container,false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserTasksDatabaseRef = mFirebaseDatabase.getReference().child("users").child("tasks");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_tasks);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new PersonalTasksAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
