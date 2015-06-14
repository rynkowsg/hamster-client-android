package info.rynkowski.hamsterclient.presentation.internal.di.modules;

import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.data.datasource.HamsterDataSourceImpl;
import info.rynkowski.hamsterclient.data.dbus.DBusConnector;
import info.rynkowski.hamsterclient.data.dbus.DBusConnectorImpl;
import info.rynkowski.hamsterclient.data.dbus.HamsterRemoteObject;
import info.rynkowski.hamsterclient.domain.datasource.HamsterDataSource;
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

  @Provides @Singleton DBusConnector provideDBusConnector() {
    return new DBusConnectorImpl(host, port);
  }

  @Provides @Singleton HamsterRemoteObject provideHamsterRemoteObject(DBusConnector dBusConnector) {
    return new HamsterRemoteObject(dBusConnector);
  }

  @Provides @Singleton HamsterDataSource provideHamsterDataSource(HamsterRemoteObject remoteObject) {
    return new HamsterDataSourceImpl(remoteObject);
  }
}
