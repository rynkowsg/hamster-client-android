package info.rynkowski.hamsterclient.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import info.rynkowski.hamsterclient.presentation.view.activity.FactFormActivity;
import info.rynkowski.hamsterclient.presentation.view.activity.FactsListActivity;

public class Navigator {

  public void navigateToFactForm(Context context) {
    if (context != null) {
      Intent intentToLaunch = FactFormActivity.getCallingIntent(context);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToFactsList(Context context) {
    if (context != null) {
      Intent intentToLaunch = FactsListActivity.getCallingIntent(context);
      context.startActivity(intentToLaunch);
    }
  }
}
