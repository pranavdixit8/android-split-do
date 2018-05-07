package com.example.pranav.splitdo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import static com.example.pranav.splitdo.MainActivity.getUid;
import static com.example.pranav.splitdo.MainActivity.getUser;

public class AddTaskActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener  {

    public static final String TAG = AddTaskActivity.class.getSimpleName();
    private static final int PLACE_PICKER_REQUEST =200 ;
    private static final int REQUEST_LOCATION_PERMISSION = 100 ;

    private EditText mEditTextView;
    private Button mButton;
    private String mPlaceId;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUserDatabaseRef;

    private GoogleApiClient mClient;


    private String mUid;
    private UserObject mUser;
    private String mUsername;
    private String mGroupName;
    private String mGroupId;
    boolean isGroupTask = false;

    private TextView mGroupNameTextView;

    private TextView mLocationNameTextView;
    private TextView mLocationAddressTextView;

    private LinearLayout mAddLocationLinearLayout;
    private LinearLayout mAddedLocationLinearLayout;


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);

        }



        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);


        mUser = getUser();
        mUid = getUid();
        mUsername = mUser.getName();

        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();



        mLocationNameTextView = (TextView) findViewById(R.id.tv_location_name);
        mLocationAddressTextView = (TextView) findViewById(R.id.tv_address);

        mAddLocationLinearLayout = findViewById(R.id.ll_add_location);
        mAddedLocationLinearLayout = findViewById(R.id.ll_added_location);
        mGroupNameTextView = findViewById(R.id.tv_group_name);



        Intent intent = getIntent();


        mFirebaseDatabase = FirebaseDatabase.getInstance();

        if(intent!=null && intent.hasExtra("groupName") ){


            isGroupTask = true;
            mGroupName = intent.getStringExtra("groupName");
            mGroupId = intent.getStringExtra("groupId");
            mGroupNameTextView.setText(mGroupName);
            mDatabaseReference = mFirebaseDatabase.getReference().child("groups").child(mGroupId).child("tasks");
        }else{
            isGroupTask = false;
            mDatabaseReference = mFirebaseDatabase.getReference().child("users").child(mUid).child("tasks");
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

    public void onClickAddTask(View view) {

        String input = ((EditText) findViewById(R.id.et_task_description)).getText().toString();
        if (input.length() == 0) {
            return;
        }
        GregorianCalendar cal = new GregorianCalendar();
        SimpleDateFormat d = new SimpleDateFormat("dd MMM HH:mm", Locale.CANADA);

        String time = d.format(cal.getTime());

        Log.d(TAG, "onClickAddTask: " + d.format(cal.getTime()));


        TaskObject taskObject = new TaskObject(input,time, null, null, mUsername,mUid, null, null,null, null, null,"created",mPlaceId);

        mDatabaseReference.push().setValue(taskObject);

        finish();

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

    public void onAddPlaceClicked(View view) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.permission_not_granted), Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_LONG).show();

        try {
            Log.d(TAG, "onAddPlaceClicked: ");
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(this);
            startActivityForResult(i, PLACE_PICKER_REQUEST);
            
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "onActivityResult: ");
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            if (place == null) {
                Log.i(TAG, "No place selected");
                return;
            }
            String placeName = place.getName().toString();
            String placeAddress = place.getAddress().toString();
            LatLng latLng = place.getLatLng();

            Log.d(TAG, "onActivityResult: Place Name: " + placeName);
            Log.d(TAG, "onActivityResult: PlaceAddress: " +placeAddress);
            mPlaceId = place.getId();
            Log.d(TAG, "onActivityResult: " + mPlaceId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mClient, new String[]{mPlaceId});
            Log.d(TAG, "onActivityResult: " + placeResult);
            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {
                    mLocationNameTextView.setText(places.get(0).getName().toString());
                    mLocationAddressTextView.setText(places.get(0).getAddress().toString());
                    mAddedLocationLinearLayout.setVisibility(View.VISIBLE);
                }
            });

        }
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
