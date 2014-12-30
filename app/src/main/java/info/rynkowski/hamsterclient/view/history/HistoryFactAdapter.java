package info.rynkowski.hamsterclient.view.history;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.gnome.Struct5;

import java.util.List;

import info.rynkowski.hamsterclient.R;

public class HistoryFactAdapter extends BaseAdapter {
    private Context mContext;
    private List<Struct5> mFacts;

    HistoryFactAdapter(Context context, List<Struct5> facts) {
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
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        // fill data
        // ...

        return rowView;
    }

    private static class ViewHolder {
        // ...
    }
}
