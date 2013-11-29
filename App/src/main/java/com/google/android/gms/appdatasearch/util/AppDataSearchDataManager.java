package com.google.android.gms.appdatasearch.util;

import android.database.Cursor;

public abstract interface AppDataSearchDataManager
{
  public abstract void cleanSequenceTable(TableStorageSpec paramTableStorageSpec, long paramLong);
  
  public abstract long getMaxSeqno(TableStorageSpec paramTableStorageSpec);
  
  public abstract TableStorageSpec[] getTableStorageSpecs();
  
  public abstract Cursor querySequenceTable(TableStorageSpec paramTableStorageSpec, long paramLong1, long paramLong2);
  
  public abstract Cursor queryTagsTable(TableStorageSpec paramTableStorageSpec, long paramLong1, long paramLong2);
  
  public abstract void recreateSequenceTable(TableStorageSpec paramTableStorageSpec);
  
  public static abstract interface TableChangeListener
  {
    public abstract boolean onTableChanged(TableStorageSpec paramTableStorageSpec);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.util.AppDataSearchDataManager
 * JD-Core Version:    0.7.0.1
 */