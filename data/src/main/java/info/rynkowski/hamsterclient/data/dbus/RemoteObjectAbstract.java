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

package info.rynkowski.hamsterclient.data.dbus;

import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

/**
 * Abstract class that implements common utilities of each {@link RemoteObject}.
 *
 * @param <Type> type of D-Bus remote object
 */
public abstract class RemoteObjectAbstract<Type> implements RemoteObject<Type> {

  private static final Logger log = LoggerFactory.getLogger(RemoteObjectAbstract.class);
  private ConnectionProvider connectionProvider;
  private String busName;
  private String objectPath;
  private Class dbusType;
  private volatile Type remoteObject;
  private Observable<Type> remoteObjectObservable;

  public RemoteObjectAbstract(ConnectionProvider connectionProvider, String busName,
      String objectPath, Class dbusType) {
    this.connectionProvider = connectionProvider;
    this.busName = busName;
    this.objectPath = objectPath;
    this.dbusType = dbusType;
    this.remoteObject = null;
    this.remoteObjectObservable = null;
  }

  @SuppressWarnings("unchecked") @Override public synchronized Type get() throws DBusException {
    if (remoteObject == null) {
      log.debug("Possesing D-Bus remote object started:");
      log.debug("    busName:    {}", busName);
      log.debug("    objectPath: {}", objectPath);
      log.debug("    dbusType:   {}", dbusType);
      remoteObject = (Type) connectionProvider.get().getRemoteObject(busName, objectPath, dbusType);
    }
    return remoteObject;
  }

  @Override public synchronized Observable<Type> getObservable() {
    if (remoteObjectObservable == null) {
      Type object;
      try {
        object = RemoteObjectAbstract.this.get();
      } catch (DBusException e) {
        log.error("Exception threw during retrieving D-Bus remote object!", e);
        return Observable.error(e);
      }
      remoteObjectObservable = Observable.just(object);
    }
    return remoteObjectObservable;
  }

  @Override @SuppressWarnings("unchecked")
  public void registerSignalCallback(Class<? extends DBusSignal> signalClass,
      DBusSigHandler<DBusSignal> callback) throws DBusException {
    connectionProvider.get().addSigHandler((Class<DBusSignal>) signalClass, callback);
  }

  @Override
  public Observable<Void> createSignalObservable(Class<? extends DBusSignal> signalClass) {
    return Observable.create(subscriber -> {
      try {
        registerSignalCallback(signalClass, signal -> subscriber.onNext(null));
      } catch (DBusException e) {
        e.printStackTrace();
        log.error("Exception threw during registering a signal! ", e);
        subscriber.onError(e);
      }
    });
  }

  @Override public void clear() {
    remoteObject = null;
    remoteObjectObservable = null;
  }
}
