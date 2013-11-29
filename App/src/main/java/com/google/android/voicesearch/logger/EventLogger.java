package com.google.android.voicesearch.logger;

import android.util.SparseArray;
import com.google.android.search.core.EventLoggerStore;
import com.google.android.voicesearch.logger.store.EventLoggerStores;
import com.google.common.base.Preconditions;

public final class EventLogger
{
  private static volatile EventLoggerStore sEventLoggerStore;
  private static SparseArray<Boolean> sOneOffEvents = new SparseArray();
  
  public static EventLoggerStore getStore()
  {
    return sEventLoggerStore;
  }
  
  public static void init()
  {
    setStore(EventLoggerStores.createEventStore());
  }
  
  public static void logTextSearchStart(String paramString)
  {
    recordSpeechEvent(9);
    recordSpeechEvent(3, paramString);
    recordClientEvent(19);
  }
  
  private static void record(int paramInt1, int paramInt2, int paramInt3, Object paramObject, boolean paramBoolean)
  {
    boolean bool1 = true;
    boolean bool2;
    boolean bool3;
    if ((0xFFFFFFF & paramInt1) == 0)
    {
      bool2 = bool1;
      Preconditions.checkArgument(bool2);
      if ((0xF0FFFFFF & paramInt2) != 0) {
        break label74;
      }
      bool3 = bool1;
      label30:
      Preconditions.checkArgument(bool3);
      if ((0xFF000000 & paramInt3) != 0) {
        break label80;
      }
    }
    int i;
    for (;;)
    {
      Preconditions.checkArgument(bool1);
      i = paramInt3 | paramInt1 | paramInt2;
      if ((!paramBoolean) || (shouldLog(i))) {
        break label86;
      }
      return;
      bool2 = false;
      break;
      label74:
      bool3 = false;
      break label30;
      label80:
      bool1 = false;
    }
    label86:
    sEventLoggerStore.recordEvent(i, paramObject);
  }
  
  public static void recordBreakdownEvent(int paramInt)
  {
    record(1342177280, 0, paramInt, null, false);
  }
  
  public static void recordBreakdownEvent(int paramInt, Object paramObject)
  {
    record(1342177280, 0, paramInt, paramObject, false);
  }
  
  public static void recordClientEvent(int paramInt)
  {
    recordClientEvent(paramInt, null);
  }
  
  public static void recordClientEvent(int paramInt, Object paramObject)
  {
    record(268435456, 0, paramInt, paramObject, false);
  }
  
  public static void recordClientEventWithSource(int paramInt1, int paramInt2, Object paramObject)
  {
    record(268435456, paramInt2, paramInt1, paramObject, false);
  }
  
  public static void recordLatencyStart(int paramInt)
  {
    record(1073741824, 0, paramInt, null, false);
  }
  
  public static void recordOneOffBreakdownEvent(int paramInt)
  {
    record(1342177280, 0, paramInt, null, true);
  }
  
  public static void recordSpeechEvent(int paramInt)
  {
    recordSpeechEvent(paramInt, null);
  }
  
  public static void recordSpeechEvent(int paramInt, Object paramObject)
  {
    record(536870912, 0, paramInt, paramObject, false);
  }
  
  public static void resetOneOff()
  {
    sOneOffEvents.clear();
  }
  
  public static void setStore(EventLoggerStore paramEventLoggerStore)
  {
    sEventLoggerStore = (EventLoggerStore)Preconditions.checkNotNull(paramEventLoggerStore);
  }
  
  /* Error */
  private static boolean shouldLog(int paramInt)
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: getstatic 18	com/google/android/voicesearch/logger/EventLogger:sOneOffEvents	Landroid/util/SparseArray;
    //   6: iload_0
    //   7: invokevirtual 89	android/util/SparseArray:get	(I)Ljava/lang/Object;
    //   10: astore_2
    //   11: aload_2
    //   12: ifnull +10 -> 22
    //   15: iconst_0
    //   16: istore_3
    //   17: ldc 2
    //   19: monitorexit
    //   20: iload_3
    //   21: ireturn
    //   22: getstatic 18	com/google/android/voicesearch/logger/EventLogger:sOneOffEvents	Landroid/util/SparseArray;
    //   25: iload_0
    //   26: getstatic 95	java/lang/Boolean:TRUE	Ljava/lang/Boolean;
    //   29: invokevirtual 98	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   32: iconst_1
    //   33: istore_3
    //   34: goto -17 -> 17
    //   37: astore_1
    //   38: ldc 2
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	43	0	paramInt	int
    //   37	5	1	localObject1	Object
    //   10	2	2	localObject2	Object
    //   16	18	3	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   3	11	37	finally
    //   22	32	37	finally
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.logger.EventLogger
 * JD-Core Version:    0.7.0.1
 */