package com.theoc.restapp.dataorganization.barcode;

/**
 * Created by saidyuce on 4.9.2016.
 *
 *
 *
 */
import android.app.Activity;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ConnectionPc {

    Socket mSocket;
Activity activity;
         public void  connect(Activity a){
         activity=a;

             try {
                 mSocket = IO.socket("http://130.211.92.93:65080");
             } catch (URISyntaxException e) {}


             mSocket.on("new message", onNewMessage);
             mSocket.connect();



           //  mSocket.emit("new message", "sadasdasd");







         }




    private io.socket.emitter.Emitter.Listener onNewMessage = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view

                    Toast.makeText(activity,username+"-"+message,Toast.LENGTH_LONG);
                }
            });
        }

    };
















}
