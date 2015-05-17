package info.rynkowski.hamsterclient.data.dbus;

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

public abstract class RemoteObjectAbstract<T> implements RemoteObject<T> {
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

  @SuppressWarnings("unchecked")
  protected void possessRemoteObject() {
    if (connector.isOpen()) {
      try {
        DBusConnection connection = connector.getConnection();
        remoteObject = (T) connection.getRemoteObject(busName, objectPath, dbusType);
      } catch (DBusException e) {
        e.printStackTrace();
      }
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
