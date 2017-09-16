package com.theoc.restapp.dataorganization.barcode;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.Screens;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.extendclass.DataProcess.GetDataFromLocal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetAndSaveServerCafeData extends GetDataFromLocal {

    protected String barcode="asdasd";
    protected String x_loc=GeneralSync.x_loc+"";
    protected String y_loc=GeneralSync.y_loc+"";
    protected String temp_password=GeneralSync.temp_key;







    Context context_;
    Map<String, String> control_tables;
    public static int cafe_id=-3;







    protected GetAndSaveServerCafeData( Activity activity) {
        super(activity.getBaseContext(), activity);
        this.context_ = activity.getBaseContext();
    }

    protected void start(){
        super.get_data_async_single(true, context_, "genel_senkronizasyon2.php", "synctable2", "(id INT PRIMARY KEY NOT NULL,id2 INT,name TEXT,type TEXT,id3 INT);", get_extra_data(), 0);
    }

    public Map<String, String> get_extra_data() {
        Map<String, String> ext = new HashMap<>();
        ext.put("id", GeneralSync.id+ "");
        ext.put("loc_x", x_loc);
        ext.put("loc_y", y_loc);
        ext.put("temp_pass", temp_password);

        try {
            JSONObject barjson=new JSONObject(barcode);
            Log.v("BARCODEJSON=", barjson.toString());
            ext.put("cafe_id",barjson.getString("cafe"));
            ext.put("masa_no", barjson.getString("masa"));

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context_,"Yanlış barcode okuttunuz!!2",Toast.LENGTH_LONG).show();
            destroy_task();
            a.finish();
        }



        return ext;
    }
    @Override
    public void sync_finish(int i) {

        super.sync_finish(i);

    }
    @Override
    protected void destroy_task(){
        super.destroy_task();
    }
    @Override
    public void cancelsync(){
        //on the interrupted sync
    }
    @Override
    protected void onSYNC(){
        //while syncing

    }
    @Override
    protected void stop_get_data_from_local(){
        super.stop_get_data_from_local();


    }


    public static void set_screen(Screens s){


    }
    public Activity get_activty(){

        return null;

    }

    public void setControl_tables(){
        control_tables=new HashMap<String, String>();
        control_tables.put("menu","(id INTEGER PRIMARY KEY  ,price DOUBLE,name TEXT,picture TEXT,category TEXT,cafe_id INT,selling_number INT,stoc_state INT,info TEXT);");
        control_tables.put("surwey","(id INTEGER PRIMARY KEY,question TEXT,a_ TEXT,b_ TEXT,c_ TEXT,d_ TEXT,cafe_id INT,a_pic TEXT,b_pic TEXT,c_pic TEXT,d_pic TEXT);");
        control_tables.put("waiter","(id INTEGER PRIMARY KEY,name_surname TEXT,picture TEXT,cafe_id INT);");

    }

    @Override
    protected void post_sync(){
        setControl_tables();
        super.control_tables(control_tables);

    }
    //on the finish thread2

    @Override
    protected void get_data_from_local(int i){



    }
    @Override
    public void sync_finish_nosyenkron(){
        //succes
        //  ((CafeJoinActivity)a).start();
        if (ReadBarcode.durum.equals("wrong_barcode")||ReadBarcode.durum.equals("fail")){
            read_error();
            return;
        }
try {
    cafe_id=get_extra_data("cafe_id");
    read_succesfull();
}catch (Exception e){
    read_error();
}




    }
    @Override
    public void server_error(ServConnection.error e){

        // ((CafeJoinActivity)a).start();
    }
    @Override
    protected void internet_is_not_activated(){

        //   ((CafeJoinActivity)a).start();
    }

    protected void read_succesfull(){



    }
    protected void read_error(){



    }
}
