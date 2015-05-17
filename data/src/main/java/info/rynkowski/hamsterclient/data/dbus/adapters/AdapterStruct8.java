package info.rynkowski.hamsterclient.data.dbus.adapters;

import org.gnome.Struct8;

// used in:
//   public List<Struct8> GetActivities(String search)

public class AdapterStruct8 {
  private Struct8 object;

  public AdapterStruct8(Struct8 object) {
    this.object = object;
  }

  public String name() {
    return name(object);
  }

  public String category() {
    return category(object);
  }

  public static String name(Struct8 object) {
    return object.a;
  }

  public static String category(Struct8 object) {
    return object.b;
  }
}
