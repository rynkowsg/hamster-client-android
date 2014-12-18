package info.rynkowski.hamsterclient.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import info.rynkowski.hamsterclient.MainActivity;
import info.rynkowski.hamsterclient.R;

public class NotificationsHelper {
    public static final int ONGOING_NOTIFICATION = 1;

    private static final String TAG = "NotificationManager";
    private Context context;

    NotificationsHelper(Context context) {
        this.context = context;
    }

    // https://developer.android.com/guide/topics/ui/notifiers/notifications.html#SimpleNotification
    public void setOngoingNotification(Class activity) {
        Log.d(TAG, "initNotification()");
        Intent clickNotification = new Intent(context, activity);
        PendingIntent resultPendingIntent = TaskStackBuilder.create(context)
                .addParentStack(MainActivity.class)
                .addNextIntent(clickNotification)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.hamster_time_tracker)
                .setContentTitle("Hamster Time Tracker")
                .setContentText("It works!")
                .setContentIntent(resultPendingIntent)
                .setOngoing(true)
                .build();
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ONGOING_NOTIFICATION, notification);
    }

    public void cancelOngoingNotification() {
        Log.d(TAG, "cancelNotification()");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ONGOING_NOTIFICATION);
    }
}
