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

package info.rynkowski.hamsterclient.data.preferences;

import javax.annotation.Nonnull;
import rx.Observable;

public interface Preferences {

  @Nonnull Boolean isDatabaseRemote();

  @Nonnull String dbusHost();

  @Nonnull String dbusPort();

  /**
   * Provides a signal that reports changes in shared preferences.
   *
   * @return an {@link Observable} that contains a preference key
   */
  @Nonnull Observable<Type> signalOnChanged();

  enum Type {
    DbusHost, DbusPort, IsDatabaseRemote
  }
}
