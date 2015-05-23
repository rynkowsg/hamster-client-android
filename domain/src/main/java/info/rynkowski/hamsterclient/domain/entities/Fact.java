package info.rynkowski.hamsterclient.domain.entities;

import java.util.Calendar;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

public class Fact {
  @Getter @Setter private Activity activity;
  @Getter @Setter private Calendar startTime;
  @Getter @Setter private Calendar endTime;
  @Getter @Setter private List<String> tags;
  @Getter @Setter private String description;

  private Fact(Builder b) {
    this.activity = b.activity;
    this.description = b.description;
    this.tags = b.tags;
    this.startTime = b.startTime;
    this.endTime = b.endTime;
  }

  @NoArgsConstructor(access = AccessLevel.PUBLIC)
  @Setter
  @Accessors(fluent = true, chain = true)
  public static class Builder {

    private Activity activity;
    private Calendar startTime;
    private Calendar endTime;
    private List<String> tags;
    private String description;

    public Fact build() {
      return new Fact(this);
    }
  }
}
