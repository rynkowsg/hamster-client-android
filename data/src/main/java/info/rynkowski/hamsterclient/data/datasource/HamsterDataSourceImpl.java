package info.rynkowski.hamsterclient.data.datasource;

import android.util.Log;
import info.rynkowski.hamsterclient.data.dbus.HamsterRemoteObject;
import info.rynkowski.hamsterclient.data.entity.FactEntity;
import info.rynkowski.hamsterclient.data.entity.mapper.FactEntityMapper;
import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.Date;
import java.util.List;
import org.gnome.Hamster;
import org.gnome.Struct5;

public class HamsterDataSourceImpl implements HamsterDataSource {

  private final static String TAG = "HamsterDataSourceImpl";

  // TODO: Use DI container!
  private HamsterRemoteObject hamsterObject;
  private FactEntityMapper factEntityMapper;

  public HamsterDataSourceImpl(HamsterRemoteObject object) {
    this.hamsterObject = object;
    this.factEntityMapper = new FactEntityMapper();
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
    Hamster hamster = hamsterObject.get();
    List<Struct5> dbusData = hamster.GetTodaysFacts();
    List<Fact> facts = factEntityMapper.transformFromStruct5(dbusData);
    Log.d(TAG, "Today's facts received: " + facts);
    return facts;
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
