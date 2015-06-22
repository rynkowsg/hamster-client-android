package info.rynkowski.hamsterclient.data.dbus;

import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

public abstract class RemoteObjectAbstract<Type> implements RemoteObject<Type> {

  private static final Logger log = LoggerFactory.getLogger(RemoteObjectAbstract.class);
  private ConnectionProvider connectionProvider;
  private String busName;
  private String objectPath;
  private Class dbusType;
  private volatile Type remoteObject;
  private Observable<Type> remoteObjectObservable;

  public RemoteObjectAbstract(ConnectionProvider connectionProvider, String busName,
      String objectPath, Class dbusType) {
    this.connectionProvider = connectionProvider;
    this.busName = busName;
    this.objectPath = objectPath;
    this.dbusType = dbusType;
    this.remoteObject = null;
    this.remoteObjectObservable = null;
  }

  @SuppressWarnings("unchecked") @Override public synchronized Type get() throws DBusException {
    if (remoteObject == null) {
      log.debug("Possesing D-Bus remote object started:");
      log.debug("    busName:    {}", busName);
      log.debug("    objectPath: {}", objectPath);
      log.debug("    dbusType:   {}", dbusType);
      remoteObject = (Type) connectionProvider.get().getRemoteObject(busName, objectPath, dbusType);
    }
    return remoteObject;
  }

  @Override public synchronized Observable<Type> getObservable() {
    if (remoteObjectObservable == null) {
      Type object;
      try {
        object = RemoteObjectAbstract.this.get();
      } catch (DBusException e) {
        log.error("Exception threw during retrieving D-Bus remote object!", e);
        return Observable.error(e);
      }
      remoteObjectObservable = Observable.just(object);
    }
    return remoteObjectObservable;
  }

  @Override @SuppressWarnings("unchecked")
  public void registerSignalCallback(Class<? extends DBusSignal> signalClass,
      DBusSigHandler<DBusSignal> callback) throws DBusException {
    connectionProvider.get().addSigHandler((Class<DBusSignal>) signalClass, callback);
  }

  @Override
  public Observable<Void> createSignalObservable(Class<? extends DBusSignal> signalClass) {
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

  @Override public void clear() {
    remoteObject = null;
    remoteObjectObservable = null;
  }
}
