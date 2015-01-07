package info.rynkowski.hamsterclient.ui;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.gnome.Struct5;

import java.util.Calendar;
import java.util.List;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.model.AdapterStruct5;

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
            viewHolder.startTime = (TextView) rowView.findViewById(R.id.startTime);
            viewHolder.endTime = (TextView) rowView.findViewById(R.id.endTime);
            viewHolder.tags = (TextView) rowView.findViewById(R.id.tags);
            viewHolder.activityName = (TextView) rowView.findViewById(R.id.activityName);
            viewHolder.categoryName = (TextView) rowView.findViewById(R.id.categoryName);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        // fill data
        AdapterStruct5 data = new AdapterStruct5(mFacts.get(position));

        viewHolder.startTime.setText(getTime(data.start_time()));
        viewHolder.endTime.setText(getTime(data.end_time()));

        viewHolder.tags.setText(StringUtils.join(data.tags(), ", "));

        viewHolder.activityName.setText(data.name());
        viewHolder.categoryName.setText(data.category());

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
        TextView startTime;
        TextView endTime;
        TextView tags;
        TextView activityName;
        TextView categoryName;
    }
}
