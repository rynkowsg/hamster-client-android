package info.rynkowski.hamsterclient.presentation.internal.di.components;

import dagger.Component;
import info.rynkowski.hamsterclient.presentation.internal.di.modules.ApplicationModule;
import info.rynkowski.hamsterclient.presentation.view.activity.BaseActivity;
import info.rynkowski.hamsterclient.presentation.view.fragment.BaseFragment;
import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
  void inject(BaseActivity baseActivity);
  void inject(BaseFragment baseFragment);
}
