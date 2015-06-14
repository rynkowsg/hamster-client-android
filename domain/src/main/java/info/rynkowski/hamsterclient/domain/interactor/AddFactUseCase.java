package info.rynkowski.hamsterclient.domain.interactor;

import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AddFactUseCase implements UseCaseOneArg<Integer, Fact> {
  private final HamsterDataSource hamsterDataSource;

  @Inject
  public AddFactUseCase(HamsterDataSource hamsterDataSource) {
    this.hamsterDataSource = hamsterDataSource;
  }

  @Override public Integer execute(Fact arg) throws Exception {
    return hamsterDataSource.AddFact(arg);
  }
}
