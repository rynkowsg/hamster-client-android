package info.rynkowski.hamsterclient.domain.interactor;

public interface UseCaseOneArg<Result, Argument> {
  Result execute(Argument arg) throws Exception;
}
