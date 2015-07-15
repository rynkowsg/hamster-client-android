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

  //private PublishSubject<Status> status;

  @Inject public HamsterRepositoryImpl(@Named("local") @Nonnull HamsterDataStore localStore,
      @Named("remote") @Nonnull HamsterDataStore remoteStore,
      @Nonnull FactEntityMapper factEntityMapper) {

    this.factEntityMapper = factEntityMapper;
    this.localStore = localStore;
    this.remoteStore = remoteStore;

    //this.status = PublishSubject.create();
  }

  //@Override public void initialize(@Nonnull Type type) {
  //  log.debug("Entering initialize(type={})", type);
  //  switch (type) {
  //    case LOCAL:
  //      localStore.initialize().subscribe(new OnCompletedObserver<Void>() {
  //        @Override public void onCompleted() {
  //          log.info("Initialized a LOCAL store");
  //          currentStore = localStore;
  //          status.onNext(Status.SWITCHED_TO_LOCAL);
  //        }
  //        @Override public void onError(Throwable e) {
  //          log.warn("Initializing a REMOTE store failed.");
  //          status.onNext(Status.LOCAL_UNAVAILABLE);
  //        }
  //      });
  //      break;
  //    case REMOTE:
  //      remoteStore.initialize().subscribe(new OnCompletedObserver<Void>() {
  //        @Override public void onCompleted() {
  //          log.info("Initialized a REMOTE store");
  //          currentStore = remoteStore;
  //          status.onNext(Status.SWITCHED_TO_REMOTE);
  //        }
  //        @Override public void onError(Throwable e) {
  //          log.warn("Initializing a REMOTE store failed.");
  //          status.onNext(Status.REMOTE_UNAVAILABLE);
  //        }
  //      });
  //      break;
  //    default:
  //      throw new RuntimeException("Unknown repository type: " + type);
  //  }
  //}
  //
  //@Override public void deinitialize() {
  //  log.debug("Entering deinitialize()");
  //  if (currentStore != null) {
  //    currentStore.deinitialize();
  //  }
  //}

  @Override public @Nonnull Observable<List<Fact>> getTodaysFacts() {
    return remoteStore.getTodaysFactEntities()
        .flatMap(Observable::from)
        .map(factEntityMapper::transform)
        .toList();
  }

  @Override public @Nonnull Observable<Integer> addFact(@Nonnull Fact fact) {
    return Observable.just(fact)
        .map(factEntityMapper::transform)
        .flatMap(remoteStore::addFactEntity);
  }

  @Override public @Nonnull Observable<Integer> updateFact(@Nonnull Fact fact) {
    return Observable.just(fact)
        .map(factEntityMapper::transform)
        .flatMap(remoteStore::updateFactEntity);
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

  //@Override public Observable<Status> onChange() {
  //  return status
  //      .doOnNext(status1 -> log.debug("doOnNext, status: {}", status1))
  //      .doOnUnsubscribe(() -> log.debug("doOnUnsubscribe"))
  //      .doOnCompleted(() -> log.debug("doOnCompleted"))
  //      .doOnSubscribe(() -> log.debug("doOnSubscribe"))
  //      .doOnTerminate(() -> log.debug("doOnTerminate"))
  //      .doOnError(throwable -> log.debug("doOnError", throwable))
  //      ;
  //}
}
