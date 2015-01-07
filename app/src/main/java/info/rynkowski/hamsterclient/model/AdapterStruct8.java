package info.rynkowski.hamsterclient.model;

import org.gnome.Struct8;

// used in:
//     public List<Struct8> GetActivities(String search)

public class AdapterStruct8 {
    public AdapterStruct8(Struct8 object) { this.object = object; }
    public String   name()      { return object.a; }
    public String   category()  { return object.b; }
    public Struct8 object;
}
