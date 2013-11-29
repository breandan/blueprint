package com.android.launcher3;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

public class InstallShortcutReceiver
  extends BroadcastReceiver
{
  private static boolean mUseInstallQueue = false;
  private static Object sLock = new Object();
  
  private static void addToInstallQueue(SharedPreferences paramSharedPreferences, PendingInstallShortcutInfo paramPendingInstallShortcutInfo)
  {
    synchronized (sLock)
    {
      try
      {
        JSONStringer localJSONStringer1 = new JSONStringer().object().key("intent.data").value(paramPendingInstallShortcutInfo.data.toUri(0)).key("intent.launch").value(paramPendingInstallShortcutInfo.launchIntent.toUri(0)).key("name").value(paramPendingInstallShortcutInfo.name);
        if (paramPendingInstallShortcutInfo.icon != null)
        {
          byte[] arrayOfByte = ItemInfo.flattenBitmap(paramPendingInstallShortcutInfo.icon);
          localJSONStringer1 = localJSONStringer1.key("icon").value(Base64.encodeToString(arrayOfByte, 0, arrayOfByte.length, 0));
        }
        if (paramPendingInstallShortcutInfo.iconResource != null) {
          localJSONStringer1 = localJSONStringer1.key("iconResource").value(paramPendingInstallShortcutInfo.iconResource.resourceName).key("iconResourcePackage").value(paramPendingInstallShortcutInfo.iconResource.packageName);
        }
        JSONStringer localJSONStringer2 = localJSONStringer1.endObject();
        SharedPreferences.Editor localEditor = paramSharedPreferences.edit();
        addToStringSet(paramSharedPreferences, localEditor, "apps_to_install", localJSONStringer2.toString());
        localEditor.commit();
      }
      catch (JSONException localJSONException)
      {
        for (;;)
        {
          Log.d("InstallShortcutReceiver", "Exception when adding shortcut: " + localJSONException);
        }
      }
      return;
    }
  }
  
  private static void addToStringSet(SharedPreferences paramSharedPreferences, SharedPreferences.Editor paramEditor, String paramString1, String paramString2)
  {
    Set localSet = paramSharedPreferences.getStringSet(paramString1, null);
    if (localSet == null) {}
    for (HashSet localHashSet = new HashSet(0);; localHashSet = new HashSet(localSet))
    {
      localHashSet.add(paramString2);
      paramEditor.putStringSet(paramString1, localHashSet);
      return;
    }
  }
  
  static void disableAndFlushInstallQueue(Context paramContext)
  {
    mUseInstallQueue = false;
    flushInstallQueue(paramContext);
  }
  
  static void enableInstallQueue()
  {
    mUseInstallQueue = true;
  }
  
  private static CharSequence ensureValidName(Context paramContext, Intent paramIntent, CharSequence paramCharSequence)
  {
    if (paramCharSequence == null) {}
    try
    {
      PackageManager localPackageManager = paramContext.getPackageManager();
      String str = localPackageManager.getActivityInfo(paramIntent.getComponent(), 0).loadLabel(localPackageManager).toString();
      paramCharSequence = str;
      return paramCharSequence;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
    return "";
  }
  
  static void flushInstallQueue(Context paramContext)
  {
    ArrayList localArrayList1 = getAndClearInstallQueue(paramContext.getSharedPreferences(LauncherAppState.getSharedPreferencesKey(), 0));
    if (!localArrayList1.isEmpty())
    {
      Iterator localIterator = localArrayList1.iterator();
      ArrayList localArrayList2 = new ArrayList();
      while (localIterator.hasNext())
      {
        PendingInstallShortcutInfo localPendingInstallShortcutInfo = (PendingInstallShortcutInfo)localIterator.next();
        Intent localIntent = localPendingInstallShortcutInfo.launchIntent;
        if (!LauncherModel.shortcutExists(paramContext, localPendingInstallShortcutInfo.name, localIntent)) {
          localArrayList2.add(getShortcutInfo(paramContext, localPendingInstallShortcutInfo.data, localPendingInstallShortcutInfo.launchIntent));
        }
      }
      if (-1 == 0) {
        Toast.makeText(paramContext, paramContext.getString(2131361896, new Object[] { "" }), 0).show();
      }
      if (!localArrayList2.isEmpty()) {
        LauncherAppState.getInstance().getModel().addAndBindAddedApps(paramContext, localArrayList2, null);
      }
    }
  }
  
  private static ArrayList<PendingInstallShortcutInfo> getAndClearInstallQueue(SharedPreferences paramSharedPreferences)
  {
    ArrayList localArrayList2;
    for (;;)
    {
      String str1;
      synchronized (sLock)
      {
        Set localSet = paramSharedPreferences.getStringSet("apps_to_install", null);
        if (localSet == null)
        {
          ArrayList localArrayList1 = new ArrayList();
          return localArrayList1;
        }
        localArrayList2 = new ArrayList();
        Iterator localIterator = localSet.iterator();
        if (!localIterator.hasNext()) {
          break;
        }
        str1 = (String)localIterator.next();
      }
      try
      {
        JSONTokener localJSONTokener = new JSONTokener(str1);
        JSONObject localJSONObject = (JSONObject)localJSONTokener.nextValue();
        localIntent1 = Intent.parseUri(localJSONObject.getString("intent.data"), 0);
        Intent localIntent2 = Intent.parseUri(localJSONObject.getString("intent.launch"), 0);
        String str2 = localJSONObject.getString("name");
        String str3 = localJSONObject.optString("icon");
        str4 = localJSONObject.optString("iconResource");
        str5 = localJSONObject.optString("iconResourcePackage");
        if ((str3 != null) && (!str3.isEmpty()))
        {
          byte[] arrayOfByte = Base64.decode(str3, 0);
          localIntent1.putExtra("android.intent.extra.shortcut.ICON", BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length));
          localIntent1.putExtra("android.intent.extra.shortcut.INTENT", localIntent2);
          localArrayList2.add(new PendingInstallShortcutInfo(localIntent1, str2, localIntent2));
        }
      }
      catch (JSONException localJSONException)
      {
        for (;;)
        {
          Intent localIntent1;
          String str4;
          String str5;
          Log.d("InstallShortcutReceiver", "Exception reading shortcut to add: " + localJSONException);
          break;
          localObject2 = finally;
          throw localObject2;
          if ((str4 != null) && (!str4.isEmpty()))
          {
            Intent.ShortcutIconResource localShortcutIconResource = new Intent.ShortcutIconResource();
            localShortcutIconResource.resourceName = str4;
            localShortcutIconResource.packageName = str5;
            localIntent1.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", localShortcutIconResource);
          }
        }
      }
      catch (URISyntaxException localURISyntaxException)
      {
        Log.d("InstallShortcutReceiver", "Exception reading shortcut to add: " + localURISyntaxException);
      }
    }
    paramSharedPreferences.edit().putStringSet("apps_to_install", new HashSet()).commit();
    return localArrayList2;
  }
  
  private static ShortcutInfo getShortcutInfo(Context paramContext, Intent paramIntent1, Intent paramIntent2)
  {
    if (paramIntent2.getAction() == null) {
      paramIntent2.setAction("android.intent.action.VIEW");
    }
    for (;;)
    {
      ShortcutInfo localShortcutInfo = LauncherAppState.getInstance().getModel().infoFromShortcutIntent(paramContext, paramIntent1, null);
      localShortcutInfo.title = ensureValidName(paramContext, paramIntent2, localShortcutInfo.title);
      return localShortcutInfo;
      if ((paramIntent2.getAction().equals("android.intent.action.MAIN")) && (paramIntent2.getCategories() != null) && (paramIntent2.getCategories().contains("android.intent.category.LAUNCHER"))) {
        paramIntent2.addFlags(270532608);
      }
    }
  }
  
  public static void removeFromInstallQueue(SharedPreferences paramSharedPreferences, ArrayList<String> paramArrayList)
  {
    if (paramArrayList.isEmpty()) {
      return;
    }
    HashSet localHashSet;
    for (;;)
    {
      Iterator localIterator;
      String str1;
      synchronized (sLock)
      {
        Set localSet = paramSharedPreferences.getStringSet("apps_to_install", null);
        if (localSet == null) {
          break label236;
        }
        localHashSet = new HashSet(localSet);
        localIterator = localHashSet.iterator();
        if (!localIterator.hasNext()) {
          break;
        }
        str1 = (String)localIterator.next();
      }
      try
      {
        Intent localIntent = Intent.parseUri(((JSONObject)new JSONTokener(str1).nextValue()).getString("intent.launch"), 0);
        String str2 = localIntent.getPackage();
        if (str2 == null) {
          str2 = localIntent.getComponent().getPackageName();
        }
        if (paramArrayList.contains(str2)) {
          localIterator.remove();
        }
      }
      catch (JSONException localJSONException)
      {
        Log.d("InstallShortcutReceiver", "Exception reading shortcut to remove: " + localJSONException);
        continue;
        localObject2 = finally;
        throw localObject2;
      }
      catch (URISyntaxException localURISyntaxException)
      {
        Log.d("InstallShortcutReceiver", "Exception reading shortcut to remove: " + localURISyntaxException);
      }
    }
    paramSharedPreferences.edit().putStringSet("apps_to_install", new HashSet(localHashSet)).commit();
    label236:
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if (!"com.android.launcher.action.INSTALL_SHORTCUT".equals(paramIntent.getAction())) {}
    Intent localIntent;
    do
    {
      return;
      localIntent = (Intent)paramIntent.getParcelableExtra("android.intent.extra.shortcut.INTENT");
    } while (localIntent == null);
    String str = ensureValidName(paramContext, localIntent, paramIntent.getStringExtra("android.intent.extra.shortcut.NAME")).toString();
    Bitmap localBitmap = (Bitmap)paramIntent.getParcelableExtra("android.intent.extra.shortcut.ICON");
    Intent.ShortcutIconResource localShortcutIconResource = (Intent.ShortcutIconResource)paramIntent.getParcelableExtra("android.intent.extra.shortcut.ICON_RESOURCE");
    LauncherAppState.setApplicationContext(paramContext.getApplicationContext());
    if (LauncherAppState.getInstance().getDynamicGrid() == null) {}
    for (int i = 1;; i = 0)
    {
      PendingInstallShortcutInfo localPendingInstallShortcutInfo = new PendingInstallShortcutInfo(paramIntent, str, localIntent);
      localPendingInstallShortcutInfo.icon = localBitmap;
      localPendingInstallShortcutInfo.iconResource = localShortcutIconResource;
      addToInstallQueue(paramContext.getSharedPreferences(LauncherAppState.getSharedPreferencesKey(), 0), localPendingInstallShortcutInfo);
      if ((mUseInstallQueue) || (i != 0)) {
        break;
      }
      flushInstallQueue(paramContext);
      return;
    }
  }
  
  private static class PendingInstallShortcutInfo
  {
    Intent data;
    Bitmap icon;
    Intent.ShortcutIconResource iconResource;
    Intent launchIntent;
    String name;
    
    public PendingInstallShortcutInfo(Intent paramIntent1, String paramString, Intent paramIntent2)
    {
      this.data = paramIntent1;
      this.name = paramString;
      this.launchIntent = paramIntent2;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.InstallShortcutReceiver
 * JD-Core Version:    0.7.0.1
 */