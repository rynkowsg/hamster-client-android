package info.rynkowski.hamsterclient.ui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.gnome.Struct5;

import java.util.ArrayList;
import java.util.List;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.data.dbus.adapters.AdapterStruct5;

public class TestFragmentHelper {
    private static final String TAG = MainActivityHelper.class.getName();
    private Fragment mFragment;

    TestFragmentHelper(Fragment fragment) {
        this.mFragment = fragment;
    }

    //--- Methods used by view components  -------------------------------------------------------//
    //---  - TodayFacts list
    protected void fillListTodayFacts(List<Struct5> listOfFacts) {
        Log.d(TAG, "fillListTodayFacts");
        final ListView listview = (ListView) mFragment.getActivity().findViewById(R.id.listOfTodayFacts);
        ArrayList<String> list = new ArrayList<String>();
        for (Struct5 row : listOfFacts) {
            list.add(AdapterStruct5.name(row));
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mFragment.getActivity(), android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
    }

    //---  - toast the settings
    protected void displaySettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mFragment.getActivity());
        String server_ip = prefs.getString("host", mFragment.getActivity().getResources().getString(R.string.host));
        String server_port = prefs.getString("port", mFragment.getActivity().getResources().getString(R.string.port));
        String message = "DBus address: " + server_ip + ":" + server_port + "\n";
        Toast.makeText(mFragment.getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
