package info.rynkowski.hamsterclient.presentation.internal.di.modules;

import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.data.dbus.DBusConnector;
import info.rynkowski.hamsterclient.domain.interactor.AddFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.GetTodaysFacts;
import info.rynkowski.hamsterclient.presentation.internal.di.ActivityScope;
import info.rynkowski.hamsterclient.presentation.model.mapper.FactModelDataMapper;
import info.rynkowski.hamsterclient.presentation.presenter.FactListPresenter;

/**
 * Dagger module that provides collaborators related to fact's list.
 */
@Module(includes = DomainModule.class)
public class FactListModule {

  @ActivityScope @Provides FactListPresenter provideFactListPresenter(DBusConnector dbusConnector,
      AddFactUseCase addFactUseCase, GetTodaysFacts getTodaysFacts, FactModelDataMapper mapper) {
    return new FactListPresenter(dbusConnector, addFactUseCase, getTodaysFacts, mapper);
  }
}
