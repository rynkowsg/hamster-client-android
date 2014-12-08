package info.rynkowski.hamsterclient.hamster;

import org.gnome.Struct6;

// used in:
//     public List<Struct6> GetCategories();

class AdapterStruct6 {
    AdapterStruct6(Struct6 object) { this.object = object; }
    public int      id()    { return object.a; }
    public String   name()  { return object.b; }
    public Struct6 object;
}