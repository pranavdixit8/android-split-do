package com.example.pranav.splitdo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.pranav.splitdo.MainActivity.getUid;
import static com.example.pranav.splitdo.MainActivity.getUser;

public class PersonalTasksFragment extends Fragment implements PersonalTasksAdapter.OnLocationClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, PersonalTasksAdapter.OnLongPressedUpdate {

    public static final String TAG = PersonalTasksFragment.class.getSimpleName();

    ArrayList<TaskObject> mTasks;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mTasksDatabaseReference;
    private DatabaseReference mGroupDatabaseReference;

    private DatabaseReference mDatabaseReference;

    private RecyclerView mRecyclerView;
    private PersonalTasksAdapter mAdapter;

    private String mUid;
    private UserObject mUser;
    private String mGroupId;
    private  boolean isGroupTasks = false;

    private ChildEventListener mChildEventListener;



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
        mTasksDatabaseReference = databaseRef;
    }


    public void setGroupFlag(boolean flag, String groupId) {
        isGroupTasks = true;
        mGroupId = groupId;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_personal_tasks,container,false);

        GeoDataClient geoDataClient = Places.getGeoDataClient(getContext());





        mUid = getUid();
        mUser = getUser();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mTasksDatabaseReference = mFirebaseDatabase.getReference().child("users").child(mUid).child("tasks");



        if(isGroupTasks){
            mGroupDatabaseReference = mFirebaseDatabase.getReference().child("groups").child(mGroupId).child("tasks");
            mDatabaseReference = mGroupDatabaseReference;
        }else {
            mDatabaseReference = mTasksDatabaseReference;
        }


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_tasks);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new PersonalTasksAdapter(getContext(), this, this);

        mAdapter.setGoogleAPIClient(geoDataClient);
        mRecyclerView.setAdapter(mAdapter);



        return view;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: ");
    }

    @Override
    public void onclick(LatLng latLng, String name, String address) {
        Intent intent = new Intent(getContext(),MapMarkerActivity.class);
        Double lat = latLng.latitude;
        Double lng = latLng.longitude;
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        intent.putExtra("name", name);
        intent.putExtra("address", address);

        startActivity(intent);
    }

    @Override
    public void onLongPressed(TaskObject obj) {

        Map<String, Object> task = new HashMap<>();

        task.put(obj.getTaskId(),obj);

        mTasksDatabaseReference.updateChildren(task);


    }

    @Override
    public void onResume() {
        super.onResume();
        attachTasksDatabaseListener();
    }


    @Override
    public void onPause() {
        super.onPause();
        mAdapter.clearData();
        detachTasksDatabaseListener();
    }

    void detachTasksDatabaseListener(){

        if (mChildEventListener != null) {
            mTasksDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    void attachTasksDatabaseListener(){

        if(mChildEventListener==null) {

            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    TaskObject object = dataSnapshot.getValue(TaskObject.class);
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

            mDatabaseReference.addChildEventListener(mChildEventListener);

        }
    }


}
