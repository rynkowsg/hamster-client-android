package info.rynkowski.hamsterclient.presentation.presenter;

import android.support.annotation.NonNull;
import android.util.Log;
import info.rynkowski.hamsterclient.data.datasource.HamsterDataSourceImpl;
import info.rynkowski.hamsterclient.data.dbus.DBusConnector;
import info.rynkowski.hamsterclient.data.dbus.DBusConnectorImpl;
import info.rynkowski.hamsterclient.data.dbus.HamsterRemoteObject;
import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.interactor.AddFactUseCase;
import info.rynkowski.hamsterclient.domain.interactor.GetTodaysFacts;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import info.rynkowski.hamsterclient.presentation.model.mapper.FactModelDataMapper;
import info.rynkowski.hamsterclient.presentation.view.FactListView;
import java.util.List;

public class FactListPresenter implements Presenter {

  public static final String TAG = "FactListPresenter";

  // TODO: Use DI container!
  private static DBusConnector dependencyConnector;
  private static HamsterRemoteObject hamsterRemoteObject;
  private static HamsterDataSource hamsterDataSource;
  private static AddFactUseCase addFactUseCase;
  private static GetTodaysFacts getTodaysFacts;

  private FactListView viewListView;
  private FactModelDataMapper mapper;

  public void setView(@NonNull FactListView view) {
    this.viewListView = view;
  }

  @Override public void initialize() {
    Log.d(TAG, "initialize()");
    new Thread(() -> {
      dependencyConnector = new DBusConnectorImpl("10.0.2.5", "55555");
      dependencyConnector.open();
      hamsterRemoteObject = new HamsterRemoteObject(dependencyConnector);
      hamsterDataSource = new HamsterDataSourceImpl(hamsterRemoteObject);
      addFactUseCase = new AddFactUseCase(hamsterDataSource);
      getTodaysFacts = new GetTodaysFacts(hamsterDataSource);
    }).start();
    mapper = new FactModelDataMapper();
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
    dependencyConnector.close();
  }

  public void onFactClicked(FactModel factModel) {
    Log.d(TAG, "onFactClicked()");
    // Empty still
  }

  private void loadFactList() {
    Log.d(TAG, "loadFactList()");
    try {
      List<Fact> useCaseResult = getTodaysFacts.execute();
      List<FactModel> facts = mapper.transform(useCaseResult);
      viewListView.renderFactList(facts);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addFact(FactModel fact){
    Log.d(TAG, "addFact()");
    try {
      addFactUseCase.execute(mapper.transform(fact));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
