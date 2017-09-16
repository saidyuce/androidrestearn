package com.theoc.restapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.theoc.restapp.R;
import com.theoc.restapp.dataorganization.screendata.GetDataSurwey;

import java.util.concurrent.ExecutionException;

public class AnketCafeAdapter extends BaseAdapter {

    Context context;
    GetDataSurwey getDataSurwey;
    public AnketCafeAdapter(Context context, GetDataSurwey getDataSurwey) {
        this.context = context;
        this.getDataSurwey=getDataSurwey;
    }

    @Override
    public int getCount() {
        return getDataSurwey.get_cafe_Surwey_count(); // data.get_size();
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
        View row = convertView;
        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.anket_cafe_single_item, parent, false);
            holder = new ViewHolder();
            holder.soruTextView = (TextView) row.findViewById(R.id.soruTextView);
            holder.soruNoTextView = (TextView) row.findViewById(R.id.soruNoTextView);
            holder.radioGroup = (RadioGroup) row.findViewById(R.id.radioGroup);
            holder.radioButton1 = (RadioButton) row.findViewById(R.id.radioButton1);
            holder.radioButton2 = (RadioButton) row.findViewById(R.id.radioButton2);
            holder.radioButton3 = (RadioButton) row.findViewById(R.id.radioButton3);
            holder.radioButton4 = (RadioButton) row.findViewById(R.id.radioButton4);
            holder.radioButton1.setText(getDataSurwey.get_a(position));
            holder.radioButton2.setText(getDataSurwey.get_b(position));
            holder.radioButton3.setText(getDataSurwey.get_c(position));
            holder.radioButton4.setText(getDataSurwey.get_d(position));
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.radioGroup.check(holder.radioButton1.getId());
                holder.radioButton1.setBackgroundColor(Color.parseColor("#FFCFD8DC"));
                holder.radioButton2.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                holder.radioButton3.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                holder.radioButton4.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            }
        });
        holder.radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.radioGroup.check(holder.radioButton2.getId());
                holder.radioButton1.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                holder.radioButton2.setBackgroundColor(Color.parseColor("#FFCFD8DC"));
                holder.radioButton3.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                holder.radioButton4.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            }
        });
        holder.radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.radioGroup.check(holder.radioButton3.getId());
                holder.radioButton1.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                holder.radioButton2.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                holder.radioButton3.setBackgroundColor(Color.parseColor("#FFCFD8DC"));
                holder.radioButton4.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            }
        });
        holder.radioButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.radioGroup.check(holder.radioButton4.getId());
                holder.radioButton1.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                holder.radioButton2.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                holder.radioButton3.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                holder.radioButton4.setBackgroundColor(Color.parseColor("#FFCFD8DC"));
            }
        });

        /*holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.radioButton1:
                        holder.linearLayout1.setBackgroundColor(Color.parseColor("#FFCFD8DC"));
                        holder.linearLayout2.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        holder.linearLayout3.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        holder.linearLayout4.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        break;
                    case R.id.radioButton2:
                        holder.linearLayout1.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        holder.linearLayout2.setBackgroundColor(Color.parseColor("#FFCFD8DC"));
                        holder.linearLayout3.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        holder.linearLayout4.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        break;
                    case R.id.radioButton3:
                        holder.linearLayout1.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        holder.linearLayout2.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        holder.linearLayout3.setBackgroundColor(Color.parseColor("#FFCFD8DC"));
                        holder.linearLayout4.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        break;
                    case R.id.radioButton4:
                        holder.linearLayout1.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        holder.linearLayout2.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        holder.linearLayout3.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        holder.linearLayout4.setBackgroundColor(Color.parseColor("#FFCFD8DC"));
                        break;
                    default:
                        break;
                }
            }
        });*/


        holder.soruTextView.setText(getDataSurwey.get_question(position)); // data.get_soru(position);
        holder.soruNoTextView.setText((position + 1)+")"); // burada ayrı metoda gerek yok, zaten sorular 1,2,3,4... diye gidecek.
     /*    try {
          bitmap = Glide.with(context)
                    .load("") // data.get_image_url(position);
                    .asBitmap()
                    .into(80, 80)
                    .get();
            drawable = new BitmapDrawable(context.getResources(), bitmap);

            // Burada 4 anket şıkkı için buton resimleri çekilecek. Ana çekme formatını yazdım.
            // Glide ile bitmap çekip, drawable çevirip setliyoruz.

            //    holder.radioButton1.setButtonDrawable(drawable);
            //    holder.radioButton2.setButtonDrawable(drawable);
            //    holder.radioButton3.setButtonDrawable(drawable);
            //     holder.radioButton4.setButtonDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        return row;
    }

    public static class ViewHolder {
        TextView soruTextView, soruNoTextView;
        RadioGroup radioGroup;
        RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    }
}
