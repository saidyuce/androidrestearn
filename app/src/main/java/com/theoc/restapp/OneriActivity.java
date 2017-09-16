package com.theoc.restapp.sidemenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.R;
import com.theoc.restapp.adapters.NavAdapter;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.Screens;
import com.theoc.restapp.dataorganization.ServerYanıt;

import info.hoang8f.android.segmented.SegmentedGroup;

import static com.theoc.restapp.R.id.radioGroup;

public class OneriActivity extends AppCompatActivity {
    ListView navListView;
    ServerYanıt serverYanıt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneri);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navListView = (ListView) findViewById(R.id.navListView);
        NavAdapter adapter = new NavAdapter(this);
        navListView.setAdapter(adapter);
        ImageView navLogoImageView = (ImageView) findViewById(R.id.navLogoImageView);
        navLogoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OneriActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        ((TextView) findViewById(R.id.navNameTextView)).setText(GeneralSync.isim + " " + GeneralSync.soyisim);
        final SegmentedGroup segmentedGroup = (SegmentedGroup) findViewById(R.id.segmented);
        segmentedGroup.check(R.id.button21);
        findViewById(R.id.oneriFab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((EditText) findViewById(R.id.oneriEditText)).getText().toString().length() < 10) {
                    Toast.makeText(OneriActivity.this, "Lutfen 10 karakterden uzun bir mesaj girin", Toast.LENGTH_SHORT).show();
                } else {
                    int checkedId = segmentedGroup.getCheckedRadioButtonId();
                    View radioButton = segmentedGroup.findViewById(checkedId);
                    int radioId = segmentedGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) segmentedGroup.getChildAt(radioId);
                    String selection = (String) btn.getText();
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@restearn.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, selection);
                    i.putExtra(Intent.EXTRA_TEXT, ((EditText) findViewById(R.id.oneriEditText)).getText().toString());
                    try {
                        startActivity(Intent.createChooser(i, "E-mail Gonder"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(OneriActivity.this, "E-mail gondermek icin uygun bir uygulama bulunamadi", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GeneralSync.set_screen(Screens.OneriScreen);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
