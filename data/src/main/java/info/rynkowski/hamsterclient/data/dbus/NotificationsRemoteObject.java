package info.rynkowski.hamsterclient.data.dbus;

import org.freedesktop.Notifications;

public class NotificationsRemoteObject extends RemoteObjectAbstract<Notifications> {
  private static final String busName = "org.freedesktop.Notifications";
  private static final String objectPath = "/org/freedesktop/Notifications";
  private static final Class dbusType = Notifications.class;

  public NotificationsRemoteObject(DBusConnector connector) {
    super(connector, busName, objectPath, dbusType);
  }
}
