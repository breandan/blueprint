package com.google.android.gms.appdatasearch.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.HashSet;
import java.util.Set;

class MasterTableHelper
{
  public static void dropTable(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS [" + paramString + "]");
  }
  
  public static void dropTrigger(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    paramSQLiteDatabase.execSQL("DROP TRIGGER IF EXISTS [" + paramString + "]");
  }
  
  private static Set<String> getObjects(SQLiteDatabase paramSQLiteDatabase, String paramString, String... paramVarArgs)
  {
    StringBuilder localStringBuilder = new StringBuilder(1024);
    localStringBuilder.append("type").append(" = '").append(paramString).append('\'');
    if ((paramVarArgs != null) && (paramVarArgs.length > 0))
    {
      localStringBuilder.append(" AND (");
      localStringBuilder.append("name LIKE '%' || ?");
      for (int j = 1; j < paramVarArgs.length; j++) {
        localStringBuilder.append(" OR ").append("name LIKE '%' || ?");
      }
      localStringBuilder.append(")");
    }
    Cursor localCursor = paramSQLiteDatabase.query("sqlite_master", new String[] { "name" }, localStringBuilder.toString(), paramVarArgs, null, null, null);
    HashSet localHashSet = new HashSet();
    if (localCursor != null) {
      try
      {
        int i = localCursor.getColumnIndex("name");
        while (localCursor.moveToNext()) {
          localHashSet.add(localCursor.getString(i));
        }
      }
      finally
      {
        localCursor.close();
      }
    }
    return localHashSet;
  }
  
  public static Set<String> getTables(SQLiteDatabase paramSQLiteDatabase, String... paramVarArgs)
  {
    return getObjects(paramSQLiteDatabase, "table", paramVarArgs);
  }
  
  public static Set<String> getTriggers(SQLiteDatabase paramSQLiteDatabase, String... paramVarArgs)
  {
    return getObjects(paramSQLiteDatabase, "trigger", paramVarArgs);
  }
  
  public static boolean tableExists(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    Cursor localCursor = paramSQLiteDatabase.query("sqlite_master", new String[] { "name" }, "type = ? AND name == ?", new String[] { "table", paramString }, null, null, null);
    if (localCursor == null) {
      return false;
    }
    try
    {
      boolean bool = localCursor.moveToNext();
      return bool;
    }
    finally
    {
      localCursor.close();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.util.MasterTableHelper
 * JD-Core Version:    0.7.0.1
 */