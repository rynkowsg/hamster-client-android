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

import info.rynkowski.hamsterclient.domain.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.domain.executor.ThreadExecutor;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;

public class SignalFactsChangedUseCase extends UseCaseArgumentless<Void> {

  private HamsterRepository hamsterRepository;

  @Inject public SignalFactsChangedUseCase(@Named("remote") HamsterRepository hamsterRepository,
      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.hamsterRepository = hamsterRepository;
  }

  @Override protected Observable<Void> buildUseCaseObservable() {
    return hamsterRepository.signalFactsChanged();
  }
}
