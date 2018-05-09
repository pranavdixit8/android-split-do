package com.example.pranav.splitdo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapMarkerActivity extends AppCompatActivity  implements OnMapReadyCallback {

    private Double mLat;
    private Double mLng;
    private String mName;
    private String mAddress;
    private LatLng mLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_marker);

        Intent intent = getIntent();

        mLat = intent.getDoubleExtra(PersonalTasksFragment.LATITUDE_TOKEN, 0);
        mLng = intent.getDoubleExtra(PersonalTasksFragment.LONGITUDE_TOKEN, 0);
        mName = intent.getStringExtra(PersonalTasksFragment.LOCATION_NAME_TOKEN);
        mAddress = intent.getStringExtra(PersonalTasksFragment.ADDRESS_TOKEN);

        mLatLng = new LatLng(mLat,mLng);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(mLatLng)
                .title(mName)).setSnippet(mAddress);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,11));

    }
}
