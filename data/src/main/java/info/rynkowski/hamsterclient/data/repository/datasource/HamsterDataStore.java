package info.rynkowski.hamsterclient.data.repository.datasource;

import info.rynkowski.hamsterclient.data.entity.FactEntity;
import java.util.List;
import rx.Observable;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface HamsterDataStore extends DataStore {

  /**
   * Get an {@link rx.Observable} which will emit a today's list of {@link FactEntity}.
   */
  Observable<List<FactEntity>> getTodaysFactEntities();

  /**
   * Get an {@link rx.Observable} which will add a {@link FactEntity} and emits its id.
   *
   * @param factEntity The entity to add to store.
   * @return The id of added {@link FactEntity}.
   */
  Observable<Integer> addFactEntity(FactEntity factEntity);

  Observable<Void> signalActivitiesChanged();

  Observable<Void> signalFactsChanged();

  Observable<Void> signalTagsChanged();

  Observable<Void> signalToggleCalled();
}
