package com.theoc.restapp.dataorganization.screendata;

import android.app.Activity;

import com.theoc.restapp.MyPointsDetailActivity;
import com.theoc.restapp.extendclass.DataProcess.GetDataFromLocal;

import org.json.JSONException;
import org.json.JSONObject;

public class GetDataPointsDetail extends GetDataFromLocal {

    String cafe_id;

    public GetDataPointsDetail( Activity activity, String cafe_id) {
        super(activity.getBaseContext(), activity);
        this.cafe_id = cafe_id;
    }
    JSONObject main_data;

    public void start_paralel() {

        super.sync_finish(8);

    }
    @Override  // buraya tablo verileri girilecek
    protected void get_data_from_local(int i) {

        super.result_json = new JSONObject();
        super.select_and_response_json("prize",
                "select name from prize where cafe_id = " + cafe_id + ";");
    }

    @Override
    protected void data_received(int i) {

        if(i==8)
            try {
                main_data = super.result_json;
                if (main_data.getJSONArray("prize").length() > 0)
                    set_prizes();
            } catch (Exception e) {
                e.printStackTrace();
            }



    }

    String[] prizes;

    private void set_prizes() {
        ((MyPointsDetailActivity) a).prizeTextView.setText(get_prizes(0));
    }

    public String get_prizes(int pos) {

        try {
            return main_data.getJSONArray("prize").getJSONObject(pos).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int get_size() {
        try {
            return main_data.getJSONArray("prize").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
