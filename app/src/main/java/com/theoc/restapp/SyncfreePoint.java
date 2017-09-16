package com.theoc.restapp.dataorganization.barcode;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.extendclass.DataConnections.LocalConnection;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;

/**
 * Created by saidyuce on 4.9.2017.
 */
 class  SyncfreePoint extends GeneralSync {


    Activity ac;
    OdulQrRead od;
    SiparisQRread sqr;
    protected SyncfreePoint(Activity activity,OdulQrRead odulQrRead) {
        super(activity);

       od=odulQrRead;
        sqr=null;
    }
    protected SyncfreePoint(Activity activity,SiparisQRread siparisQRread) {
        super(activity);
       od=null;
        sqr=siparisQRread;

    }
    public void makeSync(){

        super.start();
    }

    @Override
    public void sync_finish_nosyenkron(){


        if(od==null){

            sqr.onsyncfinish();
        }
        else {
            od.onsyncfinish();
        }

    }
    @Override
    public void server_error(ServConnection.error e){
        // ((HomeActivity)a).basla();

    }
    @Override
    protected void internet_is_not_activated(){


    }



}