package info.rynkowski.hamsterclient.presentation.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import info.rynkowski.hamsterclient.presentation.AndroidApplication;
import info.rynkowski.hamsterclient.presentation.internal.di.components.ApplicationComponent;
import info.rynkowski.hamsterclient.presentation.navigation.Navigator;
import javax.inject.Inject;

/**
 * Base {@link android.app.Fragment} class for every fragment in this application.
 */
public class BaseFragment extends Fragment {

  @Inject Navigator navigator;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getApplicationComponent().inject(this);
  }

  /**
   * Get the Main Application component for dependency injection.
   *
   * @return {@link info.rynkowski.hamsterclient.presentation.internal.di.components.ApplicationComponent}
   */
  protected ApplicationComponent getApplicationComponent() {
    return ((AndroidApplication)getActivity().getApplication()).getApplicationComponent();
  }

  /**
   * Shows a {@link android.widget.Toast} message.
   *
   * @param message An string representing a message to be shown.
   */
  protected void showToastMessage(String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  }
}
