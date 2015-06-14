package info.rynkowski.hamsterclient.presentation;

import android.app.Application;
import info.rynkowski.hamsterclient.presentation.internal.di.components.ApplicationComponent;
import info.rynkowski.hamsterclient.presentation.internal.di.components.DaggerApplicationComponent;

public class AndroidApplication extends Application {

  private ApplicationComponent applicationComponent;

  @Override public void onCreate() {
    super.onCreate();
    this.initializeInjector();
  }

  private void initializeInjector() {
    this.applicationComponent = DaggerApplicationComponent.builder().build();
  }

  public ApplicationComponent getApplicationComponent() {
    return this.applicationComponent;
  }
}
