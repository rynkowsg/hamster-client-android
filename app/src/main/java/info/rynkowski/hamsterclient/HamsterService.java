package info.rynkowski.hamsterclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import org.freedesktop.Notifications;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
import org.gnome.Hamster;
import org.gnome.Struct5;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HamsterService extends AbstractService {
    private final String TAG = "HamsterService";
    private static final int NOTIFICATION_ID = 1;
    private DBusConnection dBusConnection = null;

    // messages to HamsterService
    static final int MSG_NOTIFY = 2;
    static final int MSG_TODAY_FACTS = 3;
    // messages from HamsterService
    static final int MSG_EXCEPTION = 4;
    static final int MSG_DBUS_EXCEPTION = 5;

    public HamsterService() {
        Log.d(TAG, "HamsterService()");
    }

    //----------------  Overridden methods of AbstractService  -----------------------------------//
    @Override
    public void onStartService() {
        Log.d(TAG, "onStartService()");
        initNotification();
        openDbusConnection();
    }

    @Override
    public void onStopService() {
        Log.d(TAG, "onDestroy()");
        closeDbusConnection();
        cancelNotification();
    }

    @Override
    public void onReceiveMessage(Message msg) {
        switch (msg.what) {
            case (MSG_NOTIFY):
                dbusNotify();
                break;
            case (MSG_TODAY_FACTS):
                getTodaysFacts();
                break;
            default:
                break;
        }
    }

    //----------------  Android Notifications  ---------------------------------------------------//
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

    //----------------  DBUS connection  ---------------------------------------------------------//
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
        Log.i(TAG, "Before get dbus connection");
        long startTime = System.currentTimeMillis();
        try {
            dBusConnection = DBusConnection.getConnection("tcp:host=10.0.0.101,port=55555");
        } catch (DBusException e) {
            e.printStackTrace();
            Log.i(TAG, "dBusConnection is not established, dbusConnection = " + dBusConnection);
            dBusConnection = null;
            send(Message.obtain(null, MSG_EXCEPTION, e));
        }
        long difference = System.currentTimeMillis() - startTime;
        Log.i(TAG, "After get dbus connection: it takes " + difference/1000 + " seconds");
    }

    private void closeDbusConnection() {
        Log.d(TAG, "closeDbusConnection()");
        dBusConnection = null;
    }

    //---------------- DBUS - operations on the Notifications object  ----------------------------//
    public void dbusNotify() {
        dbusNotify("", "", "Message1", "Message 2");
    }

    public void dbusNotify(final String... messages) {
        Log.d(TAG, "dbusNotify()");
        if (dBusConnection != null) {
            Log.d(TAG, "dBusConnection != null");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Notifications notify = (Notifications) dBusConnection.getRemoteObject("org.freedesktop.Notifications", "/org/freedesktop/Notifications", Notifications.class);
                        Log.i(TAG, "notify = " + notify);
                        Map<String, Variant<Byte>> hints = new HashMap<String, Variant<Byte>>();
                        hints.put("urgency", new Variant<Byte>((byte) 0));
                        notify.Notify(messages[0], new UInt32(0), messages[1], messages[2], messages[3], new LinkedList<String>(), hints, 5);
                    } catch (DBusException e) {
                        e.printStackTrace();
                        send(Message.obtain(null, MSG_DBUS_EXCEPTION, e));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        send(Message.obtain(null, MSG_EXCEPTION, e));
                    }
                }
            }).start();
        } else {
            Log.d(TAG, "dBusConnection == null");
        }
    }

    //---------------- DBUS - operations on the Hamster object  ----------------------------------//
    public void getTodaysFacts() {
        Log.d(TAG, "dbusNotify()");
        if (dBusConnection != null) {
            Log.d(TAG, "dBusConnection != null");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Hamster hamster = (Hamster) dBusConnection.getRemoteObject("org.gnome.Hamster", "/org/gnome/Hamster", Hamster.class);
                        List<Struct5> lista = hamster.GetTodaysFacts();
                        send(Message.obtain(null, MSG_TODAY_FACTS, lista));
                    } catch (DBusException e) {
                        e.printStackTrace();
                        send(Message.obtain(null, MSG_DBUS_EXCEPTION, e));
                    }
                }
            }).start();
        } else {
            Log.d(TAG, "dBusConnection == null");
        }
    }
}
