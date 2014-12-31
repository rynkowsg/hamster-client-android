package info.rynkowski.hamsterclient.model;

import org.gnome.Struct4;

import java.util.List;

// used in:
//     public Struct4 GetFact(int fact_id);

public class AdapterStruct4 {
    private Struct4 object;

    AdapterStruct4(Struct4 object) {
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

    public static int id(Struct4 object) {
        return object.a;
    }

    public static int start_time(Struct4 object) {
        return object.b;
    }

    public static int end_time(Struct4 object) {
        return object.c;
    }

    public static String description(Struct4 object) {
        return object.d;
    }

    public static String name(Struct4 object) {
        return object.e;
    }

    public static int activity_id(Struct4 object) {
        return object.f;
    }

    public static String category(Struct4 object) {
        return object.g;
    }

    public static List<String> tags(Struct4 object) {
        return object.h;
    }

    public static int date(Struct4 object) {
        return object.i;
    }

    public static int delta(Struct4 object) {
        return object.j;
    }

    public static boolean exported(Struct4 object) {
        return object.k;
    }
}
