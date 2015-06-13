package info.rynkowski.hamsterclient.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import info.rynkowski.hamsterclient.presentation.view.activity.FactFormActivity;
import info.rynkowski.hamsterclient.presentation.view.activity.FactListActivity;

public class Navigator {

  public static final int REQUEST_CODE_PICK_FACT = 1;

  public void navigateToFactsList(Context context) {
    if (context != null) {
      Intent intentToLaunch = FactListActivity.getCallingIntent(context);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToFactFormForResult(Fragment fragment, int requestCode) {
    if (fragment != null) {
      Intent intentToLaunch = FactFormActivity.getCallingIntent(fragment.getActivity());
      fragment.startActivityForResult(intentToLaunch, requestCode);
    }
  }
}
