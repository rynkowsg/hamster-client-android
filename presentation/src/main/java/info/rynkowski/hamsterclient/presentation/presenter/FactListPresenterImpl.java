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

package info.rynkowski.hamsterclient.presentation.presenter;

import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.interactors.UseCase;
import info.rynkowski.hamsterclient.domain.interactors.UseCaseNoArgs;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import info.rynkowski.hamsterclient.presentation.model.PresentationFact;
import info.rynkowski.hamsterclient.presentation.model.mapper.PresentationFactMapper;
import info.rynkowski.hamsterclient.presentation.view.FactListView;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer for fact list screen.
 */
@Slf4j
@Singleton
public class FactListPresenterImpl implements FactListPresenter {

  private final @Nonnull HamsterRepository hamsterRepository;
  private final @Nonnull PresentationFactMapper mapper;

  private final @Nonnull UseCase<Fact, Void> addFactUseCase;
  private final @Nonnull UseCase<Fact, Void> editFactUseCase;
  private final @Nonnull UseCaseNoArgs<List<Fact>> getTodaysFactsUseCase;
  private final @Nonnull UseCase<Fact, Void> startFactUseCase;
  private final @Nonnull UseCase<Fact, Void> stopFactUseCase;
  private final @Nonnull UseCase<Fact, Void> removeFactUseCase;

  private final @Nonnull Scheduler postExecuteScheduler;

  private @Nullable FactListView viewListView;

  private @Nullable Subscription signalFactsChangedSubscription;

  @Inject public FactListPresenterImpl(@Nonnull HamsterRepository hamsterRepository,
      @Nonnull PresentationFactMapper mapper,
      @Named("PresenterPostExecute") @Nonnull Scheduler postExecuteScheduler,
      @Named("AddFact") @Nonnull UseCase<Fact, Void> addFactUseCase,
      @Named("EditFact") @Nonnull UseCase<Fact, Void> editFactUseCase,
      @Named("GetTodaysFacts") @Nonnull UseCaseNoArgs<List<Fact>> getTodaysFactsUseCase,
      @Named("RemoveFact") @Nonnull UseCase<Fact, Void> removeFactUseCase,
      @Named("StartFact") @Nonnull UseCase<Fact, Void> startFactUseCase,
      @Named("StopFact") @Nonnull UseCase<Fact, Void> stopFactUseCase) {
    this.hamsterRepository = hamsterRepository;
    this.mapper = mapper;
    this.postExecuteScheduler = postExecuteScheduler;
    this.addFactUseCase = addFactUseCase;
    this.editFactUseCase = editFactUseCase;
    this.getTodaysFactsUseCase = getTodaysFactsUseCase;
    this.removeFactUseCase = removeFactUseCase;
    this.startFactUseCase = startFactUseCase;
    this.stopFactUseCase = stopFactUseCase;
  }

  public void setView(@Nullable FactListView view) {
    this.viewListView = view;
  }

  @Override public void start() {
    log.debug("start()");

    registerSignals();
    loadFactList();

    log.debug("FactList started.");
  }

  @Override public void resume() {
    log.debug("FactList resumed.");
  }

  @Override public void pause() {
    log.debug("FactList paused.");
  }

  @Override public void destroy() {
    unregisterSignals();
    log.debug("FactList destroyed.");
  }

  private void loadFactList() {
    log.debug("loadFactList()");
    if (viewListView == null) {
      throw new RuntimeException("View is not provided.");
    }

    viewListView.showLoading();
    getTodaysFactsUseCase.execute()
        .doOnNext(list -> log.info("Received {} facts.", list.size()))
        .map(mapper::transform)
        .subscribeOn(Schedulers.io())
        .observeOn(postExecuteScheduler)
        .subscribe( //
            presentationFacts -> {
              viewListView.hideLoading();
              viewListView.showFactList(presentationFacts);
            }, //
            this::onException);
  }

  private void registerSignals() {
    signalFactsChangedSubscription = hamsterRepository.signalFactsChanged()
        .subscribeOn(Schedulers.io())
        .observeOn(postExecuteScheduler)
        .subscribe(o -> this.onFactsChanged(), this::onException);
    log.debug("Signals registered.");
  }

  private void unregisterSignals() {
    assert signalFactsChangedSubscription != null : "Signal should be registered on resume.";
    signalFactsChangedSubscription.unsubscribe();
    log.debug("Signals unregistered.");
  }

  private void onFactsChanged() {
    log.debug("Signal FactsChanged appeared!");
    loadFactList();
  }

  public void onRetry() {
    log.debug("onRetry()");
    loadFactList();
    // TODO: onRetry() should call last interrupted presenter's command
  }

  private void onException(Throwable e) {
    log.debug("onException()");
    if (viewListView == null) return;

    viewListView.hideLoading();
    log.error("Unknown Exception!", e);
  }

  @Override public void onAddFact() {
    if (viewListView != null) {
      viewListView.navigateToAddFact();
    }
  }

  @Override public void onEditFact(@Nonnull PresentationFact fact) {
    log.debug("onEditFact()");
    if (viewListView != null) {
      viewListView.navigateToEditFact(fact);
    }
  }

  @Override public void onStartFact(@Nonnull PresentationFact fact) {
    log.debug("onStartFact()");
    Observable.just(fact)
        .map(mapper::transform)
        .flatMap(startFactUseCase::execute)
        .subscribeOn(Schedulers.io())
        .observeOn(postExecuteScheduler)
        .subscribe(new OnCompletedObserver(() -> log.info("The fact started."), this::onException));
  }

  @Override public void onStopFact(@Nonnull PresentationFact fact) {
    log.debug("onStopFact()");
    log.debug("    id:             {}", fact.getId().isPresent() ? fact.getId().get() : "absent");
    log.debug("    activity:       \"{}\"", fact.getActivity());
    Observable.just(fact)
        .map(mapper::transform)
        .flatMap(stopFactUseCase::execute)
        .subscribeOn(Schedulers.io())
        .observeOn(postExecuteScheduler)
        .subscribe(new OnCompletedObserver(() -> log.info("The fact stopped."), this::onException));
  }

  @Override public void onRemoveFact(@Nonnull PresentationFact fact) {
    log.debug("onRemoveFact()");
    Observable.just(fact)
        .map(mapper::transform)
        .flatMap(removeFactUseCase::execute)
        .subscribeOn(Schedulers.io())
        .observeOn(postExecuteScheduler)
        .subscribe(new OnCompletedObserver(() -> log.info("The fact removed."), this::onException));
  }

  @Override public void onNewFactPrepared(@Nonnull PresentationFact newFact) {
    log.debug("onAddFact()");
    Observable.just(newFact)
        .map(mapper::transform)
        .flatMap(addFactUseCase::execute)
        .subscribeOn(Schedulers.io())
        .observeOn(postExecuteScheduler)
        .subscribe(new OnCompletedObserver(() -> log.info("The fact added."), this::onException));
  }

  @Override public void onEditedFactPrepared(@Nonnull PresentationFact editedFact) {
    Observable.just(editedFact)
        .map(mapper::transform)
        .flatMap(editFactUseCase::execute)
        .subscribeOn(Schedulers.io())
        .observeOn(postExecuteScheduler)
        .subscribe(
            new OnCompletedObserver(() -> log.info("The fact was edited."), this::onException));
  }
}
