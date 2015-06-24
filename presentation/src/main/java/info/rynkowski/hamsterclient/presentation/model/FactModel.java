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

package info.rynkowski.hamsterclient.presentation.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

// TODO: Transform to parcelable
public class FactModel implements Serializable {

  @Getter private int factId;
  @Getter private String activity;
  @Getter private String category;
  @Getter private List<String> tags;
  @Getter private String description;
  @Getter private Calendar startTime;
  @Getter private Calendar endTime;

  private FactModel(Builder b) {
    this.activity = b.activity;
    this.category = b.category;
    this.description = b.description;
    this.tags = b.tags;
    this.startTime = b.startTime;
    this.endTime = b.endTime;
  }

  @NoArgsConstructor(access = AccessLevel.PUBLIC)
  @Setter
  @Accessors(fluent = true, chain = true)
  public static class Builder {

    private String activity;
    private String category;
    private String description;
    private List<String> tags;
    private Calendar startTime;
    private Calendar endTime;

    public FactModel build() {
      return new FactModel(this);
    }
  }
}
