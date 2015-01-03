package info.rynkowski.hamsterclient.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.freedesktop.dbus.exceptions.DBusException;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.service.HamsterService;


public class MainActivity extends BaseActivity
        implements IMainActivity, NavDrawerFragment.OnItemClickListener {
    public static final int PICK_FACT_DATA = 1;

    private static final String TAG = MainActivity.class.getName();

    private ServiceManager mServiceManger;
    private MainActivityHelper mHelper;

    private Fragment mFragment;
    private DrawerLayout mDrawerLayout;
    private NavDrawerFragment drawerFragment;
    private ActionBarDrawerToggle mDrawerToggle;

    //----------------  Activity' lifecycle methods  ---------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        this.mServiceManger = new ServiceManager(MainActivity.this, HamsterService.class, new LocalHandler());
        this.mHelper = new MainActivityHelper(MainActivity.this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, getActionBarToolbar(),
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        drawerFragment = (NavDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navdrawer);
        drawerFragment.setup(getActionBarToolbar(), mDrawerLayout);

        // enabling action bar app icon and behaving it as toggle button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            openFragment(new TestFragment());
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d(TAG, "onPostCreate()");
        mDrawerToggle.syncState();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged()");
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected(), item.getItemId() = " + item.getItemId());
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
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

    // NavigationDrawer.OnItemClickListener
    @Override
    public void onDrawerItemClick(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TestFragment();
                break;
            case 1:
                fragment = new HomeFragment();
                break;
            case 2:
                fragment = new HistoryFragment();
                break;
            case 3:
            case 4:
            case 5:
                String msg = "Fragment have not implemented yet.";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            default:
                Log.e(TAG, "Error in creating fragmentl;");
                break;
        }
        openFragment(fragment);
    }

    private void openFragment(Fragment fragment) throws RuntimeException {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
        } else {
            throw new RuntimeException("fragment should contain reference to object (it can not be null)");
        }
        mFragment = fragment;
    }

    //----------------  Message handling and sending  --------------------------------------------//
    private class LocalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
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
                    if (mFragment != null && mFragment instanceof IFragment) {
                        Handler handler = ((IFragment) mFragment).getHandler();
                        if (handler != null)
                            handler.handleMessage(msg);
                    }
                    super.handleMessage(msg);
            }
        }
    }
}
