package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public final class ThreadFactoryBuilder
{
  private ThreadFactory backingThreadFactory = null;
  private Boolean daemon = null;
  private String nameFormat = null;
  private Integer priority = null;
  private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;
  
  private static ThreadFactory build(ThreadFactoryBuilder paramThreadFactoryBuilder)
  {
    final String str = paramThreadFactoryBuilder.nameFormat;
    final Boolean localBoolean = paramThreadFactoryBuilder.daemon;
    final Integer localInteger = paramThreadFactoryBuilder.priority;
    final Thread.UncaughtExceptionHandler localUncaughtExceptionHandler = paramThreadFactoryBuilder.uncaughtExceptionHandler;
    ThreadFactory localThreadFactory;
    if (paramThreadFactoryBuilder.backingThreadFactory != null)
    {
      localThreadFactory = paramThreadFactoryBuilder.backingThreadFactory;
      if (str == null) {
        break label73;
      }
    }
    label73:
    for (final AtomicLong localAtomicLong = new AtomicLong(0L);; localAtomicLong = null)
    {
      new ThreadFactory()
      {
        public Thread newThread(Runnable paramAnonymousRunnable)
        {
          Thread localThread = this.val$backingThreadFactory.newThread(paramAnonymousRunnable);
          if (str != null)
          {
            String str = str;
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Long.valueOf(localAtomicLong.getAndIncrement());
            localThread.setName(String.format(str, arrayOfObject));
          }
          if (localBoolean != null) {
            localThread.setDaemon(localBoolean.booleanValue());
          }
          if (localInteger != null) {
            localThread.setPriority(localInteger.intValue());
          }
          if (localUncaughtExceptionHandler != null) {
            localThread.setUncaughtExceptionHandler(localUncaughtExceptionHandler);
          }
          return localThread;
        }
      };
      localThreadFactory = Executors.defaultThreadFactory();
      break;
    }
  }
  
  public ThreadFactory build()
  {
    return build(this);
  }
  
  public ThreadFactoryBuilder setNameFormat(String paramString)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(0);
    String.format(paramString, arrayOfObject);
    this.nameFormat = paramString;
    return this;
  }
  
  public ThreadFactoryBuilder setThreadFactory(ThreadFactory paramThreadFactory)
  {
    this.backingThreadFactory = ((ThreadFactory)Preconditions.checkNotNull(paramThreadFactory));
    return this;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.ThreadFactoryBuilder
 * JD-Core Version:    0.7.0.1
 */