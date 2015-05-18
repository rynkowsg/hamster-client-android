package info.rynkowski.hamsterclient.domain.interactor;

import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.List;

public class GetTodaysFacts implements UseCaseArgumentless<List<Fact>> {
  private final HamsterDataSource hamsterDataSource;

  public GetTodaysFacts(HamsterDataSource hamsterDataSource) {
    this.hamsterDataSource = hamsterDataSource;
  }

  @Override public List<Fact> execute() throws Exception {
    return hamsterDataSource.GetTodaysFacts();
  }
}
