package info.rynkowski.hamsterclient.data.dbus;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.gnome.Hamster;

@Singleton
public class HamsterRemoteObject extends RemoteObjectAbstract<Hamster> {
  private static final String busName = "org.gnome.Hamster";
  private static final String objectPath = "/org/gnome/Hamster";
  private static final Class dbusType = Hamster.class;

  @Inject public HamsterRemoteObject(DBusConnector connector) {
    super(connector, busName, objectPath, dbusType);
  }
}
