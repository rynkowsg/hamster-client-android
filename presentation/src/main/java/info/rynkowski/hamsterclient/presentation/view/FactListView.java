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

package info.rynkowski.hamsterclient.presentation.view;

import info.rynkowski.hamsterclient.presentation.model.PresentationFact;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 * In this case is used as a view representing a list of {@link PresentationFact}.
 */
public interface FactListView extends LoadDataView {

  /**
   * Opens a view that allows entering a data of new fact.
   */
  void navigateToAdditionForm();

  /**
   * Opens a view that allows modification of picked fact.
   */
  void navigateToEditionForm(@Nonnull PresentationFact fact);

  /**
   * Render a fact list in the UI.
   *
   * @param facts The collection of {@link PresentationFact} that will be shown.
   */
  void showFactList(@Nonnull List<PresentationFact> facts);
}
