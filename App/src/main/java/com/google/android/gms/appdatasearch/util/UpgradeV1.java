package com.google.android.gms.appdatasearch.util;

import android.database.sqlite.SQLiteDatabase;
import java.util.Iterator;
import java.util.Set;

class UpgradeV1
{
  static Set<String> getOldSequenceTables(SQLiteDatabase paramSQLiteDatabase)
  {
    return MasterTableHelper.getTables(paramSQLiteDatabase, new String[] { "_appdatasearch_seqno_table" });
  }
  
  static Set<String> getOldTriggers(SQLiteDatabase paramSQLiteDatabase)
  {
    return MasterTableHelper.getTriggers(paramSQLiteDatabase, new String[] { "_appdatasearch_insert_trigger", "_appdatasearch_delete_trigger", "_appdatasearch_update_trigger" });
  }
  
  public static void upgrade(SQLiteDatabase paramSQLiteDatabase)
  {
    Iterator localIterator1 = getOldSequenceTables(paramSQLiteDatabase).iterator();
    while (localIterator1.hasNext()) {
      MasterTableHelper.dropTable(paramSQLiteDatabase, (String)localIterator1.next());
    }
    Iterator localIterator2 = getOldTriggers(paramSQLiteDatabase).iterator();
    while (localIterator2.hasNext()) {
      MasterTableHelper.dropTrigger(paramSQLiteDatabase, (String)localIterator2.next());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.util.UpgradeV1
 * JD-Core Version:    0.7.0.1
 */