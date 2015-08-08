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

package info.rynkowski.hamsterclient.data.repository.datasources.db.entities;

import com.google.common.base.Optional;
import info.rynkowski.hamsterclient.data.utils.Time;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Class represents a fact representation comfortable for data layer.
 */
@Getter
@Setter
@Accessors(chain = true)
public class DbFact {

  private @Nonnull Optional<Integer> id;
  private @Nonnull String activity;
  private @Nonnull String category;
  private @Nonnull String description;
  private @Nonnull List<String> tags;
  private @Nonnull Time startTime;
  private @Nonnull Optional<Time> endTime;

  private DbFact(@Nonnull Builder b) {
    this.id = b.id;
    this.activity = b.activity;
    this.category = b.category;
    this.tags = b.tags;
    this.description = b.description;
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
    private @Nonnull List<String> tags = new ArrayList<>();
    private @Nonnull String description = "";
    private @Nonnull Time startTime = Time.getInstance();
    private @Nonnull Optional<Time> endTime = Optional.absent();

    public @Nonnull DbFact build() {
      return new DbFact(this);
    }
  }
}
