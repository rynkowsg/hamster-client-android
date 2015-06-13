package info.rynkowski.hamsterclient.presentation.internal.di.modules;

import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.presentation.navigation.Navigator;
import javax.inject.Singleton;

@Module
public class ApplicationModule {

  @Provides @Singleton Navigator provideNavigator() {
    return new Navigator();
  }
}
