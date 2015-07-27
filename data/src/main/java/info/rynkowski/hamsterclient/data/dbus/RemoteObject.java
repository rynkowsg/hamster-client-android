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
import rx.Observable;

/**
 * Interface of wrapper to D-Bus remote object.
 *
 * @param <Type> type of D-Bus remote object
 */
public interface RemoteObject<Type> {

  Type get() throws DBusException;

  Observable<Type> getObservable();

  void registerSignalCallback(Class<? extends DBusSignal> signalClass,
      DBusSigHandler<DBusSignal> callback) throws DBusException;

  void unregisterSignalCallback(Class<? extends DBusSignal> signalClass,
      DBusSigHandler<DBusSignal> callback) throws DBusException;

  void unregisterAllSignalCallbacks() throws DBusException;

  Observable<Void> createSignalObservable(Class<? extends DBusSignal> signalClass);

  /**
   * Removes a retrieved remote object.
   */
  void deinit();
}
