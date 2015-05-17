package info.rynkowski.hamsterclient.ui;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.gnome.Struct5;

import java.util.Calendar;
import java.util.List;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.data.dbus.adapters.AdapterStruct5;

public class HistoryListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Struct5> mFacts;

    HistoryListAdapter(Context context, List<Struct5> facts) {
        mContext = context;
        mFacts = facts;
    }

    @Override
    public int getCount() {
        return mFacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mFacts.get(position);
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
            rowView = mInflater.inflate(R.layout.history_list_item, parent, false);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.timePeriod = (TextView) rowView.findViewById(R.id.time_period);
            viewHolder.activity = (TextView) rowView.findViewById(R.id.activity);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        // fill data
        AdapterStruct5 data = new AdapterStruct5(mFacts.get(position));
        viewHolder.timePeriod.setText(getTime(data.start_time()) + " - " + getTime(data.end_time()));
        viewHolder.activity.setText(data.name());

        return rowView;
    }

    private String getTime(long timestamp) {
        if(timestamp == 0) {
            return mContext.getResources().getString(R.string.now);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp * 1000);
        return DateFormat.format("HH:mm", cal).toString();
    }

    private static class ViewHolder {
        TextView timePeriod;
        TextView activity;
    }
}
