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
import javax.inject.Inject;
import javax.inject.Singleton;
import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.DBusSignal;
import org.gnome.Hamster;

/**
 * Wrapper class for {@link org.gnome.Hamster}'s remote object.
 */
@Singleton
public class HamsterRemoteObject extends RemoteObjectAbstract<Hamster> {

  private static final String busName = "org.gnome.Hamster";
  private static final String objectPath = "/org/gnome/Hamster";
  private static final Class dbusType = Hamster.class;

  @Inject public HamsterRemoteObject(ConnectionProvider connectionProvider) {
    super(connectionProvider, busName, objectPath, dbusType);
  }

  public void registerSignalActivitiesChanged(DBusSigHandler<DBusSignal> callback)
      throws DBusConnectionNotReachableException, DBusInternalException {
    registerSignalCallback(Hamster.ActivitiesChanged.class, callback);
  }

  public void registerSignalFactsChanged(DBusSigHandler<DBusSignal> callback)
      throws DBusConnectionNotReachableException, DBusInternalException {
    registerSignalCallback(Hamster.FactsChanged.class, callback);
  }

  public void registerSignalTagsChanged(DBusSigHandler<DBusSignal> callback)
      throws DBusConnectionNotReachableException, DBusInternalException {
    registerSignalCallback(Hamster.TagsChanged.class, callback);
  }

  public void registerSignalToggleCalled(DBusSigHandler<DBusSignal> callback)
      throws DBusConnectionNotReachableException, DBusInternalException {
    registerSignalCallback(Hamster.ToggleCalled.class, callback);
  }

  public void unregisterSignalActivitiesChanged()
      throws DBusConnectionNotReachableException, DBusInternalException {
    unregisterSignalCallbacks(Hamster.ActivitiesChanged.class);
  }

  public void unregisterSignalFactsChanged()
      throws DBusConnectionNotReachableException, DBusInternalException {
    unregisterSignalCallbacks(Hamster.FactsChanged.class);
  }

  public void unregisterSignalTagsChanged()
      throws DBusConnectionNotReachableException, DBusInternalException {
    unregisterSignalCallbacks(Hamster.TagsChanged.class);
  }

  public void unregisterSignalToggleCalled()
      throws DBusConnectionNotReachableException, DBusInternalException {
    unregisterSignalCallbacks(Hamster.ToggleCalled.class);
  }
}
