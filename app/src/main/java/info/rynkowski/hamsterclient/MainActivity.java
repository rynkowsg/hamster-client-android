package info.rynkowski.hamsterclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {
    private class LocalServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "LocalServiceConnection.onServiceConnected()");
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mBoundService = binder.getService();

            Toast.makeText(getApplicationContext(), "LocalService connected",
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, "LocalServiceConnection.onServiceDisconnected()");
            mBoundService = null;
            Toast.makeText(getApplicationContext(),  "LocalService disconnected",
                    Toast.LENGTH_SHORT).show();
        }
    };

    public MainActivity() {
        Log.d(TAG, "MainActivity()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        doStartService();
        doBindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState()");
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()");
        doUnbindService();
        //doStopService();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void doBindService() {
        doBindService(new Intent(this, LocalService.class));
    }
    void doBindService(Intent serviceIntent) {
        Log.d(TAG, "doBindService()");
        bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        mIsServiceBound = true;
    }

    void doUnbindService() {
        Log.d(TAG, "doUnbindService()");
        if (mIsServiceBound) {
            // Detach our existing connection.
            unbindService(mServiceConnection);
            mIsServiceBound = false;
        }
    }

    private void doStartService() {
        doStartService(new Intent(this, LocalService.class));
    }
    private void doStartService(Intent serviceIntent) {
        Log.d(TAG, "doStartService()");
        startService(serviceIntent);
    }

    private void doStopService() {
        doStopService(new Intent(this, LocalService.class));
    }
    private void doStopService(Intent serviceIntent) {
        Log.d(TAG, "doStopService()");
        stopService(serviceIntent);
    }

    // button methods
    public void bStartService(View view) {
        doStartService();
        doBindService();
    }
    public void bStopService(View view) {
        doUnbindService();
        doStopService();
    }

    private final String TAG = this.getClass().getSimpleName();
    private LocalService mBoundService = null;
    private LocalServiceConnection mServiceConnection = new LocalServiceConnection();
    private boolean mIsServiceBound;
}
