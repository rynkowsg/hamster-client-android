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

package info.rynkowski.hamsterclient.domain.interactor;

import info.rynkowski.hamsterclient.domain.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.domain.executor.ThreadExecutor;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture) with arguments.
 *
 * By convention each UseCase implementation will return the result using a {@link rx.Observable}.
 */
public abstract class UseCase<Result, Argument> {

  private final ThreadExecutor threadExecutor;
  private final PostExecutionThread postExecutionThread;

  protected UseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    this.threadExecutor = threadExecutor;
    this.postExecutionThread = postExecutionThread;
  }

  /**
   * Builds an {@link rx.Observable} which will be used when executing the current {@link UseCase}.
   */
  protected abstract Observable<Result> buildUseCaseObservable(Argument argument);

  /**
   * Return a {@link rx.Observable} with result of execution.
   */
  public Observable<Result> execute(Argument argument) {
    return this.buildUseCaseObservable(argument)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler());
  }
}
