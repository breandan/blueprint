package com.google.android.shared.util;

import com.google.common.base.Preconditions;
import java.util.concurrent.atomic.AtomicBoolean;

public class CachedConsumer<A>
  extends CachedLater<A>
  implements Consumer<A>
{
  private final AtomicBoolean mStored = new AtomicBoolean();
  
  public boolean consume(A paramA)
  {
    Preconditions.checkState(this.mStored.compareAndSet(false, true));
    store(paramA);
    return true;
  }
  
  protected void create() {}
  
  public String toString()
  {
    if (haveNow()) {
      return "CachedConsumer{have now, " + getNow() + "}";
    }
    return "CachedConsumer{pending}";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.CachedConsumer
 * JD-Core Version:    0.7.0.1
 */