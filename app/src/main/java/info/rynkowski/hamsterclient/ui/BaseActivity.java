package info.rynkowski.hamsterclient.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import info.rynkowski.hamsterclient.R;

public abstract class BaseActivity extends ActionBarActivity implements NavDrawerFragment.OnItemClickListener {
    private static final String TAG = MainActivity.class.getName();

    private ActionBarDrawerToggle mDrawerToggle;

    // Primary toolbar
    private Toolbar mActionBarToolbar;

    private Fragment mFragment;

    private NavDrawerFragment mDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged()");
        // Pass any configuration change to the drawer toggle
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected(), item.getItemId() = " + item.getItemId());
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerFragment.isNavDrawerOpen()) {
            mDrawerFragment.closeNavDrawer();
            return;
        }
        super.onBackPressed();
    }

    protected Fragment getCurrentFragment() {
        return mFragment;
    }

    /*
     * This method should be called at least one time at onCreate() of inheriting class.
     **/
    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (getActionBarToolbar() != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    /**
     * Sets up the navigation drawer as appropriate.
     */
    private void setupNavDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout == null) {
            return;
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, getActionBarToolbar(),
                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerFragment = (NavDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navdrawer);
        mDrawerFragment.setup(getActionBarToolbar(), drawerLayout);
    }

    // NavigationDrawer.OnItemClickListener
    @Override
    public void onDrawerItemClick(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 0:
                openFragment(new TestFragment());
                changeActionBarTitle(position);
                break;
            case 1:
                openFragment(new HomeFragment());
                changeActionBarTitle(position);
                break;
            case 2:
                openFragment(new HistoryFragment());
                changeActionBarTitle(position);
                break;
            case 5:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case 3:
            case 4:
            case 6:
                String msg = "Fragment have not implemented yet.";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            default:
                Log.e(TAG, "Error in creating fragment.");
                break;
        }
    }

    protected void openFragment(Fragment fragment) throws RuntimeException {
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

    private void changeActionBarTitle(int position) {
        String[] titles = getResources().getStringArray(R.array.navdrawer_titles);
        mActionBarToolbar.setTitle(titles[position]);
    }
}
