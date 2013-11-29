package com.google.android.shared.util;

public abstract class NonCancellableNamedTask
  implements NamedTask
{
  public void cancelExecution()
  {
    throw new UnsupportedOperationException("NonCancellableNamedTask can't be canceled.");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.NonCancellableNamedTask
 * JD-Core Version:    0.7.0.1
 */