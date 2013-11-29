package com.google.android.shared.util;

public class NoOpConsumer<A>
  implements Consumer<A>
{
  public boolean consume(A paramA)
  {
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.NoOpConsumer
 * JD-Core Version:    0.7.0.1
 */