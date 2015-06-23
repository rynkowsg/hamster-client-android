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

import info.rynkowski.hamsterclient.data.entity.FactEntity;
import info.rynkowski.hamsterclient.domain.entities.Activity;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    Calendar startTime = Calendar.getInstance();
    startTime.setTime(new Date((long) (factEntity.getStartTime()) * 1000));
    Calendar endTime = Calendar.getInstance();
    endTime.setTime(new Date((long) (factEntity.getEndTime()) * 1000));

    Activity activity = new Activity(factEntity.getActivity(), factEntity.getCategory());
    return new Fact.Builder().activity(activity)
        .startTime(startTime)
        .endTime(endTime)
        .description(factEntity.getDescription())
        .tags(factEntity.getTags())
        .build();
  }

  public FactEntity transform(Fact fact) {
    return new FactEntity.Builder().activity(fact.getActivity().getName())
        .category(fact.getActivity().getCategory())
        .startTime((int) (fact.getStartTime().getTimeInMillis() / 1000))
        .endTime((int) (fact.getEndTime().getTimeInMillis() / 1000))
        .description(fact.getDescription())
        .tags(fact.getTags())
        .build();
  }
}
