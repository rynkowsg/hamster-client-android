package info.rynkowski.hamsterclient.domain.interactor;

import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.domain.executor.ThreadExecutor;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving today's {@link info.rynkowski.hamsterclient.domain.entities.Fact}s.
 */
public class GetTodaysFactsUseCase extends UseCaseArgumentless<List<Fact>> {

  private HamsterRepository hamsterRepository;

  @Inject
  public GetTodaysFactsUseCase(@Named("remote") HamsterRepository hamsterRepository,
      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.hamsterRepository = hamsterRepository;
  }

  @Override protected Observable<List<Fact>> buildUseCaseObservable() {
    return hamsterRepository.getTodaysFacts();
  }
}
