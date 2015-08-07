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

  @Inject public HamsterRemoteObject() {
    super(busName, objectPath, dbusType);
  }

  public void registerSignal(SignalType type, DBusSigHandler<DBusSignal> callback)
      throws DBusConnectionNotReachableException, DBusInternalException {

    switch (type) {
      case ActivitiesChanged:
        registerSignalCallback(Hamster.ActivitiesChanged.class, callback);
        break;
      case FactsChanged:
        registerSignalCallback(Hamster.FactsChanged.class, callback);
        break;
      case TagsChanged:
        registerSignalCallback(Hamster.TagsChanged.class, callback);
        break;
      case ToggleCalled:
        registerSignalCallback(Hamster.ToggleCalled.class, callback);
        break;
      default:
        assert false : "Unknown signal type";
    }
  }

  public void unregisterSignal(SignalType type)
      throws DBusConnectionNotReachableException, DBusInternalException {

    switch (type) {
      case ActivitiesChanged:
        unregisterSignalCallbacks(Hamster.ActivitiesChanged.class);
        break;
      case FactsChanged:
        unregisterSignalCallbacks(Hamster.FactsChanged.class);
        break;
      case TagsChanged:
        unregisterSignalCallbacks(Hamster.TagsChanged.class);
        break;
      case ToggleCalled:
        unregisterSignalCallbacks(Hamster.ToggleCalled.class);
        break;
      default:
        assert false : "Unknown signal type";
    }
  }

  public enum SignalType {
    ActivitiesChanged, FactsChanged, TagsChanged, ToggleCalled
  }
}
