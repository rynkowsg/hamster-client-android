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

package info.rynkowski.hamsterclient.data.repository.datasources.db;

import android.content.Context;
import info.rynkowski.hamsterclient.data.repository.datasources.HamsterDataSource;
import info.rynkowski.hamsterclient.data.repository.datasources.db.entities.DbFact;
import info.rynkowski.hamsterclient.data.repository.datasources.db.entities.mapper.DbFactMapper;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.subjects.PublishSubject;

@Singleton
public class DbHamsterDataSource implements HamsterDataSource {

  private final @Nonnull DbFactMapper mapper;

  private @Nonnull PublishSubject<Void> signalActivitiesChanged = PublishSubject.create();
  private @Nonnull PublishSubject<Void> signalFactsChanged = PublishSubject.create();
  private @Nonnull PublishSubject<Void> signalTagsChanged = PublishSubject.create();
  private @Nonnull PublishSubject<Void> signalToggleCalled = PublishSubject.create();

  private @Nonnull Context context;

  @Inject public DbHamsterDataSource(@Nonnull Context context, @Nonnull DbFactMapper mapper) {
    this.context = context;
    this.mapper = mapper;
  }

  @Override public @Nonnull Observable<List<Fact>> getTodaysFacts() {
    FactsDatabaseAdapter factsDbAdapter = new FactsDatabaseAdapter(context).open();
    List<DbFact> list = factsDbAdapter.getFacts();
    factsDbAdapter.close();

    return Observable.just(list) //
        .flatMap(Observable::from).map(mapper::transform).toList();
  }

  @Override public @Nonnull Observable<Integer> addFact(@Nonnull Fact fact) {
    FactsDatabaseAdapter factsDbAdapter = new FactsDatabaseAdapter(context).open();
    int id = factsDbAdapter.insertFact(mapper.transform(fact));
    factsDbAdapter.close();

    if (id == -1) {
      return Observable.error(new RuntimeException("Inserting a fact to database failed"));
    }

    signalFactsChanged.onNext(null);

    return Observable.just(id);
  }

  @Override public @Nonnull Observable<Void> removeFact(@Nonnull Integer id) {
    FactsDatabaseAdapter factsDbAdapter = new FactsDatabaseAdapter(context).open();
    boolean resultValue = factsDbAdapter.deleteFact(id);
    factsDbAdapter.close();

    if (!resultValue) {
      return Observable.error(new RuntimeException("Deleting a fact from database failed"));
    }

    signalFactsChanged.onNext(null);

    return Observable.empty();
  }

  @Override public @Nonnull Observable<Integer> updateFact(@Nonnull Fact fact) {
    FactsDatabaseAdapter factsDbAdapter = new FactsDatabaseAdapter(context).open();
    boolean resultValue = factsDbAdapter.updateFact(mapper.transform(fact));
    factsDbAdapter.close();

    if (!resultValue) {
      return Observable.error(new RuntimeException("Updating a fact in database failed"));
    }

    signalFactsChanged.onNext(null);

    return Observable.just(fact.getId().get());
  }

  @Override public @Nonnull Observable<Void> signalActivitiesChanged() {
    return signalActivitiesChanged;
  }

  @Override public @Nonnull Observable<Void> signalFactsChanged() {
    return signalFactsChanged;
  }

  @Override public @Nonnull Observable<Void> signalTagsChanged() {
    return signalTagsChanged;
  }

  @Override public @Nonnull Observable<Void> signalToggleCalled() {
    return signalToggleCalled;
  }

  @Override public @Nonnull Observable<Void> initialize() {
    return Observable.empty();
  }

  @Override public @Nonnull Observable<Void> deinitialize() {
    return Observable.empty();
  }
}
