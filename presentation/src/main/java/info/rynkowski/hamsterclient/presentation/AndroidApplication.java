package info.rynkowski.hamsterclient.presentation;

import android.app.Application;
import info.rynkowski.hamsterclient.presentation.internal.di.components.ApplicationComponent;
import info.rynkowski.hamsterclient.presentation.internal.di.components.DaggerApplicationComponent;
import info.rynkowski.hamsterclient.presentation.internal.di.modules.ApplicationModule;
import info.rynkowski.hamsterclient.presentation.internal.di.modules.DataModule;
import timber.log.Timber;

public class AndroidApplication extends Application {

  private ApplicationComponent applicationComponent;

  @Override public void onCreate() {
    super.onCreate();
    this.initializeDependencyInjector();
    this.initializeLogger();
  }

  private void initializeDependencyInjector() {
    this.applicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this))
        //TODO: Move host&port setting to DBusConnector.open(host,port) method
        .dataModule(new DataModule("10.0.2.5", "55555"))
        .build();
  }

  private void initializeLogger() {
    Timber.plant(new Timber.DebugTree());
  }

  public ApplicationComponent getApplicationComponent() {
    return this.applicationComponent;
  }
}
