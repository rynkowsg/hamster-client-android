package info.rynkowski.hamsterclient.data.dbus;

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

/**
 * Interface representing a connection provider for {@link org.freedesktop.dbus.DBusConnection}s.
 */
public interface DBusConnectionProvider {

  /**
   * Returns {@link org.freedesktop.dbus.DBusConnection}s.
   *
   * @return a {@link org.freedesktop.dbus.DBusConnection}.
   * @throws DBusException is an exception from D-Bus library.
   */
  DBusConnection get() throws DBusException;

  /**
   * Closes the connection provider and releases its resources.
   */
  void close();
}
