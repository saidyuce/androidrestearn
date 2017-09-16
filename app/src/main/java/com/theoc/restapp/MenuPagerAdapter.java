package com.theoc.restapp.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.theoc.restapp.MenuActivity;
import com.theoc.restapp.MyFragment;
import com.theoc.restapp.dataorganization.screendata.GetDataMenu;
import com.theoc.restapp.helper.SmartFragmentStatePagerAdapter;

public class MenuPagerAdapter extends FragmentPagerAdapter {


    GetDataMenu data;

    public MenuPagerAdapter(FragmentManager fm, GetDataMenu data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return new MyFragment(position,data);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return data.get_category(position);
    }

    @Override
    public int getCount() {
        return data.get_category_length();
    }
}