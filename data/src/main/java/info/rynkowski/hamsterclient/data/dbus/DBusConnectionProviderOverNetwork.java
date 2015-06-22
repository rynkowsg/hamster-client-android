package info.rynkowski.hamsterclient.data.dbus;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link DBusConnectionProvider} providing a {@link org.freedesktop.dbus.DBusConnection}
 * over the network.
 */
@Singleton
public class DBusConnectionProviderOverNetwork implements DBusConnectionProvider {

  private static final Logger log =
      LoggerFactory.getLogger(DBusConnectionProviderOverNetwork.class);

  private String addressHost;
  private String addressPort;

  private volatile DBusConnection connection;

  @Inject
  public DBusConnectionProviderOverNetwork(String addressHost, String addressPort) {
    this.addressHost = addressHost;
    this.addressPort = addressPort;
    this.connection = null;
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
    connection.disconnect();
    connection = null;
    log.info("D-Bus connection closed.");
  }

  private static String dbusAddress(String host, String port) {
    return "tcp:host=" + host + ",port=" + port;
  }
}
