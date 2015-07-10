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

package info.rynkowski.hamsterclient.presentation.model.mapper;

import android.support.annotation.NonNull;
import info.rynkowski.hamsterclient.domain.entities.Activity;
import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.presentation.model.FactModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class FactModelDataMapper {

  public @Inject FactModelDataMapper() {
    //empty
  }

  public @NonNull Fact transform(@NonNull FactModel factModel) {
    return new Fact.Builder()
        .id(factModel.getId())
        .activity(new Activity(factModel.getActivity(), factModel.getCategory()))
        .tags(factModel.getTags())
        .description(factModel.getDescription())
        .startTime(factModel.getStartTime())
        .endTime(factModel.getEndTime())
        .build();
  }

  public @NonNull FactModel transform(@NonNull Fact fact) {
    return new FactModel.Builder()
        .id(fact.getId())
        .activity(fact.getActivity().getName())
        .category(fact.getActivity().getCategory())
        .tags(fact.getTags())
        .description(fact.getDescription())
        .startTime(fact.getStartTime())
        .endTime(fact.getEndTime())
        .build();
  }

  public @NonNull List<FactModel> transform(@NonNull List<Fact> factList) {
    List<FactModel> factModelList = new ArrayList<>(factList.size());
    FactModel factModel;
    for (Fact fact : factList) {
      factModel = transform(fact);
      factModelList.add(factModel);
    }
    return factModelList;
  }
}
