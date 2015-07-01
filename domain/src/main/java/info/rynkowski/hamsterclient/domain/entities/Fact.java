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

package info.rynkowski.hamsterclient.domain.entities;

import com.google.common.base.Optional;
import java.util.Calendar;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

public class Fact {
  @Getter @Setter private Activity activity;
  @Getter @Setter private Calendar startTime;
  @Getter @Setter private Optional<Calendar> endTime;
  @Getter @Setter private List<String> tags;
  @Getter @Setter private String description;

  private Fact(Builder b) {
    this.activity = b.activity;
    this.description = b.description;
    this.tags = b.tags;
    this.startTime = b.startTime;
    this.endTime = b.endTime;
  }

  @NoArgsConstructor(access = AccessLevel.PUBLIC)
  @Setter
  @Accessors(fluent = true, chain = true)
  public static class Builder {

    private Activity activity;
    private Calendar startTime;
    private Optional<Calendar> endTime;
    private List<String> tags;
    private String description;

    public Fact build() {
      return new Fact(this);
    }
  }
}
