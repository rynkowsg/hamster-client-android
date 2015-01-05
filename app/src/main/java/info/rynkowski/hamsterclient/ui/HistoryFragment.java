package info.rynkowski.hamsterclient.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.gnome.Struct5;

import java.util.List;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.service.HamsterService;

public class HistoryFragment extends Fragment implements IFragment {
    private static final String TAG = HistoryFragment.class.getName();

    private LocalHandler mEventHandler;
    private IMainActivity mActivityListener;

    public HistoryFragment() {
        this.mEventHandler = new LocalHandler();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach()");
        super.onAttach(activity);
        if (activity instanceof IMainActivity) {
            mActivityListener = (IMainActivity) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet IMainActivity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivityListener.sendRequestToService(HamsterService.MSG_TODAY_FACTS);
    }

    private void fillListTodayFacts(List<Struct5> listOfFacts) {
        Log.d(TAG, "fillListTodayFacts");
        final ListView listview = (ListView) getActivity().findViewById(R.id.history_list);
        listview.setAdapter(new HistoryListAdapter(getActivity(), listOfFacts));
    }

    @Override
    public Handler getHandler() {
        return mEventHandler;
    }

    private class LocalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HamsterService.MSG_TODAY_FACTS:
                    Log.i(TAG, "Handled message: HamsterService.MSG_TODAY_FACTS");
                    fillListTodayFacts((List<Struct5>) msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}
