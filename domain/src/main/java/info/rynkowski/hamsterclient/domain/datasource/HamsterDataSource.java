package info.rynkowski.hamsterclient.domain.datasource;

import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.Date;
import java.util.List;

public interface HamsterDataSource {
  public int AddFact(Fact fact);

  public Fact GetFact(int factId);

  public List<Fact> GetFacts(Date start_date, Date end_date, String search_terms, int limit,
      boolean asc_by_date);

  public List<Fact> GetTodaysFacts();

  public void RemoveFact(int fact_id);

  public void UpdateFact(int factId, Fact fact);
}
