package com.theoc.restapp.extendclass.SYNC;

import android.content.Context;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.extendclass.DataProcess.GetDataFromLocal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

public class SYNCServer extends ServConnection {


    SYNCTypes syncTypes;
    public SYNCLocal syncLocal;
    String local_table_name;
    String create_query;
    Map<String,String> tables;
    GetDataFromLocal getDataFromLocal;





    @Override
    protected  void adj_parameters_string_normal_mode(Map m){
        super.adj_parameters_string_normal_mode(m);


    }
    @Override
    protected  void adj_parameters_json_normal_mode(JSONObject j){

        super.adj_parameters_json_normal_mode(j);


    }



    // main methods
    public   SYNCServer (Context context,Map<String,String> extra_req){


        syncLocal=new SYNCLocal(context,extra_req);


    }


    public void make_Sync (String php_file_name, String table_name, String table_create_query, GetDataFromLocal getDataFromLocal){
        this.getDataFromLocal=getDataFromLocal;
        this.syncTypes=SYNCTypes.SINGLESYNC;
        this.local_table_name=table_name;
        this.create_query=table_create_query;

        onStart(input_type.JSON_IMPUT,output_type.JSONOUT,php_file_name);


    }
    public void make_Sync_multiple (String php_file_name,Map<String,String> tables,GetDataFromLocal getDataFromLocal){
        this.getDataFromLocal=getDataFromLocal;
        this.syncTypes=SYNCTypes.MULTIPLE_SYNC;
        this.tables=tables;

        onStart(input_type.JSON_IMPUT,output_type.JSONOUT,php_file_name);


    }






    @Override
    protected  void adj_parameter_json_async() {

        if (syncTypes==SYNCTypes.SINGLESYNC) {
            super.sending_parameter=syncLocal.start_single_sync(local_table_name,create_query);

        }
        else if (syncTypes==SYNCTypes.MULTIPLE_SYNC) {
            super.sending_parameter=syncLocal.start_multiple_sync(tables);

        }
    }



    @Override
    protected  void onStart(input_type i,output_type o,String php_file_name){

        super.onStart(i,o,php_file_name);
    }


    @Override
    protected void parser_to_object(){
        try {
            if (syncTypes==SYNCTypes.SINGLESYNC)

                syncLocal.execute_server_response(super.response_json,local_table_name);
        }catch (Exception e){
            e.printStackTrace();
            //the server response is  not available for parser
        }finally {

        }

    }


    @Override
    protected void onfinish(){

        getDataFromLocal.sync_finish(0);
        getDataFromLocal.sync_finish_nosyenkron();
    }

    @Override
    protected void onError(error e){

        getDataFromLocal.server_error(e);

    }
    @Override
    public void destroy(){

        super.destroy();
    }
    @Override
    protected void canceled_task(){

        getDataFromLocal.cancelsync();
    }

    public JSONArray get_extra_object(String key){
        try {
            return  super.response_json.getJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
