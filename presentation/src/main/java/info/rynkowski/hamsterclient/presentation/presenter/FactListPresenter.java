package info.rynkowski.hamsterclient.presentation.presenter;

import android.support.annotation.NonNull;
import android.util.Log;
import info.rynkowski.hamsterclient.data.dbus.DBusConnector;
import info.rynkowski.hamsterclient.domain.interactor.AddFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.GetTodaysFactsUseCase;
import info.rynkowski.hamsterclient.presentation.internal.di.ActivityScope;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.presentation.model.mapper.FactModelDataMapper;
import info.rynkowski.hamsterclient.presentation.view.FactListView;
import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer for fact list screen.
 */
@ActivityScope
public class FactListPresenter implements Presenter {

  private static final String TAG = "FactListPresenter";

  private final DBusConnector dBusConnector;
  private final AddFactUseCase addFactUseCase;
  private final GetTodaysFactsUseCase getTodaysFactsUseCase;

  private final FactModelDataMapper mapper;

  private FactListView viewListView;

  @Inject public FactListPresenter(DBusConnector dbusConnector, AddFactUseCase addFactUseCase,
      GetTodaysFactsUseCase getTodaysFactsUseCase, FactModelDataMapper mapper) {
    this.dBusConnector = dbusConnector;
    this.addFactUseCase = addFactUseCase;
    this.getTodaysFactsUseCase = getTodaysFactsUseCase;
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
    getTodaysFactsUseCase.execute()
        .map(mapper::transform)
        .subscribe(viewListView::renderFactList);
  }

  public void addFact(FactModel fact) {
    Log.d(TAG, "addFact()");
    addFactUseCase
        .setFact(mapper.transform(fact))
        .execute()
        .subscribe(
            id -> Log.i(TAG, "New fact added, id: " + id),
            Throwable::printStackTrace);
  }
}
