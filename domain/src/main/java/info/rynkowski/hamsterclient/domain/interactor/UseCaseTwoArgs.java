package info.rynkowski.hamsterclient.domain.interactor;

public interface UseCaseTwoArgs<Result, Argument, SecondArgument> {
  Result execute(Argument arg, SecondArgument secondArg) throws Exception;
}
