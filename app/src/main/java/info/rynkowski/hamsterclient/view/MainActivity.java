package info.rynkowski.hamsterclient.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.freedesktop.dbus.exceptions.DBusException;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.service.HamsterService;


public class MainActivity extends Activity implements InterfaceMainActivity {
    public static final int PICK_FACT_DATA = 1;
    private final String TAG = "MainActivity";
    private ServiceManager service;
    private MainActivityHelper helper;
    private Fragment fragment;

    //----------------  Message handling and sending  --------------------------------------------//
    private class LocalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HamsterService.MSG_EXCEPTION:
                    helper.showExceptionDialog((Exception) msg.obj);
                    break;
                case HamsterService.MSG_DBUS_EXCEPTION:
                    helper.showExceptionDialog((DBusException) msg.obj);
                    break;
                case HamsterService.SIGNAL_ACTIVITIES_CHANGED:
                    Toast.makeText(getApplicationContext(), "SIGNAL_ACTIVITIES_CHANGED", Toast.LENGTH_LONG).show();
                    break;
                case HamsterService.SIGNAL_FACTS_CHANGED:
                    Toast.makeText(getApplicationContext(), "SIGNAL_FACTS_CHANGED", Toast.LENGTH_LONG).show();
                    break;
                case HamsterService.SIGNAL_TAGS_CHANGED:
                    Toast.makeText(getApplicationContext(), "SIGNAL_TAGS_CHANGED", Toast.LENGTH_LONG).show();
                    break;
                case HamsterService.SIGNAL_TOGGLE_CHANGED:
                    Toast.makeText(getApplicationContext(), "SIGNAL_TOGGLE_CHANGED", Toast.LENGTH_LONG).show();
                    break;
                default:
                    if (fragment != null && fragment instanceof InterfaceFragment) {
                        Handler handler = ((InterfaceFragment) fragment).getHandler();
                        if (handler != null)
                            handler.handleMessage(msg);
                    }
                    super.handleMessage(msg);
            }
        }
    }

    //----------------  Activity' lifecycle methods  ---------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        this.service = new ServiceManager(MainActivity.this, HamsterService.class, new LocalHandler());
        this.helper = new MainActivityHelper(MainActivity.this);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
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
        service.start();
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
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        service.unbind();
        super.onDestroy();
    }

    //----------------  other Activity' methods  -------------------------------------------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.action_add_fact:
                helper.runAddFactActivity();
                return true;
            case R.id.action_settings:
                helper.runSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(), requestCode = " + requestCode + ", resultCode = " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    // InterfaceMainFragment - to communicate with MainFragment
    @Override
    public void startService() {
        service.start();
    }

    @Override
    public void stopService() {
        service.stop();
    }

    @Override
    public void sendRequestToService(int what) {
        sendMessageToService(Message.obtain(null, what));
    }

    private void sendMessageToService(Message msg) {
        try {
            service.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
            helper.showExceptionDialog((RemoteException) msg.obj);
        }
    }

    /**
     * Diplaying fragment view
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        fragment = null;
        switch (position) {
            case 0:
                fragment = new MainFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
        } else {
            // error in creating fragment
            Log.e(TAG, "Error in creating fragment");
        }
    }
}
