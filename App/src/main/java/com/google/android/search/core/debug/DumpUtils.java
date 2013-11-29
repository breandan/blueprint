package com.google.android.search.core.debug;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DumpUtils
{
  public static void dumpSqliteTable(SQLiteDatabase paramSQLiteDatabase, String paramString1, PrintWriter paramPrintWriter, String paramString2)
  {
    println(paramPrintWriter, new Object[] { paramString1, "Table ", paramString2, ":" });
    String str = paramString1 + "  ";
    Cursor localCursor = paramSQLiteDatabase.rawQuery("SELECT * FROM " + paramString2, null);
    if (localCursor != null) {
      try
      {
        Object[] arrayOfObject1 = new Object[2];
        arrayOfObject1[0] = str;
        arrayOfObject1[1] = TextUtils.join("|", localCursor.getColumnNames());
        println(paramPrintWriter, arrayOfObject1);
        int i = localCursor.getColumnCount();
        StringBuilder localStringBuilder = new StringBuilder();
        while (localCursor.moveToNext())
        {
          localStringBuilder.setLength(0);
          for (int j = 0; j < i; j++) {
            localStringBuilder.append(localCursor.getString(j)).append('|');
          }
          Object[] arrayOfObject2 = new Object[2];
          arrayOfObject2[0] = str;
          arrayOfObject2[1] = localStringBuilder.toString();
          println(paramPrintWriter, arrayOfObject2);
        }
      }
      finally
      {
        localCursor.close();
      }
    }
  }
  
  public static final CharSequence formatTimestampISO8301(long paramLong)
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
    if (paramLong == 0L) {
      return "0";
    }
    return localSimpleDateFormat.format(new Date(paramLong));
  }
  
  public static void println(PrintWriter paramPrintWriter, Object... paramVarArgs)
  {
    if (paramPrintWriter == null)
    {
      Log.e("Search.DumpUtils", "Null PrintWriter");
      return;
    }
    int i = paramVarArgs.length;
    int j = 0;
    if (j < i)
    {
      Object localObject = paramVarArgs[j];
      if (localObject == null) {}
      for (String str = "null";; str = localObject.toString())
      {
        paramPrintWriter.print(str);
        j++;
        break;
      }
    }
    paramPrintWriter.println();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.debug.DumpUtils
 * JD-Core Version:    0.7.0.1
 */