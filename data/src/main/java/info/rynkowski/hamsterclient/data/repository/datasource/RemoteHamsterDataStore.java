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

import com.google.common.base.Optional;
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

  @Override public @Nonnull Observable<Void> deinitialize() {
    hamsterObject.deinit();
    log.debug("Remote data store deinitialized.");
    return Observable.empty();
  }

  @Override public @Nonnull Observable<List<FactEntity>> getTodaysFactEntities() {
    return Observable.defer(hamsterObject::getObservable)
        .map(Hamster::GetTodaysFacts)
        .flatMap(Observable::from)
        .map(FactEntity::new)
        .map(FactEntity::timeFixRemoteToLocal)
        .toList();
  }

  @Override public @Nonnull Observable<Optional<Integer>> addFactEntity(
      @Nonnull FactEntity factEntity) {
    String serializedName = factEntity.serializedName();

    factEntity.timeFixLocalToRemote();
    int startTime = factEntity.getStartTime().getTimeInSeconds();
    int endTime =
        factEntity.getEndTime().isPresent() ? factEntity.getEndTime().get().getTimeInSeconds() : 0;

    return Observable.defer(hamsterObject::getObservable)
        .doOnNext(object -> {
          log.debug("Calling AddFact() on remote DBus object:");
          log.debug("    serializedName: \"{}\"", serializedName);
          log.debug("    startTime:      {}", startTime);
          log.debug("    endTime:        {}", endTime);
        })
        .map(remoteObject -> Optional.of(
            remoteObject.AddFact(serializedName, startTime, endTime, false)));
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
