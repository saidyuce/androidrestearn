package com.theoc.restapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.theoc.restapp.CafeFragment;
import com.theoc.restapp.GarsonFragment;
import com.theoc.restapp.dataorganization.screendata.GetDataSurwey;

public class AnketPagerAdapter extends FragmentPagerAdapter {

    GetDataSurwey getDataSurwey;
    public AnketPagerAdapter(FragmentManager fm,GetDataSurwey getDataSurwey) {
        super(fm);
        this.getDataSurwey=getDataSurwey;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CafeFragment(getDataSurwey);
            case 1:
                return new GarsonFragment(getDataSurwey);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Kafe Anketi";
            case 1:
                return "Garson Anketi";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return getDataSurwey.surwey_category; // data.get_category_length();
    }
}