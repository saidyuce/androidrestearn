package com.theoc.restapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.R;
import com.theoc.restapp.sidemenu.AyarlarActivity;
import com.theoc.restapp.sidemenu.HakkimizdaActivity;
import com.theoc.restapp.sidemenu.OneriActivity;
import com.theoc.restapp.sidemenu.SSSActivity;

public class NavAdapter extends BaseAdapter {

    private String[] texts = {
            "Kampanyalar",
            "Hakkımızda",
            "Değerlendir",
            "Öneri ve Şikayet",
            "SSS",
            "Ayarlar"
    };
    private int[] images = {
            R.drawable.kampanyalar,
            R.drawable.hakkimizda,
            R.drawable.degerlendir,
            R.drawable.sss,
            R.drawable.oneri,
            R.drawable.ayarlar
    };

    private Context context;

    public NavAdapter (Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return texts.length;
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
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.nav_single_item, parent, false);
            holder = new ViewHolder();
            holder.navTextView = (TextView) row.findViewById(R.id.navTextView);
            holder.navImageView = (ImageView) row.findViewById(R.id.navImageView);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.navTextView.setText(texts[position]);
        holder.navImageView.setImageResource(images[position]);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        Activity activity0 = (Activity) context;
                        Intent intent0 = new Intent(context, HomeActivity.class);
                        activity0.startActivity(intent0);
                        break;
                    case 1:
                        Activity activity = (Activity) context;
                        Intent intent = new Intent(context, HakkimizdaActivity.class);
                        activity.startActivity(intent);
                        break;
                    case 2:
                        break;
                    case 3:
                        Activity activity2 = (Activity) context;
                        Intent intent3 = new Intent(context, OneriActivity.class);
                        activity2.startActivity(intent3);
                        break;
                    case 4:
                        Activity activity3 = (Activity) context;
                        Intent intent5 = new Intent(context, SSSActivity.class);
                        activity3.startActivity(intent5);
                        break;
                    case 5:
                        Activity activity4 = (Activity) context;
                        Intent intent6 = new Intent(context, AyarlarActivity.class);
                        activity4.startActivity(intent6);
                        break;
                    default:
                        // do nothing
                        break;
                }
            }
        });

        return row;
    }

    private static class ViewHolder {
        ImageView navImageView;
        TextView navTextView;

    }
}
