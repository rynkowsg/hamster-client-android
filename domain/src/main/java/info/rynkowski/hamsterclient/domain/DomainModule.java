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

package info.rynkowski.hamsterclient.domain;

import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.interactor.AddFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.EditFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.GetTodaysFactsUseCase;
import info.rynkowski.hamsterclient.domain.interactor.RemoveFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.StartFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.StopFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.UseCase;
import info.rynkowski.hamsterclient.domain.interactor.UseCaseArgumentless;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import java.util.List;
import javax.inject.Named;

/**
 * Dagger module that provides collaborators from Domain layer.
 */
@Module
public class DomainModule {

  @Provides @Named("AddFact") UseCase<Integer, Fact> provideAddFactUseCase(
      HamsterRepository hamsterRepository) {
    return new AddFactUseCase(hamsterRepository);
  }

  @Provides @Named("EditFact") UseCase<Integer, Fact> provideUpdateFactUseCase(
      HamsterRepository hamsterRepository) {
    return new EditFactUseCase(hamsterRepository);
  }

  @Provides @Named("GetTodaysFacts") UseCaseArgumentless<List<Fact>> provideGetTodaysFacts(
      HamsterRepository hamsterRepository) {
    return new GetTodaysFactsUseCase(hamsterRepository);
  }

  @Provides @Named("RemoveFact") UseCase<Void, Fact> provideRemoveFactUseCase(
      HamsterRepository hamsterRepository) {
    return new RemoveFactUseCase(hamsterRepository);
  }

  @Provides @Named("StartFact") UseCase<Void, Fact> provideStartFactUseCase(
      HamsterRepository hamsterRepository) {
    return new StartFactUseCase(hamsterRepository);
  }

  @Provides @Named("StopFact") UseCase<Void, Fact> provideStopFactUseCase(
      HamsterRepository hamsterRepository) {
    return new StopFactUseCase(hamsterRepository);
  }
}
