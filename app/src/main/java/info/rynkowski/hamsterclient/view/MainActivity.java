package info.rynkowski.hamsterclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.freedesktop.dbus.exceptions.DBusException;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.service.HamsterService;


public class MainActivity extends ActionBarActivity implements InterfaceMainActivity, NavigationDrawerFragment.Listener {
    public static final int PICK_FACT_DATA = 1;
    private static final String TAG = "MainActivity";
    private ServiceManager service;
    private MainActivityHelper helper;
    private Fragment fragment;

    Toolbar toolbar;
    NavigationDrawerFragment drawerFragment;

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

        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(toolbar);

//        // enabling action bar app icon and behaving it as toggle button
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            onNavDrawerItemSelected(0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        service.start();
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
        service.unbind();
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
        if (drawerFragment.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
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

    //----------------  other Activity' methods  -------------------------------------------------//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(), requestCode = " + requestCode + ", resultCode = " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    // InterfaceMainActivity methods - fragments use those methods to communicate with MainActivity
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

    @Override
    public Result onNavDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        fragment = null;
        switch (position) {
            case 0:
                fragment = new TestFragment();
                break;
            case 1:
                fragment = new HomeFragment();
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                String msg = "Fragment have not implemented yet.";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            default:
                Log.e(TAG, "Error in creating fragmentl;");
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
            return Result.SUCCESS;
        } else {
            // error in creating fragment
            Log.e(TAG, "Error in creating fragment");
            return Result.FAILED;
        }
    }
}
