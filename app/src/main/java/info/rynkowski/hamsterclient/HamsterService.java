package info.rynkowski.hamsterclient;

import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import org.freedesktop.Notifications;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusSigHandler;
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

    private NotificationsHelper notificationsHelper;
    private DBusConnection dBusConnection = null;

    // messages to HamsterService
    static final int MSG_NOTIFY = 1;
    static final int MSG_TODAY_FACTS = 2;
    static final int MSG_REFRESH = 3;
    // messages from HamsterService
    static final int MSG_EXCEPTION = 4;
    static final int MSG_DBUS_EXCEPTION = 5;
    // signals
    static final int SIGNAL_ACTIVITIES_CHANGED = 6;
    static final int SIGNAL_FACTS_CHANGED = 7;
    static final int SIGNAL_TAGS_CHANGED = 8;
    static final int SIGNAL_TOGGLE_CHANGED = 9;

    public HamsterService() {
        Log.d(TAG, "HamsterService()");
    }

    //----------------  Overridden methods of AbstractService  -----------------------------------//
    @Override
    public void onStartService() {
        Log.d(TAG, "onStartService()");
        notificationsHelper = new NotificationsHelper(this);
        notificationsHelper.setOngoingNotification(MainActivity.class);
        openDbusConnection();
        PreferenceManager.setDefaultValues(HamsterService.this, R.xml.preferences, false);
    }

    @Override
    public void onStopService() {
        Log.d(TAG, "onDestroy()");
        closeDbusConnection();
        notificationsHelper.cancelOngoingNotification();
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
            case (MSG_REFRESH):
                break;
            default:
                break;
        }
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
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HamsterService.this);
            String host = prefs.getString("host", getResources().getString(R.string.host));
            String port = prefs.getString("port", getResources().getString(R.string.port));
            String dbus_address = "tcp:host=" + host + ",port=" + port;
            dBusConnection = DBusConnection.getConnection(dbus_address);
            registerSignals();
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
//    class LocalDBusSigHandler<T extends DBusSignal> implements DBusSigHandler<T> {
//        @Override
//        public void handle(T msg) {
//            if(msg.getName() == "tags-changed");
//            else if(msg.getName() == "facts-changed");
//            else if(msg.getName() == "activities-changed");
//            else if(msg.getName() == "toggle-called");
//            else;
//        }
//    }

    public void registerSignals() {
        Log.d(TAG, "registerSignals()");
        if (dBusConnection != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Hamster hamster = dBusConnection.getRemoteObject("org.gnome.Hamster", "/org/gnome/Hamster", Hamster.class);
                        dBusConnection.addSigHandler(Hamster.ActivitiesChanged.class, new DBusSigHandler<Hamster.ActivitiesChanged>() {
                            @Override
                            public void handle(Hamster.ActivitiesChanged s) {
                                send(Message.obtain(null, SIGNAL_ACTIVITIES_CHANGED));
                            }
                        });
                        dBusConnection.addSigHandler(Hamster.FactsChanged.class, new DBusSigHandler<Hamster.FactsChanged>() {
                            @Override
                            public void handle(Hamster.FactsChanged s) {
                                send(Message.obtain(null, SIGNAL_FACTS_CHANGED));
                                getTodaysFacts();
                            }
                        });
                        dBusConnection.addSigHandler(Hamster.TagsChanged.class, new DBusSigHandler<Hamster.TagsChanged>() {
                            @Override
                            public void handle(Hamster.TagsChanged s) {
                                send(Message.obtain(null, SIGNAL_TAGS_CHANGED));
                            }
                        });
                        dBusConnection.addSigHandler(Hamster.ToggleCalled.class, new DBusSigHandler<Hamster.ToggleCalled>() {
                            @Override
                            public void handle(Hamster.ToggleCalled s) {
                                send(Message.obtain(null, SIGNAL_TOGGLE_CHANGED));
                            }
                        });
//                        dBusConnection.addSigHandler(Hamster.ActivitiesChanged.class, new LocalDBusSigHandler<Hamster.ActivitiesChanged>());
//                        dBusConnection.addSigHandler(Hamster.FactsChanged.class, new LocalDBusSigHandler<Hamster.FactsChanged>());
//                        dBusConnection.addSigHandler(Hamster.TagsChanged.class, new LocalDBusSigHandler<Hamster.TagsChanged>());
//                        dBusConnection.addSigHandler(Hamster.ToggleCalled.class, new LocalDBusSigHandler<Hamster.ToggleCalled>());
                    } catch (DBusException e) {
                        e.printStackTrace();
                        send(Message.obtain(null, MSG_DBUS_EXCEPTION, e));
                    }
                }
            }).start();
        }
    }

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
