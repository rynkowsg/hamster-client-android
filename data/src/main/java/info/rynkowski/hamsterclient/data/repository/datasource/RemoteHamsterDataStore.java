package info.rynkowski.hamsterclient.data.repository.datasource;

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

  private HamsterRemoteObject hamsterObject;

  @Inject public RemoteHamsterDataStore(HamsterRemoteObject hamsterRemoteObject) {
    this.hamsterObject = hamsterRemoteObject;
  }

  @Override public Observable<List<FactEntity>> getTodaysFactEntities() {
    return Observable.defer(() -> Observable.just(hamsterObject.get()))
        .map(Hamster::GetTodaysFacts)
        .flatMap(Observable::from)
        .map(FactEntity::new)
        .toList();
  }

  @Override public Observable<Integer> addFactEntity(FactEntity factEntity) {
    String serializedName = factEntity.serializedName();
    int startTime = factEntity.getStartTime();
    int endTime = factEntity.getEndTime();

    return Observable.defer(() -> Observable.just(hamsterObject.get()))
        .doOnNext(object -> {
          log.debug("Calling AddFact() on remote DBus object:");
          log.debug("    serializedName: \"{}\"", serializedName);
          log.debug("    startTime:      {}", startTime);
          log.debug("    endTime:        {}", endTime);
        })
        .map(remoteObject -> remoteObject.AddFact(serializedName, startTime, endTime, false));
  }
}
