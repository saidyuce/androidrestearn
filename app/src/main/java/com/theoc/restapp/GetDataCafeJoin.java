package com.theoc.restapp.dataorganization.screendata;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.theoc.restapp.CafeJoinActivity;
import com.theoc.restapp.MyPointsDetailActivity;
import com.theoc.restapp.R;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.barcode.ReadBarcode;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.extendclass.DataProcess.GetDataFromLocal;
import com.theoc.restapp.helper.QRReadImage;

import org.json.JSONException;
import org.json.JSONObject;

public class GetDataCafeJoin extends GetDataFromLocal {
    JSONObject main_data;
    int caf_id;


    public GetDataCafeJoin(Activity activity,int cafe_id) {
        super(activity.getBaseContext(), activity);
        this.caf_id = cafe_id;
    }

    public void start_paralel() {
        super.sync_finish(9);

    }
    @Override
    protected void get_data_from_local(int i) {

        super.result_json = new JSONObject();
        super.select_and_response_json("cafe",
                        "select icon," +
                        "large_image,"+
                        "cafe_picture_url," +
                        "name," +
                        "point,"+
                        "info" +
                        " from cafe left join points on cafe.id=points.cafe_id where cafe.id="+caf_id+" and ( user_id="+ GeneralSync.id+" or user_id is null);");
    }

    @Override
    protected void data_received(int i) {

        if(i==9)
            try {
                main_data = super.result_json;
                set_gui();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public String get_cafe_info() {
        try {

            return main_data.getJSONArray("cafe").getJSONObject(0).getString("info");



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_cafe_icon() {
        try {

            return ServConnection.get_file_url()+main_data.getJSONArray("cafe").getJSONObject(0).getString("icon");



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_cafe_large_image() {
        try {

            return ServConnection.get_file_url()+main_data.getJSONArray("cafe").getJSONObject(0).getString("large_image");



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_cafe_pictur() {
        try {

            return ServConnection.get_file_url()+main_data.getJSONArray("cafe").getJSONObject(0).getString("cafe_picture_url");



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_cafe_name() {
        try {

            return main_data.getJSONArray("cafe").getJSONObject(0).getString("name");



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_cafe_point() {
        try {
            return main_data.getJSONArray("cafe").getJSONObject(0).getInt("point")+"";
        } catch (JSONException e) {
            return "0";
        }
    }

    private void set_gui(){
        ((CafeJoinActivity)a).mainTextView.setText(get_cafe_info());
        ((CafeJoinActivity)a).toolbar.setTitle(get_cafe_name());
        QRReadImage dialog = new QRReadImage(a);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if (ReadBarcode.durum.equals("yok_farklı_yok")||ReadBarcode.durum.equals("yok_farklı_var")||ReadBarcode.durum.equals("yok_farklı_yok_masa")||ReadBarcode.durum.equals("yok_farklı_var_masa"))
        dialog.show();
        Glide.with(a)
                .load(get_cafe_large_image())
                .error(R.drawable.mypointsnargile)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .dontAnimate()
                .into( ((CafeJoinActivity)a).ımageView);
    }

    public    View.OnClickListener onClickListener_textview=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(a, MyPointsDetailActivity.class);
            intent.putExtra("cafe_id", caf_id+"");
            intent.putExtra("cafe_name",get_cafe_name());
            intent.putExtra("point", get_cafe_point());
            a.startActivity(intent);
        }
    };




}


