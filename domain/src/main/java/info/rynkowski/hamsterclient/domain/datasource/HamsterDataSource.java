package info.rynkowski.hamsterclient.domain.datasource;

import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.List;

public interface HamsterDataSource {
  public int AddFact(Fact fact);

  public Fact GetFact(int factId);

  public void UpdateFact(int factId, Fact fact);

  public void RemoveFact(int fact_id);

  public List<Fact> GetTodaysFacts();
}
