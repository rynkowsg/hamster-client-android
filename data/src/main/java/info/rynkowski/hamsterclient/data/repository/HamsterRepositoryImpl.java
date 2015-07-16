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

import info.rynkowski.hamsterclient.data.entity.mapper.FactEntityMapper;
import info.rynkowski.hamsterclient.data.repository.datasource.HamsterDataStore;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;

/**
 * Implementation of {@link info.rynkowski.hamsterclient.domain.repository.HamsterRepository}.
 */
@Slf4j
@Singleton
public class HamsterRepositoryImpl implements HamsterRepository {

  private @Nonnull FactEntityMapper factEntityMapper;

  private volatile @Nonnull HamsterDataStore localStore;
  private volatile @Nonnull HamsterDataStore remoteStore;

  @Inject public HamsterRepositoryImpl(@Named("local") @Nonnull HamsterDataStore localStore,
      @Named("remote") @Nonnull HamsterDataStore remoteStore,
      @Nonnull FactEntityMapper factEntityMapper) {

    this.factEntityMapper = factEntityMapper;
    this.localStore = localStore;
    this.remoteStore = remoteStore;
  }

  @Override public @Nonnull Observable<List<Fact>> getTodaysFacts() {
    return remoteStore.getTodaysFacts()
        .flatMap(Observable::from)
        .map(factEntityMapper::transform)
        .toList();
  }

  @Override public @Nonnull Observable<Integer> addFact(@Nonnull Fact fact) {
    return Observable.just(fact)
        .map(factEntityMapper::transform)
        .flatMap(remoteStore::addFact);
  }

  @Override public @Nonnull Observable<Void> removeFact(@Nonnull Integer id) {
    return remoteStore.removeFact(id);
  }

  @Override public @Nonnull Observable<Integer> updateFact(@Nonnull Fact fact) {
    return Observable.just(fact)
        .map(factEntityMapper::transform)
        .flatMap(remoteStore::updateFact);
  }

  @Override public @Nonnull Observable<Void> signalActivitiesChanged() {
    return remoteStore.signalActivitiesChanged();
  }

  @Override public @Nonnull Observable<Void> signalFactsChanged() {
    return remoteStore.signalFactsChanged();
  }

  @Override public @Nonnull Observable<Void> signalTagsChanged() {
    return remoteStore.signalTagsChanged();
  }

  @Override public @Nonnull Observable<Void> signalToggleCalled() {
    return remoteStore.signalToggleCalled();
  }
}
