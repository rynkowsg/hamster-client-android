package info.rynkowski.hamsterclient.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.gnome.Struct5;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.hamster.AdapterStruct5;
import info.rynkowski.hamsterclient.view.prefs.PrefsActivity;

public class MainActivityHelper {
    private static final String TAG = "MainActivityHelper";
    private Activity mActivity;

    MainActivityHelper(Activity activity) {
        this.mActivity = activity;
    }

    //--- Methods used by view components  -------------------------------------------------------//
    //---  - TodayFacts list
    protected void fillListTodayFacts(List<Struct5> listOfFacts) {
        Log.i(TAG, "fillListTodayFacts");
        final ListView listview = (ListView) mActivity.findViewById(R.id.listOfTodayFacts);
        ArrayList<String> list = new ArrayList<String>();
        for (Struct5 row : listOfFacts) {
            list.add(AdapterStruct5.name(row));
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
    }

    //---  - dialog with information about exception
    protected void showExceptionDialog(Exception e) {
        // https://stackoverflow.com/questions/17738768/android-print-full-exception
        // Converts the stack trace into a string.
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        // Create and show AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setTitle(R.string.exception)
                .setMessage("Exception: \n" + e.toString() + "\n\nStackTrace:\n" + errors.toString())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();
    }

    //---  - run mActivity to adding new Fact
    protected void runAddFactActivity() {
        Intent pickFactData = new Intent(mActivity, AddFactActivity.class);
        mActivity.startActivityForResult(pickFactData, MainActivity.PICK_FACT_DATA);
    }

    //---  - run mActivity with application' settings
    protected void runSettingsActivity() {
        Intent intent = new Intent(mActivity, PrefsActivity.class);
        mActivity.startActivity(intent);
    }

    //---  - toast the settings
    protected void displaySettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String server_ip = prefs.getString("host", mActivity.getResources().getString(R.string.host));
        String server_port = prefs.getString("port", mActivity.getResources().getString(R.string.port));
        String message = "DBus address: " + server_ip + ":" + server_port + "\n";
        Toast.makeText(mActivity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
