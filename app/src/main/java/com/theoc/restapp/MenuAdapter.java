package com.theoc.restapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theoc.restapp.CafeJoinActivity;
import com.theoc.restapp.MenuActivity;
import com.theoc.restapp.R;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.barcode.ReadBarcode;
import com.theoc.restapp.dataorganization.barcode.SendSiparis;
import com.theoc.restapp.dataorganization.barcode.Siparis;
import com.theoc.restapp.dataorganization.screendata.GetDataMenu;
import com.theoc.restapp.helper.MenuDialog;
import com.theoc.restapp.helper.MenuInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuAdapter extends BaseAdapter {

    GetDataMenu data;
    Context context;
    int positionn;
    SendSiparis sendSiparis;
    MenuInterface menuInterface;

    public MenuAdapter (Context context, GetDataMenu data, int positionn, MenuInterface menuInterface) {
        this.context = context;
        this.data = data;
        this.positionn = positionn;
        this.menuInterface = menuInterface;
    }

    @Override
    public int getCount() {
        return data.get_category_itemlength(positionn);
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
        final ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.menu_single_item, parent, false);
            holder = new ViewHolder();
            holder.foodTextView = (TextView) row.findViewById(R.id.foodTextView);
            holder.priceTextView = (TextView) row.findViewById(R.id.priceTextView);
            holder.addImageButton = (ImageButton) row.findViewById(R.id.addImageButton);


            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.foodTextView.setText(data.get_name(positionn,position));
        holder.priceTextView.setText(data.get_price(positionn,position)+"");
        holder.addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuInterface.setValues(holder.foodTextView.getText().toString(),
                        1,
                        holder.priceTextView.getText().toString(),
                        data.get_point(positionn, position),
                        0,
                        positionn + "");
            }
        });

        // dialoga alınacak onClickte.
        /*Glide.with(context)
                .load(data.get_picture(positionn,position))
                .error(R.drawable.mypointsnargile)
                .placeholder(R.drawable.placeholder)
                .dontAnimate()
                .centerCrop()
                //.crossFade()
                .into(holder.foodImageView);
        */

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*sendSiparis=new SendSiparis();

                List<Siparis> siparises=new ArrayList<Siparis>();

                siparises.add(new Siparis(Integer.parseInt(data.get_menuid(positionn,position)),1,"",false,"bol soğanlı olsun!!"));

                sendSiparis.send_siparis(siparises);
                CafeJoinActivity.socket_message.siparis_ver(ReadBarcode.cafe_id+"", GeneralSync.id+"");*/

                MenuDialog menuDialog = new MenuDialog(context,
                        data.get_info(positionn,position),   // data.get_food_detail(position);
                        data.get_price(positionn, position)+"",
                        data.get_picture(positionn, position),
                        data.get_name(positionn, position),
                        data.get_point(positionn,position) + (char) 0x00B0,
                        positionn + "",
                        data,
                        data);  // data.get_food_point(position);
                menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                menuDialog.show();

            }
        });

        return row;
    }

    public static class ViewHolder {
        TextView foodTextView, priceTextView;
        ImageButton addImageButton;
    }
}
