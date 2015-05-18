package info.rynkowski.hamsterclient.data.datasource;

import info.rynkowski.hamsterclient.data.dbus.DBusConnector;
import info.rynkowski.hamsterclient.data.dbus.HamsterRemoteObject;
import info.rynkowski.hamsterclient.data.entity.FactEntity;
import info.rynkowski.hamsterclient.data.entity.mapper.FactEntityMapper;
import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.Date;
import java.util.List;

public class HamsterDataSourceImpl implements HamsterDataSource {
  private DBusConnector connector;
  private HamsterRemoteObject hamsterObject;

  private FactEntityMapper factEntityMapper;

  HamsterDataSourceImpl(DBusConnector connector, HamsterRemoteObject object) {
    this.connector = connector;
    this.hamsterObject = object;
  }

  @Override public int AddFact(Fact fact) {
    FactEntity factEntity = factEntityMapper.transform(fact);
    return hamsterObject.get().AddFact(
        factEntity.serializedName(),
        (int) factEntity.getStartTime(),
        (int) factEntity.getEndTime(),
        false);
  }

  @Override public Fact GetFact(int factId) {
    FactEntity factEntity = new FactEntity(hamsterObject.get().GetFact(factId));
    return factEntityMapper.transform(factEntity);
  }

  @Override
  public List<Fact> GetFacts(Date start_date, Date end_date, String search_terms, int limit,
      boolean asc_by_date) {
    return factEntityMapper.transformFromStruct7(
        hamsterObject.get().GetFacts(
            (int) start_date.getTime(),
            (int) end_date.getTime(),
            search_terms,
            limit,
            asc_by_date));
  }

  @Override public List<Fact> GetTodaysFacts() {
    return factEntityMapper.transformFromStruct5(hamsterObject.get().GetTodaysFacts());
  }

  @Override public void RemoveFact(int fact_id) {
    hamsterObject.get().RemoveFact(fact_id);
  }

  @Override public void UpdateFact(int factId, Fact fact) {
    FactEntity factEntity = factEntityMapper.transform(fact);
    hamsterObject.get().UpdateFact(factId, factEntity.serializedName(),
        (int) factEntity.getStartTime(), (int) factEntity.getEndTime(), false, false);
  }
}
