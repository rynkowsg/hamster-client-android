package info.rynkowski.hamsterclient.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import info.rynkowski.hamsterclient.R;

public class NavDrawerListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NavDrawerItem> mNavDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        mContext = context;
        mNavDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return mNavDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        ViewHolder viewHolder;

        // reuse views
        if (rowView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            rowView = mInflater.inflate(R.layout.navdrawer_list_item, parent, false);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) rowView.findViewById(R.id.icon);
            viewHolder.title = (TextView) rowView.findViewById(R.id.title);
            viewHolder.counter = (TextView) rowView.findViewById(R.id.counter);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        // fill data
        viewHolder.image.setImageResource(mNavDrawerItems.get(position).getIcon());
        viewHolder.title.setText(mNavDrawerItems.get(position).getTitle());
        // displaying count
        // check whether it set visible or not
        if (mNavDrawerItems.get(position).getCounterVisibility()) {
            viewHolder.counter.setText(mNavDrawerItems.get(position).getCount());
        } else {
            // hide the counter view
            viewHolder.counter.setVisibility(View.GONE);
        }

        return rowView;
    }

    private static class ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView counter;
    }
}
