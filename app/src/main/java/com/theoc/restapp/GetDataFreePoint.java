package com.theoc.restapp.dataorganization.screendata;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.theoc.restapp.MyPointsActivity;
import com.theoc.restapp.adapters.MyFreePointsAdapter;
import com.theoc.restapp.adapters.MyPointsAdapter;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.extendclass.DataProcess.GetDataFromLocal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saidyuce on 3.9.2017.
 */
public class GetDataFreePoint extends GetDataFromLocal implements View.OnClickListener {

    public GetDataFreePoint(Activity activity) {
        super(activity.getBaseContext(), activity);
    }

    private JSONObject main_data;


    public void start_paralel() {

        if (GeneralSync.id != -1)
            super.sync_finish(22);
        else onnot_login();

    }

    @Override
    protected void get_data_from_local(int i) {

        super.result_json = new JSONObject();
        super.select_and_response_json("free_point",
                "select free_point.id," +
                        "kul_id," +
                        "free_point.cafe_id," +
                        "point," +
                        "cafe.icon," +
                        "cafe.name," +
                        "cafe.city," +
                        "cafecoin.coin" +
                        " from (free_point join cafe on free_point.cafe_id=cafe.id) join cafecoin on cafe.id=cafecoin.cafe_id where kul_id=" + GeneralSync.id + ";");
    }


    public void onnot_login() {


    }

    @Override
    protected void data_received(int i) { //bu metodu biraz kurcaladım

        if (i == 22)
            try {
                main_data = super.result_json;
                if (main_data.getJSONArray("free_point").length() > 0)
                    set_gui();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void set_gui() {
        // set adapter
        MyFreePointsAdapter pointsadapter = new MyFreePointsAdapter(a, this);
        ((MyPointsActivity) a).listView.setAdapter(pointsadapter);
    }

    public String get_id(int json_indis) {
        try {
            return main_data.getJSONArray("free_point").getJSONObject(json_indis).getInt("id") + " ";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int get_size() {
        try {
            return main_data.getJSONArray("free_point").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String get_user_id(int json_indis) {

        try {
            return main_data.getJSONArray("free_point").getJSONObject(json_indis).getInt("kul_id") + "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int get_cafecoin(int json_indis) {
        try {
            return main_data.getJSONArray("free_point").getJSONObject(json_indis).getInt("coin");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String get_cafeid(int json_indis) {

        try {
            return main_data.getJSONArray("free_point").getJSONObject(json_indis).getInt("cafe_id") + "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_point(int json_indis) {

        try {
            return main_data.getJSONArray("free_point").getJSONObject(json_indis).getInt("point") + "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_cafename(int json_indis) {

        try {
            return main_data.getJSONArray("free_point").getJSONObject(json_indis).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_city(int json_indis) {

        try {
            return main_data.getJSONArray("free_point").getJSONObject(json_indis).getString("city");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_cafe_picture_url(int json_indis) {

        try {
            return ServConnection.get_file_url() + main_data.getJSONArray("free_point").getJSONObject(json_indis).getString("icon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {

    }

}