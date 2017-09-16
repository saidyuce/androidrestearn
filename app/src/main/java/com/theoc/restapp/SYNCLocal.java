package com.theoc.restapp.extendclass.SYNC;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.ServerYanıt;
import com.theoc.restapp.dataorganization.barcode.GetAndSaveServerCafeData;
import com.theoc.restapp.dataorganization.barcode.ReadBarcode;
import com.theoc.restapp.extendclass.DataConnections.LocalConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SYNCLocal extends LocalConnection {




    public SYNCLocal(Context context,Map<String,String>ex) {
        super(context);
        extra_request=ex;
    }

    Map<String,String>extra_request;
    public Map<String,String>extra_response;





    @Override
    protected void when_the_control(String table) {


        super.select_and_response_json2(table,"SELECT  id,id2,name,type,id3 FROM " + table);
    }
    JSONObject rowObject;
    @Override
    protected void add_new_value_to_req_json(){

        if (extra_request!=null){
            rowObject = new JSONObject();
            for (String b:extra_request.keySet() ){

                try {
                    rowObject.put(b,extra_request.get(b.toString()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            try {
                result_json.put("extra",rowObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    String qu="";
    JSONObject jsonObject1;

    JSONArray jsonarray;
    protected void execute_server_response(JSONObject jsonObject,String table_name){

        Log.v("General senk1",jsonObject.toString());

        try {
            extra_response=new HashMap<>();

            // extra data
            if (table_name.equals("synctable2")) {
                jsonObject1 = jsonObject.getJSONObject("extra");

                ReadBarcode.durum=jsonObject1.getString("durum");

                if(ReadBarcode.durum.equals("fail")||ReadBarcode.durum.equals("wrong_barcode")){

                    return;
                }

                extra_response.put("cafe_id", jsonObject1.getInt("cafe_id")+"");
                extra_response.put("durum", jsonObject1.getString("durum")+"");
                extra_response.put("key", jsonObject1.getString("key")+"");
                extra_response.put("tarih", jsonObject1.getString("tarih")+"");
                extra_response.put("temp_point", jsonObject1.getInt("temp_point")+"");





            }
        }catch (Exception e){
            onError(errors.EXECERROR);
        }
        finally {


        }
        if (table_name.equals("synctable2")) {



            ReadBarcode.durum=extra_response.get("durum");

if(ReadBarcode.durum.equals("fail")||ReadBarcode.durum.equals("wrong_barcode")){

    return;
}

            int caf_id=Integer.parseInt(extra_response.get("cafe_id"));
            ReadBarcode.cafe_id=caf_id;
            GetAndSaveServerCafeData.cafe_id=caf_id;
            ReadBarcode.temp_key_siparis=extra_response.get("key");
            ReadBarcode.oturum_tarihi=extra_response.get("tarih");
            ReadBarcode.temp_point=Integer.parseInt(extra_response.get("temp_point"));
            try {
                jsonObject1=new JSONObject();
                jsonObject1=jsonObject.getJSONObject("datainsert");






                try {


                    insert_menu(jsonObject1.getJSONObject("menu_data").getJSONArray("menu"+caf_id));
                }catch (Exception e){


                }

                try {


                    insert_surwey(jsonObject1.getJSONObject("surwey_data").getJSONArray("surwey"+ caf_id));
                }catch (Exception e){


                }

                try {


                    insert_waiter(jsonObject1.getJSONObject("waiter_data").getJSONArray("waiter"+caf_id));
                }catch (Exception e){


                }


            }catch (Exception e){



            }
            try {
                jsonObject1=new JSONObject();
                jsonObject1=jsonObject.getJSONObject("dataupdate");

                try {


                    update_menu(jsonObject1.getJSONObject("menu_data").getJSONArray("menu"+caf_id));
                }catch (Exception e){


                }

                try {


                    update_surwey(jsonObject1.getJSONObject("surwey_data").getJSONArray("surwey"+ caf_id));
                }catch (Exception e){


                }

                try {


                    update_waiter(jsonObject1.getJSONObject("waiter_data").getJSONArray("waiter"+caf_id));
                }catch (Exception e){


                }


            }catch (Exception e){



            }





        }
        else if (table_name.equals("synctable")) {
            try {
                jsonObject1=new JSONObject();
                jsonObject1=jsonObject.getJSONObject("datainsert");
                try {


                    insert_cafecoin(jsonObject1.getJSONObject("cafecoin_data").getJSONArray("cafecoin"));
                }catch (Exception e){


                }
                try {


                    insert_cafe(jsonObject1.getJSONObject("cafe_data").getJSONArray("cafe"));
                }catch (Exception e){


                }

                try {


                    insert_campaing(jsonObject1.getJSONObject("campaing_data").getJSONArray("campaing"+ GeneralSync.location_city.toLowerCase()));
                }catch (Exception e){


                }

                try {


                    insert_prize(jsonObject1.getJSONObject("prize_data").getJSONArray("prize"));
                }catch (Exception e){


                }

                try {


                    insert_points(jsonObject1.getJSONObject("point_data").getJSONArray("point"+GeneralSync.id));
                }catch (Exception e){


                }
                try {


                    insert_free_points(jsonObject1.getJSONObject("free_point_data").getJSONArray("free_point"+GeneralSync.id));
                }catch (Exception e){


                }

            }catch (Exception e){


            }
            try {
                jsonObject1=new JSONObject();
                jsonObject1=jsonObject.getJSONObject("dataupdate");

                try {


                    update_cafecoin(jsonObject1.getJSONObject("cafecoin_data").getJSONArray("cafecoin"));
                }catch (Exception e){


                }
                try {


                    update_cafe(jsonObject1.getJSONObject("cafe_data").getJSONArray("cafe"));
                }catch (Exception e){


                }

                try {


                    update_campaing(jsonObject1.getJSONObject("campaing_data").getJSONArray("campaing"+ GeneralSync.location_city.toLowerCase()));
                }catch (Exception e){


                }

                try {


                    update_prize(jsonObject1.getJSONObject("prize_data").getJSONArray("prize"));
                }catch (Exception e){


                }

                try {


                    update_points(jsonObject1.getJSONObject("point_data").getJSONArray("point"+GeneralSync.id));
                }catch (Exception e){


                }

                try {


                    update_free_points(jsonObject1.getJSONObject("free_point_data").getJSONArray("free_point"+GeneralSync.id));
                }catch (Exception e){


                }

            }catch (Exception e){


            }


        }

        try {


            update_q(jsonObject.getJSONArray("updateq"),table_name);
        }catch (Exception e){


        }
        try {


            inser_q(jsonObject.getJSONArray("syncquerinsert"),table_name);
        }catch (Exception e){


        }
        try {


            delete(jsonObject.getJSONArray("deleteq"),table_name);
        }catch (Exception e){


        }

        try {


            String state=jsonObject.getString("state");
            if (state.equals("misafir")){
                GeneralSync.serverYanıt= ServerYanıt.misafir;
            }
            else if (state.equals("success")){GeneralSync.serverYanıt= ServerYanıt.success; GeneralSync.isim=jsonObject.getString("isim");   GeneralSync.soyisim=jsonObject.getString("soyisim");                 }
            else if (state.equals("fail")){

                GeneralSync.serverYanıt= ServerYanıt.fail;
            }else GeneralSync.serverYanıt= ServerYanıt.servererror;

        }catch (Exception e){
            GeneralSync.serverYanıt= ServerYanıt.servererror;

        }




    }
    private  void  insert_cafecoin(JSONArray jsonarray){   try {

        qu="";
        for (int i=0;i<jsonarray.length();++i){

            qu="insert into cafecoin (id,cafe_id,coin)  values(" +
                    jsonarray.getJSONObject(i).getInt("id")+"," +

                    jsonarray.getJSONObject(i).getInt("cafe_id")+"," +
                    jsonarray.getJSONObject(i).getDouble("coin") +
                    ");";
            super.exec_and_response_excquery_write(qu);
        }
    }
    catch (Exception e){
        //cafe_exception
    }
    }

    private void update_cafecoin(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="update cafecoin " +
                        "set id="+  jsonarray.getJSONObject(i).getInt("id")+"," +
                        " cafe_id="+   jsonarray.getJSONObject(i).getInt("cafe_id")+"," +
                        " coin="+   jsonarray.getJSONObject(i).getDouble("coin")+
                        " where id="+jsonarray.getJSONObject(i).getInt("id")+";";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }

    }

    private void update_cafe(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="update cafe " +
                        "set id="+        jsonarray.getJSONObject(i).getInt("id")+"," +
                        "x_="+         jsonarray.getJSONObject(i).getDouble("x_loc")+"," +
                        "y_="+        jsonarray.getJSONObject(i).getDouble("y_loc")+"," +
                        "preminium_type="+       jsonarray.getJSONObject(i).getInt("preminium_type")+"," +
                        "category="+      "'" +    jsonarray.getJSONObject(i).getString("category")+"'" +"," +
                        "name="+     "'" +    jsonarray.getJSONObject(i).getString("name")+"'" +"," +
                        "icon="+       "'" +    jsonarray.getJSONObject(i).getString("icon")+"'" +"," +
                        "city="+      "'" +     jsonarray.getJSONObject(i).getString("city")+"'" +"," +
                        "large_image="+      "'" +    jsonarray.getJSONObject(i).getString("large_image")+"'" +"," +
                        "cafe_picture_url="+       "'" +    jsonarray.getJSONObject(i).getString("cafe_picture_url")+"'" +"," +
                        "info="+       "'" +      jsonarray.getJSONObject(i).getString("info")+"'" +"," +
                        "tel="+       "'" +    jsonarray.getJSONObject(i).getString("tel")+"'" +"," +
                        "map_icon="+     "'" +jsonarray.getJSONObject(i).getString("map_icon")+"'" +","+
                        "detail="+     "'" +jsonarray.getJSONObject(i).getString("detail")+"'" +","+
                        "site="+     "'" +jsonarray.getJSONObject(i).getString("site")+"'" +","+
                        "face="+     "'" +jsonarray.getJSONObject(i).getString("face")+"'" +","+
                        "twitter="+     "'" +jsonarray.getJSONObject(i).getString("twitter")+"'" +","+
                        "instagram="+     "'" +jsonarray.getJSONObject(i).getString("instagram")+"'" +
                        " where id="+jsonarray.getJSONObject(i).getInt("id")+";";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }

    private void insert_cafe(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="insert into cafe (id,x_,y_,preminium_type,category,name,icon,city,large_image,cafe_picture_url,info,tel,map_icon,detail,site,face,twitter,instagram)  values(" +
                        jsonarray.getJSONObject(i).getInt("id")+"," +
                        jsonarray.getJSONObject(i).getDouble("x_loc")+"," +
                        jsonarray.getJSONObject(i).getDouble("y_loc")+"," +
                        jsonarray.getJSONObject(i).getInt("preminium_type")+"," +
                        "'" +    jsonarray.getJSONObject(i).getString("category")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("name")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("icon")+"'" +"," +
                        "'" +     jsonarray.getJSONObject(i).getString("city")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("large_image")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("cafe_picture_url")+"'" +"," +
                        "'" +      jsonarray.getJSONObject(i).getString("info")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("tel")+"'" +"," +
                        "'" +jsonarray.getJSONObject(i).getString("map_icon")+"'" +","+
                        "'" +jsonarray.getJSONObject(i).getString("detail")+"'" +","+
                        "'" +jsonarray.getJSONObject(i).getString("site")+"'" +","+
                        "'" +jsonarray.getJSONObject(i).getString("face")+"'" +","+
                        "'" +jsonarray.getJSONObject(i).getString("twitter")+"'" +","+
                        "'" +jsonarray.getJSONObject(i).getString("instagram")+"'" +
                        ");";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }

    private void update_campaing(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="update campaing " +
                        "set id="+     jsonarray.getJSONObject(i).getInt("id")+"," +
                        "cafe_id="+        jsonarray.getJSONObject(i).getInt("cafe_id")+"," +
                        "category="+    "'" +    jsonarray.getJSONObject(i).getString("category")+"'" +"," +
                        "picture_url="+     "'" +     jsonarray.getJSONObject(i).getString("picture_url")+"'" +"," +
                        "detail="+     "'" +    jsonarray.getJSONObject(i).getString("detail")+"'" +"," +
                        "last_time="+     "'" +      jsonarray.getJSONObject(i).getString("last_time")+"'" +"," +
                        "city="+   "'" +jsonarray.getJSONObject(i).getString("city")+"'" +
                        " where id="+jsonarray.getJSONObject(i).getInt("id")+";";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }

    private void insert_campaing(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="insert into campaing (id,cafe_id,category,picture_url,detail,last_time,city)  values(" +
                        jsonarray.getJSONObject(i).getInt("id")+"," +
                        jsonarray.getJSONObject(i).getInt("cafe_id")+"," +
                        "'" +    jsonarray.getJSONObject(i).getString("category")+"'" +"," +
                        "'" +     jsonarray.getJSONObject(i).getString("picture_url")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("detail")+"'" +"," +
                        "'" +      jsonarray.getJSONObject(i).getString("last_time")+"'" +"," +
                        "'" +jsonarray.getJSONObject(i).getString("city")+"'" +

                        ");";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }

    private void insert_prize(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="insert into prize (id,name,cafe_id)  values(" +
                        jsonarray.getJSONObject(i).getInt("id")+"," +
                        "'" +    jsonarray.getJSONObject(i).getString("name")+"'" +"," +
                        jsonarray.getJSONObject(i).getInt("cafe_id")+
                        ");";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }
    private void update_prize(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="update prize " +
                        "set id="+  jsonarray.getJSONObject(i).getInt("id")+"," +
                        " name="+  "'" +    jsonarray.getJSONObject(i).getString("name")+"'" +"," +
                        " cafe_id="+   jsonarray.getJSONObject(i).getInt("cafe_id")+
                        " where id="+jsonarray.getJSONObject(i).getInt("id")+";";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }
    private void insert_points(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="insert into points (id,user_id,cafe_id,point)  values(" +
                        jsonarray.getJSONObject(i).getInt("id")+"," +
                        jsonarray.getJSONObject(i).getInt("user_id")+"," +
                        jsonarray.getJSONObject(i).getInt("cafe_id")+"," +
                        jsonarray.getJSONObject(i).getInt("point")+
                        ");";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }
    private void update_points(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="update points " +
                        "set id="+       jsonarray.getJSONObject(i).getInt("id")+"," +
                        "user_id="+      jsonarray.getJSONObject(i).getInt("user_id")+"," +
                        "cafe_id="+        jsonarray.getJSONObject(i).getInt("cafe_id")+"," +
                        "point="+        jsonarray.getJSONObject(i).getInt("point")+
                        " where id="+jsonarray.getJSONObject(i).getInt("id")+";";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }
    private void insert_free_points(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="insert into free_point (id,kul_id,cafe_id,point)  values(" +
                        jsonarray.getJSONObject(i).getInt("id")+"," +
                        jsonarray.getJSONObject(i).getInt("kul_id")+"," +
                        jsonarray.getJSONObject(i).getInt("cafe_id")+"," +
                        jsonarray.getJSONObject(i).getInt("point")+
                        ");";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }
    private void update_free_points(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="update free_point " +
                        "set id="+       jsonarray.getJSONObject(i).getInt("id")+"," +
                        "kul_id="+      jsonarray.getJSONObject(i).getInt("kul_id")+"," +
                        "cafe_id="+        jsonarray.getJSONObject(i).getInt("cafe_id")+"," +
                        "point="+        jsonarray.getJSONObject(i).getInt("point")+
                        " where id="+jsonarray.getJSONObject(i).getInt("id")+";";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }
    private void update_menu(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="update menu " +
                        "set id="+     jsonarray.getJSONObject(i).getInt("id")+"," +
                        "price=" +    jsonarray.getJSONObject(i).getDouble("price")+"," +
                        "name="+    "'" +    jsonarray.getJSONObject(i).getString("name")+"'" +"," +
                        "picture="+    "'" +    jsonarray.getJSONObject(i).getString("picture")+"'" +"," +
                        "category="+     "'" +    jsonarray.getJSONObject(i).getString("category")+"'" +"," +
                        "cafe_id="+     jsonarray.getJSONObject(i).getInt("cafe_id")+"," +
                        "selling_number="+    jsonarray.getJSONObject(i).getInt("selling_number")+"," +
                        "stoc_state="+   jsonarray.getJSONObject(i).getInt("stoc_state")+"," +
                        "info="+    "'" +jsonarray.getJSONObject(i).getString("info")+"'" +
                        " where id="+jsonarray.getJSONObject(i).getInt("id")+";";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }
    private void update_waiter(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="update waiter " +
                        "set id="+       jsonarray.getJSONObject(i).getInt("id")+"," +
                        "name_surname="+      "'" +    jsonarray.getJSONObject(i).getString("name_surname")+"'" +"," +
                        "picture="+   "'" +    jsonarray.getJSONObject(i).getString("picture")+"'" +"," +
                        "cafe_id="+  jsonarray.getJSONObject(i).getInt("cafe_id")+
                        " where id="+jsonarray.getJSONObject(i).getInt("id")+";";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }

    private void update_surwey(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="update surwey " +
                        "set id="+     jsonarray.getJSONObject(i).getInt("id")+"," +
                        "question="+      "'" +    jsonarray.getJSONObject(i).getString("question")+"'" +"," +
                        "a_="  +   "'" +    jsonarray.getJSONObject(i).getString("a_")+"'" +"," +
                        "b_="  +   "'" +    jsonarray.getJSONObject(i).getString("b_")+"'" +"," +
                        "c_="  +      "'" +    jsonarray.getJSONObject(i).getString("c_")+"'" +"," +
                        "d_="  +       "'" +    jsonarray.getJSONObject(i).getString("d_")+"'" +"," +
                        "cafe_id="  +        jsonarray.getJSONObject(i).getInt("cafe_id")+"," +
                        "a_pic="  +        "'" +    jsonarray.getJSONObject(i).getString("a_pic")+"'" +"," +
                        "b_pic="  +        "'" +    jsonarray.getJSONObject(i).getString("b_pic")+"'" +"," +
                        "c_pic="  +        "'" +    jsonarray.getJSONObject(i).getString("c_pic")+"'" +"," +
                        "d_pic="  +       "'" +jsonarray.getJSONObject(i).getString("d_pic")+"'" +
                        " where id="+jsonarray.getJSONObject(i).getInt("id")+";";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }
    private void insert_menu(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="insert into menu (id,price,name,picture,category,cafe_id,selling_number,stoc_state,info)  values(" +
                        jsonarray.getJSONObject(i).getInt("id")+"," +
                        jsonarray.getJSONObject(i).getDouble("price")+"," +
                        "'" +    jsonarray.getJSONObject(i).getString("name")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("picture")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("category")+"'" +"," +
                        jsonarray.getJSONObject(i).getInt("cafe_id")+"," +
                        jsonarray.getJSONObject(i).getInt("selling_number")+"," +
                        jsonarray.getJSONObject(i).getInt("stoc_state")+"," +
                        "'" +jsonarray.getJSONObject(i).getString("info")+"'" +
                        ");";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }

    private void insert_surwey(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="insert into surwey (id,question,a_,b_,c_,d_,cafe_id,a_pic,b_pic,c_pic,d_pic)  values(" +
                        jsonarray.getJSONObject(i).getInt("id")+"," +
                        "'" +    jsonarray.getJSONObject(i).getString("question")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("a_")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("b_")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("c_")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("d_")+"'" +"," +
                        jsonarray.getJSONObject(i).getInt("cafe_id")+"," +
                        "'" +    jsonarray.getJSONObject(i).getString("a_pic")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("b_pic")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("c_pic")+"'" +"," +
                        "'" +jsonarray.getJSONObject(i).getString("d_pic")+"'" +
                        ");";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }
    private void insert_waiter(JSONArray jsonarray){

        try {

            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="insert into waiter (id,name_surname,picture,cafe_id)  values(" +
                        jsonarray.getJSONObject(i).getInt("id")+"," +
                        "'" +    jsonarray.getJSONObject(i).getString("name_surname")+"'" +"," +
                        "'" +    jsonarray.getJSONObject(i).getString("picture")+"'" +"," +
                        jsonarray.getJSONObject(i).getInt("cafe_id")+
                        ");";
                super.exec_and_response_excquery_write(qu);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }

    String save_q;

    private void update_q(JSONArray jsonarray,String table){

        try {
            save_q="";
            qu="";
            for (int i=0;i<jsonarray.length();++i){


                save_q="insert into "+table+" (id,id2,name,type,id3) values ("+
                        jsonarray.getJSONObject(i).getInt("id")+","+
                        jsonarray.getJSONObject(i).getInt("id2")+","+
                        "'"+jsonarray.getJSONObject(i).getString("name")+"',"+
                        "'"+jsonarray.getJSONObject(i).getString("type")+"',"+
                        jsonarray.getJSONObject(i).getInt("id3")+
                        ");" ;

                super.exec_and_response_excquery_write("delete from "+table+" where id="+jsonarray.getJSONObject(i).getInt("id")+";");
                super.exec_and_response_excquery_write(save_q);
            }
        }
        catch (Exception e){
            //cafe_exception
            e.printStackTrace();
        }
    }
    private void inser_q(JSONArray jsonarray,String table){

        try {
            save_q="";
            qu="";
            for (int i=0;i<jsonarray.length();++i){


                save_q="insert into "+table+" (id,id2,name,type,id3) values ("+
                        jsonarray.getJSONObject(i).getInt("id")+","+
                        jsonarray.getJSONObject(i).getInt("id2")+","+
                        "'"+jsonarray.getJSONObject(i).getString("name")+"',"+
                        "'"+jsonarray.getJSONObject(i).getString("type")+"',"+
                        0+
                        ");" ;

                super.exec_and_response_excquery_write("delete from "+table+" where id="+jsonarray.getJSONObject(i).getInt("id")+";");
                super.exec_and_response_excquery_write(save_q);
            }
        }
        catch (Exception e){
            //cafe_exception
            e.printStackTrace();
        }
    }
    private void delete(JSONArray jsonarray,String table){

        try {
            save_q="";
            qu="";
            for (int i=0;i<jsonarray.length();++i){

                qu="delete from "+jsonarray.getJSONObject(i).getString("tab")+" where id="+jsonarray.getJSONObject(i).getInt("id2")+";";
                save_q="insert into "+table+" (id,id2,name,type,id3) values ("+
                        jsonarray.getJSONObject(i).getInt("id")+","+
                        jsonarray.getJSONObject(i).getInt("id2")+","+
                        "'"+jsonarray.getJSONObject(i).getString("name")+"',"+
                        "'"+jsonarray.getJSONObject(i).getString("type")+"',"+
                        jsonarray.getJSONObject(i).getInt("id3")+
                        ");" ;
                super.exec_and_response_excquery_write(qu);
                super.exec_and_response_excquery_write("delete from "+table+" where id="+jsonarray.getJSONObject(i).getInt("id")+";");
                super.exec_and_response_excquery_write(save_q);
            }
        }
        catch (Exception e){
            //cafe_exception
        }
    }



    @Override
    protected void   onError(errors e){


    }


    public JSONObject start_multiple_sync(Map<String,String> controls){
        super.result_json=new JSONObject();
        super.control_tables(controls);
        return super.result_json;

    }
    public JSONObject start_single_sync(String table,String query) {
        super.result_json=new JSONObject();
        super.control_table(table, query);
        return super.result_json;
    }





}

