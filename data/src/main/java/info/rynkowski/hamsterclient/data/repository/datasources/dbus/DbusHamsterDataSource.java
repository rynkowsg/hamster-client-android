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

package info.rynkowski.hamsterclient.data.repository.datasources.dbus;

import info.rynkowski.hamsterclient.data.dbus.ConnectionProvider;
import info.rynkowski.hamsterclient.data.dbus.ConnectionProviderOverNetwork;
import info.rynkowski.hamsterclient.data.dbus.exception.DBusConnectionNotReachableException;
import info.rynkowski.hamsterclient.data.dbus.exception.DBusInternalException;
import info.rynkowski.hamsterclient.data.preferences.Preferences;
import info.rynkowski.hamsterclient.data.repository.datasources.HamsterDataSource;
import info.rynkowski.hamsterclient.data.repository.datasources.dbus.entities.DbusFact;
import info.rynkowski.hamsterclient.data.repository.datasources.dbus.entities.mapper.DbusFactMapper;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.gnome.Hamster;
import rx.Observable;
import rx.Subscription;

/**
 * {@link HamsterDataSource} based on a remote D-Bus connection.
 */
@Slf4j
@Singleton
public class DbusHamsterDataSource implements HamsterDataSource {

  private final @Nonnull HamsterRemoteObject hamsterObject;
  private final @Nonnull Preferences preferences;
  private final @Nonnull DbusFactMapper mapper;

  private @Nullable Subscription subscriptionSignalOnChanged;

  @Inject public DbusHamsterDataSource(@Nonnull HamsterRemoteObject hamsterRemoteObject,
      @Nonnull Preferences preferences, @Nonnull DbusFactMapper mapper) {
    this.hamsterObject = hamsterRemoteObject;
    this.preferences = preferences;
    this.mapper = mapper;

    updateConnectionProvider();
    registerOnAddressChangedHandler();
  }

  private void registerOnAddressChangedHandler() {
    subscriptionSignalOnChanged = preferences.signalOnChanged()
        .filter(type -> type == Preferences.Type.DbusHost
            || type == Preferences.Type.DbusPort)
        .doOnNext(type -> log.debug("Received signalOnChanged, changedType: {}", type))
        .subscribe(type -> updateConnectionProvider());
  }

  private void updateConnectionProvider() {
    ConnectionProvider connectionProvider =
        new ConnectionProviderOverNetwork(preferences.dbusHost(), preferences.dbusPort());
    hamsterObject.setConnectionProvider(connectionProvider);
  }

  @Override public @Nonnull Observable<Void> initialize() {
    return getHamsterObjectObservable() //
        .doOnNext(hamster -> log.debug("Remote data store initialized."))
        .flatMap(object -> Observable.empty());
  }

  //TODO: The method is never called.
  @Override public @Nonnull Observable<Void> deinitialize() {
    return Observable.create(subscriber -> {
      hamsterObject.deinit();
      if (subscriptionSignalOnChanged != null) {
        subscriptionSignalOnChanged.unsubscribe();
      }
      log.debug("Remote data store deinitialized.");
      subscriber.onCompleted();
    });
  }

  @Override public @Nonnull Observable<List<Fact>> getTodaysFacts() {
    return getHamsterObjectObservable() //
        .map(Hamster::GetTodaysFacts)
        .flatMap(Observable::from)
        .map(DbusFact::new)
        .map(DbusFact::timeFixRemoteToLocal)
        .map(mapper::transform)
        .toList();
  }

  @Override public @Nonnull Observable<Integer> addFact(@Nonnull Fact fact) {
    DbusFact dbusFact = mapper.transform(fact).timeFixLocalToRemote();

    String serializedName = dbusFact.serializedName();

    int startTime = dbusFact.getStartTime().getTimeInSeconds();
    int endTime =
        dbusFact.getEndTime().isPresent() ? dbusFact.getEndTime().get().getTimeInSeconds() : 0;

    return getHamsterObjectObservable().
        doOnNext(object -> {
          log.debug("Calling AddFact() on remote DBus object:");
          log.debug("    id:             {}",
              dbusFact.getId().isPresent() ? dbusFact.getId().get() : "absent");
          log.debug("    serializedName: \"{}\"", serializedName);
          log.debug("    startTime:      {}", startTime);
          log.debug("    endTime:        {}", endTime);
        }).
        map(remoteObject -> remoteObject.AddFact(serializedName, startTime, endTime, false));
  }

  @Override public @Nonnull Observable<Void> removeFact(@Nonnull Integer id) {
    return getHamsterObjectObservable() //
        .doOnNext(object -> {
          log.debug("Calling RemoveFact() on remote DBus object:");
          log.debug("    id:             {}", id);
        }) //
        .flatMap(remoteObject -> {
          remoteObject.RemoveFact(id);
          return Observable.<Void>empty();
        });
  }

  @Override public @Nonnull Observable<Integer> updateFact(@Nonnull Fact fact) {
    DbusFact dbusFact = mapper.transform(fact);

    String serializedName = dbusFact.serializedName();

    dbusFact.timeFixLocalToRemote();
    int startTime = dbusFact.getStartTime().getTimeInSeconds();
    int endTime =
        dbusFact.getEndTime().isPresent() ? dbusFact.getEndTime().get().getTimeInSeconds() : 0;

    return getHamsterObjectObservable() //
        .doOnNext(object -> {
          log.debug("Calling UpdateFact() on remote DBus object:");
          log.debug("    id:             {}",
              dbusFact.getId().isPresent() ? dbusFact.getId().get() : "absent");
          log.debug("    serializedName: \"{}\"", serializedName);
          log.debug("    startTime:      {}", startTime);
          log.debug("    endTime:        {}", endTime);
        })
        .map(remoteObject -> remoteObject.UpdateFact(dbusFact.getId().get(), serializedName,
            startTime, endTime, false, false));
  }

  @Override public @Nonnull Observable<Void> signalActivitiesChanged() {
    return produceSignal(HamsterRemoteObject.SignalType.ActivitiesChanged);
  }

  @Override public @Nonnull Observable<Void> signalFactsChanged() {
    return produceSignal(HamsterRemoteObject.SignalType.FactsChanged);
  }

  @Override public @Nonnull Observable<Void> signalTagsChanged() {
    return produceSignal(HamsterRemoteObject.SignalType.TagsChanged);
  }

  @Override public @Nonnull Observable<Void> signalToggleCalled() {
    return produceSignal(HamsterRemoteObject.SignalType.ToggleCalled);
  }

  private Observable<Hamster> getHamsterObjectObservable() {
    return Observable.defer(() -> {
      try {
        return Observable.just(hamsterObject.get());
      } catch (DBusConnectionNotReachableException | DBusInternalException e) {
        return Observable.error(e);
      }
    });
  }

  private Observable<Void> produceSignal(@Nonnull HamsterRemoteObject.SignalType signalType) {
    return Observable.<Void>create(//
        subscriber -> {
          try {
            hamsterObject.registerSignal(signalType, signal -> subscriber.onNext(null));
          } catch (DBusConnectionNotReachableException | DBusInternalException e) {
            subscriber.onError(e);
          }
        })//
        .doOnUnsubscribe(() -> {
          try {
            hamsterObject.unregisterSignal(signalType);
          } catch (DBusConnectionNotReachableException | DBusInternalException e) {
            log.error("Exception thrown during unregistering signal.", e);
          }
        });
  }
}
