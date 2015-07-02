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
import info.rynkowski.hamsterclient.domain.entities.Activity;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
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
  private TimeZone remoteTimeZone = TimeZone.getDefault();

  @Inject public FactEntityMapper() {
    //empty
  }

  public List<Fact> transformFromStruct5(List<Struct5> structList) {
    List<Fact> factList = new ArrayList<>(structList.size());
    Fact fact;
    for (Struct5 struct : structList) {
      fact = transform(new FactEntity(struct));
      factList.add(fact);
    }
    return factList;
  }

  public List<Fact> transformFromStruct7(List<Struct7> structList) {
    List<Fact> factList = new ArrayList<>(structList.size());
    Fact fact;
    for (Struct7 struct : structList) {
      fact = transform(new FactEntity(struct));
      factList.add(fact);
    }
    return factList;
  }

  public Fact transform(FactEntity factEntity) {

    // Start Time
    Calendar startTime = convertTimeFromRemote(factEntity.getStartTime());

    // End Time
    Optional<Calendar> endTime = Optional.absent();
    if (factEntity.getEndTime() != 0) {
      endTime = Optional.of(convertTimeFromRemote(factEntity.getEndTime()));
    }

    Activity activity = new Activity(factEntity.getActivity(), factEntity.getCategory());
    return new Fact.Builder().activity(activity)
        .startTime(startTime)
        .endTime(endTime)
        .description(factEntity.getDescription())
        .tags(factEntity.getTags())
        .build();
  }

  public FactEntity transform(Fact fact) {

    int startTime = convertTimeToRemote(fact.getStartTime());

    int endTime = 0;
    if (fact.getEndTime().isPresent()) {
      endTime = convertTimeToRemote(fact.getEndTime().get());
    }
    return new FactEntity.Builder().activity(fact.getActivity().getName())
        .category(fact.getActivity().getCategory())
        .startTime(startTime)
        .endTime(endTime)
        .description(fact.getDescription())
        .tags(fact.getTags())
        .build();
  }

  /**
   * Converts time from remote representation to representation used at domain module.
   * @param time is remote representation of time ({@code int})
   * @return representation of time used at domain module ({@link Calendar})
   */
  private Calendar convertTimeFromRemote(int time) {
    // Hamster uses time counted in seconds.
    long date = ((long) time) * 1000;

    // Hamster provides bad time representation.
    // It requires a correction using time zone' offset of remote PC.
    date -= remoteTimeZone.getOffset(date);

    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTimeInMillis(date);

    return calendar;
  }

  /**
   * Converts time from domain representation type to remote representation.
   * @param calendar is a time representation used at domain module ({@link Calendar})
   * @return remote representation of time ({@code int})
   */
  private int convertTimeToRemote(Calendar calendar) {
    long date = calendar.getTimeInMillis();

    // Hamster provides bad time representation.
    // It requires a correction using time zone' offset of remote PC.
    date += remoteTimeZone.getOffset(date);

    // Hamster uses time counted in seconds.
    return (int) (date / 1000);
  }
}
