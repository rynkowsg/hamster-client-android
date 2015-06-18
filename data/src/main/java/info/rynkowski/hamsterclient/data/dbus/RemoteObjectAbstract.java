package info.rynkowski.hamsterclient.data.dbus;

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

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

  @SuppressWarnings("unchecked") protected void possessRemoteObject() throws DBusException {
      DBusConnection connection = connectionProvider.get();

      log.debug("Possesing DBus remote object started:");
      log.debug("    busName:    {}", busName);
      log.debug("    objectPath: {}", objectPath);
      log.debug("    dbusType:   {}", dbusType);

      remoteObject = (Type) connection.getRemoteObject(busName, objectPath, dbusType);
  }

  @Override public Type get() throws DBusException {
    log.trace("get()");
    if (remoteObject == null) {
      possessRemoteObject();
    }
    return remoteObject;
  }

  @Override public Observable<Type> getObservable() {
    log.trace("getObservable()");
    Type remoteObject;

    try {
      remoteObject = this.get();
    } catch (DBusException e) {
      log.error("Exception threw during retrieving DBus remote object! ", e);
      return Observable.error(e);
    }

    return Observable.just(remoteObject);
  }

  @Override @SuppressWarnings("unchecked")
  public void registerSignalCallback(Class<? extends DBusSignal> signalClass,
      DBusSigHandler<DBusSignal> callback) throws DBusException {
    log.trace("registerSignalCallback()");
    connectionProvider.get().addSigHandler((Class<DBusSignal>) signalClass, callback);
  }

  @Override public Observable<Void> createSignalObservable(Class<? extends DBusSignal> signalClass) {
    log.trace("createSignalObservable()");
    return Observable.create(subscriber -> {
      try {
        registerSignalCallback(signalClass, signal -> subscriber.onNext(null));
      } catch (DBusException e) {
        e.printStackTrace();
        log.error("Exception threw during registering a signal! ", e);
        subscriber.onError(e);
      }
    });
  }
}
