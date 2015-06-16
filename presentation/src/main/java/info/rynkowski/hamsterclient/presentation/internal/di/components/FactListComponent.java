package info.rynkowski.hamsterclient.presentation.internal.di.components;

import dagger.Component;
import info.rynkowski.hamsterclient.presentation.internal.di.ActivityScope;
import info.rynkowski.hamsterclient.presentation.internal.di.modules.ActivityModule;
import info.rynkowski.hamsterclient.presentation.internal.di.modules.FactListModule;
import info.rynkowski.hamsterclient.presentation.view.fragment.FactListFragment;

@ActivityScope
@Component(dependencies = ApplicationComponent.class,
    modules = { ActivityModule.class, FactListModule.class })
public interface FactListComponent extends ActivityComponent {

  void inject(FactListFragment fragment);
}
