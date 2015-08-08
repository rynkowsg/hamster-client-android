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

import java.util.List;
import org.gnome.Struct4;

// used in:
//   public Struct4 GetFact(int fact_id);

public class AdapterStruct4 {
  private Struct4 object;

  public AdapterStruct4(Struct4 object) {
    this.object = object;
  }

  public static int id(Struct4 object) {
    return object.a;
  }

  public static int start_time(Struct4 object) {
    return object.b;
  }

  public static int end_time(Struct4 object) {
    return object.c;
  }

  public static String description(Struct4 object) {
    return object.d;
  }

  public static String name(Struct4 object) {
    return object.e;
  }

  public static int activity_id(Struct4 object) {
    return object.f;
  }

  public static String category(Struct4 object) {
    return object.g;
  }

  public static List<String> tags(Struct4 object) {
    return object.h;
  }

  public static int date(Struct4 object) {
    return object.i;
  }

  public static int delta(Struct4 object) {
    return object.j;
  }

  public static boolean exported(Struct4 object) {
    return object.k;
  }

  public int id() {
    return id(object);
  }

  public int start_time() {
    return start_time(object);
  }

  public int end_time() {
    return end_time(object);
  }

  public String description() {
    return description(object);
  }

  public String name() {
    return name(object);
  }

  public int activity_id() {
    return activity_id(object);
  }

  public String category() {
    return category(object);
  }

  public List<String> tags() {
    return tags(object);
  }

  public int date() {
    return date(object);
  }

  public int delta() {
    return delta(object);
  }

  public boolean exported() {
    return exported(object);
  }
}
