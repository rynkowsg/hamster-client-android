package org.gnome;
import org.freedesktop.dbus.Position;
import org.freedesktop.dbus.Struct;
public final class Struct1 extends Struct
{
   @Position(0)
   public final int a;
   @Position(1)
   public final String b;
   @Position(2)
   public final boolean c;
  public Struct1(int a, String b, boolean c)
  {
   this.a = a;
   this.b = b;
   this.c = c;
  }
}
