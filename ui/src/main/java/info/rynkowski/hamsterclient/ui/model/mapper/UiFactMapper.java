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

package info.rynkowski.hamsterclient.ui.model.mapper;

import info.rynkowski.hamsterclient.presentation.model.PresentationFact;
import info.rynkowski.hamsterclient.ui.model.UiFact;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class that translates a fact between two representations: presentation and ui.
 * Could transform object of {@link PresentationFact} to
 * {@link UiFact} and in the opposite direction.
 */
@Singleton
public class UiFactMapper {

  public @Inject UiFactMapper() {
    //empty
  }

  public @Nonnull PresentationFact transform(@Nonnull UiFact uiFact) {
    return new PresentationFact.Builder() //
        .id(uiFact.getId())
        .activity(uiFact.getActivity())
        .category(uiFact.getCategory())
        .tags(uiFact.getTags())
        .description(uiFact.getDescription())
        .startTime(uiFact.getStartTime())
        .endTime(uiFact.getEndTime())
        .build();
  }

  public @Nonnull UiFact transform(@Nonnull PresentationFact presentationFact) {
    return new UiFact.Builder() //
        .id(presentationFact.getId())
        .activity(presentationFact.getActivity())
        .category(presentationFact.getCategory())
        .tags(presentationFact.getTags())
        .description(presentationFact.getDescription())
        .startTime(presentationFact.getStartTime())
        .endTime(presentationFact.getEndTime())
        .build();
  }

  public @Nonnull List<UiFact> transform(@Nonnull List<PresentationFact> facts) {
    List<UiFact> uiFacts = new ArrayList<>(facts.size());
    for (PresentationFact fact : facts) {
      uiFacts.add(transform(fact));
    }
    return uiFacts;
  }
}
