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

package info.rynkowski.hamsterclient.ui.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import info.rynkowski.hamsterclient.ui.model.UiFact;
import info.rynkowski.hamsterclient.ui.view.activity.FactFormActivity;
import info.rynkowski.hamsterclient.ui.view.activity.SettingsActivity;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
@Singleton
public class Navigator {

  public static final int REQUEST_CODE_PICK_FACT = 1;

  public @Inject Navigator() {
    //empty
  }

  /**
   * Goes to a {@link FactFormActivity} to allow entering a new fact.
   *
   * @param fragment A {@link Fragment} that opens the {@link FactFormActivity}.
   */
  public void navigateToFactAdditionForm(@NonNull Fragment fragment) {
    Intent intentToLaunch = FactFormActivity.getCallingIntent(fragment.getActivity());
    fragment.startActivityForResult(intentToLaunch, FactFormActivity.REQUEST_CODE_ADD_FACT);
  }

  /**
   * Goes to a {@link FactFormActivity} to allow modification an existing fact.
   *
   * @param fragment A {@link Fragment} that opens the {@link FactFormActivity}.
   */
  public void navigateToFactEditionForm(@NonNull Fragment fragment, @NonNull UiFact fact) {
    Intent intentToLaunch = FactFormActivity.getCallingIntent(fragment.getActivity());
    intentToLaunch.putExtra(FactFormActivity.INPUT_EXTRAS_KEY_FACT, fact);
    fragment.startActivityForResult(intentToLaunch, FactFormActivity.REQUEST_CODE_EDIT_FACT);
  }

  /**
   * Goes to application's settings.
   *
   * @param context A Context needed to open the destiny activity.
   */
  public void navigateToSettings(@NonNull Context context) {
    Intent intent = new Intent(context, SettingsActivity.class);
    context.startActivity(intent);
  }
}
