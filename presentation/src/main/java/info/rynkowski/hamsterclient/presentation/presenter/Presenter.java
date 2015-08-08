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

package info.rynkowski.hamsterclient.presentation.presenter;

import info.rynkowski.hamsterclient.presentation.view.FactListView;

/**
 * Interface representing a Presenter in a model view presenter (MVP) pattern.
 */
public interface Presenter {

  void setView(FactListView view);

  /**
   * Called when the presenter is initialized, this method represents the start of the presenter
   * lifecycle.
   */
  void start();

  /**
   * Called when the presenter is resumed. After the initialization and when the presenter comes
   * from a pause state.
   */
  void resume();

  /**
   * Called when the presenter is paused.
   */
  void pause();

  /**
   * Called at the end of the presenter life cycle.
   */
  void destroy();
}
