package info.rynkowski.hamsterclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.freedesktop.dbus.exceptions.DBusException;
import org.gnome.Struct5;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import info.rynkowski.hamsterclient.hamster.AdapterStruct5;
import info.rynkowski.hamsterclient.service.HamsterService;


public class MainActivity extends Activity {
    private final String TAG = "MainActivity";
    private ServiceManager service;

    static final int PICK_FACT_DATA = 1;

    //----------------  Message handling and sending  --------------------------------------------//
    private class LocalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HamsterService.MSG_TODAY_FACTS:
                    Log.i(TAG, "Handled message: HamsterService.MSG_TODAY_FACTS");
                    fillListTodayFacts((List<Struct5>) msg.obj);
                    break;
                case HamsterService.MSG_EXCEPTION:
                    showExceptionDialog((Exception) msg.obj);
                    break;
                case HamsterService.MSG_DBUS_EXCEPTION:
                    showExceptionDialog((DBusException) msg.obj);
                    break;
                case HamsterService.SIGNAL_ACTIVITIES_CHANGED:
                    Toast.makeText(getApplicationContext(), "SIGNAL_ACTIVITIES_CHANGED", Toast.LENGTH_LONG).show();
                    break;
                case HamsterService.SIGNAL_FACTS_CHANGED:
                    Toast.makeText(getApplicationContext(), "SIGNAL_FACTS_CHANGED", Toast.LENGTH_LONG).show();
                    break;
                case HamsterService.SIGNAL_TAGS_CHANGED:
                    Toast.makeText(getApplicationContext(), "SIGNAL_TAGS_CHANGED", Toast.LENGTH_LONG).show();
                    break;
                case HamsterService.SIGNAL_TOGGLE_CHANGED:
                    Toast.makeText(getApplicationContext(), "SIGNAL_TOGGLE_CHANGED", Toast.LENGTH_LONG).show();
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }

    void sendRequest(int what) {
        sendMessage(Message.obtain(null, what));
    }

    void sendMessage(Message msg) {
        try {
            service.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
            showExceptionDialog((RemoteException) msg.obj);
        }
    }

    //----------------  Activity' lifecycle methods  ---------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        this.service = new ServiceManager(this, HamsterService.class, new LocalHandler());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        service.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState()");
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        service.unbind();
        super.onDestroy();
    }

    //----------------  Activity' methods  -------------------------------------------------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                sendRequest(HamsterService.MSG_REFRESH);
                return true;
            case R.id.action_add_fact:
                runAddFactActivity();
                return true;
            case R.id.action_settings:
                runSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case (PICK_FACT_DATA):
                if (resultCode == RESULT_OK)
                    ;// Do something with the intent data
                break;
            default:
                break;
        }
    }

    //----------------  Buttons  -----------------------------------------------------------------//
    public void onClickButton(View view) {
        switch (view.getId()) {
            case R.id.btnStartService:
                service.start();
                break;
            case R.id.btnStopService:
                service.stop();
                break;
            case R.id.btnDbusNotify:
                sendRequest(HamsterService.MSG_NOTIFY);
                break;
            case R.id.btnFillTodayFactsList:
                sendRequest(HamsterService.MSG_TODAY_FACTS);
                break;
            case R.id.btnShowPrefs:
                displaySettings();
                break;
            default:
                ;
        }
    }

    //--- Methods used by view components  -------------------------------------------------------//
    //---  - TodayFacts list
    public void fillListTodayFacts(List<Struct5> listOfFacts) {
        Log.i(TAG, "fillListTodayFacts");
        final ListView listview = (ListView) findViewById(R.id.listOfTodayFacts);
        ArrayList<String> list = new ArrayList<String>();
        for (Struct5 row : listOfFacts) {
            list.add(AdapterStruct5.name(row));
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
    }

    //---  - dialog with information about exception
    public void showExceptionDialog(Exception e) {
        // https://stackoverflow.com/questions/17738768/android-print-full-exception
        // Converts the stack trace into a string.
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        // Create and show AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.exception)
                .setMessage("Exception: \n" + e.toString() + "\n\nStackTrace:\n" + errors.toString())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();
    }

    //---  - run activity to adding new Fact
    private void runAddFactActivity() {
        Intent pickFactData = new Intent(MainActivity.this, AddFactActivity.class);
        startActivityForResult(pickFactData, PICK_FACT_DATA);
    }

    //---  - run activity with application' settings
    private void runSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, PrefsActivity.class);
        startActivity(intent);
    }

    //---  - toast the settings
    private void displaySettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String server_ip = prefs.getString("host", getResources().getString(R.string.host));
        String server_port = prefs.getString("port", getResources().getString(R.string.port));
        String message = "DBus address: " + server_ip + ":" + server_port + "\n";
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
