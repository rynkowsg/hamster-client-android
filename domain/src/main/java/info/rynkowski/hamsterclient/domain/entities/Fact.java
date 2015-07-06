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
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
public class Fact {

  private @Nonnull Optional<Integer> id;
  private @Nonnull Activity activity;
  private @Nonnull Calendar startTime;
  private @Nonnull Optional<Calendar> endTime;
  private @Nonnull List<String> tags;
  private @Nonnull String description;

  private Fact(@Nonnull Builder b) {
    this.id = b.id;
    this.activity = b.activity;
    this.description = b.description;
    this.tags = b.tags;
    this.startTime = b.startTime;
    this.endTime = b.endTime;
  }

  @SuppressWarnings("NullableProblems")  // To ignore warnings "Not-null fields must be initialized"
  @NoArgsConstructor(access = AccessLevel.PUBLIC)
  @Setter
  @Accessors(fluent = true, chain = true)
  public static class Builder {

    private @Nonnull Optional<Integer> id = Optional.absent();
    private @Nonnull Activity activity;
    private @Nonnull Calendar startTime;
    private @Nonnull Optional<Calendar> endTime;
    private @Nonnull List<String> tags;
    private @Nonnull String description;

    public @Nonnull Fact build() {
      return new Fact(this);
    }
  }
}
