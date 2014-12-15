package info.rynkowski.hamsterclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.freedesktop.dbus.exceptions.DBusException;
import org.gnome.Struct5;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import info.rynkowski.hamsterclient.hamster.AdapterStruct5;


public class MainActivity extends Activity {
    private final String TAG = "MainActivity";
    private ServiceManager service;

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
                default:
                    super.handleMessage(msg);
            }
        }
    }

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
            case R.id.action_add_fact:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
            default:
                ;
        }
    }

    void sendRequest(int what) {
        try {
            service.send(Message.obtain(null, what));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void fillListExampleData() {
        // http://www.vogella.com/tutorials/AndroidListView/article.html
        final ListView listview = (ListView) findViewById(R.id.listOfTodayFacts);
        String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
    }

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
}
