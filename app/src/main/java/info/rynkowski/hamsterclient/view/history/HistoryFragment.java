package info.rynkowski.hamsterclient.view.history;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.gnome.Struct5;

import java.util.ArrayList;
import java.util.List;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.hamster.AdapterStruct5;
import info.rynkowski.hamsterclient.service.HamsterService;
import info.rynkowski.hamsterclient.view.IFragment;

public class HistoryFragment extends Fragment implements IFragment {
    private static final String TAG = "HistoryFragment";
    private LocalHandler handler;

    public HistoryFragment() {
        this.handler = new LocalHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public Handler getHandler() {
        return handler;
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

    protected void fillListTodayFacts(List<Struct5> listOfFacts) {
        Log.d(TAG, "fillListTodayFacts");
        final ListView listview = (ListView) getActivity().findViewById(R.id.history_list);
        ArrayList<String> list = new ArrayList<>();
        for (Struct5 row : listOfFacts) {
            list.add(AdapterStruct5.name(row));
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
    }

}