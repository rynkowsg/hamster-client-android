package info.rynkowski.hamsterclient.domain.interactor;

public interface UseCaseArgumentless<Result> {
  Result execute() throws Exception;
}
