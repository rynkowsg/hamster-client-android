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

import org.gnome.Struct2;

// used in:
//   public List<Struct2> GetCategoryActivities(int category_id);

public class AdapterStruct2 {
  private Struct2 object;

  public AdapterStruct2(Struct2 object) {
    this.object = object;
  }

  public static int id(Struct2 object) {
    return object.a;
  }

  public static String name(Struct2 object) {
    return object.b;
  }

  public static int category_id(Struct2 object) {
    return object.c;
  }

  public static String category(Struct2 object) {
    return object.d;
  }

  public int id() {
    return id(object);
  }

  public String name() {
    return name(object);
  }

  public int category_id() {
    return category_id(object);
  }

  public String category() {
    return category(object);
  }
}
