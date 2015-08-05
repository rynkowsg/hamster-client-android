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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import rx.Observer;
import rx.functions.Action0;
import rx.functions.Action1;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class OnCompletedObserver implements Observer<Void> {

  private final Action0 onCompleted;
  private final Action1<Throwable> onError;

  @Override public void onCompleted() {
    onCompleted.call();
  }

  @Override public void onError(Throwable e) {
    onError.call(e);
  }

  @Override public void onNext(Void o) {
    assert false : "OnCompletedObserver doesn't support onNext events.";
  }
}
