package com.theoc.restapp.dataorganization.screendata;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.theoc.restapp.AnketActivity;
import com.theoc.restapp.R;
import com.theoc.restapp.adapters.AnketPagerAdapter;
import com.theoc.restapp.dataorganization.cafedata.SendCafeSurwey;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.extendclass.DataProcess.GetDataFromLocal;

import org.json.JSONException;
import org.json.JSONObject;

public class GetDataSurwey extends GetDataFromLocal implements View.OnClickListener {

    public GetDataSurwey(Activity activity) {
        super(activity.getBaseContext(), activity);
    }


    public   int surwey_category=0;
    JSONObject main_data;
    JSONObject main_data2;
    int cafe_id;
    SendCafeSurwey sendCafeSurwey;
    //SendWaiterSurwey sendWaiterSurwey;
    public void start_paralel(int cafe_id) {

        this.cafe_id = cafe_id;
        sendCafeSurwey=new SendCafeSurwey();
        //sendWaiterSurwey=new SendWaiterSurwey();
        super.sync_finish(12);


    }
    @Override
    protected void get_data_from_local(int i) {




        if (i==12) {

            super.result_json = new JSONObject();
            select_and_response_json("surwey", "SELECT  " +
                    "id ," +
                    "question ,"+
                    "a_ ," +
                    "b_ ,"+
                    "c_ ,"+
                    "d_ ,"+
                    "cafe_id "+

                    " FROM " + "surwey where cafe_id="+cafe_id+"");

        }else if(i==13){

            super.result_json = new JSONObject();
            select_and_response_json("waiter", "SELECT  " +
                    "id ," +
                    "name_surname ,"+
                    "picture," +
                    "cafe_id "+

                    " FROM " + "waiter where cafe_id="+cafe_id+"");



        }

    }
    @Override
    protected void data_received(int i) {

        if(i==12)
            try {
                main_data = super.result_json;
                if (main_data.getJSONArray("surwey").length() > 0)
                {   surwey_category+=1;



                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                super.sync_finish(13);
            }
        else  if(i==13){

            try {
                main_data2 = super.result_json;
                if (main_data2.getJSONArray("waiter").length() > 0)
                {   surwey_category+=1;



                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (surwey_category>0) set_cafe_surwey();
            }

            //burda garson çekcem
        }

    }


    private void set_cafe_surwey(){

        AnketPagerAdapter pagerAdapter=new AnketPagerAdapter(((AnketActivity)a).getSupportFragmentManager(),this);

        ( (AnketActivity)a).pager.setAdapter(pagerAdapter);
        ((AnketActivity)a).tabs.setDistributeEvenly(true);
        ((AnketActivity)a).tabs.setViewPager(((AnketActivity)a).pager);
        ((AnketActivity)a).tabs.setBackgroundColor(Color.parseColor("#FFFFFFFF"));

    }


    public String get_id(int json_indis) {

        try {
            return main_data.getJSONArray("surwey").getJSONObject(json_indis).getInt("id") + "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_cafeid_surwey(int json_indis) {

        try {
            return main_data.getJSONArray("surwey").getJSONObject(json_indis).getInt("cafe_id") + "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_waitername(int json_indis) {

        try {
            return main_data2.getJSONArray("waiter").getJSONObject(json_indis).getString("name_surname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  int get_cafe_waiter_count(){
        try {
            return main_data2.getJSONArray("waiter").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public  String get_cafe_waiter_picture(int json_indis){
        try {
            return  ServConnection.get_file_url()+main_data2.getJSONArray("waiter").getJSONObject(json_indis).getString("picture");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  String get_surwey_a_pic(int json_indis){
        try {
            return  ServConnection.get_file_url()+main_data.getJSONArray("surwey").getJSONObject(json_indis).getString("a_pic");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  String get_surwey_b_pic(int json_indis){
        try {
            return  ServConnection.get_file_url()+main_data.getJSONArray("surwey").getJSONObject(json_indis).getString("b_pic");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  String get_surwey_c_pic(int json_indis){
        try {
            return  ServConnection.get_file_url()+main_data.getJSONArray("surwey").getJSONObject(json_indis).getString("c_pic");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  String get_surwey_d_pic(int json_indis){
        try {
            return  ServConnection.get_file_url()+main_data.getJSONArray("surwey").getJSONObject(json_indis).getString("d_pic");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_question(int json_indis) {

        try {
            return main_data.getJSONArray("surwey").getJSONObject(json_indis).getString("question");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_a(int json_indis) {

        try {
            return main_data.getJSONArray("surwey").getJSONObject(json_indis).getString("a_");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_b(int json_indis) {

        try {
            return main_data.getJSONArray("surwey").getJSONObject(json_indis).getString("b_");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_c(int json_indis) {

        try {
            return main_data.getJSONArray("surwey").getJSONObject(json_indis).getString("c_");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_d(int json_indis) {

        try {
            return main_data.getJSONArray("surwey").getJSONObject(json_indis).getString("d_");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  int get_cafe_Surwey_count(){
        try {
            return main_data.getJSONArray("surwey").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {





    }







}