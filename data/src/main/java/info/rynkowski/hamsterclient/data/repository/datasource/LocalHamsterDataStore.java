/*
 * Copyright (C) 2015 Grzegorz Rynkowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.rynkowski.hamsterclient.data.repository.datasource;

import android.content.Context;
import com.google.common.base.Optional;
import info.rynkowski.hamsterclient.data.db.FactsDbAdapter;
import info.rynkowski.hamsterclient.data.entity.FactEntity;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.subjects.PublishSubject;

@Slf4j
@Singleton
public class LocalHamsterDataStore implements HamsterDataStore {

  private PublishSubject<Void> signalActivitiesChanged = PublishSubject.create();
  @Nonnull private PublishSubject<Void> signalFactsChanged = PublishSubject.create();
  @Nonnull private PublishSubject<Void> signalTagsChanged = PublishSubject.create();
  @Nonnull private PublishSubject<Void> signalToggleCalled = PublishSubject.create();

  @Nonnull private Context context;

  @Inject public LocalHamsterDataStore(@Nonnull Context context) {
    this.context = context;
  }

  @Override public Observable<List<FactEntity>> getTodaysFactEntities() {
    return null;
  }

  @Override public Observable<Optional<Integer>> addFactEntity(@Nonnull FactEntity factEntity) {
    FactsDbAdapter factsDbAdapter = new FactsDbAdapter(context).open();
    int localId = factsDbAdapter.insertFact(factEntity);
    factsDbAdapter.close();

    signalFactsChanged.onNext(null);

    return Observable.just(Optional.of(localId));
  }

  @Override public Observable<Void> signalActivitiesChanged() {
    return signalActivitiesChanged;
  }

  @Override public Observable<Void> signalFactsChanged() {
    return signalFactsChanged;
  }

  @Override public Observable<Void> signalTagsChanged() {
    return signalTagsChanged;
  }

  @Override public Observable<Void> signalToggleCalled() {
    return signalToggleCalled;
  }

  @Override public Observable<Void> initialize() {
    return null;
  }

  @Override public Observable<Void> deinitialize() {
    return null;
  }
}
