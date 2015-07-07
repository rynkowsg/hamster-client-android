/*
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

package info.rynkowski.hamsterclient.presentation;

import android.app.Application;
import info.rynkowski.hamsterclient.data.DataModule;
import info.rynkowski.hamsterclient.presentation.internal.di.components.ApplicationComponent;
import info.rynkowski.hamsterclient.presentation.internal.di.components.DaggerApplicationComponent;
import info.rynkowski.hamsterclient.presentation.internal.di.modules.ApplicationModule;
//import timber.log.Timber;

public class AndroidApplication extends Application {

  private ApplicationComponent applicationComponent;

  @Override public void onCreate() {
    super.onCreate();
    this.initializeDependencyInjector();
    this.initializeLogger();
  }

  private void initializeDependencyInjector() {
    //TODO: Move host&port setting to DBusConnector.open(host,port) method
    this.applicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this))
        .dataModule(new DataModule("10.0.2.5", "55555"))
        .build();
  }

  private void initializeLogger() {
    //Timber.plant(new Timber.DebugTree());
  }

  public ApplicationComponent getApplicationComponent() {
    return this.applicationComponent;
  }
}
