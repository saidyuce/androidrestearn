package com.theoc.restapp.dataorganization.screendata;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.v4.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.extendclass.DataProcess.GetDataFromLocal;
import com.theoc.restapp.MapActivity;
import com.theoc.restapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class GetDataMap extends GetDataFromLocal {

    public GetDataMap( Activity activity) {
        super(activity.getBaseContext(), activity);
    }
    private JSONObject main_data;
    private String[] listItems = {"Tümünü Göster",
            "Kafe",
            "Fast Food",
            "Restaurant",
            "Pub & Bar",
            "Gece Kulübü"};

    public void start_paralel() {

        indis=0;
        super.sync_finish(4);



    }
    @Override
    protected void get_data_from_local(int i) {

        super.result_json = new JSONObject();
        super.select_and_response_json("cafe",
                "select id," +
                        "category,"+
                        "map_icon," +
                        "name," +
                        "x_," +
                        "y_" +
                        " from cafe;");
    }
    @Override
    protected void data_received(int i) {

        if(i==4)
            try {
                if (super.result_json!=null){
                    main_data =(JSONObject) cloneObject(super.result_json);
                }
                if (main_data != null && main_data.getJSONArray("cafe").length() > 0) {
                    search_locations = new HashMap<>();
                    suggestion_name = new String[main_data.getJSONArray("cafe").length()];
                    set_gui();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private int indis;
    private URL myurl;
    private Bitmap b;
    private LatLng marker1;
    double latitude, longitude;


    private static Object cloneObject(Object obj){
        try{
            Object clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(obj));
            }
            return clone;
        }catch(Exception e){
            return null;
        }
    }

    private void set_gui(){

        // set adapter
        try {
            if (indis>=main_data.getJSONArray("cafe").length()){
                latitude = GeneralSync.x_loc;
                longitude = GeneralSync.y_loc;
                LatLng latLng = new LatLng(GeneralSync.x_loc, GeneralSync.y_loc);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                ((MapActivity) a).mMap.addMarker(new MarkerOptions().position(latLng));
                //((MapActivity) a).mMap.animateCamera(cameraUpdate);
                set_suggestion_adapter_array();
            }
            else {
                add_marker();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private GetIcon getIcon;
    private String urlString;
    private void add_marker(){
        getIcon=new GetIcon();
        getIcon.execute(get_icon(indis));

    }
    private class GetIcon extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                myurl=new URL("http", ServConnection.ip, ServConnection.port_no,ServConnection.subdomain_file+ params[0]);
                urlString = String.valueOf(myurl);
                b = Glide.with(a)
                        .load(urlString)
                        .asBitmap()
                        .into(300, 300)
                        .get();
                //b= BitmapFactory.decodeStream(myurl.openConnection().getInputStream());
                //b = Bitmap.createScaledBitmap(b, 100, 100, true);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Success";
        }

        @Override
        protected void onPostExecute(String a) {

            put_icon();
        }
    }
    private void put_icon(){

        marker1 = new LatLng(get_x(indis), get_y(indis));
        suggestion_name[indis]=get_name(indis);
        search_locations.put(get_name(indis),new CafeInfo(get_x(indis),get_y(indis),get_category(indis)));
        ((MapActivity) a).markermain = ((MapActivity)a).mMap
                .addMarker(new MarkerOptions().position(marker1).title(get_name(indis)).snippet(indis + "").icon(BitmapDescriptorFactory.fromBitmap(b)));
        ++indis;
        set_gui();
    }




    public String get_id(int json_indis) {

        try {
            return main_data.getJSONArray("cafe").getJSONObject(json_indis).getInt("id") + "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int get_size() {


        try {
            return main_data.getJSONArray("cafe").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public String get_name(int json_indis) {

        try {
            return main_data.getJSONArray("cafe").getJSONObject(json_indis).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Double get_x(int json_indis) {

        try {
            return main_data.getJSONArray("cafe").getJSONObject(json_indis).getDouble("x_");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Double get_y(int json_indis) {

        try {
            return main_data.getJSONArray("cafe").getJSONObject(json_indis).getDouble("y_");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_icon(int json_indis) {

        try {
            return main_data.getJSONArray("cafe").getJSONObject(json_indis).getString("map_icon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_category(int json_indis) {

        try {
            return main_data.getJSONArray("cafe").getJSONObject(json_indis).getString("category");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }




    Map<String,CafeInfo> search_locations;
    String []suggestion_name;//bu dizi suggestion cursora konulcak
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
        ((MapActivity) a).actionView2.setSuggestionsAdapter(cursorAdapter);

    }

    public void search(String key){
        if(search_locations.keySet().contains(key.toLowerCase())&&(search_locations.get(key).category.equalsIgnoreCase(choice)||choice.equalsIgnoreCase(listItems[0]))) {
            LatLng latLng = new LatLng(search_locations.get(key).latitude, search_locations.get(key).longitude);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
            ((MapActivity) a).mMap.animateCamera(cameraUpdate);

        }
    }

    public void populate(String text){
        matrixCursor = new MatrixCursor(new String[]{ BaseColumns._ID, "cafe_name" });
        try {
            for (int i=0; i<main_data.getJSONArray("cafe").length(); i++) {
                if (suggestion_name[i].toLowerCase().startsWith(text.toLowerCase())&&(get_category(i).equalsIgnoreCase(choice)||choice.equalsIgnoreCase(listItems[0])))
                    matrixCursor.addRow(new Object[] {i, suggestion_name[i]});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cursorAdapter.changeCursor(matrixCursor);
        cursorAdapter.notifyDataSetChanged();

    }

    public void navigate(int pos){
        String suggestion = getSuggestion(pos);
        search(suggestion);
    }

    private String getSuggestion(int pos){
        Cursor cursor = (Cursor) ((MapActivity) a).actionView2.getSuggestionsAdapter().getItem(
                pos);
        return cursor.getString(cursor
                .getColumnIndex("cafe_name"));
    }

    public void createFilterDialog(){
        filterAdapter adapter = new filterAdapter(a, R.layout.filter_single_item, listItems);
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                filter(which);
            }
        })
                .create()
                .show();

    }
    private String choice=listItems[0];
    public void filter(int pos){
        ((MapActivity)a).mMap.clear();
        if (pos == 0) {
            indis = 0;
            choice = listItems[pos];
            add_marker();
        } else {
            choice = listItems[pos];
            try {
                for (int i = 0; i < main_data.getJSONArray("cafe").length(); i++) {
                    if (choice.equalsIgnoreCase(get_category(i))) {
                        add_marker_filter(i);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class filterAdapter extends ArrayAdapter<Object> {

        String[] listItems;
        Context context;
        int layoutID;

        filterAdapter(Context context, int res, String[] listItems) {
            super(context, res);
            this.context = context;
            this.layoutID = res;
            this.listItems = listItems;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = convertView;
            row = inflater.inflate(layoutID, parent, false);
            TextView filterTextView = (TextView) row.findViewById(R.id.filterTextView);
            ImageView filterImageView = (ImageView) row.findViewById(R.id.filterImageView);
            // buraya res/drawable'dan koyacağız kategori ikonlarını

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

    public void add_marker_filter(int pos){

        GetIconFilter getIconFilter = new GetIconFilter();
        getIconFilter.execute(pos);

    }
    private class GetIconFilter extends AsyncTask<Integer,Void,String> {
        int pos;
        @Override
        protected String doInBackground(Integer... params) {
            try {
                pos = params[0];
                myurl=new URL("http", ServConnection.ip,ServConnection.port_no,ServConnection.subdomain_file+ get_icon(pos));
                urlString = String.valueOf(myurl);
                b = Glide.with(a)
                        .load(urlString)
                        .asBitmap()
                        .into(100, 100)
                        .get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Success";
        }

        @Override
        protected void onPostExecute(String a) {
            put_icon_filter(pos);
        }
    }
    private void put_icon_filter(int pos){

        marker1 = new LatLng(get_x(pos), get_y(pos));
        ((MapActivity)a).markermain =((MapActivity)a).
                mMap.addMarker(new MarkerOptions().position(marker1).title(get_name(pos)).snippet(pos + "").icon(BitmapDescriptorFactory.fromBitmap(b)));
    }


    private class CafeInfo{

        public String category;
        double latitude;
        double longitude;
        CafeInfo(double x_, double y_, String category){
            latitude=x_;
            longitude=y_;
            this.category=category;
        }

    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.e("Success", "file written successfully");
            Log.v("asd", "HEBEBEBE");
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            Log.v("asd", "HEBEBEBE2");
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}

