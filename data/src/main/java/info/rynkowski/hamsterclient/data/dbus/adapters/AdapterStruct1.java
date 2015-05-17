package info.rynkowski.hamsterclient.data.dbus.adapters;

import org.gnome.Struct1;

// used in:
//   public List<Struct1> GetTags(boolean only_autocomplete);

public class AdapterStruct1 {
  private Struct1 object;

  public AdapterStruct1(Struct1 object) {
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

  public static int id(Struct1 object) {
    return object.a;
  }

  public static String name(Struct1 object) {
    return object.b;
  }

  public static boolean autocomplete(Struct1 object) {
    return object.c;
  }
}
