package info.rynkowski.hamsterclient.domain.interactor;

public interface UseCase<Result, Argument> {
  Result execute(Argument arg) throws Exception;
}
