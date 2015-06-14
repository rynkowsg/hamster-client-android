package info.rynkowski.hamsterclient.presentation.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import info.rynkowski.hamsterclient.presentation.AndroidApplication;
import info.rynkowski.hamsterclient.presentation.internal.di.components.ApplicationComponent;
import info.rynkowski.hamsterclient.presentation.internal.di.modules.ActivityModule;
import info.rynkowski.hamsterclient.presentation.navigation.Navigator;
import javax.inject.Inject;

/**
 * Base {@link android.app.Activity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity {

  @Inject Navigator navigator;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.injectDependencies();
  }

  /**
   * Get the Main Application component for dependency injection.
   *
   * @return {@link info.rynkowski.hamsterclient.presentation.internal.di.components.ApplicationComponent}
   */
  protected ApplicationComponent getApplicationComponent() {
    return ((AndroidApplication) getApplication()).getApplicationComponent();
  }

  /**
   * Get an Activity module for dependency injection.
   *
   * @return {@link info.rynkowski.hamsterclient.presentation.internal.di.modules.ActivityModule}
   */
  protected ActivityModule getActivityModule() {
    return new ActivityModule(this);
  }

  private void injectDependencies() {
    this.getApplicationComponent().inject(this);
  }
}
