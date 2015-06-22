package info.rynkowski.hamsterclient.presentation.internal.di.modules;

import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.data.dbus.DBusConnectionProvider;
import info.rynkowski.hamsterclient.data.dbus.DBusConnectionProviderOverNetwork;
import info.rynkowski.hamsterclient.data.dbus.HamsterRemoteObject;
import info.rynkowski.hamsterclient.data.repository.HamsterDataRepository;
import info.rynkowski.hamsterclient.data.repository.datasource.HamsterDataStore;
import info.rynkowski.hamsterclient.data.repository.datasource.RemoteHamsterDataStore;
import info.rynkowski.hamsterclient.domain.repository.HamsterRepository;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Dagger module that provides collaborators from Data layer.
 */
@Module
public class DataModule {

  private final String host;
  private final String port;

  public DataModule(String host, String port) {
    this.host = host;
    this.port = port;
  }

  @Provides @Singleton DBusConnectionProvider provideDBusConnectionProvider() {
    return new DBusConnectionProviderOverNetwork(host, port);
  }

  @Provides @Singleton HamsterRemoteObject provideHamsterRemoteObject(
      DBusConnectionProvider connectionProvider) {
    return new HamsterRemoteObject(connectionProvider);
  }

  @Provides @Singleton @Named("remote") HamsterDataStore remoteHamsterDataStore(
      DBusConnectionProvider connectionProvider, HamsterRemoteObject hamsterRemoteObject) {
    return new RemoteHamsterDataStore(connectionProvider, hamsterRemoteObject);
  }

  @Provides @Singleton HamsterRepository provideHamsterRepository(
      HamsterDataRepository repository) {
    return repository;
  }
}
