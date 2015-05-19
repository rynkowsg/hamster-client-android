package info.rynkowski.hamsterclient.presentation.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

public class FactModel implements Serializable {

  private int factId;
  private String activity;
  private String category;
  private List<String> tags;
  private String description;
  private Date startTime;
  private Date endTime;

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
    private Date startTime;
    private Date endTime;

    public FactModel build() {
      return new FactModel(this);
    }
  }
}
