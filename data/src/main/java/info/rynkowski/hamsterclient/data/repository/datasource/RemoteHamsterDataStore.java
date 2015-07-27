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
import javax.annotation.Nonnull;
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

  private @Nonnull HamsterRemoteObject hamsterObject;

  @Inject public RemoteHamsterDataStore(@Nonnull HamsterRemoteObject hamsterRemoteObject) {
    this.hamsterObject = hamsterRemoteObject;
  }

  @Override public @Nonnull Observable<Void> initialize() {
    return hamsterObject.getObservable()
        .doOnNext(hamster -> log.debug("Remote data store initialized."))
        .flatMap(object -> Observable.empty());
  }

  //TODO: The method is never called.
  @Override public @Nonnull Observable<Void> deinitialize() {
    return Observable.create(subscriber -> {
      hamsterObject.deinit();
      log.debug("Remote data store deinitialized.");
      subscriber.onCompleted();
    });
  }

  @Override public @Nonnull Observable<List<FactEntity>> getTodaysFacts() {
    return Observable.defer(hamsterObject::getObservable)
        .map(Hamster::GetTodaysFacts)
        .flatMap(Observable::from)
        .map(FactEntity::new)
        .map(FactEntity::timeFixRemoteToLocal)
        .toList();
  }

  @Override public @Nonnull Observable<Integer> addFact(@Nonnull FactEntity fact) {
    String serializedName = fact.serializedName();

    fact.timeFixLocalToRemote();
    int startTime = fact.getStartTime().getTimeInSeconds();
    int endTime = fact.getEndTime().isPresent() ? fact.getEndTime().get().getTimeInSeconds() : 0;

    return Observable.defer(hamsterObject::getObservable).
        doOnNext(object -> {
          log.debug("Calling AddFact() on remote DBus object:");
          log.debug("    id:             {}",
              fact.getId().isPresent() ? fact.getId().get() : "absent");
          log.debug("    serializedName: \"{}\"", serializedName);
          log.debug("    startTime:      {}", startTime);
          log.debug("    endTime:        {}", endTime);
        }).
        map(remoteObject -> remoteObject.AddFact(serializedName, startTime, endTime, false));
  }

  @Override public @Nonnull Observable<Void> removeFact(@Nonnull Integer id) {
    return Observable.defer(hamsterObject::getObservable).
        doOnNext(object -> {
          log.debug("Calling RemoveFact() on remote DBus object:");
          log.debug("    id:             {}", id);
        }).
        flatMap(remoteObject -> {
          remoteObject.RemoveFact(id);
          return Observable.<Void>empty();
        });
  }

  @Override public @Nonnull Observable<Integer> updateFact(@Nonnull FactEntity fact) {
    String serializedName = fact.serializedName();

    fact.timeFixLocalToRemote();
    int startTime = fact.getStartTime().getTimeInSeconds();
    int endTime = fact.getEndTime().isPresent() ? fact.getEndTime().get().getTimeInSeconds() : 0;

    return Observable.defer(hamsterObject::getObservable).
        doOnNext(object -> {
          log.debug("Calling UpdateFact() on remote DBus object:");
          log.debug("    id:             {}",
              fact.getId().isPresent() ? fact.getId().get() : "absent");
          log.debug("    serializedName: \"{}\"", serializedName);
          log.debug("    startTime:      {}", startTime);
          log.debug("    endTime:        {}", endTime);
        }).
        map(remoteObject -> remoteObject.UpdateFact(fact.getId().get(), serializedName, startTime,
            endTime, false, false));
  }

  @Override public @Nonnull Observable<Void> signalActivitiesChanged() {
    return hamsterObject.signalActivitiesChanged();
  }

  @Override public @Nonnull Observable<Void> signalFactsChanged() {
    return hamsterObject.signalFactsChanged();
  }

  @Override public @Nonnull Observable<Void> signalTagsChanged() {
    return hamsterObject.signalTagsChanged();
  }

  @Override public @Nonnull Observable<Void> signalToggleCalled() {
    return hamsterObject.signalToggleCalled();
  }
}
