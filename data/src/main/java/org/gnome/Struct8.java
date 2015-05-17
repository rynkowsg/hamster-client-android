package org.gnome;
import org.freedesktop.dbus.Position;
import org.freedesktop.dbus.Struct;
public final class Struct8 extends Struct
{
   @Position(0)
   public final String a;
   @Position(1)
   public final String b;
  public Struct8(String a, String b)
  {
   this.a = a;
   this.b = b;
  }
}
