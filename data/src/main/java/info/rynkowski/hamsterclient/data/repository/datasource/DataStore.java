package info.rynkowski.hamsterclient.data.repository.datasource;

/**
 * Root interface for specific data store's interfaces.
 */
public interface DataStore {

  /**
   * Called to initialize data store object.
   */
  void initialize();

  /**
   * Called to deinitialize data store object.
   */
  void deinitialize();
}
