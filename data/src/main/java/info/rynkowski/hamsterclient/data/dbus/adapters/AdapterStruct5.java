package info.rynkowski.hamsterclient.data.dbus.adapters;

import org.gnome.Struct5;

import java.util.List;

// used in:
//   public List<Struct7> GetFacts(UInt32 start_date, UInt32 end_date, String search_terms, UInt32 limit, boolean asc_by_date);

public class AdapterStruct5 {
  private Struct5 object;

  public AdapterStruct5(Struct5 object) {
    this.object = object;
  }

  public int id() {
    return id(object);
  }

  public int start_time() {
    return start_time(object);
  }

  public int end_time() {
    return end_time(object);
  }

  public String description() {
    return description(object);
  }

  public String name() {
    return name(object);
  }

  public int activity_id() {
    return activity_id(object);
  }

  public String category() {
    return category(object);
  }

  public List<String> tags() {
    return tags(object);
  }

  public int date() {
    return date(object);
  }

  public int delta() {
    return delta(object);
  }

  public boolean exported() {
    return exported(object);
  }

  public static int id(Struct5 object) {
    return object.a;
  }

  public static int start_time(Struct5 object) {
    return object.b;
  }

  public static int end_time(Struct5 object) {
    return object.c;
  }

  public static String description(Struct5 object) {
    return object.d;
  }

  public static String name(Struct5 object) {
    return object.e;
  }

  public static int activity_id(Struct5 object) {
    return object.f;
  }

  public static String category(Struct5 object) {
    return object.g;
  }

  public static List<String> tags(Struct5 object) {
    return object.h;
  }

  public static int date(Struct5 object) {
    return object.i;
  }

  public static int delta(Struct5 object) {
    return object.j;
  }

  public static boolean exported(Struct5 object) {
    return object.k;
  }
}
