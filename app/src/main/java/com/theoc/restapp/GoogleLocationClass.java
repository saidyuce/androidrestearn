package com.theoc.restapp.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.theoc.restapp.MapActivity;
import com.theoc.restapp.dataorganization.screendata.GetDataMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GoogleLocationClass implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "FusedLocationService";

    private static final long INTERVAL = 1000 * 30;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final long TWO_MIN = 1000 * 120;
    private static final long REFRESH_TIME = TWO_MIN * 5;
    private static final float MINIMUM_ACCURACY = 50.0f;
    private Activity locationActivity;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Location location;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

    private FusedLocationReceiver locationReceiver = null;

    public GoogleLocationClass(Activity locationActivity, FusedLocationReceiver locationReceiver) {
        this.locationReceiver = locationReceiver;
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        this.locationActivity = locationActivity;

        googleApiClient = new GoogleApiClient.Builder(locationActivity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(locationActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(locationActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        // Schedule a Thread to unregister location listeners
        Executors.newScheduledThreadPool(1).schedule(new Runnable() {
            @Override
            public void run() {
                fusedLocationProviderApi.removeLocationUpdates(googleApiClient,
                        GoogleLocationClass.this);
            }
        }, TWO_MIN, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onLocationChanged(Location location) {
        //if the existing location is empty or
        //the current location accuracy is greater than existing accuracy
        //then store the current location
        if (null == this.location || location.getAccuracy() < this.location.getAccuracy()) {
            this.location = location;
// let's inform my client class through the receiver
            locationReceiver.onLocationChanged();
            //if the accuracy is not better, remove all location updates for this listener
            if (this.location.getAccuracy() < MINIMUM_ACCURACY) {
                fusedLocationProviderApi.removeLocationUpdates(googleApiClient, this);
            }
        }
    }

    public Location getLocation() {
        return this.location;
    }

    private Location getLastLocation() {
        if (ActivityCompat.checkSelfPermission(locationActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(locationActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return fusedLocationProviderApi.getLastLocation(googleApiClient);
        }
        return fusedLocationProviderApi.getLastLocation(googleApiClient);
    }

    private void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(locationActivity);
        alertDialog.setTitle("Unable to get location");
        alertDialog.setMessage("Enable Location services to proceed");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                locationActivity.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public int key;

    public void checkLocation(final int key) {
        this.key = key;
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        switch (key) {
                            case 1:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        GetDataMap getDataMap = new GetDataMap(locationActivity);
                                        getDataMap.start_paralel();
                                    }
                                }, 1000);
                                break;
                            case 2:

                                Location location = getLocation();
                                if (location == null) {
                                    location = getLastLocation();
                                }
                                if (location != null) {
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();
                                    LatLng latLng = new LatLng(latitude, longitude);
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                                    if (((MapActivity) locationActivity).markermain != null) {
                                        ((MapActivity) locationActivity).markermain.remove();
                                    }
                                    ((MapActivity) locationActivity).markermain = ((MapActivity) locationActivity).mMap.addMarker(new MarkerOptions().position(latLng));
                                    ((MapActivity) locationActivity).mMap.animateCamera(cameraUpdate);
                                }
                                break;
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        showSettingsAlert();
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}