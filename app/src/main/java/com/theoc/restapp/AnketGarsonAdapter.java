package com.theoc.restapp.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theoc.restapp.R;
import com.theoc.restapp.dataorganization.screendata.GetDataSurwey;
import com.theoc.restapp.helper.AnketGarsonDialog;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnketGarsonAdapter extends BaseAdapter {


    Context context;
    GetDataSurwey getDataSurwey;

    public AnketGarsonAdapter (Context context,GetDataSurwey getDataSurwey) {
        this.context = context;
        this.getDataSurwey=getDataSurwey;
    }
    @Override
    public int getCount() {
        return getDataSurwey.get_cafe_waiter_count();
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
            row = inflater.inflate(R.layout.anket_garson_single_item, parent, false);
            holder = new ViewHolder();
            holder.garsonNameTextView = (TextView) row.findViewById(R.id.garsonNameTextView);
            holder.garsonImageView = (CircleImageView) row.findViewById(R.id.garsonImageView);
            holder.coverImageView = (CircleImageView) row.findViewById(R.id.coverImageView);
            holder.main_frame = (RelativeLayout) row.findViewById(R.id.mainRL);
            holder.rating_textview=(TextView)row.findViewById(R.id.ratingTextView) ;
            holder.starImageView = (ImageView) row.findViewById(R.id.imageView5);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.main_frame.setTag(R.id.frame_id, holder);
        holder.main_frame.setTag(R.id.waiter_id, "0"); // buraya data.get_waiter_id(position); gelecek
        holder.garsonNameTextView.setText(getDataSurwey.get_waitername(position));
        Glide.with(context)
                .load(getDataSurwey.get_cafe_waiter_picture(position))
                .error(R.drawable.mypointsnargile)
                .placeholder(R.drawable.placeholder)
                .dontAnimate()
                .centerCrop()
                .into(holder.garsonImageView);
        holder.main_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnketGarsonDialog garsonDialog = new AnketGarsonDialog(context, getDataSurwey.get_waitername(position),v);
                garsonDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                garsonDialog.show();

            }
        });

        return row;
    }



    public static class ViewHolder {
        public   TextView garsonNameTextView;
        public    CircleImageView garsonImageView, coverImageView;
        public     RelativeLayout main_frame;
        public TextView rating_textview;
        public ImageView starImageView;
    }
}
