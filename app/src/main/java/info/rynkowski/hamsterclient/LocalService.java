package info.rynkowski.hamsterclient;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

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
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return mBinder;
    }

    private final String TAG = this.getClass().getSimpleName();
    private final IBinder mBinder = new LocalBinder();
}
