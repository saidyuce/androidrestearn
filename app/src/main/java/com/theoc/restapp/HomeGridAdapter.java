package com.theoc.restapp.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.dataorganization.screendata.GetDataHomeScreen;
import com.theoc.restapp.R;
import com.theoc.restapp.helper.HomeDialog;

public class HomeGridAdapter extends BaseAdapter {

    GetDataHomeScreen data;
    Context context;

    public HomeGridAdapter (Context context, GetDataHomeScreen data) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.home_single_item, parent, false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) row.findViewById(R.id.placeholderImageView);
                holder.kafeNameTextView = (TextView) row.findViewById(R.id.kafeNameTextView);
                holder.kafeLocationTextView = (TextView) row.findViewById(R.id.kafeLocationTextView);
                holder.main_frame = (CardView) row.findViewById(R.id.itemCV);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            holder.kafeNameTextView.setText(data.get_prize(position));
            holder.kafeLocationTextView.setText(data.get_cafe_distance(position));
            Glide.with(context)
                    .load(data.get_campaing_picture_url(position))
                    .error(R.drawable.mypointsnargile)
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.imageView);

        final int finalPosition = position;
        holder.main_frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeDialog dialog = new HomeDialog(context,
                            data.get_campaing_category(finalPosition),
                            data.get_campaing_detail(finalPosition),
                            data.get_cafe_id(finalPosition),
                            data.get_cafe_icon(finalPosition),
                            data.get_cafe_name(finalPosition),
                            data.get_cafe_x(finalPosition),
                            data.get_cafe_y(finalPosition),
                            data.get_cafe_detail(finalPosition),
                            data.get_cafe_large_image(finalPosition));
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();
                }
        });
        return row;
    }

    public static class ViewHolder {
        ImageView imageView;
        TextView kafeNameTextView,
                 kafeLocationTextView;
        CardView main_frame;
    }
}