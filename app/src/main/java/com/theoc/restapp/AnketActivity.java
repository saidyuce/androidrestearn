package com.theoc.restapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.Screens;
import com.theoc.restapp.dataorganization.barcode.ReadBarcode;
import com.theoc.restapp.dataorganization.screendata.GetDataSurwey;
import com.theoc.restapp.helper.SlidingTabLayout;

public class AnketActivity extends AppCompatActivity {

    public ViewPager pager;
    public SlidingTabLayout tabs;
    GetDataSurwey getDataSurwey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);

        getDataSurwey=new GetDataSurwey(this);
        getDataSurwey.start_paralel(ReadBarcode.cafe_id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GeneralSync.set_screen(Screens.SurveyScreen);
    }

    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
