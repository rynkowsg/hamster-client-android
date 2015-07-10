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

package info.rynkowski.hamsterclient.data.entity;

import com.google.common.base.Optional;
import info.rynkowski.hamsterclient.data.dbus.adapters.AdapterStruct4;
import info.rynkowski.hamsterclient.data.dbus.adapters.AdapterStruct5;
import info.rynkowski.hamsterclient.data.dbus.adapters.AdapterStruct7;
import info.rynkowski.hamsterclient.data.utils.Time;
import info.rynkowski.hamsterclient.data.utils.TimeDifferenceConverter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.gnome.Struct4;
import org.gnome.Struct5;
import org.gnome.Struct7;

/**
 * Class represents a fact representation comfortable for data layer.
 */
@Getter
@Setter
@Accessors(chain = true)
public class FactEntity {

  private @Nonnull Optional<Integer> id;
  private @Nonnull Optional<Integer> remoteId;
  private @Nonnull String activity;
  private @Nonnull String category;
  private @Nonnull String description;
  private @Nonnull List<String> tags;
  private @Nonnull Time startTime;
  private @Nonnull Optional<Time> endTime;

  public FactEntity(@Nonnull Struct4 struct) {
    this.id = Optional.absent();
    this.remoteId = Optional.of(AdapterStruct4.id(struct));
    this.activity = AdapterStruct4.name(struct);
    this.category = AdapterStruct4.category(struct);
    this.description = AdapterStruct4.description(struct);
    this.tags = AdapterStruct4.tags(struct);

    this.startTime = Time.getInstance(AdapterStruct4.start_time(struct));
    this.endTime = AdapterStruct4.end_time(struct) == 0 ? Optional.absent()
        : Optional.of(Time.getInstance(AdapterStruct4.end_time(struct)));
  }

  public FactEntity(@Nonnull Struct5 struct) {
    this.id = Optional.absent();
    this.remoteId = Optional.of(AdapterStruct5.id(struct));
    this.activity = AdapterStruct5.name(struct);
    this.category = AdapterStruct5.category(struct);
    this.description = AdapterStruct5.description(struct);
    this.tags = AdapterStruct5.tags(struct);

    this.startTime = Time.getInstance(AdapterStruct5.start_time(struct));
    this.endTime = AdapterStruct5.end_time(struct) == 0 ? Optional.absent()
        : Optional.of(Time.getInstance(AdapterStruct5.end_time(struct)));
  }

  public FactEntity(@Nonnull Struct7 struct) {
    this.id = Optional.absent();
    this.remoteId = Optional.of(AdapterStruct7.id(struct));
    this.activity = AdapterStruct7.name(struct);
    this.category = AdapterStruct7.category(struct);
    this.description = AdapterStruct7.description(struct);
    this.tags = AdapterStruct7.tags(struct);

    this.startTime = Time.getInstance(AdapterStruct7.start_time(struct));
    this.endTime = AdapterStruct7.end_time(struct) == 0 ? Optional.absent()
        : Optional.of(Time.getInstance(AdapterStruct7.end_time(struct)));
  }

  private FactEntity(@Nonnull Builder b) {
    this.id = b.id;
    this.remoteId = b.remoteId;
    this.activity = b.activity;
    this.category = b.category;
    this.tags = b.tags;
    this.description = b.description;
    this.startTime = b.startTime;
    this.endTime = b.endTime;
  }

  public @Nonnull String serializedName() {
    String res = activity;
    if (!category.isEmpty()) {
      res += "@" + category;
    }
    res += ",";
    if (!description.isEmpty()) {
      res += description;
    }
    res += " ";
    if (!tags.isEmpty()) {
      res += "#" + StringUtils.join(tags, '#');
    }
    return res;
  }

  public @Nonnull FactEntity timeFixRemoteToLocal() {
    this.setStartTime(TimeDifferenceConverter.remoteToLocal(this.getStartTime()));
    if (this.getEndTime().isPresent()) {
      this.setEndTime(Optional.of(TimeDifferenceConverter.remoteToLocal(this.getEndTime().get())));
    }
    return this;
  }

  public @Nonnull FactEntity timeFixLocalToRemote() {
    this.setStartTime(TimeDifferenceConverter.localToRemote(this.getStartTime()));
    if (this.getEndTime().isPresent()) {
      this.setEndTime(Optional.of(TimeDifferenceConverter.localToRemote(this.getEndTime().get())));
    }
    return this;
  }

  @NoArgsConstructor(access = AccessLevel.PUBLIC)
  @Setter
  @Accessors(fluent = true, chain = true)
  public static class Builder {

    private @Nonnull Optional<Integer> id = Optional.absent();
    private @Nonnull Optional<Integer> remoteId = Optional.absent();
    private @Nonnull String activity = "";
    private @Nonnull String category = "";
    private @Nonnull List<String> tags = new ArrayList<>();
    private @Nonnull String description = "";
    private @Nonnull Time startTime = Time.getInstance();
    private @Nonnull Optional<Time> endTime = Optional.absent();

    public @Nonnull FactEntity build() {
      return new FactEntity(this);
    }
  }
}
