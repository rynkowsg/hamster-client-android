package info.rynkowski.hamsterclient.hamster;

import org.gnome.Struct3;

// used in:
//     public List<Struct3> GetTagIds(List<String> tags);

class AdapterStruct3 {
    AdapterStruct3(Struct3 object) { this.object = object; }
    public int      id()            { return object.a; }
    public String   name()          { return object.b; }
    public boolean  autocomplete()  { return object.c; }
    public Struct3 object;
}