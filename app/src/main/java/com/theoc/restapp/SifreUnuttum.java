package com.theoc.restapp.dataorganization;

import com.theoc.restapp.LoginActivity;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class SifreUnuttum extends ServConnection {

LoginActivity loginActivity;

JSONObject jj_temp;



    public SifreUnuttum(LoginActivity loginActivity){
        this.loginActivity=loginActivity;

    }
    public void sifremi_unuttum(String username){

        jj_temp =new JSONObject();
        try {
            jj_temp.put("user_name",username);
            send(jj_temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onfinish(){


        String state="";
        String key="";


        try {
            state=response_json.getString("state");
        } catch (JSONException e) {
            e.printStackTrace();
            loginActivity.sifre_server_cevabı(KayitType.serversorunu, key);
        }
        switch (state) {
            case "var":
                try {
                    key = response_json.getString("key");
                    loginActivity.sifre_server_cevabı(KayitType.var, key);
                } catch (JSONException e) {
                    e.printStackTrace();
                    loginActivity.sifre_server_cevabı(KayitType.serversorunu, key);
                }

                break;
            case "yok":
                loginActivity.sifre_server_cevabı(KayitType.yok, key);
                break;
            default:
                loginActivity.sifre_server_cevabı(KayitType.serversorunu, key);
                break;
        }

    }
    @Override
    protected void onError(error e){



    }

    @Override
    protected void onprocessing(){



    }

    void send(JSONObject j){

        super.adj_parameters_json_normal_mode(j);
        super.onStart(input_type.JSON_IMPUT,output_type.JSONOUT,"sifremi_unuttum_control.php");
    }



}
