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

package info.rynkowski.hamsterclient.data.repository.datasource;

import info.rynkowski.hamsterclient.data.entity.FactEntity;
import java.util.List;
import javax.annotation.Nonnull;
import rx.Observable;

/**
 * Interface that represents a Hamster's {@link DataStore}.
 */
public interface HamsterDataStore extends DataStore {

  /**
   * Get an {@link rx.Observable} which will emit a today's list of {@link FactEntity}.
   */
  @Nonnull Observable<List<FactEntity>> getTodaysFactEntities();

  /**
   * Get an {@link rx.Observable} which will add a {@link FactEntity} and emits its id.
   *
   * @param factEntity The entity to add to store.
   * @return The id of added {@link FactEntity}.
   */
  @Nonnull Observable<Integer> addFactEntity(@Nonnull FactEntity factEntity);

  @Nonnull Observable<Void> signalActivitiesChanged();

  @Nonnull Observable<Void> signalFactsChanged();

  @Nonnull Observable<Void> signalTagsChanged();

  @Nonnull Observable<Void> signalToggleCalled();
}
