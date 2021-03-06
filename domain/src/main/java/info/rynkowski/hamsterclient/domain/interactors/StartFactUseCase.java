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

package info.rynkowski.hamsterclient.domain.interactors;

import com.google.common.base.Optional;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import rx.Observable;

public class StartFactUseCase extends UseCase<Fact, Void> {

  private @Nonnull HamsterRepository hamsterRepository;

  @Inject public StartFactUseCase(@Nonnull HamsterRepository hamsterRepository) {
    this.hamsterRepository = hamsterRepository;
  }

  @Override protected @Nonnull Observable<Void> buildUseCaseObservable(@Nonnull Fact fact) {
    return Observable.just( //
        new Fact.Builder(fact) //
            .startTime(GregorianCalendar.getInstance())
            .endTime(Optional.<Calendar>absent())
            .build()) //
        .flatMap(hamsterRepository::addFact) //
        .flatMap(id -> Observable.<Void>empty());
  }
}
