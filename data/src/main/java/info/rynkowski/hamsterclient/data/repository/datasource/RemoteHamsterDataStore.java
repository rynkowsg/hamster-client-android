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

import info.rynkowski.hamsterclient.data.dbus.HamsterRemoteObject;
import info.rynkowski.hamsterclient.data.entity.FactEntity;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.gnome.Hamster;
import rx.Observable;

/**
 * {@link HamsterDataStore} based on a remote D-Bus connection.
 */
@Slf4j
@Singleton
public class RemoteHamsterDataStore implements HamsterDataStore {

  private HamsterRemoteObject hamsterObject;

  @Inject public RemoteHamsterDataStore(HamsterRemoteObject hamsterRemoteObject) {
    this.hamsterObject = hamsterRemoteObject;
  }

  @Override public Observable<Void> initialize() {
    return hamsterObject.getObservable()
        .doOnNext(hamster -> log.debug("Remote data store initialized."))
        .flatMap(object -> Observable.empty());
  }

  @Override public Observable<Void> deinitialize() {
    hamsterObject.deinit();
    log.debug("Remote data store deinitialized.");
    return Observable.empty();
  }

  @Override public Observable<List<FactEntity>> getTodaysFactEntities() {
    return Observable.defer(hamsterObject::getObservable)
        .map(Hamster::GetTodaysFacts)
        .flatMap(Observable::from)
        .map(FactEntity::new)
        .toList();
  }

  @Override public Observable<Integer> addFactEntity(FactEntity factEntity) {
    String serializedName = factEntity.serializedName();
    int startTime = factEntity.getStartTime();
    int endTime = factEntity.getEndTime();

    return Observable.defer(hamsterObject::getObservable)
        .doOnNext(object -> {
          log.debug("Calling AddFact() on remote DBus object:");
          log.debug("    serializedName: \"{}\"", serializedName);
          log.debug("    startTime:      {}", startTime);
          log.debug("    endTime:        {}", endTime);
        })
        .map(remoteObject -> remoteObject.AddFact(serializedName, startTime, endTime, false));
  }

  @Override public Observable<Void> signalActivitiesChanged() {
    return hamsterObject.signalActivitiesChanged();
  }

  @Override public Observable<Void> signalFactsChanged() {
    return hamsterObject.signalFactsChanged();
  }

  @Override public Observable<Void> signalTagsChanged() {
    return hamsterObject.signalTagsChanged();
  }

  @Override public Observable<Void> signalToggleCalled() {
    return hamsterObject.signalToggleCalled();
  }
}
