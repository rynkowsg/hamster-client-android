package info.rynkowski.hamsterclient.domain.repository;

/**
 * Base interface for Repository pattern.
 */
public interface Repository {

  void initialize();

  void deinitialize();
}
