package com.theoc.restapp.dataorganization;

import com.theoc.restapp.LoginActivity;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.sidemenu.AyarlarActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Ayarlar extends ServConnection {

    AyarlarActivity ayarlarActivity;

    JSONObject jj_temp;



    public Ayarlar(AyarlarActivity ayarlarActivity){
        this.ayarlarActivity = ayarlarActivity;

    }
    public void ayarlari_kaydet(String isim, String soyisim, String dogum, String user_id){

        jj_temp =new JSONObject();
        try {
            jj_temp.put("isim",isim);
            jj_temp.put("soyisim",soyisim);
            jj_temp.put("dogum",dogum);
            jj_temp.put("user_id",user_id);

            send(jj_temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onfinish(){
        ayarlarActivity.ayarlar_server_cevabı();
    }
    @Override
    protected void onError(error e){



    }

    @Override
    protected void onprocessing(){



    }

    void send(JSONObject j){

        super.adj_parameters_json_normal_mode(j);
        super.onStart(input_type.JSON_IMPUT,output_type.JSONOUT,"dogum.php");
    }



}
