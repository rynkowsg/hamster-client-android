package info.rynkowski.hamsterclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.freedesktop.dbus.exceptions.DBusException;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.service.HamsterService;


public class MainActivity extends BaseActivity
        implements IMainActivity {
    public static final int PICK_FACT_DATA = 1;

    private static final String TAG = MainActivity.class.getName();

    private ServiceManager mServiceManger;
    private MainActivityHelper mHelper;

    //----------------  Activity' lifecycle methods  ---------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        this.mServiceManger = new ServiceManager(MainActivity.this, HamsterService.class, new LocalHandler());
        this.mHelper = new MainActivityHelper(MainActivity.this);

        // enabling action bar app icon and behaving it as toggle button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            onDrawerItemClick(getResources().getInteger(R.integer.navdrawer_default_pick));
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d(TAG, "onPostCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        mServiceManger.start();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState()");
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
        mServiceManger.unbind();
        super.onDestroy();
    }

    //----------------  Menu related methods  ----------------------------------------------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected(), item.getItemId() = " + item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add_fact:
                mHelper.runAddFactActivity();
                return true;
            case R.id.action_settings:
                mHelper.runSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //----------------  other Activity' methods  -------------------------------------------------//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(), requestCode = " + requestCode + ", resultCode = " + resultCode);
        switch (requestCode) {
            case (MainActivity.PICK_FACT_DATA):
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "New fact data received", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // IMainActivity methods - fragments use those methods to communicate with MainActivity
    @Override
    public void startService() {
        mServiceManger.start();
    }

    @Override
    public void stopService() {
        mServiceManger.stop();
    }

    @Override
    public void sendRequestToService(int what) {
        sendMessageToService(Message.obtain(null, what));
    }

    private void sendMessageToService(Message msg) {
        try {
            mServiceManger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
            mHelper.showExceptionDialog((RemoteException) msg.obj);
        }
    }

    //----------------  Message handling and sending  --------------------------------------------//
    private class LocalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage()");
            switch (msg.what) {
                case HamsterService.MSG_EXCEPTION:
                    mHelper.showExceptionDialog((Exception) msg.obj);
                    break;
                case HamsterService.MSG_DBUS_EXCEPTION:
                    mHelper.showExceptionDialog((DBusException) msg.obj);
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
                    Fragment currentFragment = getCurrentFragment();
                    Log.i(TAG, "currentFragment = " + currentFragment);
                    if (currentFragment != null && currentFragment instanceof IFragment) {
                        Handler handler = ((IFragment) currentFragment).getHandler();
                        if (handler != null) {
                            Log.i(TAG, "handler != null");
                            handler.handleMessage(msg);
                        } else {
                            Log.i(TAG, "handler == null");
                        }
                    }
                    super.handleMessage(msg);
            }
        }
    }
}
