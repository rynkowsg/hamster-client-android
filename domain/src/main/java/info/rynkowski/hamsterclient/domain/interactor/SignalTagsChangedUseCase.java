package info.rynkowski.hamsterclient.domain.interactor;

import info.rynkowski.hamsterclient.domain.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.domain.executor.ThreadExecutor;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;

public class SignalTagsChangedUseCase extends UseCaseArgumentless<Void> {

  private HamsterRepository hamsterRepository;

  @Inject public SignalTagsChangedUseCase(@Named("remote") HamsterRepository hamsterRepository,
      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.hamsterRepository = hamsterRepository;
  }

  @Override protected Observable<Void> buildUseCaseObservable() {
    return hamsterRepository.signalTagsChanged();
  }
}
