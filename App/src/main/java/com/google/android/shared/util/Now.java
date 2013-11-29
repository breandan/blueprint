package com.google.android.shared.util;

public class Now<C>
  implements CancellableNowOrLater<C>
{
  private final C mValue;
  
  public Now(C paramC)
  {
    this.mValue = paramC;
  }
  
  public static <C> Now<C> returnNull()
  {
    return new Now(null);
  }
  
  public static <C> Now<C> returnThis(C paramC)
  {
    return new Now(paramC);
  }
  
  public void cancelGetLater(Consumer<? super C> paramConsumer) {}
  
  public void getLater(Consumer<? super C> paramConsumer)
  {
    paramConsumer.consume(getNow());
  }
  
  public C getNow()
  {
    return this.mValue;
  }
  
  public boolean haveNow()
  {
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.Now
 * JD-Core Version:    0.7.0.1
 */