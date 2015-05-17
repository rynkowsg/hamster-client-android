package info.rynkowski.hamsterclient.data.datasource;

import info.rynkowski.hamsterclient.data.dbus.DBusConnector;
import info.rynkowski.hamsterclient.data.dbus.HamsterRemoteObject;
import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.List;

public class HamsterDataSourceImpl implements HamsterDataSource {
  private DBusConnector connector;
  private HamsterRemoteObject hamsterObject;

  HamsterDataSourceImpl(DBusConnector connector, HamsterRemoteObject object) {
    this.connector = connector;
    this.hamsterObject = object;
  }

  @Override public int AddFact(Fact fact) {
    return 0;
  }

  @Override public Fact GetFact(int factId) {
    return null;
  }

  @Override public void UpdateFact(int factId, Fact fact) {
  }

  @Override public void RemoveFact(int fact_id) {
  }

  @Override public List<Fact> GetTodaysFacts() {
    return null;
  }
}
