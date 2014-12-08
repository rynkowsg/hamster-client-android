package info.rynkowski.hamsterclient.hamster;

import org.gnome.Struct5;
import java.util.List;

// used in:
//     public List<Struct7> GetFacts(UInt32 start_date, UInt32 end_date, String search_terms, UInt32 limit, boolean asc_by_date);

class AdapterStruct5 {
    AdapterStruct5(Struct5 object) { this.object = object; }
    public int          id()            { return object.a; }
    public int          start_time()    { return object.b; }
    public int          end_time()      { return object.c; }
    public String       description()   { return object.d; }
    public String       name()          { return object.e; }
    public int          activity_id()   { return object.f; }
    public String       category()      { return object.g; }
    public List<String> tags()          { return object.h; }
    public int          date()          { return object.i; }
    public int          delta()         { return object.j; }
    public boolean      exported()     { return object.k; }
    public Struct5 object;
}