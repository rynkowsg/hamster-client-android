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

package info.rynkowski.hamsterclient.data.entity.mapper;

import com.google.common.base.Optional;
import info.rynkowski.hamsterclient.data.entity.FactEntity;
import info.rynkowski.hamsterclient.data.utils.Time;
import info.rynkowski.hamsterclient.domain.entities.Activity;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.gnome.Struct5;
import org.gnome.Struct7;

/**
 * Class that translates fact representations of two layers: data and domain.
 * Could transform object of {@link info.rynkowski.hamsterclient.domain.entities.Fact} to
 * {@link info.rynkowski.hamsterclient.data.entity.FactEntity} and in the opposite direction.
 */
@Singleton
public class FactEntityMapper {

  // Computer's time zone
  // It is assumed that remote computer has the same time zone as the mobile device.
  private @Nonnull TimeZone remoteTimeZone = TimeZone.getDefault();

  public @Inject FactEntityMapper() {
    //empty
  }

  public @Nonnull List<Fact> transformFromStruct5(@Nonnull List<Struct5> structList) {
    List<Fact> factList = new ArrayList<>(structList.size());
    Fact fact;
    for (Struct5 struct : structList) {
      fact = transform(new FactEntity(struct));
      factList.add(fact);
    }
    return factList;
  }

  public @Nonnull List<Fact> transformFromStruct7(@Nonnull List<Struct7> structList) {
    List<Fact> factList = new ArrayList<>(structList.size());
    Fact fact;
    for (Struct7 struct : structList) {
      fact = transform(new FactEntity(struct));
      factList.add(fact);
    }
    return factList;
  }

  public @Nonnull Fact transform(@Nonnull FactEntity factEntity) {
    Activity activity = new Activity(factEntity.getActivity(), factEntity.getCategory());

    Calendar startTime = factEntity.getStartTime().getCalendar();

    Optional<Calendar> endTime = Optional.absent();
    if (factEntity.getEndTime().isPresent()) {
      endTime = Optional.of(factEntity.getEndTime().get().getCalendar());
    }

    return new Fact.Builder()
        .id(factEntity.getId())
        .activity(activity)
        .startTime(startTime)
        .endTime(endTime)
        .description(factEntity.getDescription())
        .tags(factEntity.getTags())
        .build();
  }

  public @Nonnull FactEntity transform(@Nonnull Fact fact) {

    Time startTime = Time.getInstance(fact.getStartTime());

    Optional<Time> endTime = Optional.absent();
    if (fact.getEndTime().isPresent()) {
      endTime = Optional.of(Time.getInstance(fact.getEndTime().get()));
    }

    return new FactEntity.Builder()
        .id(fact.getId())
        .activity(fact.getActivity().getName())
        .category(fact.getActivity().getCategory())
        .startTime(startTime)
        .endTime(endTime)
        .description(fact.getDescription())
        .tags(fact.getTags())
        .build();
  }
}
