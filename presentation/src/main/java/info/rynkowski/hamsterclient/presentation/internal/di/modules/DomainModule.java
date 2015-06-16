package info.rynkowski.hamsterclient.presentation.internal.di.modules;

import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.domain.executor.ThreadExecutor;
import info.rynkowski.hamsterclient.domain.interactor.AddFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.GetTodaysFactsUseCase;
import info.rynkowski.hamsterclient.presentation.internal.di.ActivityScope;

/**
 * Dagger module that provides collaborators from Domain layer.
 */
@Module
public class DomainModule {

  @Provides @ActivityScope AddFactUseCase provideAddFactUseCase(
      HamsterDataSource hamsterDataSource, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    return new AddFactUseCase(hamsterDataSource, threadExecutor, postExecutionThread);
  }

  @Provides @ActivityScope GetTodaysFactsUseCase provideGetTodaysFacts(
      HamsterDataSource hamsterDataSource, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    return new GetTodaysFactsUseCase(hamsterDataSource, threadExecutor, postExecutionThread);
  }
}
