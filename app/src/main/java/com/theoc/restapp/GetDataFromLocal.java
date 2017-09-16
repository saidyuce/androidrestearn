package com.theoc.restapp.extendclass.DataProcess;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.theoc.restapp.extendclass.DataConnections.LocalConnection;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.extendclass.SYNC.SYNCServer;
import com.theoc.restapp.extendclass.SYNC.SYNCTypes;

import org.json.JSONObject;

import java.util.Map;

public class GetDataFromLocal extends LocalConnection {

    private    SYNCServer syncServer;


    protected Activity a;
    protected     Thread   thread;
    protected GetDataFromLocal(Context context,Activity activity) {
        super(context);
        this.a=activity;
    }

    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                a.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    private boolean isActivatedInternet(){
        return false;
    }
    public void get_data_async_single(boolean makesync, Context context,String php_file_name,String synctable_name,String create_query,Map<String,String> extra_req,int i){

        if (makesync==true) {
            if (isOnline()) {
            } else {
                if (isActivatedInternet()) {
                } else {

                    internet_is_not_activated();
                    makesync = false;
                }
            }
        }

        if (makesync==true){
            syncServer=new SYNCServer(context,extra_req);
            post_sync();
            syncServer.make_Sync(php_file_name,synctable_name,create_query,this);
            onSYNC();

        }
        else {
            sync_finish(i);
        }
    }



  /*  public void get_data_async_multiple(boolean makesync, Context context, String php_file_name, Map<String,String> tables){

        if (isOnline()){          }
        else { if (isActivatedInternet()){       }  else {internet_is_not_activated();   makesync=false; }                  }
        if (makesync==true){
            syncServer=new SYNCServer(context);

            syncServer.make_Sync(php_file_name,synctable_name,create_query,this);
            onSYNC();

        }
        else {
            sync_finish();
        }
    }
    */

    protected void internet_is_not_activated(){

    }

    public void get_data(boolean makesync){
        if (makesync==true){

        }
        else {

        }
    }

    protected void post_sync(){


    }

    public void sync_finish(final int i){



        thread = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        //    wait(5000);

                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                get_data_from_local(i);
                                data_received(i);
                            }
                        });

                    }
                }
                //catch (InterruptedException e)
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            };
        };
        thread.start();
        while_get_data_from_local();
    }

    protected void get_data_from_local(int i){


    }

    public void sync_finish_nosyenkron(){


    }

    protected void stop_get_data_from_local(){if (thread!=null&&thread.isAlive())thread.destroy();}
    protected void data_received(int i){

    }
    @Override
    protected void onError(errors e){


    }

    protected void while_get_data_from_local(){

    }

    public void server_error(ServConnection.error e){

    }

    protected void destroy_task(){
        if(syncServer!=null)
        syncServer.destroy();
    }

    public void cancelsync(){

    }
    protected void onSYNC(){


    }

    protected int get_extra_data(String id){

        return Integer.parseInt(syncServer.syncLocal.extra_response.get(id));
    }
}
