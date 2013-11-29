package com.google.android.shared.util;

import java.util.HashMap;
import java.util.concurrent.Future;

public class PerNameExecutor
  implements NamedTaskExecutor
{
  private final Factory<NamedTaskExecutor> mExecutorFactory;
  private HashMap<String, NamedTaskExecutor> mExecutors;
  
  public PerNameExecutor(Factory<NamedTaskExecutor> paramFactory)
  {
    this.mExecutorFactory = paramFactory;
  }
  
  /* Error */
  public void cancelPendingTasks()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 22	com/google/android/shared/util/PerNameExecutor:mExecutors	Ljava/util/HashMap;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnonnull +6 -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: getfield 22	com/google/android/shared/util/PerNameExecutor:mExecutors	Ljava/util/HashMap;
    //   18: invokevirtual 28	java/util/HashMap:values	()Ljava/util/Collection;
    //   21: invokeinterface 34 1 0
    //   26: astore_3
    //   27: aload_3
    //   28: invokeinterface 40 1 0
    //   33: ifeq -22 -> 11
    //   36: aload_3
    //   37: invokeinterface 44 1 0
    //   42: checkcast 6	com/google/android/shared/util/NamedTaskExecutor
    //   45: invokeinterface 46 1 0
    //   50: goto -23 -> 27
    //   53: astore_1
    //   54: aload_0
    //   55: monitorexit
    //   56: aload_1
    //   57: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	58	0	this	PerNameExecutor
    //   53	4	1	localObject	Object
    //   6	2	2	localHashMap	HashMap
    //   26	11	3	localIterator	java.util.Iterator
    // Exception table:
    //   from	to	target	type
    //   2	7	53	finally
    //   14	27	53	finally
    //   27	50	53	finally
  }
  
  public void execute(NamedTask paramNamedTask)
  {
    try
    {
      if (this.mExecutors == null) {
        this.mExecutors = new HashMap();
      }
      String str = paramNamedTask.getName();
      NamedTaskExecutor localNamedTaskExecutor = (NamedTaskExecutor)this.mExecutors.get(str);
      if (localNamedTaskExecutor == null)
      {
        localNamedTaskExecutor = (NamedTaskExecutor)this.mExecutorFactory.create();
        this.mExecutors.put(str, localNamedTaskExecutor);
      }
      localNamedTaskExecutor.execute(paramNamedTask);
      return;
    }
    finally {}
  }
  
  public Future<?> submit(NamedTask paramNamedTask)
  {
    throw new UnsupportedOperationException("submit() method not supported.");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.PerNameExecutor
 * JD-Core Version:    0.7.0.1
 */