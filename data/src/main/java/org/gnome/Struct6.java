package org.gnome;
import org.freedesktop.dbus.Position;
import org.freedesktop.dbus.Struct;
public final class Struct6 extends Struct
{
   @Position(0)
   public final int a;
   @Position(1)
   public final String b;
  public Struct6(int a, String b)
  {
   this.a = a;
   this.b = b;
  }
}
