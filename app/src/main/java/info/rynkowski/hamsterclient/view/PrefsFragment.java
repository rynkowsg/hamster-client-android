package info.rynkowski.hamsterclient.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import info.rynkowski.hamsterclient.R;

public class PrefsFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}