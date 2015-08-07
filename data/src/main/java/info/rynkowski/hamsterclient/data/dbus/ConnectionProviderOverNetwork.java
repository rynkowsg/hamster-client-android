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
import info.rynkowski.hamsterclient.data.utils.PreferencesAdapter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

/**
 * {@link ConnectionProvider} providing a {@link org.freedesktop.dbus.DBusConnection}
 * over the network.
 */
@Slf4j
public class ConnectionProviderOverNetwork implements ConnectionProvider {

  private final @Nonnull String host;
  private final @Nonnull String port;
  private @Nullable DBusConnection connection;

  public ConnectionProviderOverNetwork(@Nonnull String host, @Nonnull String port) {
    this.host = host;
    this.port = port;
    this.connection = null;
  }

  @Override public synchronized DBusConnection get() throws DBusConnectionNotReachableException {
    if (connection == null) {
      String address = dbusAddress(host, port);
      log.debug("Opening D-Bus connection on address: {}", address);
      try {
        connection = DBusConnection.getConnection(address);
      } catch (DBusException e) {
        throw new DBusConnectionNotReachableException("Can not establish a D-Bus connection.", e);
      }
      log.info("D-Bus connection has been established successfully.");
    }
    return connection;
  }

  @Override public synchronized void close() {
    if (connection != null) {
      connection.disconnect();
      connection = null;
      log.info("D-Bus connection closed.");
    }
  }

  private static String dbusAddress(@Nonnull String host, @Nonnull String port) {
    return "tcp:host=" + host + ",port=" + port;
  }
}
