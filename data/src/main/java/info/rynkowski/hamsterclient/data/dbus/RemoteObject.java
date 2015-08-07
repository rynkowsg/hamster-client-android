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

import info.rynkowski.hamsterclient.data.dbus.exception.DBusConnectionNotReachableException;
import info.rynkowski.hamsterclient.data.dbus.exception.DBusInternalException;
import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.DBusSignal;

/**
 * Interface of wrapper to D-Bus remote object.
 *
 * @param <Type> type of D-Bus remote object
 */
public interface RemoteObject<Type> {

  void setConnectionProvider(ConnectionProvider connectionProvider);

  Type get() throws DBusConnectionNotReachableException, DBusInternalException;

  void registerSignalCallback(Class<? extends DBusSignal> signalClass,
      DBusSigHandler<DBusSignal> callback)
      throws DBusConnectionNotReachableException, DBusInternalException;

  void unregisterSignalCallback(Class<? extends DBusSignal> signalClass,
      DBusSigHandler<DBusSignal> callback)
      throws DBusConnectionNotReachableException, DBusInternalException;

  void unregisterSignalCallbacks(Class<? extends DBusSignal> signalClass)
      throws DBusConnectionNotReachableException, DBusInternalException;

  void unregisterSignalCallbacks()
      throws DBusConnectionNotReachableException, DBusInternalException;

  /**
   * Removes a retrieved remote object.
   */
  void deinit();
}
