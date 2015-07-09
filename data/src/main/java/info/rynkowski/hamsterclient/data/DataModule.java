/*
 * Copyright (C) 2015 Grzegorz Rynkowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.rynkowski.hamsterclient.data;

import dagger.Module;
import dagger.Provides;
import info.rynkowski.hamsterclient.data.dbus.ConnectionProvider;
import info.rynkowski.hamsterclient.data.dbus.ConnectionProviderOverNetwork;
import info.rynkowski.hamsterclient.data.dbus.HamsterRemoteObject;
import info.rynkowski.hamsterclient.data.repository.HamsterRepositoryImpl;
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

  @Provides @Singleton ConnectionProvider provideDBusConnectionProvider() {
    return new ConnectionProviderOverNetwork(host, port);
  }

  @Provides @Singleton HamsterRemoteObject provideHamsterRemoteObject(
      ConnectionProvider connectionProvider) {
    return new HamsterRemoteObject(connectionProvider);
  }

  @Provides @Singleton @Named("remote") HamsterDataStore remoteHamsterDataStore(
      HamsterRemoteObject hamsterRemoteObject) {
    return new RemoteHamsterDataStore(hamsterRemoteObject);
  }

  @Provides @Singleton HamsterRepository provideHamsterRepository(
      HamsterRepositoryImpl repository) {
    return repository;
  }
}
