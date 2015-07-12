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

package info.rynkowski.hamsterclient.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.common.base.Optional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
public class FactModel implements Parcelable {

  private @Nonnull Optional<Integer> id;
  private @Nonnull String activity;
  private @Nonnull String category;
  private @Nonnull List<String> tags;
  private @Nonnull String description;
  private @Nonnull Calendar startTime;
  private @Nonnull Optional<Calendar> endTime;

  private FactModel(@Nonnull Builder b) {
    this.id = b.id;
    this.activity = b.activity;
    this.category = b.category;
    this.description = b.description;
    this.tags = b.tags;
    this.startTime = b.startTime;
    this.endTime = b.endTime;
  }

  protected FactModel(@Nonnull Parcel in) {
    this.id = (in.readByte() == 1) ? Optional.of(in.readInt()) : Optional.absent();
    this.activity = in.readString();
    this.category = in.readString();
    this.tags = new ArrayList<>();
    in.readStringList(this.tags);
    this.description = in.readString();
    this.startTime = GregorianCalendar.getInstance();
    this.startTime.setTimeInMillis(in.readLong());
    this.endTime = Optional.absent();
    if (in.readByte() == 1) {
      this.endTime = Optional.of(GregorianCalendar.getInstance());
      this.endTime.get().setTimeInMillis(in.readLong());
    }
  }

  public static final Creator<FactModel> CREATOR = new Creator<FactModel>() {
    @Override public FactModel createFromParcel(@Nonnull Parcel in) {
      return new FactModel(in);
    }

    @Override public @Nonnull FactModel[] newArray(int size) {
      return new FactModel[size];
    }
  };

  @Override public int describeContents() {
    return hashCode();
  }

  @Override public void writeToParcel(@Nonnull Parcel dest, int flags) {
    if (id.isPresent()) {
      dest.writeByte((byte) 1);
      dest.writeInt(id.get());
    } else {
      dest.writeByte((byte) 0);
    }
    dest.writeString(activity);
    dest.writeString(category);
    dest.writeStringList(tags);
    dest.writeString(description);
    dest.writeLong(startTime.getTimeInMillis());
    if (endTime.isPresent()) {
      dest.writeByte((byte) 1);
      dest.writeLong(endTime.get().getTimeInMillis());
    } else {
      dest.writeByte((byte) 0);
    }
  }

  @NoArgsConstructor(access = AccessLevel.PUBLIC)
  @Setter
  @Accessors(fluent = true, chain = true)
  public static class Builder {

    private @Nonnull Optional<Integer> id = Optional.absent();
    private @Nonnull String activity = "";
    private @Nonnull String category = "";
    private @Nonnull String description = "";
    private @Nonnull List<String> tags = new ArrayList<>();
    private @Nonnull Calendar startTime = GregorianCalendar.getInstance();
    private @Nonnull Optional<Calendar> endTime = Optional.absent();

    public @Nonnull FactModel build() {
      return new FactModel(this);
    }
  }
}
