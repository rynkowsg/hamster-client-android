package info.rynkowski.hamsterclient.presentation.presenter;

import android.support.annotation.NonNull;
import info.rynkowski.hamsterclient.data.dbus.DBusConnectionProvider;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.interactor.UseCase;
import info.rynkowski.hamsterclient.domain.interactor.UseCaseArgumentless;
import info.rynkowski.hamsterclient.presentation.internal.di.ActivityScope;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.presentation.model.mapper.FactModelDataMapper;
import info.rynkowski.hamsterclient.presentation.view.FactListView;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
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
  private final DBusConnectionProvider connectionProvider;
  private final UseCase<Integer, Fact> addFactUseCase;
  private final UseCaseArgumentless<List<Fact>> getTodaysFactsUseCase;

  private final FactModelDataMapper mapper;

  private FactListView viewListView;

  @Inject
  public FactListPresenter(DBusConnectionProvider connectionProvider,
      @Named("AddFact") UseCase<Integer, Fact> addFactUseCase,
      @Named("GetTodaysFacts") UseCaseArgumentless<List<Fact>> getTodaysFactsUseCase,
      FactModelDataMapper mapper) {
    this.connectionProvider = connectionProvider;
    this.addFactUseCase = addFactUseCase;
    this.getTodaysFactsUseCase = getTodaysFactsUseCase;
    this.mapper = mapper;
  }

  public void setView(@NonNull FactListView view) {
    this.viewListView = view;
  }

  @Override public void initialize() {
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
    connectionProvider.close();
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
        .subscribe(viewListView::renderFactList, Throwable::printStackTrace);
  }

  public void addFact(FactModel factModel) {
    log.trace("addFact()");
    Fact fact = mapper.transform(factModel);
    addFactUseCase.execute(fact)
        .subscribe(id -> log.info("New fact added, id=" + id), Throwable::printStackTrace);
  }
}
