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

package info.rynkowski.hamsterclient.ui.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import dagger.Lazy;
import info.rynkowski.hamsterclient.ui.AndroidApplication;
import info.rynkowski.hamsterclient.ui.internal.di.HasComponent;
import info.rynkowski.hamsterclient.ui.internal.di.components.ApplicationComponent;
import info.rynkowski.hamsterclient.ui.view.Navigator;
import javax.inject.Inject;

/**
 * Base {@link android.app.Fragment} class for every fragment in this application.
 */
public class BaseFragment extends Fragment {

  @Inject Lazy<Navigator> navigator;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.injectDependencies();
  }

  /**
   * Get the Main Application component for dependency injection.
   *
   * @return {@link info.rynkowski.hamsterclient.ui.internal.di.components.ApplicationComponent}
   */
  protected @NonNull ApplicationComponent getApplicationComponent() {
    return ((AndroidApplication) getActivity().getApplication()).getApplicationComponent();
  }

  /**
   * Shows a {@link android.widget.Toast} message.
   *
   * @param message An string representing a message to be shown.
   */
  protected void showToastMessage(@Nullable String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  }

  /**
   * Gets a component for dependency injection by its type.
   */
  @SuppressWarnings("unchecked")
  protected @NonNull <C> C getComponent(@NonNull Class<C> componentType) {
    return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
  }

  /**
   * A method that provides a {@link Navigator}'s object for derivatives of BaseFragment.
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
