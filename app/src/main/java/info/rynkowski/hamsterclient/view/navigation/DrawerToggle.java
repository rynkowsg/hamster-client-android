package info.rynkowski.hamsterclient.view.navigation;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class DrawerToggle extends ActionBarDrawerToggle {
    private static final String TAG = "DrawerToggle";
    private Activity mActivity;

    public DrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        mActivity = activity;
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        Log.d(TAG, "onDrawerOpened()");
        //toolbar.setTitle(mDrawerTitle);
        // redraw the menu
        mActivity.invalidateOptionsMenu();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        Log.d(TAG, "onDrawerClosed()");
        //toolbar.setTitle(mTitle);
        // redraw the menu
        mActivity.invalidateOptionsMenu();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
//                toolbar.setAlpha(1 - slideOffset / 5);
    }
}