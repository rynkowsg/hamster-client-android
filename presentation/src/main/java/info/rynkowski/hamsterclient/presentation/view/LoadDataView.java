/*
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
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

package info.rynkowski.hamsterclient.presentation.view;

/**
 * Interface representing a View that will use to load data.
 */
public interface LoadDataView {
  /**
   * Show a view with a progress bar indicating a loading process.
   */
  void showLoading();

  /**
   * Hide a loading view.
   */
  void hideLoading();

  /**
   * Show a retry view in case of an error when retrieving data.
   */
  void showRetry();

  /**
   * Hide a retry view shown if there was an error when retrieving data.
   */
  void hideRetry();

  /**
   * Show an error message.
   *
   * @param message A string representing an error.
   */
  void showError(String message);
}
