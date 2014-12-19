package info.rynkowski.hamsterclient.view.prefs;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import info.rynkowski.hamsterclient.R;

public class PrefsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new PrefsFragment())
                .commit();
        PreferenceManager.setDefaultValues(PrefsActivity.this, R.xml.preferences, false);
    }
}