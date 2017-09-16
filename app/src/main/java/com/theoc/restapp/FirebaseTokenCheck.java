package com.theoc.restapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.theoc.restapp.CafeJoinActivity;
import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.LoginActivity;
import com.theoc.restapp.dataorganization.GeneralSync;

import java.io.IOException;

public class FirebaseTokenCheck extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FIREBASEE", "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (preferences.getBoolean("notification", true)) {
            FirebaseMessaging.getInstance().subscribeToTopic("/topics/campaign");
        }
    }

    private void sendRegistrationToServer(String token) {
        if (GeneralSync.id == 0) {
            try {
                Log.d("PUSH", "ID 0 GET TOKEN AGAIN");
                FirebaseInstanceId.getInstance().deleteInstanceId();
                FirebaseInstanceId.getInstance().getToken();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("PUSH", "TOKEN GONDERİLDİ");
            LoginActivity.socket_message.push_token_gonder(GeneralSync.id + "", token);
        }
    }
}
