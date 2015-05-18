package org.gnome;
import java.util.List;
import java.util.Map;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
public interface Hamster extends DBusInterface
{
   public static class FactsChanged extends DBusSignal
   {
      public FactsChanged(String path) throws DBusException
      {
         super(path);
      }
   }
   public static class ToggleCalled extends DBusSignal
   {
      public ToggleCalled(String path) throws DBusException
      {
         super(path);
      }
   }
   public static class ActivitiesChanged extends DBusSignal
   {
      public ActivitiesChanged(String path) throws DBusException
      {
         super(path);
      }
   }
   public static class TagsChanged extends DBusSignal
   {
      public TagsChanged(String path) throws DBusException
      {
         super(path);
      }
   }

  public List<Struct1> GetTags(boolean only_autocomplete);
  public void Quit();
  public List<Struct2> GetCategoryActivities(int category_id);
  public List<Struct3> GetTagIds(List<String> tags);
  public int AddActivity(String name, int category_id);
  public Struct4 GetFact(int fact_id);
  public Map<String,Variant> GetActivityByName(String activity, int category_id, boolean resurrect);
  public List<Struct5> GetTodaysFacts();
  public List<Struct6> GetCategories();
  public boolean ChangeCategory(int id, int category_id);
  public void UpdateCategory(int id, String name);
  public int AddFact(String fact, int start_time, int end_time, boolean temporary);
  public void Toggle();
  public int UpdateFact(int fact_id, String fact, int start_time, int end_time, boolean temporary, boolean exported);
  public void RemoveFact(int fact_id);
  public void UpdateActivity(int id, String name, int category_id);
  public List<Struct7> GetFacts(long start_date, long end_date, String search_terms, long limit, boolean asc_by_date);
  public int AddCategory(String name);
  public void RemoveActivity(int id);
  public void RemoveCategory(int id);
  public void SetTagsAutocomplete(String tags);
  public int GetCategoryId(String category);
  public void StopTracking(int end_time);
  public List<Struct8> GetActivities(String search);

}
