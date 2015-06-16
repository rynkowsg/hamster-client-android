package info.rynkowski.hamsterclient.presentation.presenter;

import android.support.annotation.NonNull;
import android.util.Log;
import info.rynkowski.hamsterclient.data.dbus.DBusConnector;
import info.rynkowski.hamsterclient.domain.interactor.AddFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.GetTodaysFacts;
import info.rynkowski.hamsterclient.presentation.internal.di.ActivityScope;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.presentation.model.mapper.FactModelDataMapper;
import info.rynkowski.hamsterclient.presentation.view.FactListView;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ActivityScope
public class FactListPresenter implements Presenter {

  private static final String TAG = "FactListPresenter";

  private final DBusConnector dBusConnector;
  private final AddFactUseCase addFactUseCase;
  private final GetTodaysFacts getTodaysFacts;

  private final FactModelDataMapper mapper;

  private FactListView viewListView;

  @Inject public FactListPresenter(DBusConnector dbusConnector, AddFactUseCase addFactUseCase,
      GetTodaysFacts getTodaysFacts, FactModelDataMapper mapper) {
    this.dBusConnector = dbusConnector;
    this.addFactUseCase = addFactUseCase;
    this.getTodaysFacts = getTodaysFacts;
    this.mapper = mapper;
  }

  public void setView(@NonNull FactListView view) {
    this.viewListView = view;
  }

  @Override public void initialize() {
    Log.d(TAG, "initialize()");
    new Thread(dBusConnector::open).start();
  }

  @Override public void resume() {
    Log.d(TAG, "resume()");
    this.loadFactList();
  }

  @Override public void pause() {
    Log.d(TAG, "pause()");
    // Empty
  }

  @Override public void destroy() {
    Log.d(TAG, "destroy()");
    dBusConnector.close();
  }

  public void onFactClicked(FactModel factModel) {
    Log.d(TAG, "onFactClicked()");
    // Empty still
  }

  private void loadFactList() {
    Log.d(TAG, "loadFactList()");
    Observable.defer(
        () -> Observable.just(getTodaysFacts.execute()))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .map(mapper::transform)
        .subscribe(viewListView::renderFactList);
  }

  public void addFact(FactModel fact) {
    Log.d(TAG, "addFact()");
    Observable.defer(() -> Observable.just(mapper.transform(fact)))
        .subscribe(addFactUseCase::execute);
  }
}
