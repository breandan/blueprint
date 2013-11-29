package com.google.android.shared.util;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SafeDataSetObservable
  extends DataSetObservable
{
  private List<DataSetObserver> observersCopy()
  {
    synchronized (this.mObservers)
    {
      ArrayList localArrayList2 = Lists.newArrayList(this.mObservers);
      return localArrayList2;
    }
  }
  
  public int getObserverCount()
  {
    return this.mObservers.size();
  }
  
  public void notifyChanged()
  {
    Iterator localIterator = observersCopy().iterator();
    while (localIterator.hasNext()) {
      ((DataSetObserver)localIterator.next()).onChanged();
    }
  }
  
  public void notifyInvalidated()
  {
    Iterator localIterator = observersCopy().iterator();
    while (localIterator.hasNext()) {
      ((DataSetObserver)localIterator.next()).onInvalidated();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.SafeDataSetObservable
 * JD-Core Version:    0.7.0.1
 */