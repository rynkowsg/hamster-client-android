/*
 * Copyright (C) 2015 Fernando Cejas Open Source Project
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

package info.rynkowski.hamsterclient.ui.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import dagger.Lazy;
import info.rynkowski.hamsterclient.ui.AndroidApplication;
import info.rynkowski.hamsterclient.ui.internal.di.components.ApplicationComponent;
import info.rynkowski.hamsterclient.ui.internal.di.modules.ActivityModule;
import info.rynkowski.hamsterclient.ui.view.Navigator;
import javax.inject.Inject;

/**
 * Base {@link android.app.Activity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity {

  @Inject Lazy<Navigator> navigator;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.injectDependencies();
  }

  /**
   * Get the Main Application component for dependency injection.
   *
   * @return {@link info.rynkowski.hamsterclient.ui.internal.di.components.ApplicationComponent}
   */
  protected @NonNull ApplicationComponent getApplicationComponent() {
    return ((AndroidApplication) getApplication()).getApplicationComponent();
  }

  /**
   * Get an Activity module for dependency injection.
   *
   * @return {@link info.rynkowski.hamsterclient.ui.internal.di.modules.ActivityModule}
   */
  protected @NonNull ActivityModule getActivityModule() {
    return new ActivityModule(this);
  }

  /**
   * A method that provides a {@link Navigator}'s object for derivatives of BaseActivity.
   *
   * @return an object of a {@link Navigator}
   */
  protected @NonNull Navigator getNavigator() {
    return navigator.get();
  }

  private void injectDependencies() {
    this.getApplicationComponent().inject(this);
  }
}
