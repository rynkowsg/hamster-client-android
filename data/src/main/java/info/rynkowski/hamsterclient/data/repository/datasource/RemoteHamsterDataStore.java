package info.rynkowski.hamsterclient.data.repository.datasource;

import android.util.Log;
import info.rynkowski.hamsterclient.data.dbus.HamsterRemoteObject;
import info.rynkowski.hamsterclient.data.entity.FactEntity;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.gnome.Hamster;
import rx.Observable;

@Singleton
public class RemoteHamsterDataStore implements HamsterDataStore {

  private static final String TAG = "RemoteHamsterDataStore";

  private HamsterRemoteObject hamsterObject;

  @Inject public RemoteHamsterDataStore(HamsterRemoteObject hamsterRemoteObject) {
    this.hamsterObject = hamsterRemoteObject;
  }

  @Override public Observable<List<FactEntity>> getTodaysFactEntities() {
    return Observable.defer(() -> Observable.just(hamsterObject.get()))
        .map(Hamster::GetTodaysFacts)
        .flatMap(Observable::from).map(FactEntity::new).toList();
  }

  @Override public Observable<Integer> addFactEntity(FactEntity factEntity) {
    String serializedName = factEntity.serializedName();
    int startTime = factEntity.getStartTime();
    int endTime = factEntity.getEndTime();
    Log.d(TAG,
        "AddFact( {serializedName:\"" + serializedName + "\", startTime:" + startTime + ", endTime:"
            + endTime + "} )");

    return Observable.defer(() -> Observable.just(hamsterObject.get()))
        .map(remoteObject -> remoteObject.AddFact(serializedName, startTime, endTime, false))
        .doOnNext(result -> Log.d(TAG, "Result of AddFact(): " + result));
  }
}
