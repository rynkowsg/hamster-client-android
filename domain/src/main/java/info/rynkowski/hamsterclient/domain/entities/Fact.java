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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
public class Fact {

  private final @Nonnull Optional<Integer> id;
  private final @Nonnull String activity;
  private final @Nonnull String category;
  private final @Nonnull List<String> tags;
  private final @Nonnull String description;
  private final @Nonnull Calendar startTime;
  private final @Nonnull Optional<Calendar> endTime;

  private Fact(@Nonnull Builder b) {
    this.id = b.id;
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

    private @Nonnull Optional<Integer> id = Optional.absent();
    private @Nonnull String activity = "";
    private @Nonnull String category = "";
    private @Nonnull List<String> tags = new ArrayList<String>();
    private @Nonnull String description = "";
    private @Nonnull Calendar startTime = GregorianCalendar.getInstance();
    private @Nonnull Optional<Calendar> endTime = Optional.absent();

    public Builder(@Nonnull Fact fact) {
      this.id = fact.getId();
      this.activity = fact.getActivity();
      this.category = fact.getCategory();
      this.description = fact.getDescription();
      this.tags = fact.getTags();
      this.startTime = fact.getStartTime();
      this.endTime = fact.getEndTime();
    }

    public @Nonnull Fact build() {
      return new Fact(this);
    }
  }
}
