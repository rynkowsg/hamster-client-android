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

package info.rynkowski.hamsterclient.data.dbus;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.gnome.Hamster;
import rx.Observable;

/**
 * Wrapper class for {@link org.gnome.Hamster}'s remote object.
 */
@Singleton
public class HamsterRemoteObject extends RemoteObjectAbstract<Hamster> {

  private static final String busName = "org.gnome.Hamster";
  private static final String objectPath = "/org/gnome/Hamster";
  private static final Class dbusType = Hamster.class;

  @Inject public HamsterRemoteObject(ConnectionProvider connectionProvider) {
    super(connectionProvider, busName, objectPath, dbusType);
  }

  public Observable<Void> signalActivitiesChanged() {
    return this.createSignalObservable(Hamster.ActivitiesChanged.class);
  }

  public Observable<Void> signalFactsChanged() {
    return this.createSignalObservable(Hamster.FactsChanged.class);
  }

  public Observable<Void> signalTagsChanged() {
    return this.createSignalObservable(Hamster.TagsChanged.class);
  }

  public Observable<Void> signalToggleCalled() {
    return this.createSignalObservable(Hamster.ToggleCalled.class);
  }
}
