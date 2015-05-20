package info.rynkowski.hamsterclient.presentation.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import info.rynkowski.hamsterclient.presentation.view.activity.FactFormActivity;
import info.rynkowski.hamsterclient.presentation.view.activity.FactListActivity;

public class Navigator {

  public void navigateToFactsList(Context context) {
    if (context != null) {
      Intent intentToLaunch = FactListActivity.getCallingIntent(context);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToFactFormForResult(Activity activityContext) {
    if (activityContext != null) {
      Intent intentToLaunch = FactFormActivity.getCallingIntent(activityContext);
      activityContext.startActivityForResult(intentToLaunch,
          FactFormActivity.REQUEST_CODE_PICK_FACT);
    }
  }
}
