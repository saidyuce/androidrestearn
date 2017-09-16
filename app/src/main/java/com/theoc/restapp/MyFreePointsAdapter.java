package com.theoc.restapp.adapters;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theoc.restapp.MyPointsDetailActivity;
import com.theoc.restapp.R;
import com.theoc.restapp.dataorganization.screendata.GetDataFreePoint;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saidyuce on 3.9.2017.
 */
public class MyFreePointsAdapter extends BaseAdapter {

    GetDataFreePoint data;
    Context context;
    double point;
    int counter;

    public MyFreePointsAdapter (Context context, GetDataFreePoint data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.get_size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        View row = convertView;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.my_points_single_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (CircleImageView) row.findViewById(R.id.placeholderImageView2);
            holder.kafeadıTextView = (TextView) row.findViewById(R.id.kafeadıTextView);
            holder.kafelokasyonTextView = (TextView) row.findViewById(R.id.kafelokasyonTextView);
            holder.dereceTextView = (TextView) row.findViewById(R.id.dereceTextView);
            holder.ödülTextView = (TextView) row.findViewById(R.id.ödülTextView);
            holder.progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
            holder.main_frame = (RelativeLayout) row.findViewById(R.id.firstRL);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        point = ((360 / data.get_cafecoin(position)) * Integer.parseInt(data.get_point(position))) % 360;
        counter = Integer.parseInt(data.get_point(position)) / data.get_cafecoin(position);
        Log.d("POINT=", data.get_point(position));
        Log.d("COIN=", data.get_cafecoin(position)+"");
        holder.kafeadıTextView.setText(data.get_cafename(position));
        holder.kafelokasyonTextView.setText(data.get_city(position));
        holder.dereceTextView.setText((int) point + "");
        holder.ödülTextView.setText(counter + "");
        Glide.with(context)
                .load(data.get_cafe_picture_url(position))
                .error(R.drawable.mypointsnargile)
                .placeholder(R.drawable.placeholder)
                .dontAnimate()
                .centerCrop()
                //.crossFade()
                .into(holder.imageView);
        holder.main_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (AppCompatActivity) context;
                Intent intent = new Intent(context, MyPointsDetailActivity.class);
                intent.putExtra("cafe_id", data.get_cafeid(position));
                intent.putExtra("point", data.get_point(position));
                intent.putExtra("coin", data.get_cafecoin(position));
                intent.putExtra("cafe_name", data.get_cafename(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        final ObjectAnimator animation = ObjectAnimator.ofInt (holder.progressBar, "progress", 0, Integer.parseInt((int) point + "")); // desired degree

        animation.setDuration (1500); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        animation.start();
        return row;
    }

    public static class ViewHolder {
        CircleImageView imageView;
        TextView kafeadıTextView, kafelokasyonTextView, dereceTextView, ödülTextView;
        RelativeLayout main_frame;
        ProgressBar progressBar;
    }
}

