package info.rynkowski.hamsterclient.hamster;

import org.gnome.Struct1;

// used in:
//     public List<Struct1> GetTags(boolean only_autocomplete);

public class AdapterStruct1 {
    AdapterStruct1(Struct1 object) { this.object = object; }
    public int      id()            { return object.a; }
    public String   name()          { return object.b; }
    public boolean  autocomplete()  { return object.c; }
    public Struct1 object;
}
