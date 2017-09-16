package com.theoc.restapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.theoc.restapp.R;
import com.theoc.restapp.helper.MenuInterface;
import com.theoc.restapp.helper.SepetInterface;

import java.util.Map;

public class SepetAdapter extends BaseAdapter {

    Context context;
    Map<String, Integer> sepetDict;
    Map<String, String> sepetDictPrice;
    Map<String, String> sepetDictPoint;
    Map<String, String> sepetDictCategory;
    Object[] sepetDictCopy;
    MenuInterface menuInterface;
    SepetInterface sepetInterface;

    public SepetAdapter(Context context, Map<String, Integer> sepetDict, Map<String, String> sepetDictPrice, MenuInterface menuInterface, Map<String, String> sepetDictPoint, Map<String, String> sepetDictCategory, SepetInterface sepetInterface) {
        this.context = context;
        this.sepetDict = sepetDict;
        this.sepetDictPrice = sepetDictPrice;
        this.sepetDictPoint = sepetDictPoint;
        this.sepetDictCategory = sepetDictCategory;
        this.menuInterface = menuInterface;
        this.sepetInterface = sepetInterface;
        sepetDictCopy = sepetDictPrice.keySet().toArray();
    }
    @Override
    public int getCount() {
        return sepetDictCopy.length;
    }

    @Override
    public Object getItem(int position) {
        return sepetDictCopy[position];
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
            row = inflater.inflate(R.layout.sepet_single_item, parent, false);
            holder = new ViewHolder();
            holder.foodTextView = (TextView) row.findViewById(R.id.foodTextView);
            holder.priceTextView = (TextView) row.findViewById(R.id.priceTextView);
            holder.amountTextView = (TextView) row.findViewById(R.id.amountTextView);
            holder.addImageButton = (ImageButton) row.findViewById(R.id.addImageButton);
            holder.removeImageButton = (ImageButton) row.findViewById(R.id.removeImageButton);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.foodTextView.setText(sepetDictCopy[position].toString());
        holder.priceTextView.setText(sepetDictPrice.get(sepetDictCopy[position]));
        holder.amountTextView.setText(String.valueOf(sepetDict.get(sepetDictCopy[position])));
        holder.addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sepetInterface.setValues(Double.parseDouble(holder.priceTextView.getText().toString()) / Integer.parseInt(holder.amountTextView.getText().toString()),
                        Double.parseDouble(sepetDictPoint.get(holder.foodTextView.getText().toString())) / Integer.parseInt(holder.amountTextView.getText().toString()),
                        true);
                menuInterface.setValues(holder.foodTextView.getText().toString(),
                        Integer.parseInt(holder.amountTextView.getText().toString()) + 1,
                        String.valueOf(Double.parseDouble(holder.priceTextView.getText().toString()) + Double.parseDouble(holder.priceTextView.getText().toString()) / Double.parseDouble(holder.amountTextView.getText().toString())),
                        String.valueOf(Double.parseDouble(sepetDictPoint.get(holder.foodTextView.getText().toString())) + Double.parseDouble(sepetDictPoint.get(holder.foodTextView.getText().toString())) / Double.parseDouble(holder.amountTextView.getText().toString())),
                        1,
                        sepetDictCategory.get(holder.foodTextView.getText().toString()));
                holder.priceTextView.setText(String.valueOf(Double.parseDouble(holder.priceTextView.getText().toString()) + Double.parseDouble(holder.priceTextView.getText().toString()) / Double.parseDouble(holder.amountTextView.getText().toString())));
                holder.amountTextView.setText(String.valueOf(Integer.parseInt(holder.amountTextView.getText().toString()) + 1));
            }
        });
        holder.removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sepetInterface.setValues(Double.parseDouble(holder.priceTextView.getText().toString()) / Integer.parseInt(holder.amountTextView.getText().toString()),
                        Double.parseDouble(sepetDictPoint.get(holder.foodTextView.getText().toString())) / Integer.parseInt(holder.amountTextView.getText().toString()),
                        false);
                menuInterface.setValues(holder.foodTextView.getText().toString(),
                        Integer.parseInt(holder.amountTextView.getText().toString()) - 1,
                        String.valueOf(Double.parseDouble(holder.priceTextView.getText().toString()) - Double.parseDouble(holder.priceTextView.getText().toString()) / Double.parseDouble(holder.amountTextView.getText().toString())),
                        String.valueOf(Double.parseDouble(sepetDictPoint.get(holder.foodTextView.getText().toString())) - Double.parseDouble(sepetDictPoint.get(holder.foodTextView.getText().toString())) / Double.parseDouble(holder.amountTextView.getText().toString())),
                        1,
                        sepetDictCategory.get(holder.foodTextView.getText().toString()));
                holder.priceTextView.setText(String.valueOf(Double.parseDouble(holder.priceTextView.getText().toString()) - Double.parseDouble(holder.priceTextView.getText().toString()) / Double.parseDouble(holder.amountTextView.getText().toString())));
                holder.amountTextView.setText(String.valueOf(Integer.parseInt(holder.amountTextView.getText().toString()) - 1));
            }
        });
        return row;
    }

    public static class ViewHolder {
        TextView foodTextView, priceTextView, amountTextView;
        ImageButton addImageButton, removeImageButton;
    }
}
