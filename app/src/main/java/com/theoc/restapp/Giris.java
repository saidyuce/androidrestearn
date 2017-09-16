package com.theoc.restapp.dataorganization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.LoginActivity;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class Giris extends ServConnection {


    private enum giris_type {normal,face,mail};


    private JSONObject jj_temp;
    private LoginActivity loginActivity;
    public Giris
            (LoginActivity l){
        loginActivity=l;
    }

    public void normal_giriş(String u,String pas, String tip){
        jj_temp =new JSONObject();

        try {
            jj_temp.put("user_name",u);
            jj_temp.put("password",pas);
            jj_temp.put("name", "");
            jj_temp.put("surname", "");
            jj_temp.put("tip", tip);
            send_normal(jj_temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //////////////////  sadece bu iki metodu çağırcan ///////////////////////
    public void face_giris(String username,String device_id,String isim,String soyisim){
        jj_temp =new JSONObject();
        try {
            jj_temp.put("user_name",username);
            jj_temp.put("device_id",device_id);
            jj_temp.put("type","face");
            jj_temp.put("isim",isim);
            jj_temp.put("soyisim",soyisim);
            Log.v("Face sent: ", jj_temp+"");
            send_face(jj_temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void mail_giris(String username,String device_id,String isim,String soyisim){
        jj_temp =new JSONObject();
        try {
            jj_temp.put("user_name",username);
            jj_temp.put("device_id",device_id);
            jj_temp.put("type","mail");
            jj_temp.put("isim",isim);
            jj_temp.put("soyisim",soyisim);
            send_mail(jj_temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////

//// her hangi bir giriş işlemi olduğu esnada bu metodun içi çalışır,main thread da,(onfinishde o işlem bitmiş olur)//////
    @Override
    protected void onprocessing(){

    }

/// prefsde id ve temp key mevcutmu kontrolü
    public void onceki_giris(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(loginActivity);

        String temp = preferences.getString("temp_key", "def");
        int id=preferences.getInt("id", -1);

        if (!temp.equals("") && !temp.equals("def") && id != -1){

            GeneralSync.id=id;
            GeneralSync.temp_key=temp;
            Intent intent = new Intent(loginActivity, HomeActivity.class);
            loginActivity.startActivity(intent);
            loginActivity.finish();
        }
    }

private void  giriş_result(JSONObject t){
    String state="";
    String returntip="";
    String temp_key="";
    int id=-1;
    try {
        state=t.getString("state");
        returntip=t.getString("returntip");
    } catch (JSONException e) {
        e.printStackTrace();
    }

    switch (state) {
        case "success":
            try {
                temp_key = t.getString("key");
                id = t.getInt("id");
                loginActivity.gir(GirisType.girisbasarili, temp_key, id);

            } catch (JSONException e) {
                e.printStackTrace();
                loginActivity.gir(GirisType.girisserversorunu, "", id);
            }
            break;
        case "wait":
            if (returntip.equals("true")) {
                try {
                    temp_key = t.getString("key");
                } catch (JSONException e) {
                    e.printStackTrace();
                    loginActivity.gir(GirisType.girisserversorunu, "", id);
                }
                loginActivity.gir(GirisType.onaybekleniyor, temp_key, id);
            } else {
                loginActivity.gir(GirisType.onaybekleniyor, "", id);
            }
            break;
        case "fail":
            loginActivity.gir(GirisType.girisbasarisiz, "", id);
            break;
        default:
            loginActivity.gir(GirisType.girisserversorunu, "", id);
            break;
    }
}



    private void  giriş_result_face_mail(JSONObject t){
        String state="";
        String temp_key="";
        int id=-1;
        try {
            state=t.getString("state");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (state.equals("var")){

            try {
                temp_key=t.getString("key");
                id=t.getInt("id");
                //kul id
                loginActivity.gir((g==giris_type.face)?GirisType.facegirisvar:GirisType.mailgirisvar,temp_key,id);

            } catch (JSONException e) {
                e.printStackTrace();
                loginActivity.gir(GirisType.girisserversorunu,"",id);
            }


        }
         else     if (state.equals("yok")){

            try {
                temp_key=t.getString("key");
                id=t.getInt("id");
                //kul id
                loginActivity.gir((g==giris_type.face)?GirisType.facegirisyok:GirisType.mailgirisyok,temp_key,id);

            } catch (JSONException e) {
                e.printStackTrace();
                loginActivity.gir(GirisType.girisserversorunu,"",id);
            }


        }


        else if (state.equals("fail")){   loginActivity.gir((g==giris_type.face)?GirisType.facegirisfail:GirisType.mailgirisfail,"",id);}
        else {loginActivity.gir(GirisType.girisserversorunu,"",id);}
    }







    @Override
    protected void onfinish(){


        if (g==giris_type.normal)
        giriş_result(response_json);
        else if (g==giris_type.face||g==giris_type.mail)
        giriş_result_face_mail(response_json);

    }
    @Override
    protected void onError(error e){

    //    loginActivity.gir(GirisType.girisserversorunu,"",-1);

    }

    private void send_normal(JSONObject j){
        g=giris_type.normal;
        super.adj_parameters_json_normal_mode(j);
        super.onStart(input_type.JSON_IMPUT,output_type.JSONOUT,"giris.php");
    }

    private void send_mail(JSONObject j){
        g=giris_type.mail;
        super.adj_parameters_json_normal_mode(j);
        super.onStart(input_type.JSON_IMPUT,output_type.JSONOUT,"kayit.php");
    }
    private void send_face(JSONObject j){
        g=giris_type.face;
        super.adj_parameters_json_normal_mode(j);
        super.onStart(input_type.JSON_IMPUT,output_type.JSONOUT,"kayit.php");
    }

    giris_type g;





}
