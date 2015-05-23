package info.rynkowski.hamsterclient.data.dbus;

import android.util.Log;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

public abstract class RemoteObjectAbstract<T> implements RemoteObject<T> {

  public static final String TAG = "RemoteObjectAbstract";

  private DBusConnector connector;
  private String busName;
  private String objectPath;
  private Class dbusType;
  private T remoteObject;

  public RemoteObjectAbstract(DBusConnector connector, String busName, String objectPath,
      Class dbusType) {
    this.connector = connector;
    this.busName = busName;
    this.objectPath = objectPath;
    this.dbusType = dbusType;
    this.remoteObject = null;
  }

  protected boolean isPossessed() {
    return remoteObject != null;
  }

  @SuppressWarnings("unchecked") protected void possessRemoteObject() {
    Log.d(TAG, "Possesing remote object started");
    Log.d(TAG, "  busName = " + busName);
    Log.d(TAG, "  objectPath = " + objectPath);
    Log.d(TAG, "  dbusType = " + dbusType);

    if (!connector.isOpen()) {
      connector.open();
    }

    try {
      DBusConnection connection = connector.getConnection();
      Log.d(TAG, "connector.getConnection() = " + connection);
      remoteObject = (T) connection.getRemoteObject(busName, objectPath, dbusType);
      Log.d(TAG, "connection.getRemoteObject(" + busName + "), " + objectPath + ", "
            + dbusType + ") = " + remoteObject);
    } catch (DBusException e) {
      e.printStackTrace();
    }
  }

  @Override
  public T get() {
    if (!isPossessed()) {
      possessRemoteObject();
    }
    return remoteObject;
  }
}
