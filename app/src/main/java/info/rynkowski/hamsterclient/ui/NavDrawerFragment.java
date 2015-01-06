package info.rynkowski.hamsterclient.ui;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import info.rynkowski.hamsterclient.R;

/**
 * NavigationDrawer is a fragment responsible for supporting navigation drawer.
 * It provides methods to preparing navigation drawer.
 */
public class NavDrawerFragment extends Fragment implements ListView.OnItemClickListener {
    private static final String TAG = NavDrawerFragment.class.getName();

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Toolbar mToolbar;
    private ArrayList<NavDrawerItem> mItemsList;

    private OnItemClickListener mActivityListener;

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach()");
        super.onAttach(activity);
        if (activity instanceof OnItemClickListener) {
            mActivityListener = (OnItemClickListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet NavigationDrawerFragment.Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_navdrawer, container, false);
    }

    /**
     * Initialize the navigation drawer.
     *
     * @param toolbar      The reference to object of Toolbar.
     * @param drawerLayout The reference to object of DrawerLayout.
     */
    public void setup(Toolbar toolbar, DrawerLayout drawerLayout) {
//        mTitle = mDrawerTitle = mToolbar.getTitle();
        Activity activity = getActivity();

        mToolbar = toolbar;
        mDrawerLayout = drawerLayout;

        // Drawer list view is found here, not at activity. Why?
        // I think that it is better place, because fragment drawer' layout contains this list.
        // Other fragment drawer could have other layout with different components.
        mDrawerList = (ListView) activity.findViewById(R.id.list_drawer);

        mItemsList = prepareItemsList();
        mDrawerList.setAdapter(new NavDrawerListAdapter(activity, mItemsList));
        mDrawerList.setOnItemClickListener(this);

        // activate default item on the list
        mDrawerList.setItemChecked(getResources().getInteger(R.integer.navdrawer_default_pick), true);
    }

    private ArrayList<NavDrawerItem> prepareItemsList() {
        String[] titles = getResources().getStringArray(R.array.navdrawer_titles);
        TypedArray icons = getResources().obtainTypedArray(R.array.navdrawer_icons);

        ArrayList<NavDrawerItem> items = new ArrayList<>();
        items.add(new NavDrawerItem(titles[0], icons.getResourceId(0, -1)));    // Test
        items.add(new NavDrawerItem(titles[1], icons.getResourceId(1, -1)));    // Home
        items.add(new NavDrawerItem(titles[2], icons.getResourceId(2, -1)));    // History
        items.add(new NavDrawerItem(titles[3], icons.getResourceId(3, -1)));    // Stats
        items.add(new NavDrawerItem(titles[4], icons.getResourceId(4, -1)));    // Edit tables
        items.add(new NavDrawerItem(titles[5], icons.getResourceId(5, -1)));    // Settings
        items.add(new NavDrawerItem(titles[6], icons.getResourceId(6, -1)));    // About
        return items;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // update mToolbar title
        String title = mItemsList.get(position).getTitle();
        mToolbar.setTitle(title);

        // close the drawer
        mDrawerLayout.closeDrawer(getView());

        // run the callback method at containing view (e.g. replace fragment)
        mActivityListener.onDrawerItemClick(position);
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * NavigationDrawerFragment has been clicked.
     */
    public interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this
         * NavigationDrawerFragment has been clicked.
         *
         * @param position The position of the item in the navigation drawer.
         */
        public void onDrawerItemClick(int position);
    }
}
