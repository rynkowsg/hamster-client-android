package info.rynkowski.hamsterclient.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import info.rynkowski.hamsterclient.presentation.view.activity.AddFactActivity;

public class Navigator {

  public void navigateToAddFact(Context context) {
    if (context != null) {
      Intent intentToLaunch = AddFactActivity.getCallingIntent(context);
      context.startActivity(intentToLaunch);
    }
  }
}
