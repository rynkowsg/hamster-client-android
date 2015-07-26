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

import info.rynkowski.hamsterclient.data.utils.PreferencesContainer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

/**
 * {@link ConnectionProvider} providing a {@link org.freedesktop.dbus.DBusConnection}
 * over the network.
 */
@Slf4j
@Singleton
public class ConnectionProviderOverNetwork implements ConnectionProvider {

  private @Nullable DBusConnection connection;
  private final @Nonnull PreferencesContainer preferences;

  @Inject public ConnectionProviderOverNetwork(@Nonnull PreferencesContainer preferences) {
    this.preferences = preferences;
    this.connection = null;
  }

  @Override public synchronized DBusConnection get() throws DBusException {
    if (connection == null) {
      String address = dbusAddress(preferences.dbusHost(), preferences.dbusPort());
      log.debug("Opening D-Bus connection on address: {}", address);
      connection = DBusConnection.getConnection(address);
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
