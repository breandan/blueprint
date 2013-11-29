package com.android.launcher3;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

public class UninstallShortcutReceiver
  extends BroadcastReceiver
{
  private static ArrayList<PendingUninstallShortcutInfo> mUninstallQueue = new ArrayList();
  private static boolean mUseUninstallQueue = false;
  
  static void disableAndFlushUninstallQueue(Context paramContext)
  {
    mUseUninstallQueue = false;
    Iterator localIterator = mUninstallQueue.iterator();
    while (localIterator.hasNext())
    {
      processUninstallShortcut(paramContext, (PendingUninstallShortcutInfo)localIterator.next());
      localIterator.remove();
    }
  }
  
  static void enableUninstallQueue()
  {
    mUseUninstallQueue = true;
  }
  
  private static void processUninstallShortcut(Context paramContext, PendingUninstallShortcutInfo paramPendingUninstallShortcutInfo)
  {
    Intent localIntent = paramPendingUninstallShortcutInfo.data;
    LauncherAppState.setApplicationContext(paramContext.getApplicationContext());
    synchronized (LauncherAppState.getInstance())
    {
      removeShortcut(paramContext, localIntent);
      return;
    }
  }
  
  private static void removeShortcut(Context paramContext, Intent paramIntent)
  {
    Intent localIntent = (Intent)paramIntent.getParcelableExtra("android.intent.extra.shortcut.INTENT");
    String str = paramIntent.getStringExtra("android.intent.extra.shortcut.NAME");
    boolean bool1 = paramIntent.getBooleanExtra("duplicate", true);
    ContentResolver localContentResolver;
    Cursor localCursor;
    int i;
    int j;
    int k;
    if ((localIntent != null) && (str != null))
    {
      localContentResolver = paramContext.getContentResolver();
      localCursor = localContentResolver.query(LauncherSettings.Favorites.CONTENT_URI, new String[] { "_id", "intent" }, "title=?", new String[] { str }, null);
      i = localCursor.getColumnIndexOrThrow("intent");
      j = localCursor.getColumnIndexOrThrow("_id");
      k = 0;
    }
    for (;;)
    {
      try
      {
        boolean bool2 = localCursor.moveToNext();
        if (!bool2) {}
      }
      finally
      {
        try
        {
          if (!localIntent.filterEquals(Intent.parseUri(localCursor.getString(i), 0))) {
            continue;
          }
          localContentResolver.delete(LauncherSettings.Favorites.getContentUri(localCursor.getLong(j), false), null, null);
          k = 1;
          if (bool1) {
            continue;
          }
          localCursor.close();
          if (k != 0)
          {
            localContentResolver.notifyChange(LauncherSettings.Favorites.CONTENT_URI, null);
            Toast.makeText(paramContext, paramContext.getString(2131361895, new Object[] { str }), 0).show();
          }
          return;
        }
        catch (URISyntaxException localURISyntaxException) {}
        localObject = finally;
        localCursor.close();
      }
    }
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if (!"com.android.launcher.action.UNINSTALL_SHORTCUT".equals(paramIntent.getAction())) {
      return;
    }
    PendingUninstallShortcutInfo localPendingUninstallShortcutInfo = new PendingUninstallShortcutInfo(paramIntent);
    if (mUseUninstallQueue)
    {
      mUninstallQueue.add(localPendingUninstallShortcutInfo);
      return;
    }
    processUninstallShortcut(paramContext, localPendingUninstallShortcutInfo);
  }
  
  private static class PendingUninstallShortcutInfo
  {
    Intent data;
    
    public PendingUninstallShortcutInfo(Intent paramIntent)
    {
      this.data = paramIntent;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.UninstallShortcutReceiver
 * JD-Core Version:    0.7.0.1
 */