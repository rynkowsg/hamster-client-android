package info.rynkowski.hamsterclient.domain.exception;

/**
 * Exception throw by the application when the use case object is not initialized before execution.
 */
public class NotInitializedException extends Exception {

  public NotInitializedException() {
    super();
  }

  public NotInitializedException(final String message) {
    super(message);
  }

  public NotInitializedException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public NotInitializedException(final Throwable cause) {
    super(cause);
  }
}
