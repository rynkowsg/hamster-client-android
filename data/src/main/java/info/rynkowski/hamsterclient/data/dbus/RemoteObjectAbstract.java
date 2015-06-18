package info.rynkowski.hamsterclient.data.dbus;

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RemoteObjectAbstract<Type> implements RemoteObject<Type> {

  private static final Logger log = LoggerFactory.getLogger(RemoteObjectAbstract.class);

  private DBusConnectionProvider connectionProvider;
  private String busName;
  private String objectPath;
  private Class dbusType;
  private Type remoteObject;

  public RemoteObjectAbstract(DBusConnectionProvider connectionProvider, String busName,
      String objectPath, Class dbusType) {
    this.connectionProvider = connectionProvider;
    this.busName = busName;
    this.objectPath = objectPath;
    this.dbusType = dbusType;
    this.remoteObject = null;
  }

  @SuppressWarnings("unchecked") protected void possessRemoteObject() {
    try {
      DBusConnection connection = connectionProvider.get();

      log.debug("Possesing DBus remote object started:");
      log.debug("    busName:    {}", busName);
      log.debug("    objectPath: {}", objectPath);
      log.debug("    dbusType:   {}", dbusType);

      remoteObject = (Type) connection.getRemoteObject(busName, objectPath, dbusType);
    } catch (DBusException e) {
      e.printStackTrace();
    }
  }

  @Override public Type get() {
    if (remoteObject == null) {
      possessRemoteObject();
    }
    return remoteObject;
  }
}
