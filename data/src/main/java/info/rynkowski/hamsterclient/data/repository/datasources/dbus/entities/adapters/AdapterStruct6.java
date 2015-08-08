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

package info.rynkowski.hamsterclient.data.repository.datasources.dbus.entities.adapters;

import org.gnome.Struct6;

// used in:
//   public List<Struct6> GetCategories();

public class AdapterStruct6 {
  private Struct6 object;

  public AdapterStruct6(Struct6 object) {
    this.object = object;
  }

  public static int id(Struct6 object) {
    return object.a;
  }

  public static String name(Struct6 object) {
    return object.b;
  }

  public int id() {
    return id(object);
  }

  public String name() {
    return name(object);
  }
}
