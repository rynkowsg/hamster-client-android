/*
 * Copyright (C) 2015 Grzegorz Rynkowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.rynkowski.hamsterclient.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import info.rynkowski.hamsterclient.presentation.view.activity.FactFormActivity;
import info.rynkowski.hamsterclient.presentation.view.activity.FactListActivity;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
@Singleton
public class Navigator {

  public static final int REQUEST_CODE_PICK_FACT = 1;

  @Inject public Navigator() {
    //empty
  }

  /**
   * Goes to the fact list screen.
   *
   * @param context A Context needed to open the destiny activity.
   */
  public void navigateToFactsList(Context context) {
    if (context != null) {
      Intent intentToLaunch = FactListActivity.getCallingIntent(context);
      context.startActivity(intentToLaunch);
    }
  }

  /**
   * Goes to the fact form screen.
   *
   * @param fragment A Fragment that opens the activity for result.
   */
  public void navigateToFactFormForResult(Fragment fragment, int requestCode) {
    if (fragment != null) {
      Intent intentToLaunch = FactFormActivity.getCallingIntent(fragment.getActivity());
      fragment.startActivityForResult(intentToLaunch, requestCode);
    }
  }
}
