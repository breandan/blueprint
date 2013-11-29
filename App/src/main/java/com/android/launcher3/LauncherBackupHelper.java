package com.android.launcher3;

import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupHelper;
import android.app.backup.BackupManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import com.android.launcher3.backup.BackupProtos.CheckedMessage;
import com.android.launcher3.backup.BackupProtos.Favorite;
import com.android.launcher3.backup.BackupProtos.Journal;
import com.android.launcher3.backup.BackupProtos.Key;
import com.android.launcher3.backup.BackupProtos.Resource;
import com.android.launcher3.backup.BackupProtos.Screen;
import com.android.launcher3.backup.BackupProtos.Widget;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.CRC32;

public class LauncherBackupHelper
  implements BackupHelper
{
  private static final String[] FAVORITE_PROJECTION = { "_id", "modified", "intent", "appWidgetProvider", "appWidgetId", "cellX", "cellY", "container", "icon", "iconPackage", "iconResource", "iconType", "itemType", "screen", "spanX", "spanY", "title" };
  private static final Bitmap.CompressFormat IMAGE_FORMAT = Bitmap.CompressFormat.PNG;
  private static final String[] SCREEN_PROJECTION = { "_id", "modified", "screenRank" };
  private static BackupManager sBackupManager;
  private final Context mContext;
  private ArrayList<BackupProtos.Key> mKeys;
  private HashMap<ComponentName, AppWidgetProviderInfo> mWidgetMap;
  
  public LauncherBackupHelper(Context paramContext)
  {
    this.mContext = paramContext;
  }
  
  private void backupFavorites(BackupProtos.Journal paramJournal1, BackupDataOutput paramBackupDataOutput, BackupProtos.Journal paramJournal2, ArrayList<BackupProtos.Key> paramArrayList)
    throws IOException
  {
    Set localSet = getSavedIdsByType(1, paramJournal1);
    Cursor localCursor = this.mContext.getContentResolver().query(LauncherSettings.Favorites.CONTENT_URI, FAVORITE_PROJECTION, null, null, null);
    HashSet localHashSet = new HashSet(localCursor.getCount());
    try
    {
      localCursor.moveToPosition(-1);
      while (localCursor.moveToNext())
      {
        long l1 = localCursor.getLong(0);
        long l2 = localCursor.getLong(1);
        BackupProtos.Key localKey = getKey(1, l1);
        paramArrayList.add(localKey);
        localHashSet.add(keyToBackupKey(localKey));
        if (l2 > paramJournal1.t) {
          writeRowToBackup(localKey, packFavorite(localCursor), paramJournal2, paramBackupDataOutput);
        }
      }
    }
    finally
    {
      localCursor.close();
    }
    localSet.removeAll(localHashSet);
    paramJournal2.rows += removeDeletedKeysFromBackup(localSet, paramBackupDataOutput);
  }
  
  /* Error */
  private void backupIcons(BackupProtos.Journal paramJournal1, BackupDataOutput paramBackupDataOutput, BackupProtos.Journal paramJournal2, ArrayList<BackupProtos.Key> paramArrayList)
    throws IOException
  {
    // Byte code:
    //   0: invokestatic 182	com/android/launcher3/LauncherAppState:getInstanceNoCreate	()Lcom/android/launcher3/LauncherAppState;
    //   3: astore 5
    //   5: aload 5
    //   7: ifnonnull +8 -> 15
    //   10: aload_0
    //   11: invokespecial 185	com/android/launcher3/LauncherBackupHelper:dataChanged	()V
    //   14: return
    //   15: aload_0
    //   16: getfield 78	com/android/launcher3/LauncherBackupHelper:mContext	Landroid/content/Context;
    //   19: invokevirtual 92	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   22: astore 6
    //   24: aload 5
    //   26: invokevirtual 189	com/android/launcher3/LauncherAppState:getIconCache	()Lcom/android/launcher3/IconCache;
    //   29: astore 7
    //   31: aload_0
    //   32: getfield 78	com/android/launcher3/LauncherBackupHelper:mContext	Landroid/content/Context;
    //   35: invokevirtual 193	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   38: invokevirtual 199	android/content/res/Resources:getDisplayMetrics	()Landroid/util/DisplayMetrics;
    //   41: getfield 204	android/util/DisplayMetrics:densityDpi	I
    //   44: istore 8
    //   46: aload_0
    //   47: iconst_3
    //   48: aload_1
    //   49: invokespecial 86	com/android/launcher3/LauncherBackupHelper:getSavedIdsByType	(ILcom/android/launcher3/backup/BackupProtos$Journal;)Ljava/util/Set;
    //   52: astore 9
    //   54: aload_3
    //   55: getfield 169	com/android/launcher3/backup/BackupProtos$Journal:rows	I
    //   58: istore 10
    //   60: aload 6
    //   62: getstatic 98	com/android/launcher3/LauncherSettings$Favorites:CONTENT_URI	Landroid/net/Uri;
    //   65: getstatic 68	com/android/launcher3/LauncherBackupHelper:FAVORITE_PROJECTION	[Ljava/lang/String;
    //   68: ldc 206
    //   70: aconst_null
    //   71: aconst_null
    //   72: invokevirtual 104	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   75: astore 11
    //   77: new 106	java/util/HashSet
    //   80: dup
    //   81: aload 11
    //   83: invokeinterface 112 1 0
    //   88: invokespecial 115	java/util/HashSet:<init>	(I)V
    //   91: astore 12
    //   93: aload 11
    //   95: iconst_m1
    //   96: invokeinterface 119 2 0
    //   101: pop
    //   102: aload 11
    //   104: invokeinterface 123 1 0
    //   109: ifeq +286 -> 395
    //   112: aload 11
    //   114: iconst_0
    //   115: invokeinterface 127 2 0
    //   120: lstore 16
    //   122: aload 11
    //   124: iconst_2
    //   125: invokeinterface 210 2 0
    //   130: astore 18
    //   132: aload 18
    //   134: iconst_0
    //   135: invokestatic 216	android/content/Intent:parseUri	(Ljava/lang/String;I)Landroid/content/Intent;
    //   138: astore 23
    //   140: aload 23
    //   142: invokevirtual 220	android/content/Intent:getComponent	()Landroid/content/ComponentName;
    //   145: astore 24
    //   147: aload 24
    //   149: ifnull +99 -> 248
    //   152: aload_0
    //   153: iconst_3
    //   154: aload 24
    //   156: invokevirtual 226	android/content/ComponentName:flattenToShortString	()Ljava/lang/String;
    //   159: invokespecial 229	com/android/launcher3/LauncherBackupHelper:getKey	(ILjava/lang/String;)Lcom/android/launcher3/backup/BackupProtos$Key;
    //   162: astore 25
    //   164: aload_0
    //   165: aload 25
    //   167: invokespecial 141	com/android/launcher3/LauncherBackupHelper:keyToBackupKey	(Lcom/android/launcher3/backup/BackupProtos$Key;)Ljava/lang/String;
    //   170: astore 26
    //   172: aload 12
    //   174: aload 26
    //   176: invokeinterface 144 2 0
    //   181: pop
    //   182: aload 9
    //   184: aload 26
    //   186: invokeinterface 232 2 0
    //   191: ifeq +125 -> 316
    //   194: aload 4
    //   196: aload 25
    //   198: invokevirtual 137	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   201: pop
    //   202: goto -100 -> 102
    //   205: astore 21
    //   207: ldc 234
    //   209: new 236	java/lang/StringBuilder
    //   212: dup
    //   213: invokespecial 237	java/lang/StringBuilder:<init>	()V
    //   216: ldc 239
    //   218: invokevirtual 243	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   221: lload 16
    //   223: invokevirtual 246	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   226: invokevirtual 249	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   229: invokestatic 255	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   232: pop
    //   233: goto -131 -> 102
    //   236: astore 13
    //   238: aload 11
    //   240: invokeinterface 161 1 0
    //   245: aload 13
    //   247: athrow
    //   248: ldc 234
    //   250: new 236	java/lang/StringBuilder
    //   253: dup
    //   254: invokespecial 237	java/lang/StringBuilder:<init>	()V
    //   257: ldc_w 257
    //   260: invokevirtual 243	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   263: lload 16
    //   265: invokevirtual 246	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   268: invokevirtual 249	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   271: invokestatic 255	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   274: pop
    //   275: aconst_null
    //   276: astore 26
    //   278: aconst_null
    //   279: astore 25
    //   281: goto -99 -> 182
    //   284: astore 19
    //   286: ldc 234
    //   288: new 236	java/lang/StringBuilder
    //   291: dup
    //   292: invokespecial 237	java/lang/StringBuilder:<init>	()V
    //   295: ldc_w 259
    //   298: invokevirtual 243	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   301: lload 16
    //   303: invokevirtual 246	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   306: invokevirtual 249	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   309: invokestatic 255	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   312: pop
    //   313: goto -211 -> 102
    //   316: aload 26
    //   318: ifnull -216 -> 102
    //   321: aload_3
    //   322: getfield 169	com/android/launcher3/backup/BackupProtos$Journal:rows	I
    //   325: iload 10
    //   327: isub
    //   328: bipush 10
    //   330: if_icmpge +58 -> 388
    //   333: aload 7
    //   335: aload 23
    //   337: invokevirtual 265	com/android/launcher3/IconCache:getIcon	(Landroid/content/Intent;)Landroid/graphics/Bitmap;
    //   340: astore 28
    //   342: aload 4
    //   344: aload 25
    //   346: invokevirtual 137	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   349: pop
    //   350: aload 28
    //   352: ifnull -250 -> 102
    //   355: aload 7
    //   357: aload 28
    //   359: invokevirtual 269	com/android/launcher3/IconCache:isDefaultIcon	(Landroid/graphics/Bitmap;)Z
    //   362: ifne -260 -> 102
    //   365: aload_0
    //   366: iload 8
    //   368: aload 28
    //   370: invokespecial 273	com/android/launcher3/LauncherBackupHelper:packIcon	(ILandroid/graphics/Bitmap;)[B
    //   373: astore 30
    //   375: aload_0
    //   376: aload 25
    //   378: aload 30
    //   380: aload_3
    //   381: aload_2
    //   382: invokespecial 158	com/android/launcher3/LauncherBackupHelper:writeRowToBackup	(Lcom/android/launcher3/backup/BackupProtos$Key;[BLcom/android/launcher3/backup/BackupProtos$Journal;Landroid/app/backup/BackupDataOutput;)V
    //   385: goto -283 -> 102
    //   388: aload_0
    //   389: invokespecial 185	com/android/launcher3/LauncherBackupHelper:dataChanged	()V
    //   392: goto -290 -> 102
    //   395: aload 11
    //   397: invokeinterface 161 1 0
    //   402: aload 9
    //   404: aload 12
    //   406: invokeinterface 165 2 0
    //   411: pop
    //   412: aload_3
    //   413: aload_3
    //   414: getfield 169	com/android/launcher3/backup/BackupProtos$Journal:rows	I
    //   417: aload_0
    //   418: aload 9
    //   420: aload_2
    //   421: invokespecial 173	com/android/launcher3/LauncherBackupHelper:removeDeletedKeysFromBackup	(Ljava/util/Set;Landroid/app/backup/BackupDataOutput;)I
    //   424: iadd
    //   425: putfield 169	com/android/launcher3/backup/BackupProtos$Journal:rows	I
    //   428: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	429	0	this	LauncherBackupHelper
    //   0	429	1	paramJournal1	BackupProtos.Journal
    //   0	429	2	paramBackupDataOutput	BackupDataOutput
    //   0	429	3	paramJournal2	BackupProtos.Journal
    //   0	429	4	paramArrayList	ArrayList<BackupProtos.Key>
    //   3	22	5	localLauncherAppState	LauncherAppState
    //   22	39	6	localContentResolver	ContentResolver
    //   29	327	7	localIconCache	IconCache
    //   44	323	8	i	int
    //   52	367	9	localSet	Set
    //   58	270	10	j	int
    //   75	321	11	localCursor	Cursor
    //   91	314	12	localHashSet	HashSet
    //   236	10	13	localObject	Object
    //   120	182	16	l	long
    //   130	3	18	str1	String
    //   284	1	19	localIOException	IOException
    //   205	1	21	localURISyntaxException	java.net.URISyntaxException
    //   138	198	23	localIntent	android.content.Intent
    //   145	10	24	localComponentName	ComponentName
    //   162	215	25	localKey	BackupProtos.Key
    //   170	147	26	str2	String
    //   340	29	28	localBitmap	Bitmap
    //   373	6	30	arrayOfByte	byte[]
    // Exception table:
    //   from	to	target	type
    //   132	147	205	java/net/URISyntaxException
    //   152	182	205	java/net/URISyntaxException
    //   182	202	205	java/net/URISyntaxException
    //   248	275	205	java/net/URISyntaxException
    //   321	350	205	java/net/URISyntaxException
    //   355	385	205	java/net/URISyntaxException
    //   388	392	205	java/net/URISyntaxException
    //   93	102	236	finally
    //   102	132	236	finally
    //   132	147	236	finally
    //   152	182	236	finally
    //   182	202	236	finally
    //   207	233	236	finally
    //   248	275	236	finally
    //   286	313	236	finally
    //   321	350	236	finally
    //   355	385	236	finally
    //   388	392	236	finally
    //   132	147	284	java/io/IOException
    //   152	182	284	java/io/IOException
    //   182	202	284	java/io/IOException
    //   248	275	284	java/io/IOException
    //   321	350	284	java/io/IOException
    //   355	385	284	java/io/IOException
    //   388	392	284	java/io/IOException
  }
  
  private BackupProtos.Key backupKeyToKey(String paramString)
    throws LauncherBackupHelper.KeyParsingException
  {
    BackupProtos.Key localKey;
    try
    {
      localKey = BackupProtos.Key.parseFrom(Base64.decode(paramString, 0));
      if (localKey.checksum != checkKey(localKey)) {
        throw new KeyParsingException("invalid key read from stream" + paramString);
      }
    }
    catch (InvalidProtocolBufferNanoException localInvalidProtocolBufferNanoException)
    {
      throw new KeyParsingException(localInvalidProtocolBufferNanoException, null);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new KeyParsingException(localIllegalArgumentException, null);
    }
    return localKey;
  }
  
  private void backupScreens(BackupProtos.Journal paramJournal1, BackupDataOutput paramBackupDataOutput, BackupProtos.Journal paramJournal2, ArrayList<BackupProtos.Key> paramArrayList)
    throws IOException
  {
    Set localSet = getSavedIdsByType(2, paramJournal1);
    Cursor localCursor = this.mContext.getContentResolver().query(LauncherSettings.WorkspaceScreens.CONTENT_URI, SCREEN_PROJECTION, null, null, null);
    HashSet localHashSet = new HashSet(localCursor.getCount());
    try
    {
      localCursor.moveToPosition(-1);
      while (localCursor.moveToNext())
      {
        long l1 = localCursor.getLong(0);
        long l2 = localCursor.getLong(1);
        BackupProtos.Key localKey = getKey(2, l1);
        paramArrayList.add(localKey);
        localHashSet.add(keyToBackupKey(localKey));
        if (l2 > paramJournal1.t) {
          writeRowToBackup(localKey, packScreen(localCursor), paramJournal2, paramBackupDataOutput);
        }
      }
    }
    finally
    {
      localCursor.close();
    }
    localSet.removeAll(localHashSet);
    paramJournal2.rows += removeDeletedKeysFromBackup(localSet, paramBackupDataOutput);
  }
  
  private void backupWidgets(BackupProtos.Journal paramJournal1, BackupDataOutput paramBackupDataOutput, BackupProtos.Journal paramJournal2, ArrayList<BackupProtos.Key> paramArrayList)
    throws IOException
  {
    LauncherAppState localLauncherAppState = LauncherAppState.getInstanceNoCreate();
    if (localLauncherAppState == null)
    {
      dataChanged();
      return;
    }
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    WidgetPreviewLoader localWidgetPreviewLoader = new WidgetPreviewLoader(this.mContext);
    PagedViewCellLayout localPagedViewCellLayout = new PagedViewCellLayout(this.mContext);
    IconCache localIconCache = localLauncherAppState.getIconCache();
    int i = this.mContext.getResources().getDisplayMetrics().densityDpi;
    DeviceProfile localDeviceProfile = localLauncherAppState.getDynamicGrid().getDeviceProfile();
    Set localSet = getSavedIdsByType(4, paramJournal1);
    int j = paramJournal2.rows;
    Cursor localCursor = localContentResolver.query(LauncherSettings.Favorites.CONTENT_URI, FAVORITE_PROJECTION, "itemType=4", null, null);
    HashSet localHashSet = new HashSet(localCursor.getCount());
    for (;;)
    {
      int k;
      int m;
      ComponentName localComponentName;
      try
      {
        localCursor.moveToPosition(-1);
        if (!localCursor.moveToNext()) {
          break;
        }
        long l = localCursor.getLong(0);
        String str1 = localCursor.getString(3);
        k = localCursor.getInt(14);
        m = localCursor.getInt(15);
        localComponentName = ComponentName.unflattenFromString(str1);
        if (localComponentName != null)
        {
          localKey = getKey(4, str1);
          str2 = keyToBackupKey(localKey);
          localHashSet.add(str2);
          if (!localSet.contains(str2)) {
            break label301;
          }
          paramArrayList.add(localKey);
          continue;
        }
        Log.w("LauncherBackupHelper", "empty intent on appwidget: " + l);
      }
      finally
      {
        localCursor.close();
      }
      String str2 = null;
      BackupProtos.Key localKey = null;
      continue;
      label301:
      if (str2 != null) {
        if (paramJournal2.rows - j < 5)
        {
          localWidgetPreviewLoader.setPreviewSize(k * localDeviceProfile.cellWidthPx, m * localDeviceProfile.cellHeightPx, localPagedViewCellLayout);
          byte[] arrayOfByte = packWidget(i, localWidgetPreviewLoader, localIconCache, localComponentName);
          paramArrayList.add(localKey);
          writeRowToBackup(localKey, arrayOfByte, paramJournal2, paramBackupDataOutput);
        }
        else
        {
          dataChanged();
        }
      }
    }
    localCursor.close();
    localSet.removeAll(localHashSet);
    paramJournal2.rows += removeDeletedKeysFromBackup(localSet, paramBackupDataOutput);
  }
  
  private long checkKey(BackupProtos.Key paramKey)
  {
    CRC32 localCRC32 = new CRC32();
    localCRC32.update(paramKey.type);
    localCRC32.update((int)(0xFFFF & paramKey.id));
    localCRC32.update((int)(0xFFFF & paramKey.id >> 32));
    if (!TextUtils.isEmpty(paramKey.name)) {
      localCRC32.update(paramKey.name.getBytes());
    }
    return localCRC32.getValue();
  }
  
  private void dataChanged()
  {
    if (sBackupManager == null) {
      sBackupManager = new BackupManager(this.mContext);
    }
    sBackupManager.dataChanged();
  }
  
  private AppWidgetProviderInfo findAppWidgetProviderInfo(ComponentName paramComponentName)
  {
    if (this.mWidgetMap == null)
    {
      List localList = AppWidgetManager.getInstance(this.mContext).getInstalledProviders();
      this.mWidgetMap = new HashMap(localList.size());
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        AppWidgetProviderInfo localAppWidgetProviderInfo = (AppWidgetProviderInfo)localIterator.next();
        this.mWidgetMap.put(localAppWidgetProviderInfo.provider, localAppWidgetProviderInfo);
      }
    }
    return (AppWidgetProviderInfo)this.mWidgetMap.get(paramComponentName);
  }
  
  private String geKeyType(BackupProtos.Key paramKey)
  {
    switch (paramKey.type)
    {
    default: 
      return "anonymous";
    case 1: 
      return "favorite";
    case 2: 
      return "screen";
    case 3: 
      return "icon";
    }
    return "widget";
  }
  
  private BackupProtos.Key getKey(int paramInt, long paramLong)
  {
    BackupProtos.Key localKey = new BackupProtos.Key();
    localKey.type = paramInt;
    localKey.id = paramLong;
    localKey.checksum = checkKey(localKey);
    return localKey;
  }
  
  private BackupProtos.Key getKey(int paramInt, String paramString)
  {
    BackupProtos.Key localKey = new BackupProtos.Key();
    localKey.type = paramInt;
    localKey.name = paramString;
    localKey.checksum = checkKey(localKey);
    return localKey;
  }
  
  private String getKeyName(BackupProtos.Key paramKey)
  {
    if (TextUtils.isEmpty(paramKey.name)) {
      return Long.toString(paramKey.id);
    }
    return paramKey.name;
  }
  
  private Set<String> getSavedIdsByType(int paramInt, BackupProtos.Journal paramJournal)
  {
    HashSet localHashSet = new HashSet();
    for (int i = 0; i < paramJournal.key.length; i++)
    {
      BackupProtos.Key localKey = paramJournal.key[i];
      if (localKey.type == paramInt) {
        localHashSet.add(keyToBackupKey(localKey));
      }
    }
    return localHashSet;
  }
  
  private String keyToBackupKey(BackupProtos.Key paramKey)
  {
    return Base64.encodeToString(BackupProtos.Key.toByteArray(paramKey), 2);
  }
  
  private byte[] packFavorite(Cursor paramCursor)
  {
    BackupProtos.Favorite localFavorite = new BackupProtos.Favorite();
    localFavorite.id = paramCursor.getLong(0);
    localFavorite.screen = paramCursor.getInt(13);
    localFavorite.container = paramCursor.getInt(7);
    localFavorite.cellX = paramCursor.getInt(5);
    localFavorite.cellY = paramCursor.getInt(6);
    localFavorite.spanX = paramCursor.getInt(14);
    localFavorite.spanY = paramCursor.getInt(15);
    localFavorite.iconType = paramCursor.getInt(11);
    if (localFavorite.iconType == 0)
    {
      String str4 = paramCursor.getString(9);
      if (!TextUtils.isEmpty(str4)) {
        localFavorite.iconPackage = str4;
      }
      String str5 = paramCursor.getString(10);
      if (!TextUtils.isEmpty(str5)) {
        localFavorite.iconResource = str5;
      }
    }
    if (localFavorite.iconType == 1)
    {
      byte[] arrayOfByte = paramCursor.getBlob(8);
      if ((arrayOfByte != null) && (arrayOfByte.length > 0)) {
        localFavorite.icon = arrayOfByte;
      }
    }
    String str1 = paramCursor.getString(16);
    if (!TextUtils.isEmpty(str1)) {
      localFavorite.title = str1;
    }
    String str2 = paramCursor.getString(2);
    if (!TextUtils.isEmpty(str2)) {
      localFavorite.intent = str2;
    }
    localFavorite.itemType = paramCursor.getInt(12);
    if (localFavorite.itemType == 4)
    {
      localFavorite.appWidgetId = paramCursor.getInt(4);
      String str3 = paramCursor.getString(3);
      if (!TextUtils.isEmpty(str3)) {
        localFavorite.appWidgetProvider = str3;
      }
    }
    return writeCheckedBytes(localFavorite);
  }
  
  private byte[] packIcon(int paramInt, Bitmap paramBitmap)
  {
    BackupProtos.Resource localResource = new BackupProtos.Resource();
    localResource.dpi = paramInt;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    if (paramBitmap.compress(IMAGE_FORMAT, 75, localByteArrayOutputStream)) {
      localResource.data = localByteArrayOutputStream.toByteArray();
    }
    return writeCheckedBytes(localResource);
  }
  
  private byte[] packScreen(Cursor paramCursor)
  {
    BackupProtos.Screen localScreen = new BackupProtos.Screen();
    localScreen.id = paramCursor.getLong(0);
    localScreen.rank = paramCursor.getInt(2);
    return writeCheckedBytes(localScreen);
  }
  
  private byte[] packWidget(int paramInt, WidgetPreviewLoader paramWidgetPreviewLoader, IconCache paramIconCache, ComponentName paramComponentName)
  {
    AppWidgetProviderInfo localAppWidgetProviderInfo = findAppWidgetProviderInfo(paramComponentName);
    BackupProtos.Widget localWidget = new BackupProtos.Widget();
    localWidget.provider = paramComponentName.flattenToShortString();
    localWidget.label = localAppWidgetProviderInfo.label;
    if (localAppWidgetProviderInfo.configure != null) {}
    for (boolean bool = true;; bool = false)
    {
      localWidget.configure = bool;
      if (localAppWidgetProviderInfo.icon != 0)
      {
        localWidget.icon = new BackupProtos.Resource();
        Bitmap localBitmap2 = Utilities.createIconBitmap(paramIconCache.getFullResIcon(paramComponentName.getPackageName(), localAppWidgetProviderInfo.icon), this.mContext);
        ByteArrayOutputStream localByteArrayOutputStream2 = new ByteArrayOutputStream();
        if (localBitmap2.compress(IMAGE_FORMAT, 75, localByteArrayOutputStream2))
        {
          localWidget.icon.data = localByteArrayOutputStream2.toByteArray();
          localWidget.icon.dpi = paramInt;
        }
      }
      if (localAppWidgetProviderInfo.previewImage != 0)
      {
        localWidget.preview = new BackupProtos.Resource();
        Bitmap localBitmap1 = paramWidgetPreviewLoader.generateWidgetPreview(localAppWidgetProviderInfo, null);
        ByteArrayOutputStream localByteArrayOutputStream1 = new ByteArrayOutputStream();
        if (localBitmap1.compress(IMAGE_FORMAT, 75, localByteArrayOutputStream1))
        {
          localWidget.preview.data = localByteArrayOutputStream1.toByteArray();
          localWidget.preview.dpi = paramInt;
        }
      }
      return writeCheckedBytes(localWidget);
    }
  }
  
  private byte[] readCheckedBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidProtocolBufferNanoException
  {
    BackupProtos.CheckedMessage localCheckedMessage = new BackupProtos.CheckedMessage();
    MessageNano.mergeFrom(localCheckedMessage, paramArrayOfByte, paramInt1, paramInt2);
    CRC32 localCRC32 = new CRC32();
    localCRC32.update(localCheckedMessage.payload);
    if (localCheckedMessage.checksum != localCRC32.getValue()) {
      throw new InvalidProtocolBufferNanoException("checksum does not match");
    }
    return localCheckedMessage.payload;
  }
  
  private BackupProtos.Journal readJournal(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    BackupProtos.Journal localJournal = new BackupProtos.Journal();
    if (paramParcelFileDescriptor == null) {
      return localJournal;
    }
    localFileInputStream = new FileInputStream(paramParcelFileDescriptor.getFileDescriptor());
    try
    {
      int i = localFileInputStream.available();
      byte[] arrayOfByte;
      int j;
      if (i < 1000000)
      {
        arrayOfByte = new byte[i];
        j = 0;
        while (i > 0) {
          try
          {
            int k = localFileInputStream.read(arrayOfByte, j, i);
            if (k > 0)
            {
              i -= k;
              j += k;
            }
            else
            {
              Log.w("LauncherBackupHelper", "read error: " + k);
              i = 0;
            }
          }
          catch (IOException localIOException5)
          {
            Log.w("LauncherBackupHelper", "failed to read the journal", localIOException5);
            arrayOfByte = null;
            i = 0;
          }
        }
        if (arrayOfByte == null) {}
      }
      try
      {
        MessageNano.mergeFrom(localJournal, readCheckedBytes(arrayOfByte, 0, j));
        try
        {
          localFileInputStream.close();
          return localJournal;
        }
        catch (IOException localIOException4)
        {
          Log.d("LauncherBackupHelper", "failed to close the journal", localIOException4);
          return localJournal;
        }
      }
      catch (InvalidProtocolBufferNanoException localInvalidProtocolBufferNanoException)
      {
        for (;;)
        {
          Log.d("LauncherBackupHelper", "failed to read the journal", localInvalidProtocolBufferNanoException);
          localJournal.clear();
        }
      }
      try
      {
        localFileInputStream.close();
        throw localObject;
      }
      catch (IOException localIOException1)
      {
        for (;;)
        {
          Log.d("LauncherBackupHelper", "failed to close the journal", localIOException1);
        }
      }
    }
    catch (IOException localIOException2)
    {
      localIOException2 = localIOException2;
      Log.d("LauncherBackupHelper", "failed to close the journal", localIOException2);
      try
      {
        localFileInputStream.close();
        return localJournal;
      }
      catch (IOException localIOException3)
      {
        Log.d("LauncherBackupHelper", "failed to close the journal", localIOException3);
        return localJournal;
      }
    }
    finally {}
  }
  
  private int removeDeletedKeysFromBackup(Set<String> paramSet, BackupDataOutput paramBackupDataOutput)
    throws IOException
  {
    int i = 0;
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      Log.v("LauncherBackupHelper", "dropping icon " + str);
      paramBackupDataOutput.writeEntityHeader(str, -1);
      i++;
    }
    return i;
  }
  
  private void restoreFavorite(BackupProtos.Key paramKey, byte[] paramArrayOfByte, int paramInt, ArrayList<BackupProtos.Key> paramArrayList)
  {
    Log.v("LauncherBackupHelper", "unpacking favorite " + paramKey.id + " (" + paramInt + " bytes)");
    try
    {
      unpackFavorite(paramArrayOfByte, 0, paramInt);
      return;
    }
    catch (InvalidProtocolBufferNanoException localInvalidProtocolBufferNanoException)
    {
      Log.w("LauncherBackupHelper", "failed to decode proto", localInvalidProtocolBufferNanoException);
    }
  }
  
  private void restoreIcon(BackupProtos.Key paramKey, byte[] paramArrayOfByte, int paramInt, ArrayList<BackupProtos.Key> paramArrayList)
  {
    Log.v("LauncherBackupHelper", "unpacking icon " + paramKey.id);
    try
    {
      BackupProtos.Resource localResource = unpackIcon(paramArrayOfByte, 0, paramInt);
      if (BitmapFactory.decodeByteArray(localResource.data, 0, localResource.data.length) == null) {
        Log.w("LauncherBackupHelper", "failed to unpack icon for " + paramKey.name);
      }
      return;
    }
    catch (InvalidProtocolBufferNanoException localInvalidProtocolBufferNanoException)
    {
      Log.w("LauncherBackupHelper", "failed to decode proto", localInvalidProtocolBufferNanoException);
    }
  }
  
  private void restoreScreen(BackupProtos.Key paramKey, byte[] paramArrayOfByte, int paramInt, ArrayList<BackupProtos.Key> paramArrayList)
  {
    Log.v("LauncherBackupHelper", "unpacking screen " + paramKey.id);
    try
    {
      unpackScreen(paramArrayOfByte, 0, paramInt);
      return;
    }
    catch (InvalidProtocolBufferNanoException localInvalidProtocolBufferNanoException)
    {
      Log.w("LauncherBackupHelper", "failed to decode proto", localInvalidProtocolBufferNanoException);
    }
  }
  
  private void restoreWidget(BackupProtos.Key paramKey, byte[] paramArrayOfByte, int paramInt, ArrayList<BackupProtos.Key> paramArrayList)
  {
    Log.v("LauncherBackupHelper", "unpacking widget " + paramKey.id);
    try
    {
      BackupProtos.Widget localWidget = unpackWidget(paramArrayOfByte, 0, paramInt);
      if ((localWidget.icon.data != null) && (BitmapFactory.decodeByteArray(localWidget.icon.data, 0, localWidget.icon.data.length) == null)) {
        Log.w("LauncherBackupHelper", "failed to unpack widget icon for " + paramKey.name);
      }
      return;
    }
    catch (InvalidProtocolBufferNanoException localInvalidProtocolBufferNanoException)
    {
      Log.w("LauncherBackupHelper", "failed to decode proto", localInvalidProtocolBufferNanoException);
    }
  }
  
  private BackupProtos.Favorite unpackFavorite(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidProtocolBufferNanoException
  {
    BackupProtos.Favorite localFavorite = new BackupProtos.Favorite();
    MessageNano.mergeFrom(localFavorite, readCheckedBytes(paramArrayOfByte, paramInt1, paramInt2));
    return localFavorite;
  }
  
  private BackupProtos.Resource unpackIcon(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidProtocolBufferNanoException
  {
    BackupProtos.Resource localResource = new BackupProtos.Resource();
    MessageNano.mergeFrom(localResource, readCheckedBytes(paramArrayOfByte, paramInt1, paramInt2));
    return localResource;
  }
  
  private BackupProtos.Screen unpackScreen(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidProtocolBufferNanoException
  {
    BackupProtos.Screen localScreen = new BackupProtos.Screen();
    MessageNano.mergeFrom(localScreen, readCheckedBytes(paramArrayOfByte, paramInt1, paramInt2));
    return localScreen;
  }
  
  private BackupProtos.Widget unpackWidget(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidProtocolBufferNanoException
  {
    BackupProtos.Widget localWidget = new BackupProtos.Widget();
    MessageNano.mergeFrom(localWidget, readCheckedBytes(paramArrayOfByte, paramInt1, paramInt2));
    return localWidget;
  }
  
  private byte[] writeCheckedBytes(MessageNano paramMessageNano)
  {
    BackupProtos.CheckedMessage localCheckedMessage = new BackupProtos.CheckedMessage();
    localCheckedMessage.payload = MessageNano.toByteArray(paramMessageNano);
    CRC32 localCRC32 = new CRC32();
    localCRC32.update(localCheckedMessage.payload);
    localCheckedMessage.checksum = localCRC32.getValue();
    return MessageNano.toByteArray(localCheckedMessage);
  }
  
  private void writeJournal(ParcelFileDescriptor paramParcelFileDescriptor, BackupProtos.Journal paramJournal)
  {
    try
    {
      FileOutputStream localFileOutputStream = new FileOutputStream(paramParcelFileDescriptor.getFileDescriptor());
      Log.d("LauncherBackupHelper", "failed to write backup journal", localIOException1);
    }
    catch (IOException localIOException1)
    {
      try
      {
        localFileOutputStream.write(writeCheckedBytes(paramJournal));
        localFileOutputStream.close();
        return;
      }
      catch (IOException localIOException2)
      {
        break label28;
      }
      localIOException1 = localIOException1;
    }
    label28:
  }
  
  private void writeRowToBackup(BackupProtos.Key paramKey, byte[] paramArrayOfByte, BackupProtos.Journal paramJournal, BackupDataOutput paramBackupDataOutput)
    throws IOException
  {
    String str = keyToBackupKey(paramKey);
    paramBackupDataOutput.writeEntityHeader(str, paramArrayOfByte.length);
    paramBackupDataOutput.writeEntityData(paramArrayOfByte, paramArrayOfByte.length);
    paramJournal.rows = (1 + paramJournal.rows);
    paramJournal.bytes += paramArrayOfByte.length;
    Log.v("LauncherBackupHelper", "saving " + geKeyType(paramKey) + " " + str + ": " + getKeyName(paramKey) + "/" + paramArrayOfByte.length);
  }
  
  public void performBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2)
  {
    Log.v("LauncherBackupHelper", "onBackup");
    BackupProtos.Journal localJournal1 = readJournal(paramParcelFileDescriptor1);
    BackupProtos.Journal localJournal2 = new BackupProtos.Journal();
    long l = localJournal1.t;
    localJournal2.t = System.currentTimeMillis();
    localJournal2.rows = 0;
    localJournal2.bytes = 0L;
    Log.v("LauncherBackupHelper", "lastBackupTime=" + l);
    ArrayList localArrayList = new ArrayList();
    try
    {
      backupFavorites(localJournal1, paramBackupDataOutput, localJournal2, localArrayList);
      backupScreens(localJournal1, paramBackupDataOutput, localJournal2, localArrayList);
      backupIcons(localJournal1, paramBackupDataOutput, localJournal2, localArrayList);
      backupWidgets(localJournal1, paramBackupDataOutput, localJournal2, localArrayList);
      localJournal2.key = ((BackupProtos.Key[])localArrayList.toArray(BackupProtos.Key.EMPTY_ARRAY));
      writeJournal(paramParcelFileDescriptor2, localJournal2);
      Log.v("LauncherBackupHelper", "onBackup: wrote " + localJournal2.bytes + "b in " + localJournal2.rows + " rows.");
      return;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        Log.e("LauncherBackupHelper", "launcher backup has failed", localIOException);
      }
    }
  }
  
  public void restoreEntity(BackupDataInputStream paramBackupDataInputStream)
  {
    Log.v("LauncherBackupHelper", "restoreEntity");
    if (this.mKeys == null) {
      this.mKeys = new ArrayList();
    }
    byte[] arrayOfByte = new byte[512];
    String str = paramBackupDataInputStream.getKey();
    int i = paramBackupDataInputStream.size();
    if (arrayOfByte.length < i) {
      arrayOfByte = new byte[i];
    }
    BackupProtos.Key localKey;
    try
    {
      paramBackupDataInputStream.read(arrayOfByte, 0, i);
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        try
        {
          localKey = backupKeyToKey(str);
          switch (localKey.type)
          {
          default: 
            Log.w("LauncherBackupHelper", "unknown restore entity type: " + localKey.type);
            return;
          }
        }
        catch (KeyParsingException localKeyParsingException)
        {
          Log.w("LauncherBackupHelper", "ignoring unparsable backup key: " + str);
          return;
        }
        localIOException = localIOException;
        Log.d("LauncherBackupHelper", "failed to read entity from restore data", localIOException);
        continue;
        restoreFavorite(localKey, arrayOfByte, i, this.mKeys);
        return;
      }
    }
    restoreScreen(localKey, arrayOfByte, i, this.mKeys);
    return;
    restoreIcon(localKey, arrayOfByte, i, this.mKeys);
    return;
    restoreWidget(localKey, arrayOfByte, i, this.mKeys);
  }
  
  public void writeNewStateDescription(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    BackupProtos.Journal localJournal = new BackupProtos.Journal();
    localJournal.t = 0L;
    localJournal.key = ((BackupProtos.Key[])this.mKeys.toArray(BackupProtos.Key.EMPTY_ARRAY));
    writeJournal(paramParcelFileDescriptor, localJournal);
    Log.v("LauncherBackupHelper", "onRestore: read " + this.mKeys.size() + " rows");
    this.mKeys.clear();
  }
  
  private class KeyParsingException
    extends Throwable
  {
    public KeyParsingException(String paramString)
    {
      super();
    }
    
    private KeyParsingException(Throwable paramThrowable)
    {
      super();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LauncherBackupHelper
 * JD-Core Version:    0.7.0.1
 */