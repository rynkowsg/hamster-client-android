package info.rynkowski.hamsterclient.presentation.presenter;

import android.support.annotation.NonNull;
import info.rynkowski.hamsterclient.data.dbus.DBusConnector;
import info.rynkowski.hamsterclient.domain.interactor.AddFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.GetTodaysFactsUseCase;
import info.rynkowski.hamsterclient.presentation.internal.di.ActivityScope;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.presentation.model.mapper.FactModelDataMapper;
import info.rynkowski.hamsterclient.presentation.view.FactListView;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer for fact list screen.
 */
@ActivityScope
public class FactListPresenter implements Presenter {

  private static final Logger log = LoggerFactory.getLogger(FactListPresenter.class);

  //TODO: I think DBusConnector shouldn't be a dependency for presenter, too detail (maybe factory?)
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
    new Thread(dBusConnector::open).start();
    log.debug("FactList initialized.");
  }

  @Override public void resume() {
    this.loadFactList();
    log.debug("FactList resumed.");
  }

  @Override public void pause() {
    log.debug("FactList paused.");
  }

  @Override public void destroy() {
    dBusConnector.close();
    log.debug("FactList destroyed.");
  }

  public void onFactClicked(FactModel factModel) {
    log.trace("onFactClicked()");
    // Empty still
  }

  private void loadFactList() {
    log.trace("loadFactList()");
    getTodaysFactsUseCase.execute()
        .map(mapper::transform)
        .subscribe(viewListView::renderFactList);
  }

  public void addFact(FactModel fact) {
    log.trace("addFact()");
    addFactUseCase
        .setFact(mapper.transform(fact))
        .execute()
        .subscribe(id -> log.info("New fact added, id=" + id), Throwable::printStackTrace);
  }
}
