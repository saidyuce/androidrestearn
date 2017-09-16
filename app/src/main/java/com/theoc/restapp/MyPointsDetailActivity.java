package com.theoc.restapp;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.Screens;
import com.theoc.restapp.dataorganization.barcode.OdulQrRead;
import com.theoc.restapp.dataorganization.barcode.SiparisQRread;
import com.theoc.restapp.dataorganization.screendata.GetDataPointsDetail;

import java.lang.reflect.Field;

public class MyPointsDetailActivity extends AppCompatActivity {

    String cafe_id;
    int point=0;
    int counter=0;
    public TextView prizeTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_points_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        if (intent.getStringExtra("point")!=null&&!intent.getStringExtra("point").equals("")) {
            point = ((360 / intent.getIntExtra("coin", 99)) * Integer.parseInt(intent.getStringExtra("point"))) % 360;
            counter = Integer.parseInt(intent.getStringExtra("point")) / intent.getIntExtra("coin", 99);
            Log.d("POINT=", point+"");
            Log.d("COIN=", counter+"");
            //point = Integer.parseInt(intent.getStringExtra("point")) % 360;
        } else {
            point = 0;
        }
        cafe_id = intent.getStringExtra("cafe_id");
        String cafe_name = intent.getStringExtra("cafe_name");
        toolbar.setTitle(cafe_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prizeTextView = (TextView) findViewById(R.id.prizeTextView);
        ((TextView) findViewById(R.id.odulTextView)).setText(counter+"");
        if (counter >= 1) {
            findViewById(R.id.prizeButton).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.prizeButton).setVisibility(View.GONE);
        }
        findViewById(R.id.prizeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPointsDetailActivity.this, QRActivity.class);
                startActivity(intent);
            }
        });
        TextView dereceTV = (TextView) findViewById(R.id.dereceTextView);
        dereceTV.setText(String.valueOf(point));

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, point);
        animation.setDuration (1500);
        animation.setInterpolator (new DecelerateInterpolator());
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.start ();
            }
        }, 500);
        start();
    }

    public void start(){
        GetDataPointsDetail getDataPointsDetail=new GetDataPointsDetail(this, cafe_id);
        getDataPointsDetail.start_paralel();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GeneralSync.set_screen(Screens.PointsDetailScreen);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }
}
