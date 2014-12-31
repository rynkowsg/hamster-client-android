package info.rynkowski.hamsterclient.ui;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import info.rynkowski.hamsterclient.service.AbstractService;

public class ServiceManager {
    private static final String TAG = "ServiceManager";
    private Context mActivity;
    private Handler mActivityHandler;
    private Class<? extends AbstractService> mServiceClass;
    private LocalServiceConnection mConnection;
    private boolean mIsBound;
    private Messenger mServiceMessenger;
    private Messenger mLocalMessenger;


    private class LocalHandler extends Handler {
        private static final String TAG = "ServiceManagerHandler";

        @Override
        public void handleMessage(Message msg) {
            if (mActivityHandler != null) {
                Log.i(TAG, "Incoming message. Passing to handler: " + msg);
                mActivityHandler.handleMessage(msg);
            }
        }
    }

    private class LocalServiceConnection implements ServiceConnection {
        private static final String TAG = "LocalServiceConnection";

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            mServiceMessenger = new Messenger(binder);
            Log.i(TAG, "Connected.");
            registerServiceClient();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mServiceMessenger = null;
            Log.i(TAG, "Disconnected.");
        }
    }

    private void doStartService() {
        mActivity.startService(new Intent(mActivity, mServiceClass));
    }

    private void doStopService() {
        mActivity.stopService(new Intent(mActivity, mServiceClass));
    }

    private void doBindService() {
        mActivity.bindService(new Intent(mActivity, mServiceClass), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister.
            if (mServiceMessenger != null) {
                unregisterServiceClient();
            }

            // Detach our existing connection.
            mActivity.unbindService(mConnection);
            mIsBound = false;
            Log.i(TAG, "Unbound.");
        }
    }

    private void registerServiceClient() {
        try {
            Message msg = Message.obtain(null, AbstractService.MSG_REGISTER_CLIENT);
            msg.replyTo = mLocalMessenger;
            mServiceMessenger.send(msg);
        } catch (RemoteException e) {
            // In this case the service has crashed before we could even do anything with it
            e.printStackTrace();
        }
    }

    private void unregisterServiceClient() {
        try {
            Message msg = Message.obtain(null, AbstractService.MSG_UNREGISTER_CLIENT);
            msg.replyTo = mLocalMessenger;
            mServiceMessenger.send(msg);
        } catch (RemoteException e) {
            // There is nothing special we need to do if the service has crashed.
            e.printStackTrace();
        }
    }

    public ServiceManager(Context context, Class<? extends AbstractService> serviceClass, Handler activityHandler) {
        this.mActivity = context;
        this.mActivityHandler = activityHandler;
        this.mServiceClass = serviceClass;
        this.mConnection = new LocalServiceConnection();
        this.mIsBound = false;
        this.mServiceMessenger = null;
        this.mLocalMessenger = new Messenger(new LocalHandler());


        if (isRunning()) {
            doBindService();
        }
    }

    public void start() {
        doStartService();
        doBindService();
    }

    public void stop() {
        doUnbindService();
        doStopService();
    }

    /**
     * Use with caution (only in Activity.onDestroy())!
     */
    public void unbind() {
        doUnbindService();
    }

    public boolean isRunning() {
        ActivityManager manager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (mServiceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void send(Message msg) throws RemoteException {
        if (mIsBound) {
            if (mServiceMessenger != null) {
                mServiceMessenger.send(msg);
            }
        }
    }
}
