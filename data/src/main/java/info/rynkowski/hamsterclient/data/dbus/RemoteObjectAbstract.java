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

import com.google.common.base.Preconditions;
import info.rynkowski.hamsterclient.data.dbus.exception.DBusConnectionNotReachableException;
import info.rynkowski.hamsterclient.data.dbus.exception.DBusInternalException;
import java.util.HashMap;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.exceptions.DBusException;

/**
 * Abstract class that implements common utilities of each {@link RemoteObject}.
 *
 * @param <Type> type of D-Bus remote object
 */
@Slf4j
public abstract class RemoteObjectAbstract<Type> implements RemoteObject<Type> {

  private final String busName;
  private final String objectPath;
  private final Class dbusType;
  private ConnectionProvider connectionProvider;

  private volatile Type remoteObject;

  private @Nonnull HashMap<DBusSigHandler<DBusSignal>, Class<? extends DBusSignal>>
      registeredSignals = new HashMap<>();

  public RemoteObjectAbstract(String busName, String objectPath, Class dbusType) {
    this.busName = busName;
    this.objectPath = objectPath;
    this.dbusType = dbusType;
    this.connectionProvider = null;
    this.remoteObject = null;
  }

  @Override public void setConnectionProvider(ConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
  }

  @SuppressWarnings("unchecked") @Override public synchronized Type get()
      throws DBusConnectionNotReachableException, DBusInternalException {

    Preconditions.checkNotNull(connectionProvider,
        "connectionProvider can not be null. You should use setConnectionProvider() earlier.");

    if (remoteObject == null) {
      log.debug("Possesing a D-Bus remote object started:");
      log.debug("    busName:    {}", busName);
      log.debug("    objectPath: {}", objectPath);
      log.debug("    dbusType:   {}", dbusType);

      DBusConnection dBusConnection = connectionProvider.get();
      try {
        remoteObject = (Type) dBusConnection.getRemoteObject(busName, objectPath, dbusType);
      } catch (DBusException e) {
        throw new DBusInternalException(
            "An exception thrown during retrieving a D-Bus remote object", e);
      }
      log.debug("A D-Bus remote object possessed successfully.");
    }
    return remoteObject;
  }

  @Override @SuppressWarnings("unchecked")
  public void registerSignalCallback(Class<? extends DBusSignal> signalClass,
      DBusSigHandler<DBusSignal> callback)
      throws DBusConnectionNotReachableException, DBusInternalException {

    log.debug("registerSignalCallback(signalClass={}, callback={})", signalClass, callback);

    Preconditions.checkNotNull(connectionProvider,
        "connectionProvider can not be null. You should use setConnectionProvider() earlier.");

    try {
      connectionProvider.get().addSigHandler((Class<DBusSignal>) signalClass, callback);
    } catch (DBusException e) {
      throw new DBusInternalException("An exception thrown during registering a signal!", e);
    }
    registeredSignals.put(callback, signalClass);
  }

  //TODO: Rewrite to unregisterSignalCallback(DBusSigHandler<DBusSignal> callback)
  //      In fact, the callback is a key in the hashmap (registeredSignals)
  @Override @SuppressWarnings("unchecked") public void unregisterSignalCallback(
      Class<? extends DBusSignal> signalClass, DBusSigHandler<DBusSignal> callback)
      throws DBusConnectionNotReachableException, DBusInternalException {

    log.debug("un-registerSignalCallback(signalClass={}, callback={})", signalClass, callback);

    Preconditions.checkNotNull(connectionProvider,
        "connectionProvider can not be null. You should use setConnectionProvider() earlier.");

    try {
      connectionProvider.get().removeSigHandler((Class<DBusSignal>) signalClass, callback);
    } catch (DBusException e) {
      throw new DBusInternalException("An exception thrown during un-registering a signal!", e);
    }
    registeredSignals.remove(callback);
  }

  @Override public void unregisterSignalCallbacks(Class<? extends DBusSignal> signalClass)
      throws DBusConnectionNotReachableException, DBusInternalException {

    for (HashMap.Entry<DBusSigHandler<DBusSignal>, Class<? extends DBusSignal>> entry : registeredSignals
        .entrySet()) {
      if (entry.getValue() == signalClass) {
        unregisterSignalCallback(entry.getValue(), entry.getKey());
      }
    }
  }

  @Override public void unregisterSignalCallbacks()
      throws DBusConnectionNotReachableException, DBusInternalException {

    for (HashMap.Entry<DBusSigHandler<DBusSignal>, Class<? extends DBusSignal>> entry : registeredSignals
        .entrySet()) {
      unregisterSignalCallback(entry.getValue(), entry.getKey());
    }
  }

  @Override public synchronized void deinit() {
    Preconditions.checkNotNull(connectionProvider,
        "connectionProvider can not be null. You should use setConnectionProvider() earlier.");

    try {
      unregisterSignalCallbacks();
    } catch (DBusConnectionNotReachableException | DBusInternalException e) {
      log.error("Exception during callbacks' un-registering", e);
    }
    remoteObject = null;
    connectionProvider.close();
    log.info("De-initialized successfully a D-Bus remote object.");
  }
}
