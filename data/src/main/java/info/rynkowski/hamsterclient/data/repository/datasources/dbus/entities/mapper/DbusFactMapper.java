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

package info.rynkowski.hamsterclient.data.repository.datasources.dbus.entities.mapper;

import com.google.common.base.Optional;
import info.rynkowski.hamsterclient.data.repository.datasources.dbus.entities.DbusFact;
import info.rynkowski.hamsterclient.data.utils.Time;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.Calendar;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class that translates a fact between two representations: dbus and domain.
 * Could transform object of {@link Fact} to
 * {@link DbusFact} and in the opposite direction.
 */
@Singleton
public class DbusFactMapper {

  public @Inject DbusFactMapper() {
    //empty
  }

  public @Nonnull Fact transform(@Nonnull DbusFact dbusFact) {
    Calendar startTime = dbusFact.getStartTime().getCalendar();

    Optional<Calendar> endTime = Optional.absent();
    if (dbusFact.getEndTime().isPresent()) {
      endTime = Optional.of(dbusFact.getEndTime().get().getCalendar());
    }

    return new Fact.Builder() //
        .id(dbusFact.getId())
        .activity(dbusFact.getActivity())
        .category(dbusFact.getCategory())
        .startTime(startTime)
        .endTime(endTime)
        .description(dbusFact.getDescription())
        .tags(dbusFact.getTags())
        .build();
  }

  public @Nonnull DbusFact transform(@Nonnull Fact fact) {

    Time startTime = Time.getInstance(fact.getStartTime());
    startTime.roundToMinutes(); // Hamster Time Tracker doesn't operate on seconds and millis.

    Optional<Time> endTime = Optional.absent();
    if (fact.getEndTime().isPresent()) {
      endTime = Optional.of(Time.getInstance(fact.getEndTime().get()).roundToMinutes());
      endTime.get().roundToMinutes(); // Hamster Time Tracker doesn't operate on seconds and millis.
    }

    return new DbusFact.Builder() //
        .id(fact.getId())
        .activity(fact.getActivity())
        .category(fact.getCategory())
        .startTime(startTime)
        .endTime(endTime)
        .description(fact.getDescription())
        .tags(fact.getTags())
        .build();
  }
}
