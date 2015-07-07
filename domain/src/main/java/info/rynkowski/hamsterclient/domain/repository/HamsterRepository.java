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

package info.rynkowski.hamsterclient.domain.repository;

import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.List;
import rx.Observable;

/**
 * Interface that represents a repository for communicating with Hamster database.
 */
public interface HamsterRepository {

  //void initialize(Type type);
  //
  //void deinitialize();

  //Observable<Status> onChange();

  Observable<List<Fact>> getTodaysFacts();

  Observable<Integer> addFact(Fact fact);

  Observable<Void> signalActivitiesChanged();

  Observable<Void> signalFactsChanged();

  Observable<Void> signalTagsChanged();

  Observable<Void> signalToggleCalled();

  //enum Type {
  //  LOCAL, REMOTE
  //}

  enum Status {
    SWITCHED_TO_REMOTE,
    SWITCHED_TO_LOCAL,
    REMOTE_UNAVAILABLE,
    LOCAL_UNAVAILABLE
  }

  //interface OnDataStoreChangedListener {
  //  void onDataStoreChanged(Status status);
  //}
}
