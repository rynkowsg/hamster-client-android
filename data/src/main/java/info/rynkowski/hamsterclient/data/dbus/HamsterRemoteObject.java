package info.rynkowski.hamsterclient.data.dbus;

import org.gnome.Hamster;

public class HamsterRemoteObject extends RemoteObjectAbstract<Hamster> {
  private static final String busName = "org.gnome.Hamster";
  private static final String objectPath = "/org/gnome/Hamster";
  private static final Class dbusType = Hamster.class;

  public HamsterRemoteObject(DBusConnector connector) {
    super(connector, busName, objectPath, dbusType);
  }
}
