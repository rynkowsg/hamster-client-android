package info.rynkowski.hamsterclient.presentation.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

// TODO: Transform to parcelable
public class FactModel implements Serializable {

  @Getter private int factId;
  @Getter private String activity;
  @Getter private String category;
  @Getter private List<String> tags;
  @Getter private String description;
  @Getter private Calendar startTime;
  @Getter private Calendar endTime;

  private FactModel(Builder b) {
    this.activity = b.activity;
    this.category = b.category;
    this.description = b.description;
    this.tags = b.tags;
    this.startTime = b.startTime;
    this.endTime = b.endTime;
  }

  @NoArgsConstructor(access = AccessLevel.PUBLIC)
  @Setter
  @Accessors(fluent = true, chain = true)
  public static class Builder {

    private String activity;
    private String category;
    private String description;
    private List<String> tags;
    private Calendar startTime;
    private Calendar endTime;

    public FactModel build() {
      return new FactModel(this);
    }
  }
}
