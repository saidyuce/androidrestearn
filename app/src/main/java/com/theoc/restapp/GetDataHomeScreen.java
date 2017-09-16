package com.theoc.restapp.dataorganization.screendata;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.theoc.restapp.CafeActivity;
import com.theoc.restapp.MapActivity;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.extendclass.DataProcess.GetDataFromLocal;
import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.R;
import com.theoc.restapp.adapters.HomeGridAdapter;
import com.theoc.restapp.adapters.ImageAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetDataHomeScreen extends GetDataFromLocal {

    JSONObject main_data, ana_data, filter_data;
    String[] listItems = {"Tümünü Göster",
            "Restearn",
            "Happy Hour",
            "Ürün"};


    public GetDataHomeScreen(Activity activity) {
        super(activity.getBaseContext(), activity);
    }

    public void start_paralel() {


        super.sync_finish(0);


    }

    public void start_normal() {


        get_data_from_local(0);
        data_received(0);


    }

    public void stop_paralel_task() {

        super.stop_get_data_from_local();
    }

    String q_loc="";
    @Override
    protected void get_data_from_local(int i) {
        if (GeneralSync.location_city == null)
            q_loc="";
        else{
           // q_loc="and cafe.city= '"+GeneralSync.location_city.toLowerCase()+"'";
        }

        super.result_json = new JSONObject();

        if (i==0) {

            super.select_and_response_json("campaing", "SELECT  " +
                    "campaing.id AS cid," +
                    "cafe.id AS cfid," +
                    "((" + GeneralSync.x_loc + " - cafe.x_)*(" + GeneralSync.x_loc + "-cafe.x_)+(" + GeneralSync.y_loc + " - cafe.y_)*(" + GeneralSync.y_loc + " - cafe.y_)) as distance," +
                    "campaing.picture_url," +
                    "cafe.preminium_type," +
                    "campaing.detail," +
                    "cafe.name," +
                    "cafe.large_image," +
                    "cafe.icon," +
                    "cafe.detail as cafe_detail," +
                    "prize.name as prize_name," +
                    "cafe.x_,"+
                    "cafe.y_,"+
                    "campaing.category as campaing_category,"+
                    "cafe.category as cafe_category" +
                    " FROM (campaing JOIN cafe on campaing.cafe_id=cafe.id) JOIN prize on campaing.cafe_id=prize.cafe_id  "+q_loc + " ORDER BY distance ASC");
        }else if(i==1){
            //for premium
            super.select_and_response_json("campaing", "SELECT  " +
                    "campaing.id AS cid," +
                    "cafe.id AS cfid," +
                    "((" + GeneralSync.x_loc + " - cafe.x_)*(" + GeneralSync.x_loc + "-cafe.x_)+(" + GeneralSync.y_loc + " - cafe.y_)*(" + GeneralSync.y_loc + " - cafe.y_)) as distance," +
                    "campaing.picture_url," +
                    "cafe.preminium_type," +
                    "campaing.detail," +
                    "cafe.name," +
                    "cafe.large_image," +
                    "cafe.icon," +
                    "cafe.detail as cafe_detail," +
                    "prize.name as prize_name," +
                    "cafe.x_,"+
                    "cafe.y_,"+
                    "campaing.category as campaing_category,"+
                    "cafe.category as cafe_category " +
                    " FROM (campaing JOIN cafe on campaing.cafe_id=cafe.id) JOIN prize on campaing.cafe_id=prize.cafe_id  "+q_loc + " ORDER BY distance ASC");
        }

    }

    @Override
    protected void data_received(int i) { //bu metodu biraz kurcaladım

        if(i==0)
            try {
                main_data = super.result_json;
                Log.v("MAIN DATA JSON", main_data.toString());
                if (main_data.getJSONArray("campaing").length() > 0)
                    set_main_gui();
                super.sync_finish(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        if(i==1){
            try {
                main_data = super.result_json;
                if (main_data.getJSONArray("campaing").length() > 0)
                    Log.v("CAMPAING JSON", main_data.getJSONArray("campaing").toString());
                    set_top_gui();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    private void set_main_gui() {


        adj_items();
    }

    private void set_top_gui() {
        ana_data = main_data;
        ImageAdapter adapter = new ImageAdapter(a.getBaseContext(),this);
        ViewGroup header = (ViewGroup) a.getLayoutInflater().inflate(R.layout.home_single_item_premium, ((HomeActivity) a).listView, false);
        ViewPager viewPager = (ViewPager) header.findViewById(R.id.view_pager);
        CirclePageIndicator indicator = (CirclePageIndicator) header.findViewById(R.id.titles);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        ((HomeActivity) a).listView.addHeaderView(header);
        HomeGridAdapter adapter_campaing = new HomeGridAdapter(a, this);
        ((HomeActivity) a).listView.setAdapter(adapter_campaing);

        //ViewPager   viewPager = (ViewPager)a. findViewById(R.id.view_pager);
        //CirclePageIndicator titleIndicator = (CirclePageIndicator)a.findViewById(R.id.titles);
        //titleIndicator.setViewPager(viewPager);
    }

    public String get_cafe_distance(int json_indis) {

        try {
            Double distance = main_data.getJSONArray("campaing").getJSONObject(json_indis).getDouble("distance");
            return distance.intValue() + " km";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_cafe_detail(int json_indis) {
        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("cafe_detail");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int get_premium_size() {
        try {
            int counter = 0;
            for (int i = 0; i < ana_data.getJSONArray("campaing").length(); i++) {
                if (ana_data.getJSONArray("campaing").getJSONObject(i).getInt("preminium_type") == 1) {
                    ++counter;
                }
            }
            return counter;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String get_premium_image(int json_indis) {
        try {
            return ana_data.getJSONArray("campaing").getJSONObject(json_indis).getString("large_image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_cafe_icon(int json_indis) {
        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("icon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_cafe_large_image(int json_indis) {
        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("large_image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double get_cafe_x(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getDouble("x_");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public double get_cafe_y(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getDouble("y_");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String get_campaing_picture_url(int json_indis) {

        try {
            return ServConnection.get_file_url()+main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("picture_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_campaing_category(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("campaing_category");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_cafe_category(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("cafe_category");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_campaing_detail(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("detail");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_cafe_name(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_prize(int json_indis) {
        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getString("prize_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_cafe_id(int json_indis) {

        try {
            return main_data.getJSONArray("campaing").getJSONObject(json_indis).getInt("cfid") + "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int get_size() {
        try {
            return main_data.getJSONArray("campaing").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    Map<String,String> search_items;
    JSONArray jsonarray;

    private void adj_items(){
        search_items=new HashMap<>();
        try {
            jsonarray= main_data.getJSONArray("campaing");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i=0;i<jsonarray.length();++i){
            search_items.put(get_cafe_name(i),get_cafe_id(i));

        }
        set_suggestion_adapter_array();
    }

    CursorAdapter cursorAdapter;
    MatrixCursor matrixCursor;

    public void set_suggestion_adapter_array(){
        String[] from = new String[] {"cafe_name"};
        int[] to = new int[] {R.id.suggestionTextView};
        cursorAdapter = new SimpleCursorAdapter(a,
                R.layout.suggestions_single_item,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ((HomeActivity) a).actionView2.setSuggestionsAdapter(cursorAdapter);

    }

    public void search(String key){

        int id = 0;
        double cafe_x = 0.0;
        double cafe_y = 0.0;
        String name = "";
        String cafe_detail = "";
        String large_image = "";
        try {
            JSONArray campaingArray = ana_data.getJSONArray("campaing");
            for (int i = 0; i < campaingArray.length(); i++) {
                if (campaingArray.getJSONObject(i).getString("name").equalsIgnoreCase(key)) {
                    cafe_x = campaingArray.getJSONObject(i).getDouble("x_");
                    cafe_y = campaingArray.getJSONObject(i).getDouble("y_");
                    name = campaingArray.getJSONObject(i).getString("name");
                    id = campaingArray.getJSONObject(i).getInt("cfid");
                    cafe_detail = campaingArray.getJSONObject(i).getString("cafe_detail");
                    large_image = campaingArray.getJSONObject(i).getString("large_image");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(a, CafeActivity.class);
        intent.putExtra("cafe_x", cafe_x);
        intent.putExtra("cafe_y", cafe_y);
        intent.putExtra("name", name);
        intent.putExtra("cafe_id", id + "");
        intent.putExtra("cafe_detail", cafe_detail);
        intent.putExtra("large_image", large_image);
        a.startActivity(intent);
    }


    public void populate(String text){

        matrixCursor = new MatrixCursor(new String[]{ BaseColumns._ID, "cafe_name" });
        for (int i=0;i<jsonarray.length();++i){
            search_items.put(get_cafe_name(i),i+"");
            if (get_cafe_name(i).toLowerCase().startsWith(text.toLowerCase()))
                matrixCursor.addRow(new Object[] {i, get_cafe_name(i)});
        }
        cursorAdapter.changeCursor(matrixCursor);
        cursorAdapter.notifyDataSetChanged();

    }

    public void navigate(int pos){
        String suggestion = getSuggestion(pos);
        search(suggestion);
    }


    public String getSuggestion(int pos){
        Cursor cursor = (Cursor) ((HomeActivity) a).actionView2.getSuggestionsAdapter().getItem(
                pos);
        String suggest1 = cursor.getString(cursor
                .getColumnIndex("cafe_name"));
        return suggest1;
    }

    // *****************************************************************************************

    AlertDialog.Builder builder;

    public void createFilterDialog(){
        filterAdapter adapter = new filterAdapter(a, R.layout.home_filter_single_item, listItems);
        builder = new AlertDialog.Builder(a);
        builder .setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                filter(which);
            }
        })
                .create()
                .show();

    }

    public void filter(int pos){
        try {
            if (pos == 0) {
                main_data = new JSONObject(ana_data.toString());
                HomeGridAdapter adapter_campaing = new HomeGridAdapter(a, this);
                ((HomeActivity) a).listView.setAdapter(adapter_campaing);
            } else {
                String choice = listItems[pos];
                if (choice.equalsIgnoreCase("Restearn")) {
                    choice = "Restarn";
                }
                int size = ana_data.getJSONArray("campaing").length();
                JSONArray jsonList = new JSONArray();
                boolean flag;
                for (int i = 0; i < size; i++){
                    flag = choice.equalsIgnoreCase(ana_data.getJSONArray("campaing").getJSONObject(i).getString("campaing_category"));
                    if (flag) {
                        jsonList.put(ana_data.getJSONArray("campaing").getJSONObject(i));
                    }
                }

                filter_data = new JSONObject();
                filter_data.put("campaing", jsonList);
                main_data = new JSONObject(filter_data.toString());
                HomeGridAdapter adapter_campaing = new HomeGridAdapter(a, this);
                ((HomeActivity) a).listView.setAdapter(adapter_campaing);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }


    private class filterAdapter extends ArrayAdapter<Object> {

        String[] listItems;
        Context context;
        int layoutID;

        public filterAdapter(Context context, int res, String[] listItems) {
            super(context, res);
            this.context = context;
            this.layoutID = res;
            this.listItems = listItems;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = convertView;
            row = inflater.inflate(layoutID, parent, false);
            TextView filterTextView = (TextView) row.findViewById(R.id.filterTextView);

            filterTextView.setText(listItems[position]);
            return row;
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
        public int getCount() {
            return listItems.length;
        }
    }


}