/*
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * Copyright (C) 2015 Grzegorz Rynkowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.rynkowski.hamsterclient.ui.internal.di.modules;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.data.utils.PreferencesContainer;
import info.rynkowski.hamsterclient.presentation.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.presentation.executor.ThreadExecutor;
import info.rynkowski.hamsterclient.ui.AndroidApplication;
import info.rynkowski.hamsterclient.ui.executor.JobExecutor;
import info.rynkowski.hamsterclient.ui.executor.UIThread;
import info.rynkowski.hamsterclient.ui.navigation.Navigator;
import info.rynkowski.hamsterclient.ui.utils.PreferencesContainerImpl;
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

  @Provides @Singleton PreferencesContainer providePreferencesContainer(Context context) {
    return new PreferencesContainerImpl(context);
  }
}
