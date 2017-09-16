package com.theoc.restapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.theoc.restapp.adapters.NavAdapter;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.Screens;
import com.theoc.restapp.dataorganization.screendata.GetDataMap;
import com.theoc.restapp.helper.FusedLocationReceiver;
import com.theoc.restapp.helper.GoogleLocationClass;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {


    public GoogleMap mMap;
    public Marker markermain;
    ImageButton myLocationButton;
    public android.support.v7.widget.SearchView actionView2;
    GetDataMap getDataMap;
    GoogleLocationClass googleLocationClass;
    ListView navListView;
    FloatingActionButton mapFab;
    double currentMarkerLat;
    double currentMarkerLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getDataMap = new GetDataMap(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navListView = (ListView) findViewById(R.id.navListView);
        NavAdapter adapter = new NavAdapter(this);
        navListView.setAdapter(adapter);

        mapFab = (FloatingActionButton) findViewById(R.id.mapFab);
        mapFab.animate().alpha(0.0f);
        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + currentMarkerLat + "," + currentMarkerLong));
                startActivity(intent);
            }
        });
        LinearLayout MyPointsLL = (LinearLayout) findViewById(R.id.MyPointsLL);
        MyPointsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, MyPointsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        myLocationButton = (ImageButton) findViewById(R.id.mylocationbutton);
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLocationClass = new GoogleLocationClass(MapActivity.this, new FusedLocationReceiver() {
                    @Override
                    public void onLocationChanged() {
                    }
                });
                googleLocationClass.checkLocation(2);
            }
        });

        ImageView midiconImageView = (ImageView) findViewById(R.id.midiconImageView);
        midiconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, QRActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        ((TextView) findViewById(R.id.navNameTextView)).setText(GeneralSync.isim + " " + GeneralSync.soyisim);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        final MenuItem toggleservice = menu.findItem(R.id.action_switch);
        final SwitchCompat actionView = (SwitchCompat) toggleservice.getActionView();
        actionView.setChecked(true);
        actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean check = buttonView.isChecked();
                if (!check) {
                    // asd
                    Intent intent = new Intent(MapActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });
        final MenuItem searchservice = menu.findItem(R.id.action_search);
        actionView2 = (SearchView) searchservice.getActionView();
        actionView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getDataMap.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getDataMap.populate(newText);
                return true;
            }
        });
        actionView2.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                getDataMap.navigate(position);
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                getDataMap.navigate(position);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            getDataMap.createFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setCompassEnabled(false);
    }
    @Override
    protected void onResume() {
        super.onResume();
        GeneralSync.set_screen(Screens.MapScreen);
        googleLocationClass = new GoogleLocationClass(this, new FusedLocationReceiver() {
            @Override
            public void onLocationChanged() {
            }
        });
        googleLocationClass.checkLocation(1);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        currentMarkerLat = marker.getPosition().latitude;
        currentMarkerLong = marker.getPosition().longitude;
        mapFab.animate().alpha(1.0f);
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mapFab.animate().alpha(0.0f);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, CafeActivity.class);
        intent.putExtra("name", getDataMap.get_name(Integer.parseInt(marker.getSnippet())));
        intent.putExtra("cafe_x", getDataMap.get_x(Integer.parseInt(marker.getSnippet())));
        intent.putExtra("cafe_y", getDataMap.get_y(Integer.parseInt(marker.getSnippet())));
        intent.putExtra("cafe_id", getDataMap.get_id(Integer.parseInt(marker.getSnippet())));
        startActivity(intent);
    }
}