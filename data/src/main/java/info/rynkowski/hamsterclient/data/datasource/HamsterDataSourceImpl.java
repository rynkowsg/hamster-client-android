package info.rynkowski.hamsterclient.data.datasource;

import android.util.Log;
import info.rynkowski.hamsterclient.data.dbus.HamsterRemoteObject;
import info.rynkowski.hamsterclient.data.entity.FactEntity;
import info.rynkowski.hamsterclient.data.entity.mapper.FactEntityMapper;
import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.gnome.Struct5;

@Singleton
public class HamsterDataSourceImpl implements HamsterDataSource {

  private final static String TAG = "HamsterDataSourceImpl";

  private HamsterRemoteObject hamsterObject;
  private FactEntityMapper factEntityMapper;

  @Inject
  public HamsterDataSourceImpl(HamsterRemoteObject object, FactEntityMapper factEntityMapper) {
    this.hamsterObject = object;
    this.factEntityMapper = factEntityMapper;
  }

  @Override public int AddFact(Fact fact) {
    FactEntity factEntity = factEntityMapper.transform(fact);
    String serializedName = factEntity.serializedName();
    int startTime = factEntity.getStartTime();
    int endTime = factEntity.getEndTime();

    Log.d(TAG, "AddFact( {serializedName:\""
        + serializedName + "\", startTime:" + startTime + ", endTime:" + endTime + "} )");

    int result = hamsterObject.get().AddFact(serializedName, startTime, endTime, false);
    Log.d(TAG, "Result of AddFact(): " + result);

    return result;
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
    Log.d(TAG, "GetTodaysFacts()");
    List<Struct5> dbusData = hamsterObject.get().GetTodaysFacts();
    List<Fact> facts = factEntityMapper.transformFromStruct5(dbusData);
    Log.i(TAG, "Today's facts received. Count:" + facts.size());
    return facts;
  }

  @Override public void RemoveFact(int fact_id) {
    hamsterObject.get().RemoveFact(fact_id);
  }

  @Override public void UpdateFact(int factId, Fact fact) {
    FactEntity factEntity = factEntityMapper.transform(fact);
    hamsterObject.get().UpdateFact(factId, factEntity.serializedName(),
        factEntity.getStartTime(), factEntity.getEndTime(), false, false);
  }
}
