package com.example.usermanagement.module;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationHalper {
    public static void displayNotification(Context context, String title, String body) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CreateEmployee.CHANNEL_ID)
                       // .setSmallIcon(R.drawable.ic_email)
                        // hard coded
                       /* .setContentTitle("hurrey! it is working ")
                          .setContentText("your first notification");*/
                          .setContentTitle(title)
                          .setContentText(body);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(4,mBuilder.build());
    }
}
