package info.rynkowski.hamsterclient.domain.interactor;

import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.entities.Fact;

public class UpdateFactUseCase implements UseCaseTwoArgs<Void, Integer, Fact> {
  private final HamsterDataSource hamsterDataSource;

  public UpdateFactUseCase(HamsterDataSource hamsterDataSource) {
    this.hamsterDataSource = hamsterDataSource;
  }

  @Override public Void execute(Integer factId, Fact fact) throws Exception {
    hamsterDataSource.UpdateFact(factId.intValue(), fact);
    return null;
  }
}
