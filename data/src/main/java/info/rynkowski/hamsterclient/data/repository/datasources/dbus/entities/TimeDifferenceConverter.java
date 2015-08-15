/*
 * Copyright (C) 2015 Grzegorz Rynkowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.rynkowski.hamsterclient.data.repository.datasources.dbus.entities;

import com.google.common.base.Optional;
import info.rynkowski.hamsterclient.data.utils.Time;
import java.util.TimeZone;
import javax.annotation.Nonnull;

/**
 * Class that provides a mechanism to convert local time to remote time and vice versa.
 * Those necessity is caused by Hamster Time Tracker.
 * Hamster Time Tracker returns time in seconds that takes into account computer's time zone.
 *
 * Java classes like Data, Time, Calendar takes time in millis for GMT,
 * but Hamster returns e.g. for Poland time in millis for GMT+2.
 */
public class TimeDifferenceConverter {

  // Computer's time zone
  // It is assumed that remote computer has the same time zone as the mobile device.
  private @Nonnull TimeZone remoteTimeZone;

  private @Nonnull Optional<Long> localTimeInMillis = Optional.absent();
  private @Nonnull Optional<Long> remoteTimeInMillis = Optional.absent();

  private TimeDifferenceConverter() {
    this(TimeZone.getDefault());
  }

  private TimeDifferenceConverter(@Nonnull TimeZone remoteTimeZone) {
    this.remoteTimeZone = remoteTimeZone;
  }

  public static @Nonnull TimeDifferenceConverter getInstance() {
    return new TimeDifferenceConverter();
  }

  public static @Nonnull TimeDifferenceConverter getInstance(@Nonnull TimeZone remoteTimeZone) {
    return new TimeDifferenceConverter(remoteTimeZone);
  }

  public @Nonnull TimeDifferenceConverter setLocalTime(@Nonnull Long milliseconds) {
    this.localTimeInMillis = Optional.of(milliseconds);
    this.correctRemoteTime();
    return this;
  }

  public @Nonnull TimeDifferenceConverter setLocalTime(@Nonnull Integer seconds) {
    return this.setLocalTime(seconds.longValue() * 1000);
  }

  public @Nonnull TimeDifferenceConverter setRemoteTime(@Nonnull Long milliseconds) {
    this.remoteTimeInMillis = Optional.of(milliseconds);
    this.correctLocalTime();
    return this;
  }

  public @Nonnull TimeDifferenceConverter setRemoteTime(@Nonnull Integer seconds) {
    return this.setRemoteTime(seconds.longValue() * 1000);
  }

  public @Nonnull Time getLocalTime() {
    return Time.getInstancefromMillis(this.getLocalTimeInMills());
  }

  public long getLocalTimeInMills() {
    return this.localTimeInMillis.get();
  }

  public int getLocalTimeInSeconds() {
    return (int) (this.localTimeInMillis.get() / 1000);
  }

  public @Nonnull Time getRemoteTime() {
    return Time.getInstancefromMillis(this.getRemoteTimeInMills());
  }

  public long getRemoteTimeInMills() {
    return this.remoteTimeInMillis.get();
  }

  public int getRemoteTimeInSeconds() {
    return (int) (this.remoteTimeInMillis.get() / 1000);
  }

  /**
   * Calculates local time when remote time was provided.
   */
  private void correctLocalTime() {
    long remoteTime = this.remoteTimeInMillis.get();

    // Hamster provides bad time representation - a timestamp taking into account timezone offset.
    // It requires a correction using time zone' offset of remote PC.
    long localTime = remoteTime - remoteTimeZone.getOffset(remoteTime);

    this.localTimeInMillis = Optional.of(localTime);
  }

  /**
   * Calculates remote time when local time was provided.
   */
  private void correctRemoteTime() {
    long localTime = this.localTimeInMillis.get();

    // Hamster provides bad time representation - a timestamp taking into account timezone offset.
    // It requires a correction using time zone' offset of remote PC.
    long remoteTime = localTime + remoteTimeZone.getOffset(localTime);

    this.remoteTimeInMillis = Optional.of(remoteTime);
  }

  public static @Nonnull Time remoteToLocal(@Nonnull Time remoteTime) {
    return TimeDifferenceConverter.getInstance()
        .setRemoteTime(remoteTime.getTimeInMillis())
        .getLocalTime();
  }

  public static @Nonnull Time localToRemote(@Nonnull Time localTime) {
    return TimeDifferenceConverter.getInstance()
        .setLocalTime(localTime.getTimeInMillis())
        .getRemoteTime();
  }
}
