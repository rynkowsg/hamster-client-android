package info.rynkowski.hamsterclient.data.dbus;

import android.util.Log;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

public class DBusConnectorImpl implements DBusConnector {
  private static final String TAG = "DBusConnectorImpl";

  private DBusConnection connection;

  private String addressHost;
  private String addressPort;

  DBusConnectorImpl(String host, String port) {
    this.connection = null;
    this.addressHost = host;
    this.addressPort = port;
  }

  @Override public void open() {
    Log.d(TAG, "openDbusConnection()");
    new Thread(new Runnable() {
      @Override
      public void run() {
        Log.i(TAG, "Before get dbus connection");
        long startTime = System.currentTimeMillis();
        try {
          connection = DBusConnection.getConnection(dbusAddress(addressHost, addressPort));
        } catch (DBusException e) {
          e.printStackTrace();
          Log.i(TAG, "dbus connection isn't established, connection = " + connection);
          connection = null;
        }
        long difference = System.currentTimeMillis() - startTime;
        Log.i(TAG, "After get dbus connection: it takes " + difference / 1000 + " seconds");
      }
    }).start();
  }

  @Override public void close() {
      Log.d(TAG, "closeDbusConnection()");
      connection = null;
    }

  @Override public Boolean isOpen() {
    return connection != null;
  }

  @Override public DBusConnection getConnection() {
    return connection;
  }

  private static String dbusAddress(String host, String port) {
    return "tcp:host=" + host + ",port=" + port;
  }
}
