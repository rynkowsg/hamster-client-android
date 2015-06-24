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

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

/**
 * Interface representing a connection provider for {@link org.freedesktop.dbus.DBusConnection}s.
 */
public interface ConnectionProvider {

  /**
   * Returns {@link org.freedesktop.dbus.DBusConnection}s.
   *
   * @return a {@link org.freedesktop.dbus.DBusConnection}.
   * @throws DBusException is an exception from D-Bus library.
   */
  DBusConnection get() throws DBusException;

  /**
   * Closes the connection provider and releases its resources.
   */
  void close();
}
