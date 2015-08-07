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

package info.rynkowski.hamsterclient.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import info.rynkowski.hamsterclient.data.utils.PreferencesAdapter;
import info.rynkowski.hamsterclient.ui.R;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.subjects.PublishSubject;

@Slf4j
public class PreferencesAdapterImpl implements PreferencesAdapter {

  private final @NonNull Context context;
  private final @NonNull SharedPreferences preferences;
  private final @NonNull PublishSubject<String> signalOnChangeSubject =  PublishSubject.create();

  // method called when shared preferences will change
  private final @NonNull SharedPreferences.OnSharedPreferenceChangeListener
      onPreferenceChangeListener = (sharedPreferences, key) -> signalOnChangeSubject.onNext(key);

  private int signalOnChangeObserversCounter = 0;

  @Inject public PreferencesAdapterImpl(@NonNull Context context) {
    this.context = context;
    this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
  }

  @Override public @NonNull Boolean isDatabaseRemote() {
    String key = context.getResources().getString(R.string.pref_isDatabaseRemote_key);
    Boolean defaultValue =
        context.getResources().getBoolean(R.bool.pref_isDatabaseRemote_defaultValue);
    return preferences.getBoolean(key, defaultValue);
  }

  @Override public @NonNull String dbusHost() {
    String key = context.getResources().getString(R.string.pref_dbusHost_key);
    String defaultValue = context.getResources().getString(R.string.pref_dbusHost_defaultValue);
    return preferences.getString(key, defaultValue);
  }

  @Override public @NonNull String dbusPort() {
    String key = context.getResources().getString(R.string.pref_dbusPort_key);
    String defaultValue = context.getResources().getString(R.string.pref_dbusPort_defaultValue);
    return preferences.getString(key, defaultValue);
  }

  @Override public @NonNull Observable<String> signalOnChanged() {
    return signalOnChangeSubject.
        doOnSubscribe(() -> {
          // register listener if there is at least one observer
          if (signalOnChangeObserversCounter++ == 0) {
            preferences.registerOnSharedPreferenceChangeListener(onPreferenceChangeListener);
            log.debug("SharedPreferenceChangeListener registered");
          }
        }).
        doOnUnsubscribe(() -> {
          // un-register the listener if there is no observer
          if (--signalOnChangeObserversCounter == 0) {
            preferences.unregisterOnSharedPreferenceChangeListener(onPreferenceChangeListener);
            log.debug("SharedPreferenceChangeListener un-registered");
          }
        });
  }
}
