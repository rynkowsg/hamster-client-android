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

import info.rynkowski.hamsterclient.domain.entities.Fact;
import info.rynkowski.hamsterclient.presentation.model.PresentationFact;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class that translates a fact between two representations: domain and presentation.
 * Could transform object of {@link Fact} to
 * {@link PresentationFact} and in the opposite direction.
 */
@Singleton
public class PresentationFactMapper {

  public @Inject PresentationFactMapper() {
    //empty
  }

  public @Nonnull Fact transform(@Nonnull PresentationFact factModel) {
    return new Fact.Builder()
        .id(factModel.getId())
        .activity(factModel.getActivity())
        .category(factModel.getCategory())
        .tags(factModel.getTags())
        .description(factModel.getDescription())
        .startTime(factModel.getStartTime())
        .endTime(factModel.getEndTime())
        .build();
  }

  public @Nonnull PresentationFact transform(@Nonnull Fact fact) {
    return new PresentationFact.Builder()
        .id(fact.getId())
        .activity(fact.getActivity())
        .category(fact.getCategory())
        .tags(fact.getTags())
        .description(fact.getDescription())
        .startTime(fact.getStartTime())
        .endTime(fact.getEndTime())
        .build();
  }

  public @Nonnull List<PresentationFact> transform(@Nonnull List<Fact> facts) {
    List<PresentationFact> presentationFacts = new ArrayList<>(facts.size());
    for (Fact fact : facts) {
      presentationFacts.add(transform(fact));
    }
    return presentationFacts;
  }
}
