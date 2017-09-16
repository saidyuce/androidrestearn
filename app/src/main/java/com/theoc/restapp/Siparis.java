package com.theoc.restapp.dataorganization.barcode;

/**
 * Created by saidyuce on 31.8.2016.
 */
public class Siparis {


    int urun_id;
    String tarih;
    int adet;
    String not;
    boolean prize_durumu;


    public Siparis(int i,int a,String t,boolean p,String not){



        urun_id=i;
        adet=a;
        tarih=t;
        prize_durumu=p;
        this.not=not;

    }







}
