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

import javax.inject.Inject;
import javax.inject.Singleton;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ConnectionProvider} providing a {@link org.freedesktop.dbus.DBusConnection}
 * over the network.
 */
@Singleton
public class ConnectionProviderOverNetwork implements ConnectionProvider {

  private static final Logger log = LoggerFactory.getLogger(ConnectionProviderOverNetwork.class);

  private String addressHost;
  private String addressPort;

  private volatile DBusConnection connection;

  @Inject public ConnectionProviderOverNetwork(String addressHost, String addressPort) {
    this.addressHost = addressHost;
    this.addressPort = addressPort;
    this.connection = null;
  }

  private static String dbusAddress(String host, String port) {
    return "tcp:host=" + host + ",port=" + port;
  }

  @Override public synchronized DBusConnection get() throws DBusException {
    if (connection == null) {
      String address = dbusAddress(addressHost, addressPort);
      log.debug("Opening D-Bus connection on address: {}", address);
      connection = DBusConnection.getConnection(address);
      log.info("D-Bus connection has been established successfully.");
    }
    return connection;
  }

  @Override public void close() {
    if (connection != null) {
      connection.disconnect();
      connection = null;
      log.info("D-Bus connection closed.");
    }
  }
}
