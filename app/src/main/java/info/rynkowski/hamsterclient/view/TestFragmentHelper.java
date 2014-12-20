package info.rynkowski.hamsterclient.view;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.gnome.Struct5;

import java.util.ArrayList;
import java.util.List;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.hamster.AdapterStruct5;

public class TestFragmentHelper {
    private static final String TAG = "MainActivityHelper";
    private Fragment fragment;

    TestFragmentHelper(Fragment fragment) {
        this.fragment = fragment;
    }

    //--- Methods used by view components  -------------------------------------------------------//
    //---  - TodayFacts list
    protected void fillListTodayFacts(List<Struct5> listOfFacts) {
        Log.d(TAG, "fillListTodayFacts");
        final ListView listview = (ListView) fragment.getActivity().findViewById(R.id.listOfTodayFacts);
        ArrayList<String> list = new ArrayList<String>();
        for (Struct5 row : listOfFacts) {
            list.add(AdapterStruct5.name(row));
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(fragment.getActivity(), android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
    }

    //---  - toast the settings
    protected void displaySettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
        String server_ip = prefs.getString("host", fragment.getActivity().getResources().getString(R.string.host));
        String server_port = prefs.getString("port", fragment.getActivity().getResources().getString(R.string.port));
        String message = "DBus address: " + server_ip + ":" + server_port + "\n";
        Toast.makeText(fragment.getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
