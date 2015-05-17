package info.rynkowski.hamsterclient.data.dbus;

import org.freedesktop.dbus.DBusConnection;

public interface DBusConnector {
  public void open();
  public void close();
  public Boolean isOpen();
  public DBusConnection getConnection();
}
