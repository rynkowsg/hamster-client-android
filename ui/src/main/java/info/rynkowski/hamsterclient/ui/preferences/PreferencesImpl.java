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

package info.rynkowski.hamsterclient.ui.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import info.rynkowski.hamsterclient.data.preferences.Preferences;
import info.rynkowski.hamsterclient.ui.R;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.subjects.PublishSubject;

@Slf4j
public class PreferencesImpl implements Preferences {

  private final @NonNull Context context;
  private final @NonNull SharedPreferences preferences;
  private final @NonNull PublishSubject<Type> signalOnChangeSubject = PublishSubject.create();

  // method called when shared preferences will change
  private @NonNull SharedPreferences.OnSharedPreferenceChangeListener onPreferenceChangeListener;
  // counter of signalOnChanged' observers
  private int signalOnChangeObserversCounter = 0;

  @Inject public PreferencesImpl(@NonNull Context context) {
    this.context = context;
    this.preferences = PreferenceManager.getDefaultSharedPreferences(context);

    this.onPreferenceChangeListener = (sharedPreferences, key) -> {
      log.debug("onPreferenceChangeListener called, key: {}", key);
      switch (context.getResources().getIdentifier(key, "string", context.getPackageName())) {
        // below list of cases should be consistent to nodes at preferences.xml
        case R.string.pref_dbusHost_key:
          signalOnChangeSubject.onNext(Type.DbusHost);
          break;
        case R.string.pref_dbusPort_key:
          signalOnChangeSubject.onNext(Type.DbusPort);
          break;
        case R.string.pref_isDatabaseRemote_key:
          signalOnChangeSubject.onNext(Type.IsDatabaseRemote);
          break;
        default:
          assert false : "Unknown preference key. Check your preferences.xml.";
      }
    };
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

  @Override public @NonNull Observable<Type> signalOnChanged() {
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
