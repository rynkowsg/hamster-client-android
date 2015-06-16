package info.rynkowski.hamsterclient.domain.interactor;

import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.exception.NotInitializedException;
import info.rynkowski.hamsterclient.domain.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.domain.executor.ThreadExecutor;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Setter;
import lombok.experimental.Accessors;
import rx.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * adding a new {@link info.rynkowski.hamsterclient.domain.entities.Fact}.
 */
@Singleton
public class AddFactUseCase extends UseCase<Integer> {

  private HamsterDataSource hamsterDataSource;
  @Setter @Accessors(chain = true) private Fact fact;

  @Inject public AddFactUseCase(HamsterDataSource hamsterDataSource, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.hamsterDataSource = hamsterDataSource;
  }

  @Override protected Observable<Integer> buildUseCaseObservable() {
    if (fact == null) {
      return Observable.error(
          new NotInitializedException("Use Case is not initialized - fact was not provided"));
    }
    return Observable.defer(() -> Observable.just(hamsterDataSource.AddFact(fact)));
  }
}
