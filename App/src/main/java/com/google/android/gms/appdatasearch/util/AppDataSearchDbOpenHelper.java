package com.google.android.gms.appdatasearch.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.google.android.gms.appdatasearch.SyncContentProviderHelper.SyncColumns;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class AppDataSearchDbOpenHelper
  extends SQLiteOpenHelper
  implements AppDataSearchDataManager
{
  private static final Version VERSION = new Version(1);
  private final AppDataSearchDataManager.TableChangeListener mListener;
  private final TableStorageSpec[] mTableStorageSpecs;
  
  public AppDataSearchDbOpenHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt, TableStorageSpec[] paramArrayOfTableStorageSpec, AppDataSearchDataManager.TableChangeListener paramTableChangeListener)
  {
    super(paramContext, paramString, paramCursorFactory, paramInt);
    this.mTableStorageSpecs = copyTableSpec(paramArrayOfTableStorageSpec);
    this.mListener = paramTableChangeListener;
  }
  
  private static TableStorageSpec[] copyTableSpec(TableStorageSpec[] paramArrayOfTableStorageSpec)
  {
    if ((paramArrayOfTableStorageSpec == null) || (paramArrayOfTableStorageSpec.length == 0)) {
      throw new IllegalArgumentException("Must provide at least 1 TableStorageSpec");
    }
    TableStorageSpec[] arrayOfTableStorageSpec = new TableStorageSpec[paramArrayOfTableStorageSpec.length];
    System.arraycopy(paramArrayOfTableStorageSpec, 0, arrayOfTableStorageSpec, 0, paramArrayOfTableStorageSpec.length);
    return arrayOfTableStorageSpec;
  }
  
  private void createSequenceTableAndTriggers(SQLiteDatabase paramSQLiteDatabase, TableStorageSpec paramTableStorageSpec)
  {
    createSequenceTableAndTriggers(paramSQLiteDatabase, paramTableStorageSpec, NamesHelper.getSequenceTableName(paramTableStorageSpec), NamesHelper.getInsertTriggerName(paramTableStorageSpec), NamesHelper.getDeleteTriggerName(paramTableStorageSpec), NamesHelper.getUpdateTriggerName(paramTableStorageSpec));
  }
  
  static void createSequenceTableAndTriggers(SQLiteDatabase paramSQLiteDatabase, TableStorageSpec paramTableStorageSpec, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    String str1 = paramTableStorageSpec.getUriColumn();
    MasterTableHelper.dropTable(paramSQLiteDatabase, paramString1);
    MasterTableHelper.dropTrigger(paramSQLiteDatabase, paramString2);
    MasterTableHelper.dropTrigger(paramSQLiteDatabase, paramString4);
    MasterTableHelper.dropTrigger(paramSQLiteDatabase, paramString3);
    paramSQLiteDatabase.execSQL("CREATE TABLE [" + paramString1 + "] (" + "seqno" + " INTEGER PRIMARY KEY AUTOINCREMENT," + "action_type" + " INTEGER," + "docid" + " INTEGER UNIQUE ON CONFLICT REPLACE" + ")");
    String str2 = "INSERT INTO [" + paramString1 + "]" + "  (" + "action_type" + "," + "docid" + ")" + " VALUES (%s,%s);";
    Object[] arrayOfObject1 = new Object[2];
    arrayOfObject1[0] = Integer.valueOf(0);
    arrayOfObject1[1] = ("new.[" + str1 + "]");
    String str3 = String.format(str2, arrayOfObject1);
    Object[] arrayOfObject2 = new Object[2];
    arrayOfObject2[0] = Integer.valueOf(1);
    arrayOfObject2[1] = ("old.[" + str1 + "]");
    String str4 = String.format(str2, arrayOfObject2);
    String str5 = paramTableStorageSpec.getTableName();
    paramSQLiteDatabase.execSQL("CREATE TRIGGER [" + paramString2 + "]" + " AFTER INSERT ON [" + str5 + "] FOR EACH ROW BEGIN " + str3 + " END");
    paramSQLiteDatabase.execSQL("CREATE TRIGGER [" + paramString3 + "]" + " AFTER DELETE ON [" + str5 + "] FOR EACH ROW BEGIN " + str4 + " END");
    paramSQLiteDatabase.execSQL("CREATE TRIGGER [" + paramString4 + "]" + " AFTER UPDATE ON [" + str5 + "] FOR EACH ROW BEGIN " + str3 + " END");
    paramSQLiteDatabase.execSQL("INSERT INTO [" + paramString1 + "]" + " (" + "action_type" + "," + "docid" + ")" + " SELECT " + 0 + ",[" + str1 + "]" + " FROM [" + str5 + "]");
  }
  
  private void ensureCurrentSequenceTables(SQLiteDatabase paramSQLiteDatabase)
  {
    HashSet localHashSet1 = new HashSet();
    HashSet localHashSet2 = new HashSet();
    for (TableStorageSpec localTableStorageSpec2 : this.mTableStorageSpecs)
    {
      localHashSet1.add(NamesHelper.getSequenceTableName(localTableStorageSpec2));
      localHashSet2.add(NamesHelper.getInsertTriggerName(localTableStorageSpec2));
      localHashSet2.add(NamesHelper.getUpdateTriggerName(localTableStorageSpec2));
      localHashSet2.add(NamesHelper.getDeleteTriggerName(localTableStorageSpec2));
    }
    Set localSet = NamesHelper.getExistingSequenceTables(paramSQLiteDatabase);
    Iterator localIterator1 = localSet.iterator();
    while (localIterator1.hasNext())
    {
      String str2 = (String)localIterator1.next();
      if (!localHashSet1.contains(str2)) {
        MasterTableHelper.dropTable(paramSQLiteDatabase, str2);
      }
    }
    Iterator localIterator2 = NamesHelper.getExistingTriggers(paramSQLiteDatabase).iterator();
    while (localIterator2.hasNext())
    {
      String str1 = (String)localIterator2.next();
      if (!localHashSet2.contains(str1)) {
        MasterTableHelper.dropTrigger(paramSQLiteDatabase, str1);
      }
    }
    for (TableStorageSpec localTableStorageSpec1 : this.mTableStorageSpecs) {
      if (!localSet.contains(NamesHelper.getSequenceTableName(localTableStorageSpec1))) {
        createSequenceTableAndTriggers(paramSQLiteDatabase, localTableStorageSpec1);
      }
    }
  }
  
  public void cleanSequenceTable(TableStorageSpec paramTableStorageSpec, long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
    String str = NamesHelper.getSequenceTableName(paramTableStorageSpec);
    localSQLiteDatabase.beginTransaction();
    try
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = String.valueOf(paramLong);
      localSQLiteDatabase.delete(str, "seqno <= ?", arrayOfString);
      localSQLiteDatabase.setTransactionSuccessful();
      return;
    }
    finally
    {
      localSQLiteDatabase.endTransaction();
    }
  }
  
  protected abstract void doOnCreate(SQLiteDatabase paramSQLiteDatabase);
  
  protected void doOnOpen(SQLiteDatabase paramSQLiteDatabase) {}
  
  protected abstract void doOnUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2);
  
  public long getMaxSeqno(TableStorageSpec paramTableStorageSpec)
  {
    SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
    String str = NamesHelper.getSequenceTableName(paramTableStorageSpec);
    Cursor localCursor = localSQLiteDatabase.rawQuery("SELECT MAX(seqno) FROM [" + str + "]", null);
    long l1 = 0L;
    try
    {
      if (localCursor.moveToNext())
      {
        long l2 = localCursor.getLong(0);
        l1 = l2;
      }
      return l1;
    }
    finally
    {
      localCursor.close();
    }
  }
  
  public TableStorageSpec[] getTableStorageSpecs()
  {
    return this.mTableStorageSpecs;
  }
  
  protected final boolean notifyTableChanged(TableStorageSpec paramTableStorageSpec)
  {
    if (this.mListener != null) {
      return this.mListener.onTableChanged(paramTableStorageSpec);
    }
    return true;
  }
  
  public final void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    doOnCreate(paramSQLiteDatabase);
    VERSION.create(paramSQLiteDatabase);
    TableStorageSpec[] arrayOfTableStorageSpec = this.mTableStorageSpecs;
    int i = arrayOfTableStorageSpec.length;
    for (int j = 0; j < i; j++) {
      createSequenceTableAndTriggers(paramSQLiteDatabase, arrayOfTableStorageSpec[j]);
    }
  }
  
  public final void onOpen(SQLiteDatabase paramSQLiteDatabase)
  {
    doOnOpen(paramSQLiteDatabase);
    if (VERSION.upgradeIfRequired(paramSQLiteDatabase)) {
      ensureCurrentSequenceTables(paramSQLiteDatabase);
    }
  }
  
  public final void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    doOnUpgrade(paramSQLiteDatabase, paramInt1, paramInt2);
    VERSION.upgradeIfRequired(paramSQLiteDatabase);
    ensureCurrentSequenceTables(paramSQLiteDatabase);
  }
  
  public Cursor querySequenceTable(TableStorageSpec paramTableStorageSpec, long paramLong1, long paramLong2)
  {
    String str1 = paramTableStorageSpec.getTableName();
    String str2 = NamesHelper.getSequenceTableName(paramTableStorageSpec);
    String str3 = paramTableStorageSpec.getUriColumn();
    String str4 = "[" + str1 + "].[" + str3 + "]";
    StringBuilder localStringBuilder = new StringBuilder(1024).append("SELECT DISTINCT seqno AS seqno,CASE WHEN [" + str2 + "].[" + "action_type" + "] = '" + 0 + "' AND " + str4 + " IS NOT NULL THEN '" + "add" + "'" + " ELSE '" + "del" + "'" + " END AS " + "action" + "," + "docid" + " AS " + "uri" + ",").append(paramTableStorageSpec.getScoreSpec()).append(" AS doc_score");
    Iterator localIterator = paramTableStorageSpec.getSectionToColumnNameMap().entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      String str5 = (String)localEntry.getValue();
      String str6 = SyncContentProviderHelper.SyncColumns.getColumnNameForSection((String)localEntry.getKey());
      localStringBuilder.append(",[").append(str1).append("].[").append(str5).append("] AS ").append(str6);
    }
    localStringBuilder.append(" FROM [").append(str2).append("]").append(" LEFT OUTER JOIN [").append(str1).append("]").append(" ON [").append(str2).append("].[").append("docid").append("]").append(" = ").append(str4).append(" WHERE ").append("seqno").append(" > ").append(paramLong1);
    if (paramTableStorageSpec.getCondition() != null)
    {
      localStringBuilder.append(" AND (").append(str4).append(" IS NULL");
      localStringBuilder.append(" OR (").append(paramTableStorageSpec.getCondition()).append("))");
    }
    localStringBuilder.append(" ORDER BY ").append("seqno").append(" LIMIT ").append(paramLong2);
    return getReadableDatabase().rawQuery(localStringBuilder.toString(), null);
  }
  
  public Cursor queryTagsTable(TableStorageSpec paramTableStorageSpec, long paramLong1, long paramLong2)
  {
    return new MatrixCursor((String[])SyncContentProviderHelper.SyncColumns.TAGS_QUERY_COLUMN_NAMES.toArray(new String[SyncContentProviderHelper.SyncColumns.TAGS_QUERY_COLUMN_NAMES.size()]));
  }
  
  public void recreateSequenceTable(TableStorageSpec paramTableStorageSpec)
  {
    SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
    localSQLiteDatabase.beginTransaction();
    try
    {
      createSequenceTableAndTriggers(localSQLiteDatabase, paramTableStorageSpec);
      localSQLiteDatabase.setTransactionSuccessful();
      return;
    }
    finally
    {
      localSQLiteDatabase.endTransaction();
    }
  }
  
  static class NamesHelper
  {
    public static String getDeleteTriggerName(TableStorageSpec paramTableStorageSpec)
    {
      return paramTableStorageSpec.getCorpusName() + "_delete_trigger_appdatasearch";
    }
    
    public static Set<String> getExistingSequenceTables(SQLiteDatabase paramSQLiteDatabase)
    {
      return MasterTableHelper.getTables(paramSQLiteDatabase, new String[] { "_seqno_table_appdatasearch" });
    }
    
    public static Set<String> getExistingTriggers(SQLiteDatabase paramSQLiteDatabase)
    {
      return MasterTableHelper.getTriggers(paramSQLiteDatabase, new String[] { "_trigger_appdatasearch" });
    }
    
    public static String getInsertTriggerName(TableStorageSpec paramTableStorageSpec)
    {
      return paramTableStorageSpec.getCorpusName() + "_insert_trigger_appdatasearch";
    }
    
    public static String getSequenceTableName(TableStorageSpec paramTableStorageSpec)
    {
      return paramTableStorageSpec.getCorpusName() + "_seqno_table_appdatasearch";
    }
    
    public static String getUpdateTriggerName(TableStorageSpec paramTableStorageSpec)
    {
      return paramTableStorageSpec.getCorpusName() + "_update_trigger_appdatasearch";
    }
  }
  
  static class Version
  {
    private final int mVersion;
    
    public Version(int paramInt)
    {
      this.mVersion = paramInt;
    }
    
    static void createVersionTable(SQLiteDatabase paramSQLiteDatabase, int paramInt)
    {
      paramSQLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS [%s] ([%s] INTEGER)", new Object[] { "version_appdatasearch", "version" }));
      paramSQLiteDatabase.delete("version_appdatasearch", null, null);
      ContentValues localContentValues = new ContentValues(1);
      localContentValues.put("version", Integer.valueOf(paramInt));
      paramSQLiteDatabase.insert("version_appdatasearch", null, localContentValues);
    }
    
    static int getVersion(SQLiteDatabase paramSQLiteDatabase)
    {
      if (!MasterTableHelper.tableExists(paramSQLiteDatabase, "version_appdatasearch"))
      {
        createVersionTable(paramSQLiteDatabase, 0);
        return 0;
      }
      Cursor localCursor = paramSQLiteDatabase.query("version_appdatasearch", new String[] { "version" }, null, null, null, null, null);
      if (localCursor == null)
      {
        Log.w("AppDataSearchHelper", "Empty version table.");
        return 0;
      }
      try
      {
        if (!localCursor.moveToNext())
        {
          Log.w("AppDataSearchHelper", "Empty version table.");
          return 0;
        }
        int i = localCursor.getInt(localCursor.getColumnIndex("version"));
        return i;
      }
      finally
      {
        localCursor.close();
      }
    }
    
    static void updateVersion(SQLiteDatabase paramSQLiteDatabase, int paramInt)
    {
      ContentValues localContentValues = new ContentValues(1);
      localContentValues.put("version", Integer.valueOf(paramInt));
      paramSQLiteDatabase.update("version_appdatasearch", localContentValues, null, null);
    }
    
    public void create(SQLiteDatabase paramSQLiteDatabase)
    {
      createVersionTable(paramSQLiteDatabase, this.mVersion);
    }
    
    public boolean upgradeIfRequired(SQLiteDatabase paramSQLiteDatabase)
    {
      int i = getVersion(paramSQLiteDatabase);
      if (i < this.mVersion)
      {
        if (i < 1) {
          UpgradeV1.upgrade(paramSQLiteDatabase);
        }
        updateVersion(paramSQLiteDatabase, this.mVersion);
        return true;
      }
      return false;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.util.AppDataSearchDbOpenHelper
 * JD-Core Version:    0.7.0.1
 */