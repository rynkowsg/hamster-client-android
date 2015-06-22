package info.rynkowski.hamsterclient.data.repository.datasource;

import info.rynkowski.hamsterclient.data.dbus.ConnectionProvider;
import info.rynkowski.hamsterclient.data.dbus.HamsterRemoteObject;
import info.rynkowski.hamsterclient.data.entity.FactEntity;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.gnome.Hamster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

@Singleton
public class RemoteHamsterDataStore implements HamsterDataStore {

  private static final Logger log = LoggerFactory.getLogger(RemoteHamsterDataStore.class);

  private ConnectionProvider connectionProvider;
  private HamsterRemoteObject hamsterObject;

  @Inject public RemoteHamsterDataStore(ConnectionProvider connectionProvider,
      HamsterRemoteObject hamsterRemoteObject) {
    this.connectionProvider = connectionProvider;
    this.hamsterObject = hamsterRemoteObject;
  }

  @Override public void initialize() {
    // empty
  }

  @Override public void deinitialize() {
    hamsterObject.clear();
    connectionProvider.close();
  }

  @Override public Observable<List<FactEntity>> getTodaysFactEntities() {
    return Observable.defer(hamsterObject::getObservable)
        .map(Hamster::GetTodaysFacts)
        .flatMap(Observable::from)
        .map(FactEntity::new)
        .toList();
  }

  @Override public Observable<Integer> addFactEntity(FactEntity factEntity) {
    String serializedName = factEntity.serializedName();
    int startTime = factEntity.getStartTime();
    int endTime = factEntity.getEndTime();

    return Observable.defer(hamsterObject::getObservable)
        .doOnNext(object -> {
          log.debug("Calling AddFact() on remote DBus object:");
          log.debug("    serializedName: \"{}\"", serializedName);
          log.debug("    startTime:      {}", startTime);
          log.debug("    endTime:        {}", endTime);
        })
        .map(remoteObject -> remoteObject.AddFact(serializedName, startTime, endTime, false));
  }

  @Override public Observable<Void> signalActivitiesChanged() {
    return hamsterObject.signalActivitiesChanged();
  }

  @Override public Observable<Void> signalFactsChanged() {
    return hamsterObject.signalFactsChanged();
  }

  @Override public Observable<Void> signalTagsChanged() {
    return hamsterObject.signalTagsChanged();
  }

  @Override public Observable<Void> signalToggleCalled() {
    return hamsterObject.signalToggleCalled();
  }
}
