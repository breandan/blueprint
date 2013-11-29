package com.android.launcher3;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupManager;
import android.content.Context;

public class LauncherBackupAgentHelper
  extends BackupAgentHelper
{
  private static BackupManager sBackupManager;
  
  public static void dataChanged(Context paramContext)
  {
    if (sBackupManager == null) {
      sBackupManager = new BackupManager(paramContext);
    }
    sBackupManager.dataChanged();
  }
  
  public void onCreate()
  {
    addHelper("L", new LauncherBackupHelper(this));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LauncherBackupAgentHelper
 * JD-Core Version:    0.7.0.1
 */