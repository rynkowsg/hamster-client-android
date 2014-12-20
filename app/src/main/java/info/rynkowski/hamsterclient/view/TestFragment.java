package info.rynkowski.hamsterclient.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.gnome.Struct5;

import java.util.List;

import info.rynkowski.hamsterclient.R;
import info.rynkowski.hamsterclient.service.HamsterService;

public class TestFragment extends Fragment implements View.OnClickListener, InterfaceFragment {
    private static final String TAG = "TestFragment";
    private InterfaceMainActivity listener;
    private TestFragmentHelper helper;
    protected LocalHandler handler;

    private class LocalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HamsterService.MSG_TODAY_FACTS:
                    Log.i(TAG, "Handled message: HamsterService.MSG_TODAY_FACTS");
                    helper.fillListTodayFacts((List<Struct5>) msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    public TestFragment() {
        this.handler = new LocalHandler();
    }

    // Lifecycle
    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach()");
        super.onAttach(activity);
        if (activity instanceof InterfaceMainActivity) {
            listener = (InterfaceMainActivity) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet InterfaceMainActivity");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.helper = new TestFragmentHelper(TestFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
        getView().findViewById(R.id.btnStartService).setOnClickListener(this);
        getView().findViewById(R.id.btnStopService).setOnClickListener(this);
        getView().findViewById(R.id.btnDbusNotify).setOnClickListener(this);
        getView().findViewById(R.id.btnFillTodayFactsList).setOnClickListener(this);
        getView().findViewById(R.id.btnShowPrefs).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState()");
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView()");
        super.onDestroy();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    // Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu()");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_test_fragment, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.action_refresh:
                listener.sendRequestToService(HamsterService.MSG_REFRESH);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //----------------  Buttons OnClick  ---------------------------------------------------------//
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartService:
                listener.startService();
                break;
            case R.id.btnStopService:
                listener.stopService();
                break;
            case R.id.btnDbusNotify:
                listener.sendRequestToService(HamsterService.MSG_NOTIFY);
                break;
            case R.id.btnFillTodayFactsList:
                listener.sendRequestToService(HamsterService.MSG_TODAY_FACTS);
                break;
            case R.id.btnShowPrefs:
                helper.displaySettings();
                break;
            default:
                ;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(), requestCode = " + requestCode + ", resultCode = " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (MainActivity.PICK_FACT_DATA):
                if (resultCode == Activity.RESULT_OK)
                    Toast.makeText(getActivity(), "New fact data received", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}
