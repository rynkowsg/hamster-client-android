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

package info.rynkowski.hamsterclient.presentation.presenter;

import info.rynkowski.hamsterclient.presentation.model.FactModel;
import javax.annotation.Nonnull;

public interface OnFactActionListener {

  void onStartFact(@Nonnull FactModel fact);

  void onStopFact(@Nonnull FactModel fact);

  void onEditFact(@Nonnull FactModel fact);

  void onRemoveFact(@Nonnull FactModel fact);
}