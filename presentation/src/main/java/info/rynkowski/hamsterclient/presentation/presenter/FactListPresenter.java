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
import com.google.common.base.Optional;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
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
  /*, HamsterRepository.OnDataStoreChangedListener */

  private final ThreadExecutor threadExecutor;
  private final PostExecutionThread postExecutionThread;

  private final HamsterRepository hamsterRepository;
  private final UseCase<Integer, Fact> addFactUseCase;
  private final UseCase<Void, Fact> removeFactUseCase;
  private final UseCase<Integer, Fact> updateFactUseCase;
  private final UseCaseArgumentless<List<Fact>> getTodaysFactsUseCase;

  private final FactModelDataMapper mapper;

  private @Nullable FactListView viewListView;

  //Subscription subscriptionOnChange;

  @Inject
  public FactListPresenter(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
      HamsterRepository hamsterRepository, @Named("AddFact") UseCase<Integer, Fact> addFactUseCase,
      @Named("RemoveFact") UseCase<Void, Fact> removeFactUseCase,
      @Named("UpdateFact") UseCase<Integer, Fact> updateFactUseCase,
      @Named("GetTodaysFacts") UseCaseArgumentless<List<Fact>> getTodaysFactsUseCase,
      FactModelDataMapper mapper) {
    this.threadExecutor = threadExecutor;
    this.postExecutionThread = postExecutionThread;
    this.hamsterRepository = hamsterRepository;
    this.addFactUseCase = addFactUseCase;
    this.removeFactUseCase = removeFactUseCase;
    this.updateFactUseCase = updateFactUseCase;
    this.getTodaysFactsUseCase = getTodaysFactsUseCase;
    this.mapper = mapper;
  }

  public void setView(@Nullable FactListView view) {
    this.viewListView = view;
  }

  @Override public void start() {
    log.debug("start()");

    loadFactList();
    registerSignals();
    //subscriptionOnChange = hamsterRepository.onChange()
    //    .subscribeOn(Schedulers.from(threadExecutor))
    //    .observeOn(postExecutionThread.getScheduler())
    //    .subscribe(this::onDataStoreChanged);
    //this.chooseDataStore(HamsterRepository.Type.REMOTE);

    log.debug("FactList started.");
  }

  @Override public void resume() {
    log.debug("FactList resumed.");
  }

  @Override public void pause() {
    log.debug("FactList paused.");
  }

  @Override public void destroy() {
    //subscriptionOnChange.unsubscribe();
    //hamsterRepository.deinitialize();
    log.debug("FactList destroyed.");
  }

  private void loadFactList() {
    log.debug("loadFactList()");
    if (viewListView == null) return;

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

  public void addFact(@NonNull FactModel factModel) {
    log.debug("addFact()");
    Fact fact = mapper.transform(factModel);
    addFactUseCase.execute(fact)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(id -> log.info("New fact added, id={}", id), this::onException);
  }

  private void removeFact(@NonNull FactModel fact) {
    log.debug("removeFact");
    Observable.just(fact)
        .map(mapper::transform)
        .flatMap(removeFactUseCase::execute)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(id -> log.info("A fact (id: {}) removed", id), this::onException);
  }

  public void updateFact(@NonNull FactModel factModel) {
    log.debug("updateFact()");
    Fact fact = mapper.transform(factModel);
    updateFactUseCase.execute(fact)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(id -> log.info("Fact updated, id={}", id), this::onException);
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

  //@Override public void onDataStoreChanged(HamsterRepository.Status status) {
  //  log.debug("onDataStoreChanged(status: {})", status);
  //  switch (status) {
  //    case SWITCHED_TO_REMOTE:
  //    case SWITCHED_TO_LOCAL:
  //      this.registerSignals();
  //      this.loadFactList();
  //      break;
  //    case LOCAL_UNAVAILABLE:
  //      throw new RuntimeException("Local database should be always available.");
  //    case REMOTE_UNAVAILABLE:
  //      viewListView.showRetry();
  //      break;
  //    default:
  //      throw new RuntimeException("Unknown status: " + status);
  //  }
  //}

  //private void chooseDataStore(HamsterRepository.Type type) {
  //  log.debug("chooseDataStore(type: {})", type);
  //  viewListView.showLoading();
  //  new Thread(() -> hamsterRepository.initialize(type)).start();
  //}

  public void onRetry() {
    log.debug("onRetry()");
    //chooseDataStore(HamsterRepository.Type.REMOTE);
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
      log.error(
          "Unknown Exception!", e);
    }
  }

  public void onRefresh() {
    loadFactList();
  }

  @Override public void onFactItemClicked(@NonNull FactModel fact) {
    log.debug("onFactItemClicked()");
    // Empty still
  }

  @Override public void onStartFactClicked(@NonNull FactModel fact) {
    log.debug("onStartFactClicked()");
    addFact(new FactModel.Builder(fact)
        .startTime(GregorianCalendar.getInstance())
        .endTime(Optional.<Calendar>absent())
        .build());
  }

  @Override public void onStopFactClicked(@NonNull FactModel fact) {
    log.debug("onStopFactClicked()");
    log.debug("    id:             {}",
        fact.getId().isPresent() ? fact.getId().get() : "absent");
    log.debug("    activity:       \"{}\"", fact.getActivity());
    updateFact(new FactModel.Builder(fact)
        .endTime(Optional.of(GregorianCalendar.getInstance()))
        .build());
  }

  @Override public void onEditFactClicked(@NonNull FactModel fact) {
    log.debug("onEditFactClicked()");
    // Empty still
  }

  @Override public void onRemoveFactClicked(@NonNull FactModel fact) {
    log.debug("onRemoveFactClicked()");
    removeFact(fact);
  }
}
