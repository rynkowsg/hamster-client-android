package info.rynkowski.hamsterclient.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.ui.MainActivity;

public class NotificationsHelper {

    protected static final int ONGOING_NOTIFICATION = 1;

    private static final String TAG = NotificationsHelper.class.getName();

    private Context mContext;

    NotificationsHelper(Context context) {
        this.mContext = context;
    }

    // https://developer.android.com/guide/topics/ui/notifiers/notifications.html#SimpleNotification
    protected void setOngoingNotification(Class activity) {
        Log.d(TAG, "initNotification()");
        Intent clickNotification = new Intent(mContext, activity);
        PendingIntent resultPendingIntent = TaskStackBuilder.create(mContext)
                .addParentStack(MainActivity.class)
                .addNextIntent(clickNotification)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.hamster_time_tracker)
                .setContentTitle("Hamster Time Tracker")
                .setContentText("It works!")
                .setContentIntent(resultPendingIntent)
                .setOngoing(true)
                .build();
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ONGOING_NOTIFICATION, notification);
    }

    protected void cancelOngoingNotification() {
        Log.d(TAG, "cancelNotification()");
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ONGOING_NOTIFICATION);
    }
}
