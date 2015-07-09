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
import info.rynkowski.hamsterclient.data.repository.datasource.HamsterDataStore;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Implementation of {@link info.rynkowski.hamsterclient.domain.repository.HamsterRepository}.
 */
@Slf4j
@Singleton
public class HamsterRepositoryImpl implements HamsterRepository {

  private FactEntityMapper factEntityMapper;

  private HamsterDataStore localStore;
  private HamsterDataStore remoteStore;
  private volatile HamsterDataStore currentStore;

  //private PublishSubject<Status> status;

  @Inject public HamsterRepositoryImpl(@Named("remote") HamsterDataStore remoteHamsterDataStore,
      FactEntityMapper factEntityMapper) {
    this.factEntityMapper = factEntityMapper;
    this.remoteStore = remoteHamsterDataStore;
    this.localStore = null; // TODO: provide also a local database
    this.currentStore = remoteStore;

    //this.status = PublishSubject.create();
  }

  //@Override public void initialize(Type type) {
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

  @Override public Observable<List<Fact>> getTodaysFacts() {
    if (currentStore == null) {
      return Observable.error(new RuntimeException(
          "Repository should be initialized! Take a look at method initialize(Type type)."));
    }
    return currentStore.getTodaysFactEntities()
        .flatMap(Observable::from)
        .map(factEntityMapper::transform)
        .toList();
  }

  @Override public Observable<Integer> addFact(Fact fact) {
    if (currentStore == null) {
      return Observable.error(new RuntimeException(
          "Repository should be initialized! Take a look at method initialize(Type type)."));
    }
    FactEntity factEntity = factEntityMapper.transform(fact);
    return currentStore.addFactEntity(factEntity);
  }

  @Override public Observable<Void> signalActivitiesChanged() {
    if (currentStore == null) {
      return Observable.error(new RuntimeException(
          "Repository should be initialized! Take a look at method initialize(Type type)."));
    }
    return currentStore.signalActivitiesChanged();
  }

  @Override public Observable<Void> signalFactsChanged() {
    if (currentStore == null) {
      return Observable.error(new RuntimeException(
          "Repository should be initialized! Take a look at method initialize(Type type)."));
    }
    return currentStore.signalFactsChanged();
  }

  @Override public Observable<Void> signalTagsChanged() {
    if (currentStore == null) {
      return Observable.error(new RuntimeException(
          "Repository should be initialized! Take a look at method initialize(Type type)."));
    }
    return currentStore.signalTagsChanged();
  }

  @Override public Observable<Void> signalToggleCalled() {
    if (currentStore == null) {
      return Observable.error(new RuntimeException(
          "Repository should be initialized! Take a look at method initialize(Type type)."));
    }
    return currentStore.signalToggleCalled();
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
