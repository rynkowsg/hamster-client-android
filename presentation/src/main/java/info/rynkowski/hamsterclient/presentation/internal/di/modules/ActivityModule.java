package info.rynkowski.hamsterclient.presentation.internal.di.modules;

import android.app.Activity;
import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.presentation.internal.di.PerActivity;

/**
 * A module to wrap the Activity state and expose it to the graph.
 */
@Module
public class ActivityModule {
  private final Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  /**
   * Expose the activity to dependents in the graph.
   */
  @Provides @PerActivity public Activity provideActivity() {
    return this.activity;
  }
}
