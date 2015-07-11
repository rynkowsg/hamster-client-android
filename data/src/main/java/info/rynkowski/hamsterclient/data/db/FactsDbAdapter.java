/*
 * Copyright (C) 2015 Grzegorz Rynkowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.rynkowski.hamsterclient.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.common.base.Optional;
import info.rynkowski.hamsterclient.data.entity.FactEntity;
import info.rynkowski.hamsterclient.data.utils.Time;
import java.sql.Timestamp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FactsDbAdapter {

  private static final int DB_VERSION = 1;
  private static final String DB_NAME = "database.db";
  private static final String DB_FACTS_TABLE = "facts";


  public static final String KEY_ID = "_id";
  public static final String KEY_REMOTE_ID = "remote_id";
  public static final String KEY_ACTIVITY = "activity";
  public static final String KEY_CATEGORY = "category";
  public static final String KEY_DESCRIPTION = "description";
  public static final String KEY_START_TIME = "start_time";
  public static final String KEY_END_TIME = "end_time";

  public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
  public static final String REMOTE_ID_OPTIONS = "INTEGER";
  public static final String ACTIVITY_OPTIONS = "TEXT NOT NULL";
  public static final String CATEGORY_OPTIONS = "TEXT NOT NULL";
  public static final String DESCRIPTION_OPTIONS = "TEXT NOT NULL";
  public static final String START_TIME_OPTIONS = "TIMESTAMP";
  public static final String END_TIME_OPTIONS = "TIMESTAMP";

  public static final int ID_COLUMN = 0;
  public static final int REMOTE_ID_COLUMN = 1;
  public static final int ACTIVITY_COLUMN = 2;
  public static final int CATEGORY_COLUMN = 3;
  public static final int DESCRIPTION_COLUMN = 4;
  public static final int START_TIME_COLUMN = 5;
  public static final int END_TIME_COLUMN = 6;

  private static final String DB_CREATE_FACTS_TABLE =
      "CREATE TABLE " + DB_FACTS_TABLE + "( " +
          KEY_ID + " " + ID_OPTIONS + ", " +
          KEY_REMOTE_ID + " " + REMOTE_ID_OPTIONS + ", " +
          KEY_ACTIVITY + " " + ACTIVITY_OPTIONS + ", " +
          KEY_CATEGORY + " " + CATEGORY_OPTIONS + ", " +
          KEY_DESCRIPTION + " " + DESCRIPTION_OPTIONS + ", " +
          KEY_START_TIME + " " + START_TIME_OPTIONS + ", " +
          KEY_END_TIME + " " + END_TIME_OPTIONS +
          ");";

  private static final String DROP_TODO_TABLE = "DROP TABLE IF EXISTS " + DB_FACTS_TABLE;

  private SQLiteDatabase db;
  private Context context;
  private DatabaseHelper dbHelper;

  private static class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
        int version) {
      super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(DB_CREATE_FACTS_TABLE);

      log.debug("Database creating...");
      log.debug("Table {} ver.{} created", DB_FACTS_TABLE, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL(DROP_TODO_TABLE);

      log.debug("Database updating...");
      log.debug("Table {} updated from ver.{} to ver.{}", DB_FACTS_TABLE, oldVersion, newVersion);
      log.debug("All data is lost.");

      onCreate(db);
    }
  }

  public FactsDbAdapter(Context context) {
    this.context = context;
  }

  public FactsDbAdapter open(){
    dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
    try {
      db = dbHelper.getWritableDatabase();
    } catch (SQLException e) {
      db = dbHelper.getReadableDatabase();
    }
    return this;
  }

  public void close() {
    dbHelper.close();
  }

  public int insertFact(FactEntity factEntity) {
    ContentValues newTodoValues = new ContentValues();
    newTodoValues.put(KEY_REMOTE_ID,
        factEntity.getRemoteId().isPresent() ? factEntity.getRemoteId().get() : null);
    newTodoValues.put(KEY_ACTIVITY, factEntity.getActivity());
    newTodoValues.put(KEY_CATEGORY, factEntity.getCategory());
    newTodoValues.put(KEY_DESCRIPTION, factEntity.getDescription());
    newTodoValues.put(KEY_START_TIME, factEntity.getStartTime().toString());
    if (factEntity.getEndTime().isPresent()) {
      newTodoValues.put(KEY_END_TIME, factEntity.getEndTime().get().toString());
    } else {
      newTodoValues.putNull(KEY_END_TIME);
    }
    return (int) db.insert(DB_FACTS_TABLE, null, newTodoValues);
  }

  public boolean updateFact(FactEntity factEntity) {
    String where = KEY_REMOTE_ID + "=" + factEntity.getRemoteId();
    ContentValues updateTodoValues = new ContentValues();
    updateTodoValues.put(KEY_REMOTE_ID,
        factEntity.getRemoteId().isPresent() ? factEntity.getRemoteId().get() : null);
    updateTodoValues.put(KEY_ACTIVITY, factEntity.getActivity());
    updateTodoValues.put(KEY_CATEGORY, factEntity.getCategory());
    updateTodoValues.put(KEY_DESCRIPTION, factEntity.getDescription());
    updateTodoValues.put(KEY_START_TIME, factEntity.getStartTime().toString());
    if (factEntity.getEndTime().isPresent()) {
      updateTodoValues.put(KEY_END_TIME, factEntity.getEndTime().get().toString());
    } else {
      updateTodoValues.putNull(KEY_END_TIME);
    }
    return db.update(DB_FACTS_TABLE, updateTodoValues, where, null) > 0;
  }

  public boolean deleteFact(int id) {
    String where = KEY_ID + "=" + id;
    return db.delete(DB_FACTS_TABLE, where, null) > 0;
  }

  public boolean deleteFactByRemoteId(int remoteId) {
    String where = KEY_REMOTE_ID + "=" + remoteId;
    return db.delete(DB_FACTS_TABLE, where, null) > 0;
  }

  public Optional<FactEntity> getFact(int id) {
    String[] columns = {
        KEY_ID, KEY_REMOTE_ID, KEY_ACTIVITY, KEY_CATEGORY, KEY_DESCRIPTION, KEY_START_TIME, KEY_END_TIME
    };
    String where = KEY_ID + "=" + id;
    Cursor cursor = db.query(DB_FACTS_TABLE, columns, where, null, null, null, null);

    Optional<FactEntity> factEntity = Optional.absent();

    if (cursor != null && cursor.moveToFirst()) {
      Time startTime = Time.getInstance(Timestamp.valueOf(cursor.getString(START_TIME_COLUMN)));
      Optional<Time> endTime = cursor.isNull(END_TIME_COLUMN) ? Optional.absent()
          : Optional.of(Time.getInstance(Timestamp.valueOf(cursor.getString(END_TIME_COLUMN))));

      factEntity = Optional.of(new FactEntity.Builder()
          .id(Optional.of(cursor.getInt(ID_COLUMN)))
          .remoteId(Optional.of(cursor.getInt(REMOTE_ID_COLUMN)))
          .activity(cursor.getString(ACTIVITY_COLUMN))
          .category(cursor.getString(CATEGORY_COLUMN))
          .description(cursor.getString(DESCRIPTION_COLUMN))
          .startTime(startTime)
          .endTime(endTime)
          .build());

      cursor.close();
    }

    return factEntity;
  }

  public Optional<FactEntity> getFactRemoteId(int remoteId) {
    String[] columns = {
        KEY_ID, KEY_REMOTE_ID, KEY_ACTIVITY, KEY_CATEGORY, KEY_DESCRIPTION, KEY_START_TIME,
        KEY_END_TIME
    };
    String where = KEY_REMOTE_ID + "=" + remoteId;
    Cursor cursor = db.query(DB_FACTS_TABLE, columns, where, null, null, null, null);

    Optional<FactEntity> factEntity = Optional.absent();

    if (cursor != null && cursor.moveToFirst()) {
      factEntity = Optional.of(new FactEntity.Builder()
          .id(Optional.of(cursor.getInt(ID_COLUMN)))
          .remoteId(Optional.of(cursor.getInt(REMOTE_ID_COLUMN)))
          .activity(cursor.getString(ACTIVITY_COLUMN))
          .category(cursor.getString(CATEGORY_COLUMN))
          .description(cursor.getString(DESCRIPTION_COLUMN))
          .startTime(Time.getInstance(Timestamp.valueOf(cursor.getString(START_TIME_COLUMN))))
          .endTime(Optional.of(
              Time.getInstance(Timestamp.valueOf(cursor.getString(END_TIME_COLUMN)))))
          .build());

      cursor.close();
    }

    return factEntity;
  }
}

