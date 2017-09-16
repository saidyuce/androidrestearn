package com.theoc.restapp.extendclass.DataConnections;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import com.theoc.restapp.MyPointsActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServConnection {

    public       enum error {SUCCES,ERROR    }
    protected       enum server_response {}
    protected static   enum input_type {EMPTY_INPUT,JSON_IMPUT,STR_INPUT}
    protected static   enum output_type {FILEOUT,JSONOUT,STROUT}



    public static int port_no=80;
    public static String ip="restearnserver.com";
    public static String subdomain_file="RestUpp/KontrolPaneli/caferesim/";
    public static String get_file_url(){
        return "http://"+ip+"/"+subdomain_file;
    }

    private static String subdomain="RestUpp" +
            "";
    private static String subdomain2="restearn" +
            "";
    private static String    server_ip="http://"+ip+"/"+subdomain;







    //private variables
    private String php_file_name;
    private InputStream is;
    private String line;




    private class MyTask extends AsyncTask<Object, Void, error> {




        HttpClient httpclient;
        HttpPost httppost;
        StringEntity se;
        HttpResponse response;
        HttpEntity entity;
        BufferedReader reader;
        StringBuilder sb;
        String s;

        private error parameter_json(String name){

            try {
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(server_ip+"/"+php_file_name);
                //      httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                //   se = new StringEntity(sending_parameter.toString());

                //        httppost.setEntity(se);
                String abc=sending_parameter.toString();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair(name, sending_parameter.toString()));
                Log.v("sending", abc);
                Log.v("sending2", nameValuePairs.toString())
                // Use UrlEncodedFormEntity to send in proper format which we need
             ;
                httppost.setEntity(   new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                //     httppost.setHeader("Accept", "application/json");
                //     httppost.setHeader("Content-type", "application/json");

                return error.SUCCES;
            }
            catch (Exception e){



                return error.ERROR;

            }

        }
        private error parameter_string(){

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                for (Object a :sending_parameter_str.keySet() ){
                    nameValuePairs.add(new BasicNameValuePair(a.toString(),sending_parameter_str.get(a).toString()));
                }

                StringEntity entity2 = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(server_ip+"/"+php_file_name);

                httppost.setEntity(entity2);
                return  error.SUCCES;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return error.ERROR;
            }

        }
        private error parameter_empty(){
            try {
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(server_ip+"/"+php_file_name);
                return error.SUCCES;
            }   catch (Exception e){
                return error.ERROR;
            }
        }

        private error execute (){
            try {
                response = httpclient.execute(httppost);
                entity = response.getEntity();
                is = entity.getContent();

                if (outputType==output_type.FILEOUT){
                    int len=0;
                    File f=new File(file_path+"/"+file_name);
                    FileOutputStream fileOutput;
                    long filesize=0;
                    filesize = entity.getContentLength();
                    fileOutput = new FileOutputStream(f);
                    byte[] buffer = new byte[1024];
                    while ((len = is.read(buffer, 0, 1024)) > 0) {
                        fileOutput.write(buffer, 0, len);

                    }

                    fileOutput.close();
                }
                else {

                    reader = new BufferedReader
                            (new InputStreamReader(is, "UTF-8"), 8);
                    sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");


                    }
                }

                if (outputType==output_type.JSONOUT) {
                    Log.v("RESPONSE: ", sb.toString()+"");
                    response_json = new JSONObject(sb.toString());
                    Log.v("RESPONSE JSON: ", response_json+"");

                    parser_to_object();

                }else if (outputType==output_type.STROUT){
                    response_str=sb.toString();
                    Log.v("RESPONSE JSON: ", response_str+"");
                    parser_to_object();
                }
                else if (outputType==output_type.FILEOUT){
                    response_str=file_path+"/"+file_name;
                    parser_to_object();
                }

                is.close();
                return error.SUCCES;
            } catch (Exception e) {
                e.printStackTrace();
                return error.ERROR;
            }
        }

        error eRRor;

        @Override
        protected error doInBackground(Object... input) {

            try {
                adj_parameter_strings_async();
                adj_parameter_json_async();
            }
            catch (Exception e){

                return  error.ERROR;
            }

            if (inputType==input_type.EMPTY_INPUT){
                eRRor=   parameter_empty();

                if (eRRor==error.SUCCES){
                    eRRor=execute();
                    if (eRRor==error.SUCCES){

                        return error.SUCCES;
                    }
                    else {return eRRor;  }
                }
                else return eRRor;
            }
            else if (inputType==input_type.JSON_IMPUT){
                eRRor=parameter_json(input[0].toString());
                if (eRRor==error.SUCCES){
                    eRRor=execute();
                    if (eRRor==error.SUCCES){
                        return error.SUCCES;
                    }
                    else {return eRRor;  }
                }
                else return eRRor;
            }
            else {

                eRRor=parameter_string();
                if (eRRor==error.SUCCES){
                    eRRor=execute();
                    if (eRRor==error.SUCCES){
                        return error.SUCCES;
                    }
                    else {return eRRor;  }
                }
                else return eRRor;
            }


        }

        @Override
        protected void onPostExecute(error output) {
            if (output==error.SUCCES){
                onfinish();
            }
            else {

                onError(output);
            }

        }

        @Override
        protected void onCancelled(){
            canceled_task();
        }



    }


    public JSONObject getonlyJson(){


        return response_json;
    }

    public Object getObject(){


        return response_object;
    }

    public String getStringresponse(){

        return  response_str;
    }

    //override methods


    protected void parser_to_object() {

// use parser response string or parse response json
        // output response object

    }


    private input_type inputType;
    private output_type outputType;
    private String file_path;
    private String file_name;
    private MyTask myTask;

    protected  void onStart(input_type i,output_type o,String php_file_name){
        server_ip="http://"+ip+"/"+subdomain;
        this.php_file_name=php_file_name;
        inputType=i;
        outputType=o;
        myTask=new MyTask();
        myTask.execute("req");
        onprocessing();

    }
    protected  void onStartSubdomain2(input_type i,output_type o,String php_file_name){
        server_ip="http://"+ip+"/"+subdomain2;
        this.php_file_name=php_file_name;
        inputType=i;
        outputType=o;
        myTask=new MyTask();
        myTask.execute("message_sip");
        onprocessing();

    }
    protected  void onStart(input_type i,String php_file_name,String file_path,String file_name){


        inputType=i;
        outputType=output_type.FILEOUT;
        this.php_file_name=php_file_name;
        this.file_path=file_path;
        this.file_name=file_name;
        myTask=new MyTask();
        myTask.execute("");
        onprocessing();

    }



    // dont override
    protected void set_server_ip(String t){

        server_ip=t;
    }
    protected  void adj_parameters_string_normal_mode(Map m){
        sending_parameter_str=m;



    }
    protected  void adj_parameters_json_normal_mode(JSONObject j){

        sending_parameter=j;


    }



    //only override



    //manuel


    protected void onprocessing(){


    }
    protected void onfinish(){


    }

    protected void onError(error e){



    }

    public void destroy(){

        myTask.cancel(true);
    }

    protected void canceled_task(){

    }




    protected  void adj_parameter_strings_async(){

//set senfing parameter str
    }
    protected  void adj_parameter_json_async() {

//set sending_parameter

    }



    protected JSONObject sending_parameter;
    protected Map sending_parameter_str;


    protected String response_str;
    protected JSONObject response_json;
    protected   Object response_object; //only for object parser..need to override object parser




}