package info.rynkowski.hamsterclient.presentation.internal.di.components;

import android.content.Context;
import dagger.Component;
import info.rynkowski.hamsterclient.data.dbus.DBusConnector;
import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.domain.executor.ThreadExecutor;
import info.rynkowski.hamsterclient.presentation.internal.di.modules.ApplicationModule;
import info.rynkowski.hamsterclient.presentation.internal.di.modules.DataModule;
import info.rynkowski.hamsterclient.presentation.navigation.Navigator;
import info.rynkowski.hamsterclient.presentation.view.activity.BaseActivity;
import info.rynkowski.hamsterclient.presentation.view.fragment.BaseFragment;
import javax.inject.Singleton;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton
@Component(modules = { ApplicationModule.class, DataModule.class })
public interface ApplicationComponent {

  void inject(BaseActivity baseActivity);
  void inject(BaseFragment baseFragment);

  //Exposed to sub-graphs.
  Context context();
  Navigator navigator();
  ThreadExecutor threadExecutor();
  PostExecutionThread postExecutionThread();
  DBusConnector dBusConnector();
  HamsterDataSource hamsterDataSource();
}
