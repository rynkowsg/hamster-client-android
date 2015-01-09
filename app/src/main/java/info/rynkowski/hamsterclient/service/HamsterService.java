package info.rynkowski.hamsterclient.service;

import android.os.Message;
import android.preference.PreferenceManager;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.model.Fact;
import info.rynkowski.hamsterclient.ui.MainActivity;

public class HamsterService extends AbstractService {
    // messages to HamsterService
    public static final int MSG_NOTIFY = 1;
    public static final int MSG_TODAY_FACTS = 2;
    public static final int MSG_REFRESH = 3;
    public static final int MSG_ADD_FACT = 4;
    // messages from HamsterService
    public static final int MSG_EXCEPTION = 4;
    public static final int MSG_DBUS_EXCEPTION = 5;
    // signals
    public static final int SIGNAL_ACTIVITIES_CHANGED = 6;
    public static final int SIGNAL_FACTS_CHANGED = 7;
    public static final int SIGNAL_TAGS_CHANGED = 8;
    public static final int SIGNAL_TOGGLE_CHANGED = 9;

    private NotificationsHelper mNotificationsHelper;
    private DbusHelper mDbusHelper;

    //----------------  Overridden methods of AbstractService  -----------------------------------//
    @Override
    public void onStartService() {
        mNotificationsHelper = new NotificationsHelper(this);
        mNotificationsHelper.setOngoingNotification(MainActivity.class);
        mDbusHelper = new DbusHelper(this);
        mDbusHelper.openConnection();
        PreferenceManager.setDefaultValues(HamsterService.this, R.xml.preferences, false);
    }

    @Override
    public void onStopService() {
        mDbusHelper.closeConnection();
        mNotificationsHelper.cancelOngoingNotification();
    }

    @Override
    public void onReceiveMessage(Message msg) {
        switch (msg.what) {
            case (MSG_NOTIFY):
                mDbusHelper.dbusNotify();
                break;
            case (MSG_TODAY_FACTS):
                mDbusHelper.getTodaysFacts();
                break;
            case (MSG_REFRESH):
                break;
            case (MSG_ADD_FACT):
                mDbusHelper.addFact((Fact) msg.obj);
                break;
            default:
                break;
        }
    }
}
