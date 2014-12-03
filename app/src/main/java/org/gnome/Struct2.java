package org.gnome;
import org.freedesktop.dbus.Position;
import org.freedesktop.dbus.Struct;
public final class Struct2 extends Struct
{
   @Position(0)
   public final int a;
   @Position(1)
   public final String b;
   @Position(2)
   public final int c;
   @Position(3)
   public final String d;
  public Struct2(int a, String b, int c, String d)
  {
   this.a = a;
   this.b = b;
   this.c = c;
   this.d = d;
  }
}
