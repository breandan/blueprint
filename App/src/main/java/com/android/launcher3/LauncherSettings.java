package com.android.launcher3;

import android.net.Uri;
import android.provider.BaseColumns;

class LauncherSettings
{
  static abstract interface BaseLauncherColumns
    extends LauncherSettings.ChangeLogColumns
  {}
  
  static abstract interface ChangeLogColumns
    extends BaseColumns
  {}
  
  static final class Favorites
    implements LauncherSettings.BaseLauncherColumns
  {
    static final Uri CONTENT_URI = Uri.parse("content://com.google.android.launcher.settings/favorites?notify=true");
    static final Uri CONTENT_URI_NO_NOTIFICATION = Uri.parse("content://com.google.android.launcher.settings/favorites?notify=false");
    static final Uri OLD_CONTENT_URI = Uri.parse("content://com.android.launcher2.settings/favorites?notify=true");
    
    static Uri getContentUri(long paramLong, boolean paramBoolean)
    {
      return Uri.parse("content://com.google.android.launcher.settings/favorites/" + paramLong + "?" + "notify" + "=" + paramBoolean);
    }
  }
  
  static final class WorkspaceScreens
    implements LauncherSettings.ChangeLogColumns
  {
    static final Uri CONTENT_URI = Uri.parse("content://com.google.android.launcher.settings/workspaceScreens?notify=true");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LauncherSettings
 * JD-Core Version:    0.7.0.1
 */