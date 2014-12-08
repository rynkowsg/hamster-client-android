package info.rynkowski.hamsterclient.hamster;

import org.gnome.Struct7;
import java.util.List;

// used in:
//     public List<Struct5> GetTodaysFacts();

class AdapterStruct7 {
    AdapterStruct7(Struct7 object) { this.object = object; }
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
    public boolean      exported()      { return object.k; }
    public Struct7 object;
}