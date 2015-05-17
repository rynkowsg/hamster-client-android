package info.rynkowski.hamsterclient.data.dbus.adapters;

import org.gnome.Struct3;

// used in:
//   public List<Struct3> GetTagIds(List<String> tags);

public class AdapterStruct3 {
  private Struct3 object;

  public AdapterStruct3(Struct3 object) {
    this.object = object;
  }

  public int id() {
    return id(object);
  }

  public String name() {
    return name(object);
  }

  public boolean autocomplete() {
    return autocomplete(object);
  }

  public static int id(Struct3 object) {
    return object.a;
  }

  public static String name(Struct3 object) {
    return object.b;
  }

  public static boolean autocomplete(Struct3 object) {
    return object.c;
  }
}
