package info.rynkowski.hamsterclient.hamster;

import org.gnome.Struct2;

// used in:
//     public List<Struct2> GetCategoryActivities(int category_id);

public class AdapterStruct2 {
    private Struct2 object;

    AdapterStruct2(Struct2 object) {
        this.object = object;

    }

    public int id() {
        return id(object);
    }

    public String name() {
        return name(object);
    }

    public int category_id() {
        return category_id(object);
    }

    public String category() {
        return category(object);
    }

    public static int id(Struct2 object) {
        return object.a;
    }

    public static String name(Struct2 object) {
        return object.b;
    }

    public static int category_id(Struct2 object) {
        return object.c;
    }

    public static String category(Struct2 object) {
        return object.d;
    }
}
