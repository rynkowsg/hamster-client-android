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

import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.List;
import javax.annotation.Nonnull;
import rx.Observable;

/**
 * Interface that represents a Hamster's {@link DataSource}.
 */
public interface HamsterDataSource extends DataSource {

  /**
   * Get an {@link rx.Observable} which will emit a today's list of {@link Fact}.
   */
  @Nonnull Observable<List<Fact>> getTodaysFacts();

  /**
   * Get an {@link rx.Observable} which will add a {@link Fact} and emits its id.
   *
   * @param fact The entity to add to store.
   * @return The id of added {@link Fact}.
   */
  @Nonnull Observable<Integer> addFact(@Nonnull Fact fact);

  /**
   * Get an {@link rx.Observable} that removes a fact.
   *
   * @param id The id number of fact to remove.
   * @return Empty {@link rx.Observable}.
   */
  @Nonnull Observable<Void> removeFact(@Nonnull Integer id);

  /**
   * Get an {@link rx.Observable} which will update a fact and emits its id.
   *
   * @param fact The entity to update at the store.
   * @return The id of updated {@link Fact}.
   */
  @Nonnull Observable<Integer> updateFact(@Nonnull Fact fact);

  @Nonnull Observable<Void> signalActivitiesChanged();

  @Nonnull Observable<Void> signalFactsChanged();

  @Nonnull Observable<Void> signalTagsChanged();

  @Nonnull Observable<Void> signalToggleCalled();

  // TODO: Consider changing signals into one - Observable<Event>,
  //           where events will be enum values: FactsChanged, TagsChanged, etc.
  //       Question: If I do that will I could unregister a signal during unsubscribing?
  //       Answer: Yes, I jast need other counters. But it smells bad.
}
