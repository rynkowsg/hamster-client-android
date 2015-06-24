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

import info.rynkowski.hamsterclient.data.entity.FactEntity;
import info.rynkowski.hamsterclient.data.entity.mapper.FactEntityMapper;
import info.rynkowski.hamsterclient.data.repository.datasource.HamsterDataStoreFactory;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

/**
 * {@link HamsterRepository} for retrieving data.
 */
@Singleton
public class HamsterDataRepository implements HamsterRepository {

  private HamsterDataStoreFactory hamsterDataStoreFactory;
  private FactEntityMapper factEntityMapper;

  @Inject
  public HamsterDataRepository(HamsterDataStoreFactory hamsterDataStoreFactory,
      FactEntityMapper factEntityMapper) {
    this.hamsterDataStoreFactory = hamsterDataStoreFactory;
    this.factEntityMapper = factEntityMapper;
  }

  @Override public void initialize() {
    //empty
  }

  @Override public void deinitialize() {
    hamsterDataStoreFactory.getStore().deinitialize();
  }

  @Override public Observable<List<Fact>> getTodaysFacts() {
    return hamsterDataStoreFactory.getStore()
        .getTodaysFactEntities()
        .flatMap(Observable::from)
        .map(factEntityMapper::transform)
        .toList();
  }

  @Override public Observable<Integer> addFact(Fact fact) {
    FactEntity factEntity = factEntityMapper.transform(fact);
    return hamsterDataStoreFactory.getStore().addFactEntity(factEntity);
  }

  @Override public Observable<Void> signalActivitiesChanged() {
    return hamsterDataStoreFactory.getStore().signalActivitiesChanged();
  }

  @Override public Observable<Void> signalFactsChanged() {
    return hamsterDataStoreFactory.getStore().signalFactsChanged();
  }

  @Override public Observable<Void> signalTagsChanged() {
    return hamsterDataStoreFactory.getStore().signalTagsChanged();
  }

  @Override public Observable<Void> signalToggleCalled() {
    return hamsterDataStoreFactory.getStore().signalToggleCalled();
  }
}
