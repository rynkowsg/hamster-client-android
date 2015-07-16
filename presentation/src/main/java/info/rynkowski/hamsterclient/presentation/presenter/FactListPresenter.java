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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.interactor.UseCase;
import info.rynkowski.hamsterclient.domain.interactor.UseCaseArgumentless;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import info.rynkowski.hamsterclient.presentation.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.presentation.executor.ThreadExecutor;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.presentation.model.mapper.FactModelDataMapper;
import info.rynkowski.hamsterclient.presentation.view.FactListView;
import info.rynkowski.hamsterclient.presentation.view.OnFactActionListener;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.freedesktop.dbus.exceptions.DBusException;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer for fact list screen.
 */
@Slf4j
@Singleton
public class FactListPresenter implements Presenter, OnFactActionListener {

  private final @NonNull ThreadExecutor threadExecutor;
  private final @NonNull PostExecutionThread postExecutionThread;

  private final @NonNull HamsterRepository hamsterRepository;
  private final @NonNull FactModelDataMapper mapper;

  private final @NonNull UseCase<Integer, Fact> addFactUseCase;
  private final @NonNull UseCase<Integer, Fact> editFactUseCase;
  private final @NonNull UseCaseArgumentless<List<Fact>> getTodaysFactsUseCase;
  private final @NonNull UseCase<Void, Fact> startFactUseCase;
  private final @NonNull UseCase<Void, Fact> stopFactUseCase;
  private final @NonNull UseCase<Void, Fact> removeFactUseCase;

  private @Nullable FactListView viewListView;

  @Inject public FactListPresenter(@NonNull ThreadExecutor threadExecutor,
      @NonNull PostExecutionThread postExecutionThread,
      @NonNull HamsterRepository hamsterRepository, @NonNull FactModelDataMapper mapper,
      @Named("AddFact") @NonNull UseCase<Integer, Fact> addFactUseCase,
      @Named("EditFact") @NonNull UseCase<Integer, Fact> editFactUseCase,
      @Named("GetTodaysFacts") @NonNull UseCaseArgumentless<List<Fact>> getTodaysFactsUseCase,
      @Named("RemoveFact") @NonNull UseCase<Void, Fact> removeFactUseCase,
      @Named("StartFact") @NonNull UseCase<Void, Fact> startFactUseCase,
      @Named("StopFact") @NonNull UseCase<Void, Fact> stopFactUseCase) {
    this.threadExecutor = threadExecutor;
    this.postExecutionThread = postExecutionThread;
    this.hamsterRepository = hamsterRepository;
    this.mapper = mapper;
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

    loadFactList();
    registerSignals();

    log.debug("FactList started.");
  }

  @Override public void resume() {
    log.debug("FactList resumed.");
  }

  @Override public void pause() {
    log.debug("FactList paused.");
  }

  @Override public void destroy() {
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
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(factModels -> {
          viewListView.hideLoading();
          viewListView.renderFactList(factModels);
        }, this::onException);
  }

  private void registerSignals() {
    log.debug("registerSignals()");
    hamsterRepository.signalFactsChanged()
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(o -> this.onFactsChanged(), this::onException);
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
    if (e.getClass() == DBusException.class) {
      log.error("DBusException! e.getClass()={}, e.getCause()={}", e.getClass(), e.getCause());
      viewListView.showRetry();
    } else {
      log.error("Unknown Exception!", e);
    }
  }

  public void onRefresh() {
    loadFactList();
  }

  public void onAddFact(@NonNull FactModel fact) {
    log.debug("onAddFact()");
    Observable.just(fact)
        .map(mapper::transform)
        .flatMap(addFactUseCase::execute)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(id -> log.info("New fact added, id={}", id), this::onException);
  }

  @Override public void onStartFact(@NonNull FactModel fact) {
    log.debug("onStartFact()");
    Observable.just(fact)
        .map(mapper::transform)
        .flatMap(startFactUseCase::execute)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(id -> log.info("Started a fact, id={}", id), this::onException);
  }

  @Override public void onStopFact(@NonNull FactModel fact) {
    log.debug("onStopFact()");
    log.debug("    id:             {}", fact.getId().isPresent() ? fact.getId().get() : "absent");
    log.debug("    activity:       \"{}\"", fact.getActivity());
    Observable.just(fact)
        .map(mapper::transform)
        .flatMap(stopFactUseCase::execute)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(id -> log.info("Stopped a fact, id={}", id), this::onException);
  }

  @Override public void onEditFact(@NonNull FactModel fact) {
    log.debug("onEditFact()");
    Observable.just(fact)
        .map(mapper::transform)
        .flatMap(editFactUseCase::execute)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(id -> log.info("Edited a fact , id={}", id), this::onException);
  }

  @Override public void onRemoveFact(@NonNull FactModel fact) {
    log.debug("onRemoveFact()");
    Observable.just(fact)
        .map(mapper::transform)
        .flatMap(removeFactUseCase::execute)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(id -> log.info("Removed a fact, id: {}", id), this::onException);
  }
}
