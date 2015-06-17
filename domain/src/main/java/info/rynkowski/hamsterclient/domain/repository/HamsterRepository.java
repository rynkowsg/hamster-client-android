package info.rynkowski.hamsterclient.domain.repository;

import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.List;
import rx.Observable;

/**
 * Interface that represents a Repository for communicating with Hamster database.
 */
public interface HamsterRepository {

  Observable<List<Fact>> getTodaysFacts();

  Observable<Integer> addFact(Fact fact);
}
