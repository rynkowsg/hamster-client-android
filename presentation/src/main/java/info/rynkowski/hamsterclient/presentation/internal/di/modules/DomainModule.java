package info.rynkowski.hamsterclient.presentation.internal.di.modules;

import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.domain.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.domain.executor.ThreadExecutor;
import info.rynkowski.hamsterclient.domain.interactor.AddFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.GetTodaysFactsUseCase;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import info.rynkowski.hamsterclient.presentation.internal.di.ActivityScope;

/**
 * Dagger module that provides collaborators from Domain layer.
 */
@Module
public class DomainModule {

  @Provides @ActivityScope AddFactUseCase provideAddFactUseCase(
      HamsterRepository hamsterRepository, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    return new AddFactUseCase(hamsterRepository, threadExecutor, postExecutionThread);
  }

  @Provides @ActivityScope GetTodaysFactsUseCase provideGetTodaysFacts(
      HamsterRepository hamsterRepository, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    return new GetTodaysFactsUseCase(hamsterRepository, threadExecutor, postExecutionThread);
  }
}
