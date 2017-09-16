package com.theoc.restapp.dataorganization.barcode;

import android.app.Activity;

import com.theoc.restapp.CafeJoinActivity;

public class ReadBarcode extends GetAndSaveServerCafeData {



 //static    ConnectionPc connectionPc;


    public static String temp_key_siparis="";
    public static String durum="";
    public static String oturum_tarihi="";
    public static int temp_point;




    public ReadBarcode (Activity a){
        super(a);

    }

    public void read(String barcode_code) {
        barcode=barcode_code;

        super.start();
    }

    @Override
    protected void read_succesfull(){


        if(durum.equals("yenimasa")||durum.equals("eski")||durum.equals("yenioturum")){

           //connectionPc=new ConnectionPc();
         //   connectionPc.connect(get_activty());


        }

        ((CafeJoinActivity)a).start();

    }
    @Override
    protected void read_error(){


        ((CafeJoinActivity)a).start_error();


    }

}