package info.rynkowski.hamsterclient.data.dbus;

import javax.inject.Singleton;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DBusConnectorImpl implements DBusConnector {

  private static final Logger log = LoggerFactory.getLogger(DBusConnectorImpl.class);

  private DBusConnection connection;

  private String addressHost;
  private String addressPort;

  public DBusConnectorImpl(String host, String port) {
    this.connection = null;
    this.addressHost = host;
    this.addressPort = port;
  }

  @Override public void open() {
    if (!isOpen()) {
      long startTime = System.currentTimeMillis();
      try {
        String address = dbusAddress(addressHost, addressPort);
        log.debug("Opening D-Bus connection on address: {}", address);
        connection = DBusConnection.getConnection(address);
        log.info("D-Bus connection has been established successfully.");
      } catch (DBusException e) {
        e.printStackTrace();
        log.error("D-Bus connection has not been established.");
        connection = null;
      }
      long difference = System.currentTimeMillis() - startTime;
      log.debug("Getting dbus connection took {} seconds", difference / 1000);
    }
  }

  @Override public void close() {
    connection.disconnect();
    connection = null;
    log.info("D-Bus connection closed.");
  }

  @Override public Boolean isOpen() {
    return connection != null;
  }

  @Override public DBusConnection getConnection() {
    if (!isOpen()) {
      open();
    }
    return connection;
  }

  private static String dbusAddress(String host, String port) {
    return "tcp:host=" + host + ",port=" + port;
  }
}
