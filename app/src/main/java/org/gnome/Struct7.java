package org.gnome;
import java.util.List;
import org.freedesktop.dbus.Position;
import org.freedesktop.dbus.Struct;
public final class Struct7 extends Struct
{
   @Position(0)
   public final int a;
   @Position(1)
   public final int b;
   @Position(2)
   public final int c;
   @Position(3)
   public final String d;
   @Position(4)
   public final String e;
   @Position(5)
   public final int f;
   @Position(6)
   public final String g;
   @Position(7)
   public final List<String> h;
   @Position(8)
   public final int i;
   @Position(9)
   public final int j;
   @Position(10)
   public final boolean k;
  public Struct7(int a, int b, int c, String d, String e, int f, String g, List<String> h, int i, int j, boolean k)
  {
   this.a = a;
   this.b = b;
   this.c = c;
   this.d = d;
   this.e = e;
   this.f = f;
   this.g = g;
   this.h = h;
   this.i = i;
   this.j = j;
   this.k = k;
  }
}
