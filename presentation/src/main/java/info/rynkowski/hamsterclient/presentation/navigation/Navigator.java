package info.rynkowski.hamsterclient.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import info.rynkowski.hamsterclient.presentation.view.activity.FactFormActivity;

public class Navigator {

  public void navigateToAddFact(Context context) {
  public void navigateToFactForm(Context context) {
    if (context != null) {
      Intent intentToLaunch = FactFormActivity.getCallingIntent(context);
      context.startActivity(intentToLaunch);
    }
  }
}
