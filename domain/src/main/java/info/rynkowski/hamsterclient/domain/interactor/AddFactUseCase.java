package info.rynkowski.hamsterclient.domain.interactor;

import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.entities.Fact;

public class AddFactUseCase implements UseCase<Integer, Fact> {
  private final HamsterDataSource hamsterDataSource;

  public AddFactUseCase(HamsterDataSource hamsterDataSource) {
    this.hamsterDataSource = hamsterDataSource;
  }

  @Override public Integer execute(Fact arg) throws Exception {
    return hamsterDataSource.AddFact(arg);
  }
}
