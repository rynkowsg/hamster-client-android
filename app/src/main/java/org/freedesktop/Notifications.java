package org.freedesktop;

import java.util.List;
import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;

public interface Notifications extends DBusInterface{
    public UInt32 Notify(String app_name, UInt32 id, String icon, String summary, String body, List<String> actions, Map<String,Variant<Byte>> hints, int timeout);
}
