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

import org.freedesktop.Notifications;

public class NotificationsRemoteObject extends RemoteObjectAbstract<Notifications> {
  private static final String busName = "org.freedesktop.Notifications";
  private static final String objectPath = "/org/freedesktop/Notifications";
  private static final Class dbusType = Notifications.class;

  public NotificationsRemoteObject(ConnectionProvider connectionProvider) {
    super(connectionProvider, busName, objectPath, dbusType);
  }
}
