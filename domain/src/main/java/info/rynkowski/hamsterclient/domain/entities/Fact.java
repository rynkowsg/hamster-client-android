package info.rynkowski.hamsterclient.domain.entities;

import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

public class Fact {
  @Getter @Setter private Activity activity;
  @Getter @Setter private Date startTime;
  @Getter @Setter private Date endTime;
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
    private Date startTime;
    private Date endTime;
    private List<String> tags;
    private String description;

    public Fact build() {
      return new Fact(this);
    }
  }
}
