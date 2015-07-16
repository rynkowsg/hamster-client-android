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
import rx.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving today's {@link info.rynkowski.hamsterclient.domain.entities.Fact}s.
 */
public class GetTodaysFactsUseCase extends UseCaseNoArgs<List<Fact>> {

  private @Nonnull HamsterRepository hamsterRepository;

  @Inject public GetTodaysFactsUseCase(@Nonnull HamsterRepository hamsterRepository) {
    this.hamsterRepository = hamsterRepository;
  }

  @Override protected @Nonnull Observable<List<Fact>> buildUseCaseObservable() {
    return hamsterRepository.getTodaysFacts();
  }
}
