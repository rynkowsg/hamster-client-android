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
import info.rynkowski.hamsterclient.domain.entities.Category;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.domain.entities.Tag;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

/**
 * Class that translates fact representations of two layers: data and domain.
 * Could transform object of {@link info.rynkowski.hamsterclient.domain.entities.Fact} to
 * {@link info.rynkowski.hamsterclient.data.entity.FactEntity} and in the opposite direction.
 */
@Slf4j
@Singleton
public class FactEntityMapper {

  public @Inject FactEntityMapper() {
    //empty
  }

  public @Nonnull Fact transform(@Nonnull FactEntity factEntity) {
    Category category = new Category(factEntity.getCategory());
    Activity activity = new Activity(factEntity.getActivity(), category);

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
        .tags(transformToTagList(factEntity.getTags()))
        .build();
  }

  public @Nonnull FactEntity transform(@Nonnull Fact fact) {

    Time startTime = Time.getInstance(fact.getStartTime());
    startTime.roundToMinutes(); // Hamster Time Tracker doesn't operate on seconds and millis.

    Optional<Time> endTime = Optional.absent();
    if (fact.getEndTime().isPresent()) {
      endTime = Optional.of(Time.getInstance(fact.getEndTime().get()).roundToMinutes());
      endTime.get().roundToMinutes(); // Hamster Time Tracker doesn't operate on seconds and millis.
    }

    return new FactEntity.Builder()
        .id(fact.getId())
        .activity(fact.getActivity().getName())
        .category(fact.getActivity().getCategory().getName())
        .startTime(startTime)
        .endTime(endTime)
        .description(fact.getDescription())
        .tags(transformToStringList(fact.getTags()))
        .build();
  }

  protected List<Tag> transformToTagList(@Nonnull List<String> strTags) {
    List<Tag> tags = new ArrayList<Tag>(strTags.size());
    ListIterator<String> stringIterator = strTags.listIterator();
    while (stringIterator.hasNext()) {
      tags.add(new Tag(stringIterator.next()));
    }
    return tags;
  }

  protected List<String> transformToStringList(@Nonnull List<Tag> tags) {
    List<String> strTags = new ArrayList<String>(tags.size());
    ListIterator<Tag> tagIterator = tags.listIterator();
    while (tagIterator.hasNext()) {
      strTags.add(tagIterator.next().getName());
    }
    return strTags;
  }
}
