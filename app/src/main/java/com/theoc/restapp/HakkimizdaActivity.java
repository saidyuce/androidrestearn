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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.R;
import com.theoc.restapp.adapters.NavAdapter;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.Screens;
import com.theoc.restapp.dataorganization.ServerYanıt;

public class HakkimizdaActivity extends AppCompatActivity {

    ListView navListView;
    ServerYanıt serverYanıt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hakkimizda);
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
                Intent intent = new Intent(HakkimizdaActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        ((TextView) findViewById(R.id.navNameTextView)).setText(GeneralSync.isim + " " + GeneralSync.soyisim);

        findViewById(R.id.fbImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getFacebookPageURL(HakkimizdaActivity.this)));
                startActivity(intent);
            }
        });
        findViewById(R.id.instaImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent instagramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/_u/restearnapp"));
                instagramIntent.setPackage("com.instagram.android");

                try {
                    startActivity(instagramIntent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/restearnapp")));
                }
            }
        });
        findViewById(R.id.twitterImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                try {
                    // get the Twitter app if possible
                    HakkimizdaActivity.this.getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=873274593238274052"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/restearn"));
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GeneralSync.set_screen(Screens.HakkimizdaScreen);
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

    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=https://www.facebook.com/restearn";
            } else { //older versions of fb app
                return "fb://page/restearn";
            }
        } catch (PackageManager.NameNotFoundException e) {
            return "https://www.facebook.com/restearn"; //normal web url
        }
    }
}
