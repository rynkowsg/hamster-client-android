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
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.executor.PostExecutionThread;
import info.rynkowski.hamsterclient.domain.executor.ThreadExecutor;
import info.rynkowski.hamsterclient.domain.interactor.UseCase;
import info.rynkowski.hamsterclient.domain.interactor.UseCaseArgumentless;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import info.rynkowski.hamsterclient.presentation.internal.di.ActivityScope;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.presentation.model.mapper.FactModelDataMapper;
import info.rynkowski.hamsterclient.presentation.view.FactListView;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer for fact list screen.
 */
@ActivityScope
public class FactListPresenter implements Presenter, HamsterRepository.OnDataStoreChangedListener {

  private static final Logger log = LoggerFactory.getLogger(FactListPresenter.class);

  private final ThreadExecutor threadExecutor;
  private final PostExecutionThread postExecutionThread;

  private final HamsterRepository hamsterRepository;
  private final UseCase<Integer, Fact> addFactUseCase;
  private final UseCaseArgumentless<List<Fact>> getTodaysFactsUseCase;
  private final UseCaseArgumentless<Void> signalFactsChangedUseCase;

  private final FactModelDataMapper mapper;

  private FactListView viewListView;

  Subscription subscriptionOnChange;

  @Inject
  public FactListPresenter(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
      HamsterRepository hamsterRepository, @Named("AddFact") UseCase<Integer, Fact> addFactUseCase,
      @Named("GetTodaysFacts") UseCaseArgumentless<List<Fact>> getTodaysFactsUseCase,
      @Named("SignalFactsChanged") UseCaseArgumentless<Void> signalFactsChangedUseCase,
      FactModelDataMapper mapper) {
    this.threadExecutor = threadExecutor;
    this.postExecutionThread = postExecutionThread;
    this.hamsterRepository = hamsterRepository;
    this.addFactUseCase = addFactUseCase;
    this.getTodaysFactsUseCase = getTodaysFactsUseCase;
    this.mapper = mapper;
    this.signalFactsChangedUseCase = signalFactsChangedUseCase;
  }

  public void setView(@NonNull FactListView view) {
    this.viewListView = view;
  }

  @Override public void start() {
    subscriptionOnChange = hamsterRepository.onChange()
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(this::onDataStoreChanged);
    this.chooseDataStore(HamsterRepository.Type.REMOTE);

    log.debug("FactList started.");
  }

  @Override public void resume() {
    log.debug("FactList resumed.");
  }

  @Override public void pause() {
    log.debug("FactList paused.");
  }

  @Override public void destroy() {
    subscriptionOnChange.unsubscribe();
    hamsterRepository.deinitialize();
    log.debug("FactList destroyed.");
  }

  public void onFactClicked(FactModel factModel) {
    log.trace("onFactClicked()");
    // Empty still
  }

  private void loadFactList() {
    log.debug("loadFactList()");
    viewListView.showLoading();
    getTodaysFactsUseCase.execute()
        .doOnNext(list -> log.info("Received {} facts.", list.size()))
        .map(mapper::transform)
        .subscribe(viewListView::renderFactList, this::onException);
  }

  public void addFact(FactModel factModel) {
    log.trace("addFact()");
    Fact fact = mapper.transform(factModel);
    addFactUseCase.execute(fact)
        .subscribe(id -> log.info("New fact added, id={}", id), this::onException);
  }

  private void registerSignals() {
    signalFactsChangedUseCase.execute()
        .subscribe(o -> this.onFactsChanged(), this::onException);
  }

  private void onFactsChanged() {
    log.debug("Signal FactsChanged appeared!");
    loadFactList();
  }

  @Override public void onDataStoreChanged(HamsterRepository.Status status) {
    log.debug("onDataStoreChanged(status: {})", status);
    switch (status) {
      case SWITCHED_TO_REMOTE:
      case SWITCHED_TO_LOCAL:
        this.registerSignals();
        this.loadFactList();
        break;
      case LOCAL_UNAVAILABLE:
        throw new RuntimeException("Local database should be always available.");
      case REMOTE_UNAVAILABLE:
        viewListView.showRetry();
        break;
      default:
        throw new RuntimeException("Unknown status: " + status);
    }
  }

  private void chooseDataStore(HamsterRepository.Type type) {
    log.debug("chooseDataStore(type: {})", type);
    viewListView.showLoading();
    new Thread(() -> hamsterRepository.initialize(type)).start();
  }

  public void onRetry() {
    log.debug("onRetry()");
    chooseDataStore(HamsterRepository.Type.REMOTE);
    // here should also call the last command
  }

  private void onException(Throwable e) {
    log.debug("onException()");
    viewListView.hideLoading();
    if (e.getClass() == DBusException.class) {
      log.error("DBusException! e.getClass()={}, e.getCause()={}", e.getClass(), e.getCause());
      viewListView.showRetry();
    } else {
      log.error(
          "Unknown Exception!, e.getClass()={}, e.getCause()={}", e.getClass(), e.getCause(), e);
    }
  }
}
