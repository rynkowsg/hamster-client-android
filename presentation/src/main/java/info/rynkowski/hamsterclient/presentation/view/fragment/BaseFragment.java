package info.rynkowski.hamsterclient.presentation.view.fragment;

import android.app.Fragment;
import android.widget.Toast;

/**
 * Base {@link android.app.Fragment} class for every fragment in this application.
 */
public class BaseFragment extends Fragment {

  /**
   * Shows a {@link android.widget.Toast} message.
   *
   * @param message An string representing a message to be shown.
   */
  protected void showToastMessage(String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  }
}
