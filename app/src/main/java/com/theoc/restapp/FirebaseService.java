package com.theoc.restapp.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.R;

public class FirebaseService extends FirebaseMessagingService {
    public FirebaseService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FIREBASE", "From: " + remoteMessage.getFrom());
        Log.d("FIREBASE", "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d("FIREBASE", "Data: " + remoteMessage.getData());
        showNotification(remoteMessage.getNotification().getBody());
    }

    public void showNotification(String message) {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, HomeActivity.class), 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Resturn")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("RestUpp")
                .setContentText(message)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
