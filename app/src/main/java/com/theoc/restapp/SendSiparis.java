package com.theoc.restapp.dataorganization.barcode;

import android.util.Log;

import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saidyuce on 31.8.2016.
 */
public class SendSiparis extends ServConnection {



    //success ve fail
    public String get_siparis_yanıt(){
        return response_str;
    }
















    int user_id= GeneralSync.id;
    String key_cafe_siparis=ReadBarcode.temp_key_siparis;
    double x_loc=0;
    double y_loc=0;
    int cafe_id=ReadBarcode.cafe_id;







JSONArray jarray;
    JSONObject jj_temp;
    JSONObject jj_temp2;

    public void send_siparis(List<Siparis> siparisler){


           jj_temp=new JSONObject();
           jarray=new JSONArray();

        try {

            jj_temp.put("uid",user_id+"");;
            jj_temp.put("key",key_cafe_siparis+"");
            jj_temp.put("x_",x_loc+"");
            jj_temp.put("y_",y_loc+"");
            jj_temp.put("cid",cafe_id+"");

            for (Siparis s:
                 siparisler) {

                jj_temp2=new JSONObject();
                jj_temp2.put("ad",s.adet);
                jj_temp2.put("urun_id",s.urun_id);
                jj_temp2.put("p_durum",s.prize_durumu);
                jj_temp2.put("not",s.not);
                jarray.put(jj_temp2);


            }
             jj_temp.put("sip",jarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

            send(jj_temp);
    }


    void send(JSONObject j){
        Log.v("SİPARİŞ PHP=", j.toString());

        super.adj_parameters_json_normal_mode(j);
        super.onStart(input_type.JSON_IMPUT,output_type.STROUT,"send_siparis.php");
    }






    @Override
    protected void onprocessing(){


    }
    @Override
    protected void onfinish(){

     // response_str
        if (get_siparis_yanıt().equals("success")){}
        else {}

    }
    @Override
    protected void onError(error e){



    }


}
