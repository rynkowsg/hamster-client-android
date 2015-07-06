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

package info.rynkowski.hamsterclient.domain.interactor;

import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving today's {@link info.rynkowski.hamsterclient.domain.entities.Fact}s.
 */
public class GetTodaysFactsUseCase extends UseCaseArgumentless<List<Fact>> {

  @Nonnull private HamsterRepository hamsterRepository;

  @Inject
  public GetTodaysFactsUseCase(@Nonnull @Named("remote") HamsterRepository hamsterRepository) {
    this.hamsterRepository = hamsterRepository;
  }

  @Override @Nonnull protected Observable<List<Fact>> buildUseCaseObservable() {
    return hamsterRepository.getTodaysFacts();
  }
}
