package info.rynkowski.hamsterclient.data.dbus;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.gnome.Hamster;
import rx.Observable;

@Singleton
public class HamsterRemoteObject extends RemoteObjectAbstract<Hamster> {

  private static final String busName = "org.gnome.Hamster";
  private static final String objectPath = "/org/gnome/Hamster";
  private static final Class dbusType = Hamster.class;

  @Inject public HamsterRemoteObject(DBusConnectionProvider connectionProvider) {
    super(connectionProvider, busName, objectPath, dbusType);
  }

  public Observable<Void> signalActivitiesChanged() {
    return this.createSignalObservable(Hamster.ActivitiesChanged.class);
  }

  public Observable<Void> signalFactsChanged() {
    return this.createSignalObservable(Hamster.FactsChanged.class);
  }

  public Observable<Void> signalTagsChanged() {
    return this.createSignalObservable(Hamster.TagsChanged.class);
  }

  public Observable<Void> signalToggleCalled() {
    return this.createSignalObservable(Hamster.ToggleCalled.class);
  }
}
