package com.google.android.gsf;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public final class GoogleSettingsContract
{
  public static class NameValueTable
    implements BaseColumns
  {
    public static Uri getUriFor(Uri paramUri, String paramString)
    {
      return Uri.withAppendedPath(paramUri, paramString);
    }
  }
  
  public static final class Partner
    extends GoogleSettingsContract.NameValueTable
  {
    public static final Uri CONTENT_URI = Uri.parse("content://com.google.settings/partner");
    
    public static int getInt(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      String str = getString(paramContentResolver, paramString);
      if (str != null) {}
      try
      {
        int i = Integer.parseInt(str);
        return i;
      }
      catch (NumberFormatException localNumberFormatException) {}
      return paramInt;
      return paramInt;
    }
    
    public static String getString(ContentResolver paramContentResolver, String paramString)
    {
      localCursor = null;
      try
      {
        localCursor = paramContentResolver.query(CONTENT_URI, new String[] { "value" }, "name=?", new String[] { paramString }, null);
        localObject2 = null;
        if (localCursor != null)
        {
          boolean bool = localCursor.moveToNext();
          localObject2 = null;
          if (bool)
          {
            String str = localCursor.getString(0);
            localObject2 = str;
          }
        }
      }
      catch (SQLException localSQLException)
      {
        Log.e("GoogleSettings", "Can't get key " + paramString + " from " + CONTENT_URI, localSQLException);
        Object localObject2 = null;
        return null;
      }
      finally
      {
        if (localCursor == null) {
          break label145;
        }
        localCursor.close();
      }
      return localObject2;
    }
    
    public static Uri getUriFor(String paramString)
    {
      return getUriFor(CONTENT_URI, paramString);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gsf.GoogleSettingsContract
 * JD-Core Version:    0.7.0.1
 */