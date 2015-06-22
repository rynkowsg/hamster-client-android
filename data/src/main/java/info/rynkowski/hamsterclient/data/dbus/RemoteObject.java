package info.rynkowski.hamsterclient.data.dbus;

import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.exceptions.DBusException;
import rx.Observable;

/**
 * Base interface for remote objects.
 *
 * @param <Type> type of remote object
 */
public interface RemoteObject<Type> {

  Type get() throws DBusException;

  Observable<Type> getObservable();

  void registerSignalCallback(Class<? extends DBusSignal> signalClass,
      DBusSigHandler<DBusSignal> callback) throws DBusException;

  Observable<Void> createSignalObservable(Class<? extends DBusSignal> signalClass);

  /**
   * Removes a retrieved remote object.
   */
  void clear();
}
