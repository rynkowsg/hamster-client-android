package info.rynkowski.hamsterclient.domain.entities;

import lombok.Getter;
import lombok.Setter;

public class Activity {

  public Activity(String name, String category) {
    this.name = name;
    this.category = category;
  }

  @Getter @Setter private String name;
  @Getter @Setter private String category;
}
