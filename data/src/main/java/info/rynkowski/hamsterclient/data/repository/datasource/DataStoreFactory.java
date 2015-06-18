package info.rynkowski.hamsterclient.data.repository.datasource;

/**
 * Interface that represents a factory to retrieve proper data store.
 */
public interface DataStoreFactory {

  /**
   * Provides an {@link DataStore}.
   */
  DataStore getStore();
}
