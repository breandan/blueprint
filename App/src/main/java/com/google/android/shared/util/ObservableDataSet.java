package com.google.android.shared.util;

import android.database.DataSetObserver;

public abstract interface ObservableDataSet
{
  public abstract void registerDataSetObserver(DataSetObserver paramDataSetObserver);
  
  public abstract void unregisterDataSetObserver(DataSetObserver paramDataSetObserver);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.ObservableDataSet
 * JD-Core Version:    0.7.0.1
 */