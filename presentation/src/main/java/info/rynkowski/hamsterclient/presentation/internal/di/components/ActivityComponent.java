package info.rynkowski.hamsterclient.presentation.internal.di.components;

import android.app.Activity;
import dagger.Component;
import info.rynkowski.hamsterclient.presentation.internal.di.ActivityScope;
import info.rynkowski.hamsterclient.presentation.internal.di.modules.ActivityModule;

/**
 * A base component upon which fragment's components may depend.
 * Activity-level components should extend this component.
 *
 * Subtypes of ActivityComponent should be decorated with annotation:
 * {@link info.rynkowski.hamsterclient.presentation.internal.di.ActivityScope}
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

  //Exposed to sub-graphs.
  Activity activity();
}
