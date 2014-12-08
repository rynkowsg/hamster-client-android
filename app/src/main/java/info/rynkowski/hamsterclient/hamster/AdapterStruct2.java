package info.rynkowski.hamsterclient.hamster;

import org.gnome.Struct2;

// used in:
//     public List<Struct2> GetCategoryActivities(int category_id);

public class AdapterStruct2 {
    AdapterStruct2(Struct2 object) { this.object = object; }
    public int      id()            { return object.a; }
    public String   name()          { return object.b; }
    public int      category_id()   { return object.c; }
    public String   category() { return object.d; }
    public Struct2 object;
}
