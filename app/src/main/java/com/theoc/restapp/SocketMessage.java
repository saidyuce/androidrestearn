package com.theoc.restapp.dataorganization;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketMessage {

    Activity a;
    public SocketMessage(Activity a){
        this.a=a;
    }
    public void set_Activity(Activity a){
        this.a=a;
    }

    Socket mSocket;

    public  void  oturum_Ac(String cafe_durum,String cafe_id,String user_id){

        JSONObject sending=new JSONObject();
        try {
            sending.put("type","onur1234");
            sending.put("cafe_durum",cafe_durum);
            sending.put("cafe_id",cafe_id);
            sending.put("user_id",user_id);

            try {
                mSocket = IO.socket("http://restearnserver.com:65080");
            } catch (URISyntaxException e) {}



            mSocket.connect();
            mSocket.emit("kullanici_oturum",sending.toString());


            mSocket.on(GeneralSync.id+"_siparis_geri_bildirim", siparis_geri_Bildirim);
            mSocket.on(GeneralSync.id+"_oturum_geri_bildirim", oturum_geri_Bildirim);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public  void  siparis_ver(String cafe_id,String user_id){

        JSONObject sending=new JSONObject();
        try {
            sending.put("type","onur1234siparis");
            sending.put("cafe_id",cafe_id);
            sending.put("user_id",user_id);

            if (mSocket==null){
                try {
                    mSocket = IO.socket("http://restearnserver.com:65080");
                    mSocket.connect();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }





            mSocket.emit("kullanici_oturum",sending.toString());
            Log.v("SOCKET=", sending.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void email_user(JSONObject json) {
        if (mSocket == null) {
            try {
                Log.v("SOCKET CONNECT=", "DONE");
                mSocket = IO.socket("http://130.211.92.93:65080");
                mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                mSocket.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        mSocket.emit("email_user", json.toString());
        Log.v("SOCKET=", json.toString());
    }

    public void push_token_gonder(String user_id, String push_token) {
        JSONObject sending = new JSONObject();
        try {
            sending.put("user_id", user_id);
            sending.put("push_token", push_token);

            if (mSocket == null) {
                mSocket = IO.socket("http://130.211.92.93:65080");
                mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                mSocket.connect();
            }

            mSocket.emit("push_token",sending.toString());
            Log.d("SOCKET=", sending.toString());
            Log.d("PUSH", "PUSH TOKEN GÖNDERİLDİ");
        } catch (URISyntaxException | JSONException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mSocket != null)
                        if (!mSocket.connected())
                            Log.v("SOCKET STATUS=", "NOT CONNECTED");
                    mSocket.connect();

                }
            });
        }
    };

    private io.socket.emitter.Emitter.Listener siparis_geri_Bildirim = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
                   a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {

                        Log.e("soket_sipariş:",data.getString("cafe_id")+"-"+
                                data.getString("temp_key")+"-"+
                                data.getString("user_id")+"-"+
                                data.getString("cafe_kul_id")+"-"+
                                data.getString("not")+"-"+
                                data.getString("temp_point")+"-");
                        Toast.makeText(GeneralSync.currentactivity, "Sipariş onaylandı", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        return;
                    }



                }
            });
        }

    };

    private io.socket.emitter.Emitter.Listener oturum_geri_Bildirim = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];


                 try {
               /*      Toast.makeText(a,data.getString("oturum_tarih")+"-"+
                                     data.getString("masa_no")+"-"+
                                     data.getString("kullanilan_puan")+"-"+
                                     data.getString("kazanilan_puan")+"-"+
                                     data.getString("odeme_tipi")+"-"
                             ,Toast.LENGTH_LONG);
                             */
                     Log.e("soket:",data.getString("oturum_tarih")+"-"+
                             data.getString("masa_no")+"-"+
                             data.getString("kullanilan_puan")+"-"+
                             data.getString("kazanilan_puan")+"-"+
                             data.getString("odeme_tipi")+"-");
                     Toast.makeText(GeneralSync.currentactivity, "Oturum kapatıldı", Toast.LENGTH_LONG).show();
                 } catch (JSONException e) {
                        return;
                    }



                }
            });
        }

    };






}
