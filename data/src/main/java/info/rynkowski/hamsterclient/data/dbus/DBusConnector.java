package info.rynkowski.hamsterclient.data.dbus;

import org.freedesktop.dbus.DBusConnection;

public interface DBusConnector {

  void open();

  void close();

  Boolean isOpen();

  DBusConnection getConnection();
}
