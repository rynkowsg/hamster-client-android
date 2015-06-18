package info.rynkowski.hamsterclient.data.repository.datasource;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Factory that creates different implementations of {@link HamsterDataStore}.
 */
@Singleton
public class HamsterDataStoreFactory implements DataStoreFactory {

  private HamsterDataStore dataStore;

  @Inject public HamsterDataStoreFactory(@Named("remote") HamsterDataStore remoteHamsterDataStore) {
    this.dataStore = remoteHamsterDataStore;
  }

  @Override public HamsterDataStore getStore() {
    return dataStore;
  }
}
