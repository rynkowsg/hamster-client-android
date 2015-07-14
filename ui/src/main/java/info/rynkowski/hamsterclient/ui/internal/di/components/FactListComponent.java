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

package info.rynkowski.hamsterclient.ui.internal.di.components;

import dagger.Component;
import info.rynkowski.hamsterclient.ui.internal.di.ActivityScope;
import info.rynkowski.hamsterclient.ui.internal.di.modules.ActivityModule;
import info.rynkowski.hamsterclient.ui.internal.di.modules.FactListModule;
import info.rynkowski.hamsterclient.ui.view.fragment.FactListFragment;

/**
 * Injects {@link info.rynkowski.hamsterclient.ui.view.fragment.FactListFragment}.
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class,
    modules = { ActivityModule.class, FactListModule.class })
public interface FactListComponent extends ActivityComponent {

  void inject(FactListFragment fragment);
}