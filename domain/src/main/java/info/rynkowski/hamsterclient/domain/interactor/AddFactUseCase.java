package info.rynkowski.hamsterclient.domain.interactor;

import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.exception.NotInitializedException;
import info.rynkowski.hamsterclient.domain.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.domain.executor.ThreadExecutor;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * adding a new {@link info.rynkowski.hamsterclient.domain.entities.Fact}.
 */
public class AddFactUseCase extends UseCase<Integer, Fact> {

  private HamsterRepository hamsterRepository;

  @Inject
  public AddFactUseCase(@Named("remote") HamsterRepository hamsterRepository,
      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.hamsterRepository = hamsterRepository;
  }

  @Override protected Observable<Integer> buildUseCaseObservable(Fact fact) {
    if (fact == null) {
      return Observable.error(
          new NotInitializedException("Use Case is not initialized - fact was not provided"));
    }
    return hamsterRepository.addFact(fact);
  }
}
