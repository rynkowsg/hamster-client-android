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
import java.util.Calendar;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class that translates fact representations of two layers: data and domain.
 * Could transform object of {@link info.rynkowski.hamsterclient.domain.entities.Fact} to
 * {@link info.rynkowski.hamsterclient.data.entity.FactEntity} and in the opposite direction.
 */
@Singleton
public class FactEntityMapper {

  public @Inject FactEntityMapper() {
    //empty
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
