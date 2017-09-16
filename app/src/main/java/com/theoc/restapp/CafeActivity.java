package com.theoc.restapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.Screens;

public class CafeActivity extends AppCompatActivity implements OnMapReadyCallback {

    String cafe_id;
    String name;
    double cafe_x;
    double cafe_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        cafe_id = intent.getStringExtra("cafe_id");
        cafe_x = intent.getDoubleExtra("cafe_x", 0.0);
        cafe_y = intent.getDoubleExtra("cafe_y", 0.0);

        TextView cafeTextView = (TextView) findViewById(R.id.cafeTextView);
        cafeTextView.setText(intent.getStringExtra("cafe_detail"));

        Glide.with(this)
                .load("http://restearndev.xyz/RestUpp/KontrolPaneli/caferesim/" + intent.getStringExtra("large_image"))
                .error(R.drawable.mypointsnargile)
                .placeholder(R.drawable.placeholder)
                .dontAnimate()
                .centerCrop()
                //.crossFade()
                .into((ImageView) findViewById(R.id.largeImageView));

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GeneralSync.set_screen(Screens.CafeDetailScreen);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + cafe_x + "," + cafe_y));
                startActivity(intent);
            }
        });
        LatLng location = new LatLng(cafe_x, cafe_y);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 18);
        googleMap.addMarker(new MarkerOptions().position(location).title("Yol Tarifi Al"));
        googleMap.animateCamera(cameraUpdate);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }
}
