package info.rynkowski.hamsterclient.presentation.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.rynkowski.hamsterclient.presentation.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddFactFragment extends Fragment {

  public AddFactFragment() {
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_add_fact, container, false);
  }
}
