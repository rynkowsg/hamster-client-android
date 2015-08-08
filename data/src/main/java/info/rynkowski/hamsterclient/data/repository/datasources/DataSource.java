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

package info.rynkowski.hamsterclient.data.repository.datasources;

import javax.annotation.Nonnull;
import rx.Observable;

/**
 * Root interface for specific data source's interfaces.
 */
public interface DataSource {

  /**
   * Called to initialize data source object.
   */
  @Nonnull Observable<Void> initialize();

  /**
   * Called to deinitialize data source object.
   */
  @Nonnull Observable<Void> deinitialize();
}
