package info.rynkowski.hamsterclient.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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

public class TestFragment extends Fragment implements View.OnClickListener, IFragment {
    private static final String TAG = TestFragment.class.getName();
    private IMainActivity mActivityListener;
    private TestFragmentHelper mHelper;
    private LocalHandler mEventHandler;

    public TestFragment() {
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
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.mHelper = new TestFragmentHelper(TestFragment.this);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu()");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_test_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected(), item.getItemId() = " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mActivityListener.sendRequestToService(HamsterService.MSG_REFRESH);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartService:
                mActivityListener.startService();
                break;
            case R.id.btnStopService:
                mActivityListener.stopService();
                break;
            case R.id.btnDbusNotify:
                mActivityListener.sendRequestToService(HamsterService.MSG_NOTIFY);
                break;
            case R.id.btnFillTodayFactsList:
                mActivityListener.sendRequestToService(HamsterService.MSG_TODAY_FACTS);
                break;
            case R.id.btnShowPrefs:
                mHelper.displaySettings();
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

    @Override
    public Handler getHandler() {
        return mEventHandler;
    }

    private class LocalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage()");
            switch (msg.what) {
                case HamsterService.MSG_TODAY_FACTS:
                    Log.i(TAG, "Handled message: HamsterService.MSG_TODAY_FACTS");
                    mHelper.fillListTodayFacts((List<Struct5>) msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}
