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

package info.rynkowski.hamsterclient.data.repository;

import info.rynkowski.hamsterclient.data.repository.datasources.HamsterDataSource;
import info.rynkowski.hamsterclient.data.utils.PreferencesAdapter;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import rx.Observable;

/**
 * Implementation of {@link info.rynkowski.hamsterclient.domain.repository.HamsterRepository}.
 */
@Singleton
public class HamsterRepositoryImpl implements HamsterRepository {

  private @Nonnull PreferencesAdapter preferencesAdapter;

  private @Nonnull HamsterDataSource dbDataSource;
  private @Nonnull HamsterDataSource dbusDataSource;

  @Inject public HamsterRepositoryImpl(@Nonnull PreferencesAdapter preferencesAdapter,
      @Named("db") @Nonnull HamsterDataSource dbDataSource,
      @Named("dbus") @Nonnull HamsterDataSource dbusDataSource) {

    this.preferencesAdapter = preferencesAdapter;
    this.dbDataSource = dbDataSource;
    this.dbusDataSource = dbusDataSource;
  }

  @Override public @Nonnull Observable<List<Fact>> getTodaysFacts() {
    return currentStore().getTodaysFacts();
  }

  @Override public @Nonnull Observable<Integer> addFact(@Nonnull Fact fact) {
    return Observable.just(fact) //
        .flatMap(currentStore()::addFact);
  }

  @Override public @Nonnull Observable<Void> removeFact(@Nonnull Integer id) {
    return currentStore().removeFact(id);
  }

  @Override public @Nonnull Observable<Integer> updateFact(@Nonnull Fact fact) {
    return Observable.just(fact) //
        .flatMap(currentStore()::updateFact);
  }

  @Override public @Nonnull Observable<Void> signalActivitiesChanged() {
    return currentStore().signalActivitiesChanged();
  }

  @Override public @Nonnull Observable<Void> signalFactsChanged() {
    return currentStore().signalFactsChanged();
  }

  @Override public @Nonnull Observable<Void> signalTagsChanged() {
    return currentStore().signalTagsChanged();
  }

  @Override public @Nonnull Observable<Void> signalToggleCalled() {
    return currentStore().signalToggleCalled();
  }

  protected @Nonnull HamsterDataSource currentStore() {
    return preferencesAdapter.isDatabaseRemote() ? dbusDataSource : dbDataSource;
  }

  //TODO: Add listening of preferencesAdapter to change source when preferences will change.
  //      In addition it should be proper place to init and deinit each dataSource objectes.
}
