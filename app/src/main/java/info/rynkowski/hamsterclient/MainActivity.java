package info.rynkowski.hamsterclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity {
    private class LocalServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mBoundService = binder.getService();

            Toast.makeText(getApplicationContext(), "LocalService connected",
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
            Toast.makeText(getApplicationContext(),  "LocalService disconnected",
                    Toast.LENGTH_SHORT).show();
        }
    };

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        doStartService();
        doBindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        doUnbindService();
        doStopService();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        mIsServiceBound = true;
    }

    void doUnbindService() {
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
        startService(serviceIntent);
    }

    private void doStopService() {
        doStopService(new Intent(this, LocalService.class));
    }
    private void doStopService(Intent serviceIntent) {
        stopService(serviceIntent);
    }

    private LocalService mBoundService = null;
    private LocalServiceConnection mServiceConnection = new LocalServiceConnection();
    private boolean mIsServiceBound;
}
