package com.google.common.collect;

import com.google.common.base.Objects;

public abstract class GenericMapMaker<K0, V0>
{
  MapMaker.RemovalListener<K0, V0> removalListener;
  
  <K extends K0, V extends V0> MapMaker.RemovalListener<K, V> getRemovalListener()
  {
    return (MapMaker.RemovalListener)Objects.firstNonNull(this.removalListener, NullListener.INSTANCE);
  }
  
  static enum NullListener
    implements MapMaker.RemovalListener<Object, Object>
  {
    static
    {
      NullListener[] arrayOfNullListener = new NullListener[1];
      arrayOfNullListener[0] = INSTANCE;
      $VALUES = arrayOfNullListener;
    }
    
    private NullListener() {}
    
    public void onRemoval(MapMaker.RemovalNotification<Object, Object> paramRemovalNotification) {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.GenericMapMaker
 * JD-Core Version:    0.7.0.1
 */