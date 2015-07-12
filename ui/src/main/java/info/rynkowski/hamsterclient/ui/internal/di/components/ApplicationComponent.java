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

package info.rynkowski.hamsterclient.ui.internal.di.components;

import android.content.Context;
import dagger.Component;
import info.rynkowski.hamsterclient.data.DataModule;
import info.rynkowski.hamsterclient.data.dbus.ConnectionProvider;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import info.rynkowski.hamsterclient.presentation.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.presentation.executor.ThreadExecutor;
import info.rynkowski.hamsterclient.ui.internal.di.modules.ApplicationModule;
import info.rynkowski.hamsterclient.ui.navigation.Navigator;
import info.rynkowski.hamsterclient.ui.view.activity.BaseActivity;
import info.rynkowski.hamsterclient.ui.view.fragment.BaseFragment;
import javax.inject.Singleton;

/**
 * A component whose lifetime is the life of the application.
 * It injects {@link info.rynkowski.hamsterclient.ui.view.activity.BaseActivity} and
 * {@link info.rynkowski.hamsterclient.ui.view.fragment.BaseFragment}.
 */
@Singleton
@Component(modules = { ApplicationModule.class, DataModule.class })
public interface ApplicationComponent {

  void inject(BaseActivity baseActivity);

  void inject(BaseFragment baseFragment);

  //Exposed to sub-graphs.
  Context context();

  Navigator navigator();

  ThreadExecutor threadExecutor();

  PostExecutionThread postExecutionThread();

  HamsterRepository hamsterRepository();
}
