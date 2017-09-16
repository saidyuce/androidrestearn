package com.theoc.restapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.R;
import com.theoc.restapp.dataorganization.screendata.GetDataHomeScreen;

public class ImageAdapter extends PagerAdapter {
    Context context;
    GetDataHomeScreen data;

    public ImageAdapter(Context context, GetDataHomeScreen data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.get_premium_size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Glide.with(context)
                .load("http://restearndev.xyz/RestUpp/KontrolPaneli/caferesim/" + data.get_premium_image(position))
                .error(R.drawable.mypointsnargile)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .crossFade()
                .into(imageView);
        container.addView(imageView, 0);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}