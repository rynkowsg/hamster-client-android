package info.rynkowski.hamsterclient.presentation.internal.di.modules;

import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.domain.executor.ThreadExecutor;
import info.rynkowski.hamsterclient.domain.interactor.AddFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.GetTodaysFactsUseCase;
import info.rynkowski.hamsterclient.domain.interactor.UseCase;
import info.rynkowski.hamsterclient.domain.interactor.UseCaseArgumentless;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import info.rynkowski.hamsterclient.presentation.internal.di.ActivityScope;
import java.util.List;
import javax.inject.Named;

/**
 * Dagger module that provides collaborators from Domain layer.
 */
@Module
public class DomainModule {

  @Provides @ActivityScope @Named("AddFact") UseCase<Integer, Fact> provideAddFactUseCase(
      HamsterRepository hamsterRepository, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    return new AddFactUseCase(hamsterRepository, threadExecutor, postExecutionThread);
  }

  @Provides @ActivityScope @Named("GetTodaysFacts")
  UseCaseArgumentless<List<Fact>> provideGetTodaysFacts(HamsterRepository hamsterRepository,
      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetTodaysFactsUseCase(hamsterRepository, threadExecutor, postExecutionThread);
  }
}
