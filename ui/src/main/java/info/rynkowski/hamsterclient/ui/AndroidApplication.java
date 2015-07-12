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

package info.rynkowski.hamsterclient.ui;

import android.app.Application;
import android.support.annotation.NonNull;
import com.google.common.base.Optional;
import info.rynkowski.hamsterclient.ui.internal.di.components.ApplicationComponent;
import info.rynkowski.hamsterclient.ui.internal.di.components.DaggerApplicationComponent;
import info.rynkowski.hamsterclient.ui.internal.di.modules.ApplicationModule;

public class AndroidApplication extends Application {

  private @NonNull Optional<ApplicationComponent> applicationComponent = Optional.absent();

  @Override public void onCreate() {
    super.onCreate();
    this.initializeDependencyInjector();
  }

  private void initializeDependencyInjector() {
    this.applicationComponent = Optional.of(DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this))
        .build());
  }

  public @NonNull ApplicationComponent getApplicationComponent() {
    return this.applicationComponent.get();
  }
}
