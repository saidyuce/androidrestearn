package com.theoc.restapp;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.theoc.restapp.adapters.NavAdapter;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.Screens;
import com.theoc.restapp.dataorganization.barcode.OdulQrRead;
import com.theoc.restapp.dataorganization.barcode.SiparisQRread;
import com.theoc.restapp.dataorganization.screendata.GetDataFreePoint;
import com.theoc.restapp.dataorganization.screendata.GetDataPoints;

import org.json.JSONException;
import org.json.JSONObject;

public class MyPointsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ListView listView, navListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_points);
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

        LinearLayout HomeLL = (LinearLayout) findViewById(R.id.HomeLL);
        HomeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPointsActivity.this.finish();
                overridePendingTransition(0, 0);
            }
        });

        ImageView midiconImageView = (ImageView) findViewById(R.id.midiconImageView);
        midiconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPointsActivity.this, QRActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        listView = (ListView) findViewById(R.id.listView);
       if(getIntent().getStringExtra("qrText")==null){

           start();
       }else {
           qrRead(getIntent().getStringExtra("qrText"));
       }
    }

    void qrRead(String qrcode) {
        try {
            JSONObject jj_temp=new JSONObject(qrcode);
            String tempTip = jj_temp.getString("qr_tip");
            if (tempTip != null) {
                if (tempTip.equals("siparis")) {
                    SiparisQRread qRread=new SiparisQRread(this);
                    qRread.read(qrcode);
                } else {
                    OdulQrRead qRread=new OdulQrRead(this);
                    qRread.read(qrcode);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   public void OnQrSuccess(String response){

       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setMessage("Puan kazandınız!")
               .setPositiveButton("HARİKA", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                   }
               })
               .show();

       start();
    }

    public void OnOdulQrSuccess(String response){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ödül kullandınız!")
                .setPositiveButton("HARİKA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .show();

        start();
    }

  public  void OnQrError(String error){
      if (error.equals("wrong")) {
          Toast.makeText(this, "Hatalı bir QR kod okuttunuz", Toast.LENGTH_SHORT).show();
      } else {
          Toast.makeText(this, "Lütfen internet bağlantınızı kontrol edin", Toast.LENGTH_SHORT).show();
      }
      start();
  }

    @Override
    protected void onResume() {
        super.onResume();
        GeneralSync.set_screen(Screens.PointsScreen);
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
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void start(){


       //normal puan
      /*  ((TextView) findViewById(R.id.navNameTextView)).setText(GeneralSync.isim + " " + GeneralSync.soyisim);
        GetDataPoints getDataPoints=new GetDataPoints(this);
        getDataPoints.start_paralel();*/

        //free puan
        ((TextView) findViewById(R.id.navNameTextView)).setText(GeneralSync.isim + " " + GeneralSync.soyisim);
        GetDataFreePoint getDataPoints=new GetDataFreePoint(this);
        getDataPoints.start_paralel();

    }











}
