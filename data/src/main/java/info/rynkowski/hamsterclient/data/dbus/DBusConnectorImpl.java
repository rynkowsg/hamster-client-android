package info.rynkowski.hamsterclient.data.dbus;

import android.util.Log;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

public class DBusConnectorImpl implements DBusConnector {

  private static final String TAG = "DBusConnectorImpl";

  private DBusConnection connection;

  private String addressHost;
  private String addressPort;

  public DBusConnectorImpl(String host, String port) {
    this.connection = null;
    this.addressHost = host;
    this.addressPort = port;
  }

  @Override public void open() {
    long startTime = System.currentTimeMillis();
    try {
      String address = dbusAddress(addressHost, addressPort);
      Log.d(TAG, "Opening D-Bus connection on address \"" + address + "\"");
      connection = DBusConnection.getConnection(address);
      Log.i(TAG, "D-Bus connection has been established successfully.");
    } catch (DBusException e) {
      e.printStackTrace();
      Log.e(TAG, "D-Bus connection has not been established.");
      connection = null;
    }
    long difference = System.currentTimeMillis() - startTime;
    Log.d(TAG, "Getting dbus connection took " + difference / 1000 + " seconds");
  }

  @Override public void close() {
    connection.disconnect();
    connection = null;
    Log.i(TAG, "D-Bus connection closed.");
  }

  @Override public Boolean isOpen() {
    return connection != null;
  }

  @Override public DBusConnection getConnection() {
    if (!isOpen())
      open();
    return connection;
  }

  private static String dbusAddress(String host, String port) {
    return "tcp:host=" + host + ",port=" + port;
  }
}
