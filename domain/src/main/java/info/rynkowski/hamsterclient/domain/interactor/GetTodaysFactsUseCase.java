package info.rynkowski.hamsterclient.domain.interactor;

import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.domain.executor.ThreadExecutor;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving today's {@link info.rynkowski.hamsterclient.domain.entities.Fact}s.
 */
@Singleton
public class GetTodaysFactsUseCase extends UseCase<List<Fact>> {

  private HamsterDataSource hamsterDataSource;

  @Inject public GetTodaysFactsUseCase(HamsterDataSource hamsterDataSource,
      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.hamsterDataSource = hamsterDataSource;
  }

  @Override protected Observable<List<Fact>> buildUseCaseObservable() {
    return Observable.defer(() -> Observable.just(hamsterDataSource.GetTodaysFacts()));
  }
}
