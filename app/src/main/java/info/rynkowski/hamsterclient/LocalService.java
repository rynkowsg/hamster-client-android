package info.rynkowski.hamsterclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

public class LocalService extends Service {
    public class LocalBinder extends Binder {
        LocalService getService() {
            Log.d(TAG, "LocalBinder.getService()");
            return LocalService.this;
        }
    }

    public LocalService() {
        Log.d(TAG, "LocalService()");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
        initNotification();
        openDbusConnection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand()");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        closeDbusConnection();
        cancelNotification();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return mBinder;
    }

    // https://developer.android.com/guide/topics/ui/notifiers/notifications.html#SimpleNotification
    private void initNotification() {
        Log.d(TAG, "initNotification()");
        Intent clickNotification = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = TaskStackBuilder.create(this)
                .addParentStack(MainActivity.class)
                .addNextIntent(clickNotification)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.hamster_time_tracker)
                .setContentTitle("Hamster Time Tracker")
                .setContentText("It works!")
                .setContentIntent(resultPendingIntent)
                .setOngoing(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void cancelNotification() {
        Log.d(TAG, "cancelNotification()");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private void openDbusConnection() {
        Log.d(TAG, "openDbusConnection()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                openDbusConnectionInside();
            }
        }).start();
    }

    private void openDbusConnectionInside() {
        try {
            dBusConnection = DBusConnection.getConnection("tcp:host=192.168.1.3,port=55555");
        } catch (DBusException e) {
            e.printStackTrace();
            throw new RuntimeException();
            // TODO: send information to activity
            // http://android-coding.blogspot.in/2011/11/pass-data-from-service-to-activity.html
        }
    }

    private void closeDbusConnection() {
        Log.d(TAG, "closeDbusConnection()");
        dBusConnection = null;
    }

    private final String TAG = this.getClass().getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    private static final int NOTIFICATION_ID = 1;
    private DBusConnection dBusConnection = null;
}
