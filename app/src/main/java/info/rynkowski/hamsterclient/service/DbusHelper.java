package info.rynkowski.hamsterclient.service;

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

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.model.Fact;

public class DbusHelper {
    private static final String TAG = DbusHelper.class.getName();
    HamsterService mHamsterService;
    private DBusConnection mDbusConnection = null;

    DbusHelper(HamsterService hamsterService) {
        this.mHamsterService = hamsterService;
    }

    //----------------  DBUS connection  ---------------------------------------------------------//
    protected void openConnection() {
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
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mHamsterService);
            String host = prefs.getString("host", mHamsterService.getResources().getString(R.string.host));
            String port = prefs.getString("port", mHamsterService.getResources().getString(R.string.port));
            String dbus_address = "tcp:host=" + host + ",port=" + port;
            mDbusConnection = DBusConnection.getConnection(dbus_address);
            registerSignals();
        } catch (DBusException e) {
            e.printStackTrace();
            Log.i(TAG, "dBusConnection is not established, mDbusConnection = " + mDbusConnection);
            mDbusConnection = null;
            mHamsterService.send(Message.obtain(null, mHamsterService.MSG_EXCEPTION, e));
        }
        long difference = System.currentTimeMillis() - startTime;
        Log.i(TAG, "After get dbus connection: it takes " + difference / 1000 + " seconds");
    }

    protected void closeConnection() {
        Log.d(TAG, "closeDbusConnection()");
        mDbusConnection = null;
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

    private void registerSignals() {
        Log.d(TAG, "registerSignals()");
        if (mDbusConnection != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Hamster hamster = mDbusConnection.getRemoteObject("org.gnome.Hamster", "/org/gnome/Hamster", Hamster.class);
                        mDbusConnection.addSigHandler(Hamster.ActivitiesChanged.class, new DBusSigHandler<Hamster.ActivitiesChanged>() {
                            @Override
                            public void handle(Hamster.ActivitiesChanged s) {
                                mHamsterService.send(Message.obtain(null, HamsterService.SIGNAL_ACTIVITIES_CHANGED));
                            }
                        });
                        mDbusConnection.addSigHandler(Hamster.FactsChanged.class, new DBusSigHandler<Hamster.FactsChanged>() {
                            @Override
                            public void handle(Hamster.FactsChanged s) {
                                mHamsterService.send(Message.obtain(null, HamsterService.SIGNAL_FACTS_CHANGED));
                                getTodaysFacts();
                            }
                        });
                        mDbusConnection.addSigHandler(Hamster.TagsChanged.class, new DBusSigHandler<Hamster.TagsChanged>() {
                            @Override
                            public void handle(Hamster.TagsChanged s) {
                                mHamsterService.send(Message.obtain(null, HamsterService.SIGNAL_TAGS_CHANGED));
                            }
                        });
                        mDbusConnection.addSigHandler(Hamster.ToggleCalled.class, new DBusSigHandler<Hamster.ToggleCalled>() {
                            @Override
                            public void handle(Hamster.ToggleCalled s) {
                                mHamsterService.send(Message.obtain(null, HamsterService.SIGNAL_TOGGLE_CHANGED));
                            }
                        });
//                        dBusConnection.addSigHandler(Hamster.ActivitiesChanged.class, new LocalDBusSigHandler<Hamster.ActivitiesChanged>());
//                        dBusConnection.addSigHandler(Hamster.FactsChanged.class, new LocalDBusSigHandler<Hamster.FactsChanged>());
//                        dBusConnection.addSigHandler(Hamster.TagsChanged.class, new LocalDBusSigHandler<Hamster.TagsChanged>());
//                        dBusConnection.addSigHandler(Hamster.ToggleCalled.class, new LocalDBusSigHandler<Hamster.ToggleCalled>());
                    } catch (DBusException e) {
                        e.printStackTrace();
                        mHamsterService.send(Message.obtain(null, HamsterService.MSG_DBUS_EXCEPTION, e));
                    }
                }
            }).start();
        }
    }

    //---------------- DBUS - operations on the Notifications object  ----------------------------//
    protected void dbusNotify() {
        dbusNotify("", "", "Message1", "Message 2");
    }

    protected void dbusNotify(final String... messages) {
        Log.d(TAG, "dbusNotify()");
        if (mDbusConnection != null) {
            Log.d(TAG, "mDbusConnection != null");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Notifications notify = (Notifications) mDbusConnection.getRemoteObject("org.freedesktop.Notifications", "/org/freedesktop/Notifications", Notifications.class);
                        Log.i(TAG, "notify = " + notify);
                        Map<String, Variant<Byte>> hints = new HashMap<String, Variant<Byte>>();
                        hints.put("urgency", new Variant<Byte>((byte) 0));
                        notify.Notify(messages[0], new UInt32(0), messages[1], messages[2], messages[3], new LinkedList<String>(), hints, 5);
                    } catch (DBusException e) {
                        e.printStackTrace();
                        mHamsterService.send(Message.obtain(null, HamsterService.MSG_DBUS_EXCEPTION, e));
                    } catch (Exception e) {
                        e.printStackTrace();
                        mHamsterService.send(Message.obtain(null, HamsterService.MSG_EXCEPTION, e));
                    }
                }
            }).start();
        } else {
            Log.d(TAG, "mDbusConnection == null");
        }
    }

    protected void getTodaysFacts() {
        Log.d(TAG, "dbusNotify()");
        if (mDbusConnection != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Hamster hamster = (Hamster) mDbusConnection.getRemoteObject("org.gnome.Hamster", "/org/gnome/Hamster", Hamster.class);
                        List<Struct5> lista = hamster.GetTodaysFacts();
                        mHamsterService.send(Message.obtain(null, HamsterService.MSG_TODAY_FACTS, lista));
                    } catch (DBusException e) {
                        e.printStackTrace();
                        mHamsterService.send(Message.obtain(null, HamsterService.MSG_DBUS_EXCEPTION, e));
                    }
                }
            }).start();
        } else {
            Log.d(TAG, "mDbusConnection == null");
        }
    }

    protected void addFact(final Fact fact) {
        if (mDbusConnection != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Hamster hamster = (Hamster) mDbusConnection.getRemoteObject("org.gnome.Hamster", "/org/gnome/Hamster", Hamster.class);
                        hamster.AddFact(fact.serialized_name(), fact.getStartTime(), fact.getEndTime(), false);
                    } catch (DBusException e) {
                        e.printStackTrace();
                        mHamsterService.send(Message.obtain(null, HamsterService.MSG_DBUS_EXCEPTION, e));
                    }
                }
            }).start();
        } else {
            Log.d(TAG, "mDbusConnection == null");
        }
    }
}
