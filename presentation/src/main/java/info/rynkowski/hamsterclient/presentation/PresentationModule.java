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

import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.interactor.UseCase;
import info.rynkowski.hamsterclient.domain.interactor.UseCaseNoArgs;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import info.rynkowski.hamsterclient.presentation.model.mapper.FactModelDataMapper;
import info.rynkowski.hamsterclient.presentation.presenter.FactListPresenter;
import info.rynkowski.hamsterclient.presentation.presenter.FactListPresenterImpl;
import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Dagger module that provides collaborators from Presentation layer.
 */
@Module
public class PresentationModule {

  @Provides @Singleton FactListPresenter provideFactListPresenter(
      @NonNull HamsterRepository hamsterRepository, @NonNull FactModelDataMapper mapper,
      @Named("AddFact") @NonNull UseCase<Fact, Void> addFactUseCase,
      @Named("EditFact") @NonNull UseCase<Fact, Void> editFactUseCase,
      @Named("GetTodaysFacts") @NonNull UseCaseNoArgs<List<Fact>> getTodaysFactsUseCase,
      @Named("RemoveFact") @NonNull UseCase<Fact, Void> removeFactUseCase,
      @Named("StartFact") @NonNull UseCase<Fact, Void> startFactUseCase,
      @Named("StopFact") @NonNull UseCase<Fact, Void> stopFactUseCase) {
    return new FactListPresenterImpl(hamsterRepository, mapper, addFactUseCase, editFactUseCase,
        getTodaysFactsUseCase, removeFactUseCase, startFactUseCase, stopFactUseCase);
  }
}
