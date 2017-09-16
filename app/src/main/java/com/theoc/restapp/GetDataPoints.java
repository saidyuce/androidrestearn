package com.theoc.restapp.dataorganization.screendata;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.theoc.restapp.MyPointsActivity;
import com.theoc.restapp.MyPointsDetailActivity;
import com.theoc.restapp.adapters.MyPointsAdapter;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.extendclass.DataProcess.GetDataFromLocal;

import org.json.JSONException;
import org.json.JSONObject;

public class GetDataPoints extends GetDataFromLocal implements View.OnClickListener  {

    public GetDataPoints( Activity activity) {
        super(activity.getBaseContext(), activity);
    }
    private JSONObject main_data;


    public void start_paralel() {

        if (GeneralSync.id!=-1)
            super.sync_finish(3);
        else onnot_login();

    }
    @Override
    protected void get_data_from_local(int i) {

        super.result_json = new JSONObject();
        super.select_and_response_json("points",
                "select points.id," +
                        "user_id," +
                        "cafe_id," +
                        "point," +
                        "cafe.icon," +
                        "cafe.name," +
                        "cafe.city" +
                        " from points join cafe on points.cafe_id=cafe.id where user_id="+GeneralSync.id+";");
    }


    public void onnot_login(){


    }

    @Override
    protected void data_received(int i) { //bu metodu biraz kurcaladım

        if(i==3)
            try {
                main_data = super.result_json;
                if (main_data.getJSONArray("points").length() > 0)
                    set_gui();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void set_gui(){
        // set adapter
        MyPointsAdapter pointsadapter = new MyPointsAdapter(a, this);
        ((MyPointsActivity) a).listView.setAdapter(pointsadapter);
    }

    public String get_id(int json_indis) {
        try {
            return main_data.getJSONArray("points").getJSONObject(json_indis).getInt("id")+" ";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int get_size() {
        try {
            return main_data.getJSONArray("points").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String get_user_id(int json_indis) {

        try {
            return main_data.getJSONArray("points").getJSONObject(json_indis).getInt("user_id")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_cafeid(int json_indis) {

        try {
            return main_data.getJSONArray("points").getJSONObject(json_indis).getInt("cafe_id")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_point(int json_indis) {

        try {
            return main_data.getJSONArray("points").getJSONObject(json_indis).getInt("point")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_cafename(int json_indis) {

        try {
            return main_data.getJSONArray("points").getJSONObject(json_indis).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_city(int json_indis) {

        try {
            return main_data.getJSONArray("points").getJSONObject(json_indis).getString("city");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_cafe_picture_url (int json_indis) {

        try {
            return ServConnection.get_file_url()+ main_data.getJSONArray("points").getJSONObject(json_indis).getString("icon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}
