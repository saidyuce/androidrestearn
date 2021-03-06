package com.theoc.restapp.dataorganization.barcode;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.theoc.restapp.MyPointsActivity;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.extendclass.DataConnections.LocalConnection;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by saidyuce on 3.9.2017.
 */
public class SiparisQRread extends ServConnection {


    Activity activity;

    public  SiparisQRread(Activity a){

   activity=a;


    }
    public void  read(String qrread){

        try {
            jj_temp=new JSONObject(qrread);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jj_temp2=new JSONObject();
        try {

            jj_temp2.put("qr_code",jj_temp.getString("qr_code"));
            jj_temp2.put("qr_sifre","said1234said");
            jj_temp2.put("qr_pass",jj_temp.getString("qr_pass"));
            jj_temp2.put("tip",jj_temp.getString("qr_tip"));
            jj_temp2.put("kul_id",user_id+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }
send(jj_temp2);
     /*    jj_temp.put("uid",user_id+"");;
                jj_temp.put("key",key_cafe_siparis+"");
                jj_temp.put("x_",x_loc+"");
                jj_temp.put("y_",y_loc+"");
                jj_temp.put("cid",cafe_id+"");*/





    }


    //success ve fail
    public JSONObject get_siparisqr_yanıt(){
        return response_json;
    }



    int user_id= GeneralSync.id;
    double x_loc=0;
    double y_loc=0;








    JSONObject jj_temp;

    JSONObject jj_temp2;


    void send(JSONObject j){
        Log.v("SİPARİŞ QR=", j.toString());

        super.adj_parameters_json_normal_mode(j);
        super.onStartSubdomain2(input_type.JSON_IMPUT,output_type.JSONOUT,"siparis_qr_read.php");
    }






    @Override
    protected void onprocessing(){


    }
    @Override
    protected void onfinish(){




        try {
            JSONObject jj=new JSONObject(get_siparisqr_yanıt().toString());
            if(!jj.getString("data").equals("failed")){
                syncData();

            }else {
                ((MyPointsActivity)activity).OnQrError("error");
            }



        } catch (JSONException e) {
            ((MyPointsActivity)activity).OnQrError("error");
        }





    }
    @Override
    protected void onError(error e){



    }


    void syncData(){
        //update points
         SyncfreePoint free=new SyncfreePoint(activity,this);
          free.makeSync();

    }
    public   void onsyncfinish(){

        ((MyPointsActivity)activity).OnQrSuccess(get_siparisqr_yanıt().toString());
    }






}
