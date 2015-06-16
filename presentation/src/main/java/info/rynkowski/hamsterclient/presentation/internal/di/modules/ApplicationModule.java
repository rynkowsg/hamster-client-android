package info.rynkowski.hamsterclient.presentation.internal.di.modules;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.domain.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.domain.executor.ThreadExecutor;
import info.rynkowski.hamsterclient.presentation.AndroidApplication;
import info.rynkowski.hamsterclient.presentation.executor.JobExecutor;
import info.rynkowski.hamsterclient.presentation.executor.UIThread;
import info.rynkowski.hamsterclient.presentation.navigation.Navigator;
import javax.inject.Singleton;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {

  private final AndroidApplication application;

  public ApplicationModule(AndroidApplication application) {
    this.application = application;
  }

  @Provides @Singleton Context provideApplicationContext() {
    return this.application;
  }

  @Provides @Singleton Navigator provideNavigator() {
    return new Navigator();
  }

  @Provides @Singleton ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
    return jobExecutor;
  }

  @Provides @Singleton PostExecutionThread providePostExecutionThread(UIThread uiThread) {
    return uiThread;
  }
}
