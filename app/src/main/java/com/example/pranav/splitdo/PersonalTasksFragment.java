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

public class PersonalTasksFragment extends Fragment implements PersonalTasksAdapter.OnLocationClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener  {

    public static final String TAG = PersonalTasksFragment.class.getSimpleName();

    ArrayList<TaskObject> mTasks;

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference mUserTasksDatabaseRef;

    private RecyclerView mRecyclerView;
    private PersonalTasksAdapter mAdapter;

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
        mUserTasksDatabaseRef = databaseRef;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_personal_tasks,container,false);

        GeoDataClient geoDataClient = Places.getGeoDataClient(getContext());



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserTasksDatabaseRef = mFirebaseDatabase.getReference().child("users").child("tasks");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_tasks);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new PersonalTasksAdapter(getContext(), this);

        mAdapter.setGoogleAPIClient(geoDataClient);
        mRecyclerView.setAdapter(mAdapter);


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

        mUserTasksDatabaseRef.addChildEventListener(mChildEventListener);


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
}
