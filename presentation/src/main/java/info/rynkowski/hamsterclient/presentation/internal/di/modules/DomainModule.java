package info.rynkowski.hamsterclient.presentation.internal.di.modules;

import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.interactor.AddFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.GetTodaysFacts;
import info.rynkowski.hamsterclient.presentation.internal.di.PerActivity;

/**
 * Dagger module that provides collaborators from Domain layer.
 */
@Module
public class DomainModule {

  @Provides @PerActivity AddFactUseCase provideAddFactUseCase(HamsterDataSource hamsterDataSource) {
    return new AddFactUseCase(hamsterDataSource);
  }

  @Provides @PerActivity GetTodaysFacts provideGetTodaysFacts(HamsterDataSource hamsterDataSource) {
    return new GetTodaysFacts(hamsterDataSource);
  }
}
