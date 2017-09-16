package com.theoc.restapp.sidemenu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.R;
import com.theoc.restapp.adapters.NavAdapter;
import com.theoc.restapp.dataorganization.Ayarlar;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.Screens;
import com.theoc.restapp.dataorganization.ServerYanıt;

import org.json.JSONException;
import org.json.JSONObject;

public class AyarlarActivity extends AppCompatActivity {

    ListView navListView;
    ServerYanıt serverYanıt;
    Ayarlar ayarlar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);
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
                Intent intent = new Intent(AyarlarActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        ((TextView) findViewById(R.id.navNameTextView)).setText(GeneralSync.isim + " " + GeneralSync.soyisim);
        ((EditText) findViewById(R.id.nameEditText)).setText(GeneralSync.isim);
        ((EditText) findViewById(R.id.surnameEditText)).setText(GeneralSync.soyisim);

        findViewById(R.id.ayarlarFab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!((EditText) findViewById(R.id.nameEditText)).getText().toString().equals("") &&
                        !((EditText) findViewById(R.id.surnameEditText)).getText().toString().equals("")) {
                    if (!((EditText) findViewById(R.id.nameEditText)).getText().toString().equals(GeneralSync.isim) ||
                            !((EditText) findViewById(R.id.surnameEditText)).getText().toString().equals(GeneralSync.soyisim)) {
                        ayarlar = new Ayarlar(AyarlarActivity.this);
                        ayarlar.ayarlari_kaydet(((EditText) findViewById(R.id.nameEditText)).getText().toString(),
                                ((EditText) findViewById(R.id.surnameEditText)).getText().toString(),
                                "01/01/1970",
                                GeneralSync.id+"");
                    } else {
                        Toast.makeText(AyarlarActivity.this, "Hiçbir değişiklik yapmadınız", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AyarlarActivity.this, "Lütfen tüm gerekli alanları doldurun", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GeneralSync.set_screen(Screens.AyarlarScreen);
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

    public void ayarlar_server_cevabı() {
        GeneralSync.isim = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        GeneralSync.soyisim = ((EditText) findViewById(R.id.surnameEditText)).getText().toString();
        ((TextView) findViewById(R.id.navNameTextView)).setText(GeneralSync.isim + " " + GeneralSync.soyisim);
        ((EditText) findViewById(R.id.nameEditText)).setText(GeneralSync.isim);
        ((EditText) findViewById(R.id.surnameEditText)).setText(GeneralSync.soyisim);
        Toast.makeText(this, "Ayarlarınız başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
    }
}
