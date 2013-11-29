package com.google.common.util.concurrent;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class UncaughtExceptionHandlers
{
  static final class Exiter
    implements Thread.UncaughtExceptionHandler
  {
    private static final Logger logger = Logger.getLogger(Exiter.class.getName());
    private final Runtime runtime;
    
    public void uncaughtException(Thread paramThread, Throwable paramThrowable)
    {
      logger.log(Level.SEVERE, String.format("Caught an exception in %s.  Shutting down.", new Object[] { paramThread }), paramThrowable);
      this.runtime.exit(1);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.UncaughtExceptionHandlers
 * JD-Core Version:    0.7.0.1
 */