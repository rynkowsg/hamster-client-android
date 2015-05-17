package info.rynkowski.hamsterclient.data.entity;

import info.rynkowski.hamsterclient.data.dbus.adapters.AdapterStruct4;
import info.rynkowski.hamsterclient.data.dbus.adapters.AdapterStruct5;
import info.rynkowski.hamsterclient.data.dbus.adapters.AdapterStruct7;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.gnome.Struct4;
import org.gnome.Struct5;
import org.gnome.Struct7;

public class FactEntity {
  private String activity;
  private String category;
  private String description;
  private List<String> tags;
  private int startTime;
  private int endTime;

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

  public FactEntity(String activity, String category, String description, List<String> tags, int startTime, int endTime) {
    this.activity = activity;
    this.category = category;
    this.description = description;
    this.tags = tags;
    this.startTime = startTime;
    this.endTime = endTime;
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

  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public int getStartTime() {
    return startTime;
  }

  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }

  public int getEndTime() {
    return endTime;
  }

  public void setEndTime(int endTime) {
    this.endTime = endTime;
  }
}
