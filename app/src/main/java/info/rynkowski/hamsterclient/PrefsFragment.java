package info.rynkowski.hamsterclient;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PrefsFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}