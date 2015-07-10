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

package info.rynkowski.hamsterclient.data.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Time {

  private @Nonnull Calendar calendar;
  public static @Nonnull String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";


  private Time() {
    calendar = GregorianCalendar.getInstance();
  }

  private Time(@Nonnull Calendar calendar) {
    this.calendar = calendar;
  }

  public static @Nonnull Time getInstance() {
    return new Time();
  }

  public static @Nonnull Time getInstance(@Nonnull Calendar calendar) {
    return new Time(calendar);
  }

  public static @Nonnull Time getInstance(@Nonnull Integer seconds) {
    return new Time().setTimeInSeconds(seconds);
  }

  public static @Nonnull Time getInstance(@Nonnull Timestamp timestamp) {
    return new Time().setTimeInMillis(timestamp.getTime());
  }

  public static @Nonnull Time getInstancefromMillis(long milliseconds) {
    return new Time().setTimeInMillis(milliseconds);
  }

  public @Nonnull Calendar getCalendar() {
    return this.calendar;
  }

  public @Nonnull Date getDate() {
    return this.calendar.getTime();
  }

  public @Nonnull Timestamp getTimestamp() {
    return new Timestamp(this.getTimeInMillis());
  }

  public long getTimeInMillis() {
    return this.calendar.getTimeInMillis();
  }

  public int getTimeInSeconds() {
    return (int)(this.getTimeInMillis() / 1000);
  }

  public Time set(@Nonnull Calendar calendar) {
    this.calendar = calendar;
    return this;
  }

  public Time set(@Nonnull Date date) {
    this.calendar.setTime(date);
    return this;
  }

  public Time set(@Nonnull String timeStr) {
    return this.set(timeStr, TIMESTAMP_FORMAT);
  }

  public Time set(@Nonnull String timeStr, @Nonnull String format) {
    SimpleDateFormat dateFormatGmt = new SimpleDateFormat(format, Locale.getDefault());

    try {
      Date date = dateFormatGmt.parse(timeStr);
      this.set(date);
    } catch (ParseException e) {
      log.warn("Exception during parsing a date from string.", e);
    }
    return this;
  }

  public Time setTimeInMillis(long timeInMillis) {
    this.calendar.setTimeInMillis(timeInMillis);
    return this;
  }

  public Time setTimeInSeconds(int timeInSeconds) {
    this.setTimeInMillis(((long) timeInSeconds) * 1000);
    return this;
  }

  public @Nonnull String toString() {
    return toString(TIMESTAMP_FORMAT);
  }

  public @Nonnull String toString(@Nonnull String format) {
    if (calendar.getTimeInMillis() != 0) {
      SimpleDateFormat dateFormatGmt = new SimpleDateFormat(format, Locale.getDefault());
      return dateFormatGmt.format(calendar.getTime());
    }
    return "";
  }
}
