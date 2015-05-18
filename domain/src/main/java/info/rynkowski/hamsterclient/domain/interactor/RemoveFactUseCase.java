package info.rynkowski.hamsterclient.domain.interactor;

import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;

public class RemoveFactUseCase implements UseCaseOneArg<Void, Integer> {
  private final HamsterDataSource hamsterDataSource;

  public RemoveFactUseCase(HamsterDataSource hamsterDataSource) {
    this.hamsterDataSource = hamsterDataSource;
  }

  @Override public Void execute(Integer arg) throws Exception {
    hamsterDataSource.RemoveFact(arg.intValue());
    return null;
  }
}

