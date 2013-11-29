package com.google.android.search.shared.ui;

import android.view.View;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;

public abstract class PendingViewDismiss
{
  private final ImmutableList<View> mDismissedViews;
  private boolean mExpired = false;
  private boolean mIntercepted = false;
  private final ArrayList<Observer> mObservers;
  
  public PendingViewDismiss(View paramView)
  {
    this(ImmutableList.of(paramView));
  }
  
  private PendingViewDismiss(ImmutableList<View> paramImmutableList)
  {
    this.mDismissedViews = paramImmutableList;
    this.mObservers = Lists.newArrayList();
  }
  
  public PendingViewDismiss(Collection<View> paramCollection)
  {
    this(ImmutableList.copyOf(paramCollection));
  }
  
  public void addObserver(Observer paramObserver)
  {
    try
    {
      this.mObservers.add(paramObserver);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  /* Error */
  public void commit()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 29	com/google/android/search/shared/ui/PendingViewDismiss:mExpired	Z
    //   6: istore_2
    //   7: iload_2
    //   8: ifeq +6 -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: invokevirtual 58	com/google/android/search/shared/ui/PendingViewDismiss:doCommit	()V
    //   18: aload_0
    //   19: iconst_1
    //   20: putfield 29	com/google/android/search/shared/ui/PendingViewDismiss:mExpired	Z
    //   23: aload_0
    //   24: getfield 41	com/google/android/search/shared/ui/PendingViewDismiss:mObservers	Ljava/util/ArrayList;
    //   27: invokevirtual 62	java/util/ArrayList:iterator	()Ljava/util/Iterator;
    //   30: astore_3
    //   31: aload_3
    //   32: invokeinterface 68 1 0
    //   37: ifeq -26 -> 11
    //   40: aload_3
    //   41: invokeinterface 72 1 0
    //   46: checkcast 74	com/google/android/search/shared/ui/PendingViewDismiss$Observer
    //   49: aload_0
    //   50: invokeinterface 78 2 0
    //   55: goto -24 -> 31
    //   58: astore_1
    //   59: aload_0
    //   60: monitorexit
    //   61: aload_1
    //   62: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	63	0	this	PendingViewDismiss
    //   58	4	1	localObject	Object
    //   6	2	2	bool	boolean
    //   30	11	3	localIterator	java.util.Iterator
    // Exception table:
    //   from	to	target	type
    //   2	7	58	finally
    //   14	31	58	finally
    //   31	55	58	finally
  }
  
  protected abstract void doCommit();
  
  protected abstract void doRestore();
  
  public ImmutableList<View> getDismissedViews()
  {
    return this.mDismissedViews;
  }
  
  public void intercept()
  {
    this.mIntercepted = true;
  }
  
  public boolean isExpired()
  {
    return this.mExpired;
  }
  
  public boolean isIntercepted()
  {
    return this.mIntercepted;
  }
  
  /* Error */
  public void restore()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 29	com/google/android/search/shared/ui/PendingViewDismiss:mExpired	Z
    //   6: istore_2
    //   7: iload_2
    //   8: ifeq +6 -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: invokevirtual 87	com/google/android/search/shared/ui/PendingViewDismiss:doRestore	()V
    //   18: aload_0
    //   19: iconst_1
    //   20: putfield 29	com/google/android/search/shared/ui/PendingViewDismiss:mExpired	Z
    //   23: aload_0
    //   24: getfield 41	com/google/android/search/shared/ui/PendingViewDismiss:mObservers	Ljava/util/ArrayList;
    //   27: invokevirtual 62	java/util/ArrayList:iterator	()Ljava/util/Iterator;
    //   30: astore_3
    //   31: aload_3
    //   32: invokeinterface 68 1 0
    //   37: ifeq -26 -> 11
    //   40: aload_3
    //   41: invokeinterface 72 1 0
    //   46: checkcast 74	com/google/android/search/shared/ui/PendingViewDismiss$Observer
    //   49: aload_0
    //   50: invokeinterface 90 2 0
    //   55: goto -24 -> 31
    //   58: astore_1
    //   59: aload_0
    //   60: monitorexit
    //   61: aload_1
    //   62: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	63	0	this	PendingViewDismiss
    //   58	4	1	localObject	Object
    //   6	2	2	bool	boolean
    //   30	11	3	localIterator	java.util.Iterator
    // Exception table:
    //   from	to	target	type
    //   2	7	58	finally
    //   14	31	58	finally
    //   31	55	58	finally
  }
  
  public static abstract interface Observer
  {
    public abstract void onCommit(PendingViewDismiss paramPendingViewDismiss);
    
    public abstract void onRestore(PendingViewDismiss paramPendingViewDismiss);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.PendingViewDismiss
 * JD-Core Version:    0.7.0.1
 */