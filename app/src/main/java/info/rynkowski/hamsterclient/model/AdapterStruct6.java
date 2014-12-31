package info.rynkowski.hamsterclient.model;

import org.gnome.Struct6;

// used in:
//     public List<Struct6> GetCategories();

public class AdapterStruct6 {
    private Struct6 object;

    AdapterStruct6(Struct6 object) {
        this.object = object;
    }

    public int id() {
        return id(object);
    }

    public String name() {
        return name(object);
    }

    public static int id(Struct6 object) {
        return object.a;
    }

    public static String name(Struct6 object) {
        return object.b;
    }
}