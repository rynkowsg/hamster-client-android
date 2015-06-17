package info.rynkowski.hamsterclient.data.entity;

import info.rynkowski.hamsterclient.data.dbus.adapters.AdapterStruct4;
import info.rynkowski.hamsterclient.data.dbus.adapters.AdapterStruct5;
import info.rynkowski.hamsterclient.data.dbus.adapters.AdapterStruct7;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.gnome.Struct4;
import org.gnome.Struct5;
import org.gnome.Struct7;

public class FactEntity {
  @Getter private String activity;
  @Getter private String category;
  @Getter private String description;
  @Getter private List<String> tags;
  @Getter private int startTime;
  @Getter private int endTime;

  public FactEntity(Struct4 struct) {
    this.activity = AdapterStruct4.name(struct);
    this.category = AdapterStruct4.category(struct);
    this.description = AdapterStruct4.description(struct);
    this.tags = AdapterStruct4.tags(struct);
    this.startTime = AdapterStruct4.start_time(struct);
    this.endTime = AdapterStruct4.end_time(struct);
  }

  public FactEntity(Struct5 struct) {
    this.activity = AdapterStruct5.name(struct);
    this.category = AdapterStruct5.category(struct);
    this.description = AdapterStruct5.description(struct);
    this.tags = AdapterStruct5.tags(struct);
    this.startTime = AdapterStruct5.start_time(struct);
    this.endTime = AdapterStruct5.end_time(struct);
  }

  public FactEntity(Struct7 struct) {
    this.activity = AdapterStruct7.name(struct);
    this.category = AdapterStruct7.category(struct);
    this.description = AdapterStruct7.description(struct);
    this.tags = AdapterStruct7.tags(struct);
    this.startTime = AdapterStruct7.start_time(struct);
    this.endTime = AdapterStruct7.end_time(struct);
  }

  private FactEntity(Builder b) {
    this.activity = b.activity;
    this.category = b.category;
    this.tags = b.tags;
    this.description = b.description;
    this.startTime = b.startTime;
    this.endTime = b.endTime;
  }

  public String serializedName() {
    String res = activity;
    if (!category.isEmpty()) {
      res += "@" + category;
    }
    res += ",";
    if (!description.isEmpty()) {
      res += description;
    }
    res += " ";
    if (!tags.isEmpty()) {
      res += "#" + StringUtils.join(tags, '#');
    }
    return res;
  }

  @NoArgsConstructor(access = AccessLevel.PUBLIC)
  @Setter
  @Accessors(fluent = true, chain = true)
  public static class Builder {

    private String activity;
    private String category;
    private List<String> tags;
    private String description;
    private int startTime;
    private int endTime;

    public FactEntity build() {
      return new FactEntity(this);
    }
  }
}
