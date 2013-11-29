package com.android.launcher3;

import android.app.SearchManager;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class LauncherProvider
  extends ContentProvider
{
  static final Uri CONTENT_APPWIDGET_RESET_URI = Uri.parse("content://com.google.android.launcher.settings/appWidgetReset");
  private static boolean sJustLoadedFromOldDb;
  private DatabaseHelper mOpenHelper;
  
  private void addModifiedTime(ContentValues paramContentValues)
  {
    paramContentValues.put("modified", Long.valueOf(System.currentTimeMillis()));
  }
  
  static String buildOrWhereString(String paramString, int[] paramArrayOfInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = -1 + paramArrayOfInt.length; i >= 0; i--)
    {
      localStringBuilder.append(paramString).append("=").append(paramArrayOfInt[i]);
      if (i > 0) {
        localStringBuilder.append(" OR ");
      }
    }
    return localStringBuilder.toString();
  }
  
  private static long dbInsertAndCheck(DatabaseHelper paramDatabaseHelper, SQLiteDatabase paramSQLiteDatabase, String paramString1, String paramString2, ContentValues paramContentValues)
  {
    if (!paramContentValues.containsKey("_id")) {
      throw new RuntimeException("Error: attempting to add item without specifying an id");
    }
    return paramSQLiteDatabase.insert(paramString1, paramString2, paramContentValues);
  }
  
  private static void deleteId(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    SqlArguments localSqlArguments = new SqlArguments(LauncherSettings.Favorites.getContentUri(paramLong, false), null, null);
    paramSQLiteDatabase.delete(localSqlArguments.table, localSqlArguments.where, localSqlArguments.args);
  }
  
  private void sendNotify(Uri paramUri)
  {
    String str = paramUri.getQueryParameter("notify");
    if ((str == null) || ("true".equals(str))) {
      getContext().getContentResolver().notifyChange(paramUri, null);
    }
    LauncherBackupAgentHelper.dataChanged(getContext());
  }
  
  public int bulkInsert(Uri paramUri, ContentValues[] paramArrayOfContentValues)
  {
    SqlArguments localSqlArguments = new SqlArguments(paramUri);
    SQLiteDatabase localSQLiteDatabase = this.mOpenHelper.getWritableDatabase();
    localSQLiteDatabase.beginTransaction();
    try
    {
      int i = paramArrayOfContentValues.length;
      for (int j = 0; j < i; j++)
      {
        addModifiedTime(paramArrayOfContentValues[j]);
        long l = dbInsertAndCheck(this.mOpenHelper, localSQLiteDatabase, localSqlArguments.table, null, paramArrayOfContentValues[j]);
        if (l < 0L) {
          return 0;
        }
      }
      localSQLiteDatabase.setTransactionSuccessful();
      localSQLiteDatabase.endTransaction();
      sendNotify(paramUri);
      return paramArrayOfContentValues.length;
    }
    finally
    {
      localSQLiteDatabase.endTransaction();
    }
  }
  
  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    SqlArguments localSqlArguments = new SqlArguments(paramUri, paramString, paramArrayOfString);
    int i = this.mOpenHelper.getWritableDatabase().delete(localSqlArguments.table, localSqlArguments.where, localSqlArguments.args);
    if (i > 0) {
      sendNotify(paramUri);
    }
    return i;
  }
  
  public long generateNewItemId()
  {
    return this.mOpenHelper.generateNewItemId();
  }
  
  public long generateNewScreenId()
  {
    return this.mOpenHelper.generateNewScreenId();
  }
  
  public String getType(Uri paramUri)
  {
    SqlArguments localSqlArguments = new SqlArguments(paramUri, null, null);
    if (TextUtils.isEmpty(localSqlArguments.where)) {
      return "vnd.android.cursor.dir/" + localSqlArguments.table;
    }
    return "vnd.android.cursor.item/" + localSqlArguments.table;
  }
  
  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    SqlArguments localSqlArguments = new SqlArguments(paramUri);
    SQLiteDatabase localSQLiteDatabase = this.mOpenHelper.getWritableDatabase();
    addModifiedTime(paramContentValues);
    long l = dbInsertAndCheck(this.mOpenHelper, localSQLiteDatabase, localSqlArguments.table, null, paramContentValues);
    if (l <= 0L) {
      return null;
    }
    Uri localUri = ContentUris.withAppendedId(paramUri, l);
    sendNotify(localUri);
    return localUri;
  }
  
  public boolean justLoadedOldDb()
  {
    try
    {
      String str = LauncherAppState.getSharedPreferencesKey();
      SharedPreferences localSharedPreferences = getContext().getSharedPreferences(str, 0);
      boolean bool = sJustLoadedFromOldDb;
      sJustLoadedFromOldDb = false;
      if (localSharedPreferences.getBoolean("UPGRADED_FROM_OLD_DATABASE", false))
      {
        SharedPreferences.Editor localEditor = localSharedPreferences.edit();
        localEditor.remove("UPGRADED_FROM_OLD_DATABASE");
        localEditor.commit();
        bool = true;
      }
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void loadDefaultFavoritesIfNecessary(int paramInt)
  {
    try
    {
      String str = LauncherAppState.getSharedPreferencesKey();
      SharedPreferences localSharedPreferences = getContext().getSharedPreferences(str, 0);
      if (localSharedPreferences.getBoolean("EMPTY_DATABASE_CREATED", false))
      {
        int i = paramInt;
        if (i == 0) {
          i = localSharedPreferences.getInt("DEFAULT_WORKSPACE_RESOURCE_ID", 2131099655);
        }
        SharedPreferences.Editor localEditor = localSharedPreferences.edit();
        localEditor.remove("EMPTY_DATABASE_CREATED");
        if (paramInt != 0) {
          localEditor.putInt("DEFAULT_WORKSPACE_RESOURCE_ID", paramInt);
        }
        this.mOpenHelper.loadFavorites(this.mOpenHelper.getWritableDatabase(), i);
        this.mOpenHelper.setFlagJustLoadedOldDb();
        localEditor.commit();
      }
      return;
    }
    finally {}
  }
  
  public boolean onCreate()
  {
    this.mOpenHelper = new DatabaseHelper(getContext());
    LauncherAppState.setLauncherProvider(this);
    return true;
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    SqlArguments localSqlArguments = new SqlArguments(paramUri, paramString1, paramArrayOfString2);
    SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
    localSQLiteQueryBuilder.setTables(localSqlArguments.table);
    Cursor localCursor = localSQLiteQueryBuilder.query(this.mOpenHelper.getWritableDatabase(), paramArrayOfString1, localSqlArguments.where, localSqlArguments.args, null, null, paramString2);
    localCursor.setNotificationUri(getContext().getContentResolver(), paramUri);
    return localCursor;
  }
  
  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    SqlArguments localSqlArguments = new SqlArguments(paramUri, paramString, paramArrayOfString);
    addModifiedTime(paramContentValues);
    int i = this.mOpenHelper.getWritableDatabase().update(localSqlArguments.table, paramContentValues, localSqlArguments.where, localSqlArguments.args);
    if (i > 0) {
      sendNotify(paramUri);
    }
    return i;
  }
  
  public void updateMaxItemId(long paramLong)
  {
    this.mOpenHelper.updateMaxItemId(paramLong);
  }
  
  public void updateMaxScreenId(long paramLong)
  {
    this.mOpenHelper.updateMaxScreenId(paramLong);
  }
  
  private static abstract interface ContentValuesCallback
  {
    public abstract void onRow(ContentValues paramContentValues);
  }
  
  private static class DatabaseHelper
    extends SQLiteOpenHelper
  {
    private final AppWidgetHost mAppWidgetHost;
    private final Context mContext;
    private long mMaxItemId = -1L;
    private long mMaxScreenId = -1L;
    
    DatabaseHelper(Context paramContext)
    {
      super("launcher.db", null, 15);
      this.mContext = paramContext;
      this.mAppWidgetHost = new AppWidgetHost(paramContext, 1024);
      if (this.mMaxItemId == -1L) {
        this.mMaxItemId = initializeMaxItemId(getWritableDatabase());
      }
      if (this.mMaxScreenId == -1L) {
        this.mMaxScreenId = initializeMaxScreenId(getWritableDatabase());
      }
    }
    
    private long addAppShortcut(SQLiteDatabase paramSQLiteDatabase, ContentValues paramContentValues, TypedArray paramTypedArray, PackageManager paramPackageManager, Intent paramIntent)
    {
      l = -1L;
      str1 = paramTypedArray.getString(1);
      str2 = paramTypedArray.getString(0);
      try
      {
        ComponentName localComponentName = new ComponentName(str1, str2);
        ActivityInfo localActivityInfo2 = paramPackageManager.getActivityInfo(localComponentName, 0);
        Object localObject = localActivityInfo2;
        ActivityInfo localActivityInfo1;
        return l;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException1)
      {
        for (;;)
        {
          try
          {
            l = generateNewItemId();
            paramIntent.setComponent(localComponentName);
            paramIntent.setFlags(270532608);
            paramContentValues.put("intent", paramIntent.toUri(0));
            paramContentValues.put("title", ((ActivityInfo)localObject).loadLabel(paramPackageManager).toString());
            paramContentValues.put("itemType", Integer.valueOf(0));
            paramContentValues.put("spanX", Integer.valueOf(1));
            paramContentValues.put("spanY", Integer.valueOf(1));
            paramContentValues.put("_id", Long.valueOf(generateNewItemId()));
            if (LauncherProvider.dbInsertAndCheck(this, paramSQLiteDatabase, "favorites", null, paramContentValues) >= 0L) {
              break;
            }
            return -1L;
          }
          catch (PackageManager.NameNotFoundException localNameNotFoundException2)
          {
            Log.w("Launcher.LauncherProvider", "Unable to add favorite: " + str1 + "/" + str2, localNameNotFoundException2);
          }
          localNameNotFoundException1 = localNameNotFoundException1;
          localComponentName = new ComponentName(paramPackageManager.currentToCanonicalPackageNames(new String[] { str1 })[0], str2);
          localActivityInfo1 = paramPackageManager.getActivityInfo(localComponentName, 0);
          localObject = localActivityInfo1;
        }
      }
    }
    
    private boolean addAppWidget(XmlResourceParser paramXmlResourceParser, AttributeSet paramAttributeSet, int paramInt, SQLiteDatabase paramSQLiteDatabase, ContentValues paramContentValues, TypedArray paramTypedArray, PackageManager paramPackageManager)
      throws XmlPullParserException, IOException
    {
      String str1 = paramTypedArray.getString(1);
      String str2 = paramTypedArray.getString(0);
      if ((str1 == null) || (str2 == null)) {
        return false;
      }
      int i = 1;
      ComponentName localComponentName = new ComponentName(str1, str2);
      int j;
      int k;
      Bundle localBundle;
      try
      {
        paramPackageManager.getReceiverInfo(localComponentName, 0);
        if (i == 0) {
          break label282;
        }
        j = paramTypedArray.getInt(6, 0);
        k = paramTypedArray.getInt(7, 0);
        localBundle = new Bundle();
        int m = paramXmlResourceParser.getDepth();
        for (;;)
        {
          int n = paramXmlResourceParser.next();
          if ((n == 3) && (paramXmlResourceParser.getDepth() <= m)) {
            break label265;
          }
          if (n == 2)
          {
            TypedArray localTypedArray = this.mContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Extra);
            if (!"extra".equals(paramXmlResourceParser.getName())) {
              break label255;
            }
            String str3 = localTypedArray.getString(0);
            String str4 = localTypedArray.getString(1);
            if ((str3 == null) || (str4 == null)) {
              break;
            }
            localBundle.putString(str3, str4);
            localTypedArray.recycle();
          }
        }
      }
      catch (Exception localException1)
      {
        for (;;)
        {
          localComponentName = new ComponentName(paramPackageManager.currentToCanonicalPackageNames(new String[] { str1 })[0], str2);
          try
          {
            paramPackageManager.getReceiverInfo(localComponentName, 0);
          }
          catch (Exception localException2)
          {
            i = 0;
          }
        }
      }
      throw new RuntimeException("Widget extras must have a key and value");
      label255:
      throw new RuntimeException("Widgets can contain only extras");
      label265:
      return addAppWidget(paramSQLiteDatabase, paramContentValues, localComponentName, j, k, localBundle);
      label282:
      return false;
    }
    
    private boolean addAppWidget(SQLiteDatabase paramSQLiteDatabase, ContentValues paramContentValues, ComponentName paramComponentName, int paramInt1, int paramInt2, Bundle paramBundle)
    {
      boolean bool = false;
      AppWidgetManager localAppWidgetManager = AppWidgetManager.getInstance(this.mContext);
      try
      {
        int i = this.mAppWidgetHost.allocateAppWidgetId();
        paramContentValues.put("itemType", Integer.valueOf(4));
        paramContentValues.put("spanX", Integer.valueOf(paramInt1));
        paramContentValues.put("spanY", Integer.valueOf(paramInt2));
        paramContentValues.put("appWidgetId", Integer.valueOf(i));
        paramContentValues.put("appWidgetProvider", paramComponentName.flattenToString());
        paramContentValues.put("_id", Long.valueOf(generateNewItemId()));
        LauncherProvider.dbInsertAndCheck(this, paramSQLiteDatabase, "favorites", null, paramContentValues);
        bool = true;
        localAppWidgetManager.bindAppWidgetIdIfAllowed(i, paramComponentName);
        if ((paramBundle != null) && (!paramBundle.isEmpty()))
        {
          Intent localIntent = new Intent("com.android.launcher.action.APPWIDGET_DEFAULT_WORKSPACE_CONFIGURE");
          localIntent.setComponent(paramComponentName);
          localIntent.putExtras(paramBundle);
          localIntent.putExtra("appWidgetId", i);
          this.mContext.sendBroadcast(localIntent);
        }
        return bool;
      }
      catch (RuntimeException localRuntimeException)
      {
        Log.e("Launcher.LauncherProvider", "Problem allocating appWidgetId", localRuntimeException);
      }
      return bool;
    }
    
    private boolean addClockWidget(SQLiteDatabase paramSQLiteDatabase, ContentValues paramContentValues)
    {
      return addAppWidget(paramSQLiteDatabase, paramContentValues, new ComponentName("com.android.alarmclock", "com.android.alarmclock.AnalogAppWidgetProvider"), 2, 2, null);
    }
    
    private long addFolder(SQLiteDatabase paramSQLiteDatabase, ContentValues paramContentValues)
    {
      paramContentValues.put("itemType", Integer.valueOf(2));
      paramContentValues.put("spanX", Integer.valueOf(1));
      paramContentValues.put("spanY", Integer.valueOf(1));
      long l = generateNewItemId();
      paramContentValues.put("_id", Long.valueOf(l));
      if (LauncherProvider.dbInsertAndCheck(this, paramSQLiteDatabase, "favorites", null, paramContentValues) <= 0L) {
        l = -1L;
      }
      return l;
    }
    
    private boolean addSearchWidget(SQLiteDatabase paramSQLiteDatabase, ContentValues paramContentValues)
    {
      return addAppWidget(paramSQLiteDatabase, paramContentValues, getSearchWidgetProvider(), 4, 1, null);
    }
    
    private long addUriShortcut(SQLiteDatabase paramSQLiteDatabase, ContentValues paramContentValues, TypedArray paramTypedArray)
    {
      Resources localResources = this.mContext.getResources();
      int i = paramTypedArray.getResourceId(8, 0);
      int j = paramTypedArray.getResourceId(9, 0);
      String str = null;
      do
      {
        Intent localIntent;
        try
        {
          str = paramTypedArray.getString(10);
          localIntent = Intent.parseUri(str, 0);
          if ((i == 0) || (j == 0))
          {
            Log.w("Launcher.LauncherProvider", "Shortcut is missing title or icon resource ID");
            l = -1L;
            return l;
          }
        }
        catch (URISyntaxException localURISyntaxException)
        {
          Log.w("Launcher.LauncherProvider", "Shortcut has malformed uri: " + str);
          return -1L;
        }
        long l = generateNewItemId();
        localIntent.setFlags(268435456);
        paramContentValues.put("intent", localIntent.toUri(0));
        paramContentValues.put("title", localResources.getString(j));
        paramContentValues.put("itemType", Integer.valueOf(1));
        paramContentValues.put("spanX", Integer.valueOf(1));
        paramContentValues.put("spanY", Integer.valueOf(1));
        paramContentValues.put("iconType", Integer.valueOf(0));
        paramContentValues.put("iconPackage", this.mContext.getPackageName());
        paramContentValues.put("iconResource", localResources.getResourceName(i));
        paramContentValues.put("_id", Long.valueOf(l));
      } while (LauncherProvider.dbInsertAndCheck(this, paramSQLiteDatabase, "favorites", null, paramContentValues) >= 0L);
      return -1L;
    }
    
    private void addWorkspacesTable(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE workspaceScreens (_id INTEGER,screenRank INTEGER,modified INTEGER NOT NULL DEFAULT 0);");
    }
    
    private static final void beginDocument(XmlPullParser paramXmlPullParser, String paramString)
      throws XmlPullParserException, IOException
    {
      int i;
      do
      {
        i = paramXmlPullParser.next();
      } while ((i != 2) && (i != 1));
      if (i != 2) {
        throw new XmlPullParserException("No start tag found");
      }
      if (!paramXmlPullParser.getName().equals(paramString)) {
        throw new XmlPullParserException("Unexpected start tag: found " + paramXmlPullParser.getName() + ", expected " + paramString);
      }
    }
    
    /* Error */
    private boolean convertDatabase(SQLiteDatabase paramSQLiteDatabase, Uri paramUri, LauncherProvider.ContentValuesCallback paramContentValuesCallback, boolean paramBoolean)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 26	com/android/launcher3/LauncherProvider$DatabaseHelper:mContext	Landroid/content/Context;
      //   4: invokevirtual 370	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
      //   7: astore 5
      //   9: aload 5
      //   11: aload_2
      //   12: aconst_null
      //   13: aconst_null
      //   14: aconst_null
      //   15: aconst_null
      //   16: invokevirtual 376	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
      //   19: astore 12
      //   21: aload 12
      //   23: astore 7
      //   25: iconst_0
      //   26: istore 8
      //   28: aload 7
      //   30: ifnull +60 -> 90
      //   33: aload 7
      //   35: invokeinterface 381 1 0
      //   40: istore 10
      //   42: iconst_0
      //   43: istore 8
      //   45: iload 10
      //   47: ifle +36 -> 83
      //   50: aload_0
      //   51: aload_1
      //   52: aload 7
      //   54: aload_3
      //   55: invokespecial 385	com/android/launcher3/LauncherProvider$DatabaseHelper:copyFromCursor	(Landroid/database/sqlite/SQLiteDatabase;Landroid/database/Cursor;Lcom/android/launcher3/LauncherProvider$ContentValuesCallback;)I
      //   58: ifle +54 -> 112
      //   61: iconst_1
      //   62: istore 8
      //   64: iload 8
      //   66: ifeq +17 -> 83
      //   69: iload 4
      //   71: ifeq +12 -> 83
      //   74: aload 5
      //   76: aload_2
      //   77: aconst_null
      //   78: aconst_null
      //   79: invokevirtual 389	android/content/ContentResolver:delete	(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I
      //   82: pop
      //   83: aload 7
      //   85: invokeinterface 392 1 0
      //   90: iload 8
      //   92: ifeq +17 -> 109
      //   95: aload_0
      //   96: aload_1
      //   97: invokespecial 395	com/android/launcher3/LauncherProvider$DatabaseHelper:convertWidgets	(Landroid/database/sqlite/SQLiteDatabase;)V
      //   100: aload_0
      //   101: aload_0
      //   102: aload_1
      //   103: invokespecial 41	com/android/launcher3/LauncherProvider$DatabaseHelper:initializeMaxItemId	(Landroid/database/sqlite/SQLiteDatabase;)J
      //   106: putfield 22	com/android/launcher3/LauncherProvider$DatabaseHelper:mMaxItemId	J
      //   109: iload 8
      //   111: ireturn
      //   112: iconst_0
      //   113: istore 8
      //   115: goto -51 -> 64
      //   118: astore 9
      //   120: aload 7
      //   122: invokeinterface 392 1 0
      //   127: aload 9
      //   129: athrow
      //   130: astore 6
      //   132: aconst_null
      //   133: astore 7
      //   135: goto -110 -> 25
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	138	0	this	DatabaseHelper
      //   0	138	1	paramSQLiteDatabase	SQLiteDatabase
      //   0	138	2	paramUri	Uri
      //   0	138	3	paramContentValuesCallback	LauncherProvider.ContentValuesCallback
      //   0	138	4	paramBoolean	boolean
      //   7	68	5	localContentResolver	ContentResolver
      //   130	1	6	localException	Exception
      //   23	111	7	localCursor1	Cursor
      //   26	88	8	bool	boolean
      //   118	10	9	localObject	Object
      //   40	6	10	i	int
      //   19	3	12	localCursor2	Cursor
      // Exception table:
      //   from	to	target	type
      //   33	42	118	finally
      //   50	61	118	finally
      //   74	83	118	finally
      //   9	21	130	java/lang/Exception
    }
    
    private void convertWidgets(SQLiteDatabase paramSQLiteDatabase)
    {
      AppWidgetManager localAppWidgetManager = AppWidgetManager.getInstance(this.mContext);
      String str = LauncherProvider.buildOrWhereString("itemType", new int[] { 1000, 1002, 1001 });
      Cursor localCursor = null;
      paramSQLiteDatabase.beginTransaction();
      int i;
      int j;
      for (;;)
      {
        try
        {
          localCursor = paramSQLiteDatabase.query("favorites", new String[] { "_id", "itemType" }, str, null, null, null, null);
          localContentValues = new ContentValues();
          if ((localCursor == null) || (!localCursor.moveToNext())) {
            break label397;
          }
          l = localCursor.getLong(0);
          i = localCursor.getInt(1);
        }
        catch (SQLException localSQLException)
        {
          ContentValues localContentValues;
          long l;
          Log.w("Launcher.LauncherProvider", "Problem while allocating appWidgetIds for existing widgets", localSQLException);
          paramSQLiteDatabase.endTransaction();
          if (localCursor == null) {
            continue;
          }
          localCursor.close();
          this.mMaxItemId = initializeMaxItemId(paramSQLiteDatabase);
          return;
          localContentValues.put("spanX", Integer.valueOf(2));
          localContentValues.put("spanY", Integer.valueOf(2));
          continue;
        }
        finally
        {
          paramSQLiteDatabase.endTransaction();
          if (localCursor == null) {
            continue;
          }
          localCursor.close();
        }
        try
        {
          j = this.mAppWidgetHost.allocateAppWidgetId();
          localContentValues.clear();
          localContentValues.put("itemType", Integer.valueOf(4));
          localContentValues.put("appWidgetId", Integer.valueOf(j));
          if (i != 1001) {
            continue;
          }
          localContentValues.put("spanX", Integer.valueOf(4));
          localContentValues.put("spanY", Integer.valueOf(1));
          paramSQLiteDatabase.update("favorites", localContentValues, "_id=" + l, null);
          if (i != 1000) {
            break label344;
          }
          localAppWidgetManager.bindAppWidgetIdIfAllowed(j, new ComponentName("com.android.alarmclock", "com.android.alarmclock.AnalogAppWidgetProvider"));
        }
        catch (RuntimeException localRuntimeException)
        {
          Log.e("Launcher.LauncherProvider", "Problem allocating appWidgetId", localRuntimeException);
        }
      }
      for (;;)
      {
        label344:
        if (i == 1002)
        {
          localAppWidgetManager.bindAppWidgetIdIfAllowed(j, new ComponentName("com.android.camera", "com.android.camera.PhotoAppWidgetProvider"));
          break;
        }
        if (i != 1001) {
          break;
        }
        localAppWidgetManager.bindAppWidgetIdIfAllowed(j, getSearchWidgetProvider());
        break;
        label397:
        paramSQLiteDatabase.setTransactionSuccessful();
        paramSQLiteDatabase.endTransaction();
        if (localCursor != null) {
          localCursor.close();
        }
      }
    }
    
    private int copyFromCursor(SQLiteDatabase paramSQLiteDatabase, Cursor paramCursor, LauncherProvider.ContentValuesCallback paramContentValuesCallback)
    {
      int i = paramCursor.getColumnIndexOrThrow("_id");
      int j = paramCursor.getColumnIndexOrThrow("intent");
      int k = paramCursor.getColumnIndexOrThrow("title");
      int m = paramCursor.getColumnIndexOrThrow("iconType");
      int n = paramCursor.getColumnIndexOrThrow("icon");
      int i1 = paramCursor.getColumnIndexOrThrow("iconPackage");
      int i2 = paramCursor.getColumnIndexOrThrow("iconResource");
      int i3 = paramCursor.getColumnIndexOrThrow("container");
      int i4 = paramCursor.getColumnIndexOrThrow("itemType");
      int i5 = paramCursor.getColumnIndexOrThrow("screen");
      int i6 = paramCursor.getColumnIndexOrThrow("cellX");
      int i7 = paramCursor.getColumnIndexOrThrow("cellY");
      int i8 = paramCursor.getColumnIndexOrThrow("uri");
      int i9 = paramCursor.getColumnIndexOrThrow("displayMode");
      ContentValues[] arrayOfContentValues = new ContentValues[paramCursor.getCount()];
      int i11;
      for (int i10 = 0; paramCursor.moveToNext(); i10 = i11)
      {
        ContentValues localContentValues = new ContentValues(paramCursor.getColumnCount());
        localContentValues.put("_id", Long.valueOf(paramCursor.getLong(i)));
        localContentValues.put("intent", paramCursor.getString(j));
        localContentValues.put("title", paramCursor.getString(k));
        localContentValues.put("iconType", Integer.valueOf(paramCursor.getInt(m)));
        localContentValues.put("icon", paramCursor.getBlob(n));
        localContentValues.put("iconPackage", paramCursor.getString(i1));
        localContentValues.put("iconResource", paramCursor.getString(i2));
        localContentValues.put("container", Integer.valueOf(paramCursor.getInt(i3)));
        localContentValues.put("itemType", Integer.valueOf(paramCursor.getInt(i4)));
        localContentValues.put("appWidgetId", Integer.valueOf(-1));
        localContentValues.put("screen", Integer.valueOf(paramCursor.getInt(i5)));
        localContentValues.put("cellX", Integer.valueOf(paramCursor.getInt(i6)));
        localContentValues.put("cellY", Integer.valueOf(paramCursor.getInt(i7)));
        localContentValues.put("uri", paramCursor.getString(i8));
        localContentValues.put("displayMode", Integer.valueOf(paramCursor.getInt(i9)));
        if (paramContentValuesCallback != null) {
          paramContentValuesCallback.onRow(localContentValues);
        }
        i11 = i10 + 1;
        arrayOfContentValues[i10] = localContentValues;
      }
      int i12 = 0;
      if (i10 > 0) {
        paramSQLiteDatabase.beginTransaction();
      }
      try
      {
        int i13 = arrayOfContentValues.length;
        for (int i14 = 0; i14 < i13; i14++)
        {
          long l = LauncherProvider.dbInsertAndCheck(this, paramSQLiteDatabase, "favorites", null, arrayOfContentValues[i14]);
          if (l < 0L) {
            return 0;
          }
          i12++;
        }
        paramSQLiteDatabase.setTransactionSuccessful();
        return i12;
      }
      finally
      {
        paramSQLiteDatabase.endTransaction();
      }
    }
    
    private ComponentName getProviderInPackage(String paramString)
    {
      List localList = AppWidgetManager.getInstance(this.mContext).getInstalledProviders();
      ComponentName localComponentName;
      if (localList == null)
      {
        localComponentName = null;
        return localComponentName;
      }
      int i = localList.size();
      for (int j = 0;; j++)
      {
        if (j >= i) {
          break label76;
        }
        localComponentName = ((AppWidgetProviderInfo)localList.get(j)).provider;
        if ((localComponentName != null) && (localComponentName.getPackageName().equals(paramString))) {
          break;
        }
      }
      label76:
      return null;
    }
    
    private ComponentName getSearchWidgetProvider()
    {
      ComponentName localComponentName = ((SearchManager)this.mContext.getSystemService("search")).getGlobalSearchActivity();
      if (localComponentName == null) {
        return null;
      }
      return getProviderInPackage(localComponentName.getPackageName());
    }
    
    private long initializeMaxItemId(SQLiteDatabase paramSQLiteDatabase)
    {
      Cursor localCursor = paramSQLiteDatabase.rawQuery("SELECT MAX(_id) FROM favorites", null);
      long l = -1L;
      if ((localCursor != null) && (localCursor.moveToNext())) {
        l = localCursor.getLong(0);
      }
      if (localCursor != null) {
        localCursor.close();
      }
      if (l == -1L) {
        throw new RuntimeException("Error: could not query max item id");
      }
      return l;
    }
    
    private long initializeMaxScreenId(SQLiteDatabase paramSQLiteDatabase)
    {
      Cursor localCursor = paramSQLiteDatabase.rawQuery("SELECT MAX(_id) FROM workspaceScreens", null);
      long l = -1L;
      if ((localCursor != null) && (localCursor.moveToNext())) {
        l = localCursor.getLong(0);
      }
      if (localCursor != null) {
        localCursor.close();
      }
      if (l == -1L) {
        throw new RuntimeException("Error: could not query max screen id");
      }
      return l;
    }
    
    private int loadFavorites(SQLiteDatabase paramSQLiteDatabase, int paramInt)
    {
      Intent localIntent = new Intent("android.intent.action.MAIN", null);
      localIntent.addCategory("android.intent.category.LAUNCHER");
      ContentValues localContentValues = new ContentValues();
      PackageManager localPackageManager = this.mContext.getPackageManager();
      int i = 0;
      XmlResourceParser localXmlResourceParser;
      AttributeSet localAttributeSet;
      int k;
      String str1;
      label226:
      TypedArray localTypedArray1;
      try
      {
        localXmlResourceParser = this.mContext.getResources().getXml(paramInt);
        localAttributeSet = Xml.asAttributeSet(localXmlResourceParser);
        beginDocument(localXmlResourceParser, "favorites");
        int j = localXmlResourceParser.getDepth();
        for (;;)
        {
          k = localXmlResourceParser.next();
          if (((k == 3) && (localXmlResourceParser.getDepth() <= j)) || (k == 1)) {
            break;
          }
          if (k == 2)
          {
            str1 = localXmlResourceParser.getName();
            if (!"include".equals(str1)) {
              break label274;
            }
            TypedArray localTypedArray3 = this.mContext.obtainStyledAttributes(localAttributeSet, R.styleable.Include);
            i2 = localTypedArray3.getResourceId(0, 0);
            if ((i2 == 0) || (i2 == paramInt)) {
              break label226;
            }
            i += loadFavorites(paramSQLiteDatabase, i2);
            localTypedArray3.recycle();
          }
        }
      }
      catch (XmlPullParserException localXmlPullParserException)
      {
        for (;;)
        {
          int i2;
          Log.w("Launcher.LauncherProvider", "Got exception parsing favorites.", localXmlPullParserException);
          if (this.mMaxItemId == -1L) {
            this.mMaxItemId = initializeMaxItemId(paramSQLiteDatabase);
          }
          return i;
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = Integer.valueOf(i2);
          Log.w("Launcher.LauncherProvider", String.format("Skipping <include workspace=0x%08x>", arrayOfObject));
        }
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          Log.w("Launcher.LauncherProvider", "Got exception parsing favorites.", localIOException);
          continue;
          localTypedArray1 = this.mContext.obtainStyledAttributes(localAttributeSet, R.styleable.Favorite);
          long l1 = -100L;
          if (localTypedArray1.hasValue(2)) {
            l1 = Long.valueOf(localTypedArray1.getString(2)).longValue();
          }
          String str2 = localTypedArray1.getString(3);
          String str3 = localTypedArray1.getString(4);
          String str4 = localTypedArray1.getString(5);
          localContentValues.clear();
          localContentValues.put("container", Long.valueOf(l1));
          localContentValues.put("screen", str2);
          localContentValues.put("cellX", str3);
          localContentValues.put("cellY", str4);
          if (!"favorite".equals(str1)) {
            break label452;
          }
          if (addAppShortcut(paramSQLiteDatabase, localContentValues, localTypedArray1, localPackageManager, localIntent) < 0L) {
            break;
          }
          bool2 = true;
          break label916;
          localTypedArray1.recycle();
        }
      }
      catch (RuntimeException localRuntimeException)
      {
        for (;;)
        {
          label274:
          Log.w("Launcher.LauncherProvider", "Got exception parsing favorites.", localRuntimeException);
        }
        bool2 = false;
      }
      label452:
      String str5;
      long l2;
      if ("search".equals(str1))
      {
        bool2 = addSearchWidget(paramSQLiteDatabase, localContentValues);
      }
      else if ("clock".equals(str1))
      {
        bool2 = addClockWidget(paramSQLiteDatabase, localContentValues);
      }
      else if ("appwidget".equals(str1))
      {
        bool2 = addAppWidget(localXmlResourceParser, localAttributeSet, k, paramSQLiteDatabase, localContentValues, localTypedArray1, localPackageManager);
      }
      else
      {
        if ("shortcut".equals(str1))
        {
          if (addUriShortcut(paramSQLiteDatabase, localContentValues, localTypedArray1) < 0L) {
            break label930;
          }
          bool2 = true;
          break label927;
        }
        boolean bool1 = "folder".equals(str1);
        bool2 = false;
        if (bool1)
        {
          int m = localTypedArray1.getResourceId(9, -1);
          if (m != -1)
          {
            str5 = this.mContext.getResources().getString(m);
            localContentValues.put("title", str5);
            l2 = addFolder(paramSQLiteDatabase, localContentValues);
            if (l2 < 0L) {
              break label936;
            }
          }
        }
      }
      label656:
      label916:
      label927:
      label930:
      label936:
      for (boolean bool2 = true;; bool2 = false)
      {
        ArrayList localArrayList = new ArrayList();
        int n = localXmlResourceParser.getDepth();
        int i1;
        do
        {
          i1 = localXmlResourceParser.next();
          if ((i1 == 3) && (localXmlResourceParser.getDepth() <= n)) {
            break;
          }
        } while (i1 != 2);
        String str6 = localXmlResourceParser.getName();
        TypedArray localTypedArray2 = this.mContext.obtainStyledAttributes(localAttributeSet, R.styleable.Favorite);
        localContentValues.clear();
        localContentValues.put("container", Long.valueOf(l2));
        if (("favorite".equals(str6)) && (l2 >= 0L))
        {
          long l3 = addAppShortcut(paramSQLiteDatabase, localContentValues, localTypedArray2, localPackageManager, localIntent);
          if (l3 >= 0L) {
            localArrayList.add(Long.valueOf(l3));
          }
        }
        for (;;)
        {
          localTypedArray2.recycle();
          break label656;
          str5 = this.mContext.getResources().getString(2131361862);
          break;
          if ((!"shortcut".equals(str6)) || (l2 < 0L)) {
            break label856;
          }
          long l4 = addUriShortcut(paramSQLiteDatabase, localContentValues, localTypedArray2);
          if (l4 >= 0L) {
            localArrayList.add(Long.valueOf(l4));
          }
        }
        label856:
        throw new RuntimeException("Folders can contain only shortcuts");
        if ((localArrayList.size() < 2) && (l2 >= 0L))
        {
          LauncherProvider.deleteId(paramSQLiteDatabase, l2);
          if (localArrayList.size() > 0) {
            LauncherProvider.deleteId(paramSQLiteDatabase, ((Long)localArrayList.get(0)).longValue());
          }
          bool2 = false;
        }
        if (bool2)
        {
          i++;
          break;
        }
        for (;;)
        {
          break label916;
          break;
          bool2 = false;
        }
      }
    }
    
    private void normalizeIcons(SQLiteDatabase paramSQLiteDatabase)
    {
      Log.d("Launcher.LauncherProvider", "normalizing icons");
      paramSQLiteDatabase.beginTransaction();
      Cursor localCursor = null;
      SQLiteStatement localSQLiteStatement = null;
      int i = 0;
      for (;;)
      {
        try
        {
          localSQLiteStatement = paramSQLiteDatabase.compileStatement("UPDATE favorites SET icon=? WHERE _id=?");
          localCursor = paramSQLiteDatabase.rawQuery("SELECT _id, icon FROM favorites WHERE iconType=1", null);
          int j = localCursor.getColumnIndexOrThrow("_id");
          int k = localCursor.getColumnIndexOrThrow("icon");
          if (localCursor.moveToNext())
          {
            long l = localCursor.getLong(j);
            byte[] arrayOfByte1 = localCursor.getBlob(k);
            try
            {
              Bitmap localBitmap = Utilities.resampleIconBitmap(BitmapFactory.decodeByteArray(arrayOfByte1, 0, arrayOfByte1.length), this.mContext);
              if (localBitmap == null) {
                continue;
              }
              localSQLiteStatement.bindLong(1, l);
              byte[] arrayOfByte2 = ItemInfo.flattenBitmap(localBitmap);
              if (arrayOfByte2 != null)
              {
                localSQLiteStatement.bindBlob(2, arrayOfByte2);
                localSQLiteStatement.execute();
              }
              localBitmap.recycle();
            }
            catch (Exception localException)
            {
              if (i == 0) {
                Log.e("Launcher.LauncherProvider", "Failed normalizing icon " + l, localException);
              } else {
                Log.e("Launcher.LauncherProvider", "Also failed normalizing icon " + l);
              }
            }
          }
          else
          {
            i = 1;
          }
        }
        catch (SQLException localSQLException)
        {
          Log.w("Launcher.LauncherProvider", "Problem while allocating appWidgetIds for existing widgets", localSQLException);
          return;
          paramSQLiteDatabase.setTransactionSuccessful();
          return;
        }
        finally
        {
          paramSQLiteDatabase.endTransaction();
          if (localSQLiteStatement != null) {
            localSQLiteStatement.close();
          }
          if (localCursor != null) {
            localCursor.close();
          }
        }
      }
    }
    
    private void sendAppWidgetResetNotify()
    {
      this.mContext.getContentResolver().notifyChange(LauncherProvider.CONTENT_APPWIDGET_RESET_URI, null);
    }
    
    private void setFlagEmptyDbCreated()
    {
      String str = LauncherAppState.getSharedPreferencesKey();
      SharedPreferences.Editor localEditor = this.mContext.getSharedPreferences(str, 0).edit();
      localEditor.putBoolean("EMPTY_DATABASE_CREATED", true);
      localEditor.putBoolean("UPGRADED_FROM_OLD_DATABASE", false);
      localEditor.commit();
    }
    
    private void setFlagJustLoadedOldDb()
    {
      String str = LauncherAppState.getSharedPreferencesKey();
      SharedPreferences.Editor localEditor = this.mContext.getSharedPreferences(str, 0).edit();
      localEditor.putBoolean("UPGRADED_FROM_OLD_DATABASE", true);
      localEditor.putBoolean("EMPTY_DATABASE_CREATED", false);
      localEditor.commit();
    }
    
    private boolean updateContactsShortcuts(SQLiteDatabase paramSQLiteDatabase)
    {
      String str1 = LauncherProvider.buildOrWhereString("itemType", new int[] { 1 });
      Object localObject1 = null;
      paramSQLiteDatabase.beginTransaction();
      try
      {
        Cursor localCursor = paramSQLiteDatabase.query("favorites", new String[] { "_id", "intent" }, str1, null, null, null, null);
        localObject1 = localCursor;
        if (localObject1 != null) {
          break label69;
        }
      }
      catch (SQLException localSQLException)
      {
        int i;
        int j;
        long l;
        String str2;
        Intent localIntent1;
        Uri localUri;
        String str3;
        Intent localIntent2;
        ContentValues localContentValues;
        break label89;
      }
      finally
      {
        paramSQLiteDatabase.endTransaction();
        if (localObject1 == null) {
          break label401;
        }
        localObject1.close();
      }
      return false;
      label69:
      i = localObject1.getColumnIndex("_id");
      j = localObject1.getColumnIndex("intent");
      label89:
      do
      {
        if (!localObject1.moveToNext()) {
          break;
        }
        l = localObject1.getLong(i);
        str2 = localObject1.getString(j);
      } while (str2 == null);
      label401:
      paramSQLiteDatabase.setTransactionSuccessful();
      paramSQLiteDatabase.endTransaction();
      if (localObject1 != null) {
        localObject1.close();
      }
      return true;
    }
    
    private long upgradeLauncherDb_permuteScreens(long paramLong)
    {
      if (paramLong >= 2L) {
        return paramLong - 2L;
      }
      return 3L + paramLong;
    }
    
    public long generateNewItemId()
    {
      if (this.mMaxItemId < 0L) {
        throw new RuntimeException("Error: max item id was not initialized");
      }
      this.mMaxItemId = (1L + this.mMaxItemId);
      return this.mMaxItemId;
    }
    
    public long generateNewScreenId()
    {
      if (this.mMaxScreenId < 0L) {
        throw new RuntimeException("Error: max screen id was not initialized");
      }
      this.mMaxScreenId = (1L + this.mMaxScreenId);
      return this.mMaxScreenId;
    }
    
    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      this.mMaxItemId = 1L;
      this.mMaxScreenId = 0L;
      paramSQLiteDatabase.execSQL("CREATE TABLE favorites (_id INTEGER PRIMARY KEY,title TEXT,intent TEXT,container INTEGER,screen INTEGER,cellX INTEGER,cellY INTEGER,spanX INTEGER,spanY INTEGER,itemType INTEGER,appWidgetId INTEGER NOT NULL DEFAULT -1,isShortcut INTEGER,iconType INTEGER,iconPackage TEXT,iconResource TEXT,icon BLOB,uri TEXT,displayMode INTEGER,appWidgetProvider TEXT,modified INTEGER NOT NULL DEFAULT 0);");
      addWorkspacesTable(paramSQLiteDatabase);
      if (this.mAppWidgetHost != null)
      {
        this.mAppWidgetHost.deleteHost();
        sendAppWidgetResetNotify();
      }
      LauncherProvider.ContentValuesCallback local1 = new LauncherProvider.ContentValuesCallback()
      {
        public void onRow(ContentValues paramAnonymousContentValues)
        {
          if (paramAnonymousContentValues.getAsInteger("container").intValue() == -100)
          {
            int i = paramAnonymousContentValues.getAsInteger("screen").intValue();
            paramAnonymousContentValues.put("screen", Integer.valueOf((int)LauncherProvider.DatabaseHelper.this.upgradeLauncherDb_permuteScreens(i)));
          }
        }
      };
      if ((!convertDatabase(paramSQLiteDatabase, Uri.parse("content://settings/old_favorites?notify=true"), local1, true)) && (!convertDatabase(paramSQLiteDatabase, LauncherSettings.Favorites.OLD_CONTENT_URI, local1, false)))
      {
        setFlagEmptyDbCreated();
        return;
      }
      setFlagJustLoadedOldDb();
    }
    
    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      int i = paramInt1;
      if (i < 3) {
        paramSQLiteDatabase.beginTransaction();
      }
      try
      {
        paramSQLiteDatabase.execSQL("ALTER TABLE favorites ADD COLUMN appWidgetId INTEGER NOT NULL DEFAULT -1;");
        paramSQLiteDatabase.setTransactionSuccessful();
        i = 3;
      }
      catch (SQLException localSQLException4)
      {
        for (;;)
        {
          Log.e("Launcher.LauncherProvider", localSQLException4.getMessage(), localSQLException4);
          paramSQLiteDatabase.endTransaction();
        }
      }
      finally
      {
        paramSQLiteDatabase.endTransaction();
      }
      if (i == 3) {
        convertWidgets(paramSQLiteDatabase);
      }
      if (i < 4) {
        i = 4;
      }
      if (i < 6) {
        paramSQLiteDatabase.beginTransaction();
      }
      try
      {
        paramSQLiteDatabase.execSQL("UPDATE favorites SET screen=(screen + 1);");
        paramSQLiteDatabase.setTransactionSuccessful();
      }
      catch (SQLException localSQLException3)
      {
        for (;;)
        {
          Log.e("Launcher.LauncherProvider", localSQLException3.getMessage(), localSQLException3);
          paramSQLiteDatabase.endTransaction();
        }
      }
      finally
      {
        paramSQLiteDatabase.endTransaction();
      }
      if (updateContactsShortcuts(paramSQLiteDatabase)) {
        i = 6;
      }
      if (i < 7)
      {
        convertWidgets(paramSQLiteDatabase);
        i = 7;
      }
      if (i < 8)
      {
        normalizeIcons(paramSQLiteDatabase);
        i = 8;
      }
      if (i < 9)
      {
        if (this.mMaxItemId == -1L) {
          this.mMaxItemId = initializeMaxItemId(paramSQLiteDatabase);
        }
        loadFavorites(paramSQLiteDatabase, 2131099677);
        i = 9;
      }
      if (i < 12)
      {
        updateContactsShortcuts(paramSQLiteDatabase);
        i = 12;
      }
      if (i < 13)
      {
        this.mMaxScreenId = 0L;
        LauncherProvider.access$402(true);
        addWorkspacesTable(paramSQLiteDatabase);
        i = 13;
      }
      if (i < 14) {
        paramSQLiteDatabase.beginTransaction();
      }
      try
      {
        paramSQLiteDatabase.execSQL("ALTER TABLE favorites ADD COLUMN appWidgetProvider TEXT;");
        paramSQLiteDatabase.setTransactionSuccessful();
        i = 14;
      }
      catch (SQLException localSQLException2)
      {
        for (;;)
        {
          Log.e("Launcher.LauncherProvider", localSQLException2.getMessage(), localSQLException2);
          paramSQLiteDatabase.endTransaction();
        }
      }
      finally
      {
        paramSQLiteDatabase.endTransaction();
      }
      if (i < 15) {
        paramSQLiteDatabase.beginTransaction();
      }
      try
      {
        paramSQLiteDatabase.execSQL("ALTER TABLE favorites ADD COLUMN modified INTEGER NOT NULL DEFAULT 0;");
        paramSQLiteDatabase.execSQL("ALTER TABLE workspaceScreens ADD COLUMN modified INTEGER NOT NULL DEFAULT 0;");
        paramSQLiteDatabase.setTransactionSuccessful();
        i = 15;
      }
      catch (SQLException localSQLException1)
      {
        for (;;)
        {
          Log.e("Launcher.LauncherProvider", localSQLException1.getMessage(), localSQLException1);
          paramSQLiteDatabase.endTransaction();
        }
      }
      finally
      {
        paramSQLiteDatabase.endTransaction();
      }
      if (i != 15)
      {
        Log.w("Launcher.LauncherProvider", "Destroying all old data.");
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS favorites");
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS workspaceScreens");
        onCreate(paramSQLiteDatabase);
      }
    }
    
    public void updateMaxItemId(long paramLong)
    {
      this.mMaxItemId = (1L + paramLong);
    }
    
    public void updateMaxScreenId(long paramLong)
    {
      this.mMaxScreenId = paramLong;
    }
  }
  
  static class SqlArguments
  {
    public final String[] args;
    public final String table;
    public final String where;
    
    SqlArguments(Uri paramUri)
    {
      if (paramUri.getPathSegments().size() == 1)
      {
        this.table = ((String)paramUri.getPathSegments().get(0));
        this.where = null;
        this.args = null;
        return;
      }
      throw new IllegalArgumentException("Invalid URI: " + paramUri);
    }
    
    SqlArguments(Uri paramUri, String paramString, String[] paramArrayOfString)
    {
      if (paramUri.getPathSegments().size() == 1)
      {
        this.table = ((String)paramUri.getPathSegments().get(0));
        this.where = paramString;
        this.args = paramArrayOfString;
        return;
      }
      if (paramUri.getPathSegments().size() != 2) {
        throw new IllegalArgumentException("Invalid URI: " + paramUri);
      }
      if (!TextUtils.isEmpty(paramString)) {
        throw new UnsupportedOperationException("WHERE clause not supported: " + paramUri);
      }
      this.table = ((String)paramUri.getPathSegments().get(0));
      this.where = ("_id=" + ContentUris.parseId(paramUri));
      this.args = null;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LauncherProvider
 * JD-Core Version:    0.7.0.1
 */