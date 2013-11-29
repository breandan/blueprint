package com.google.android.search.core.state;

import com.google.android.velvet.ActionData;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;

public class LoggingState
{
  private final Map<ActionData, Integer> mFlags = Maps.newHashMap();
  
  private boolean setFlags(ActionData paramActionData, int paramInt)
  {
    boolean bool = true;
    for (;;)
    {
      try
      {
        Integer localInteger = (Integer)this.mFlags.get(paramActionData);
        if (localInteger != null)
        {
          int i = localInteger.intValue();
          int j = i | paramInt;
          if (j != i)
          {
            this.mFlags.put(paramActionData, Integer.valueOf(j));
            return bool;
          }
        }
        else
        {
          this.mFlags.put(paramActionData, Integer.valueOf(paramInt));
          continue;
        }
        bool = false;
      }
      finally {}
    }
  }
  
  public void setGwsLoggableEvent(ActionData paramActionData, int paramInt)
  {
    boolean bool1 = true;
    boolean bool2;
    if (paramActionData.isGwsLoggable())
    {
      if ((0xFF00 & paramInt) == 0) {
        break label43;
      }
      bool2 = bool1;
      Preconditions.checkArgument(bool2);
      if ((0xFFFF00FF & paramInt) != 0) {
        break label49;
      }
    }
    for (;;)
    {
      Preconditions.checkArgument(bool1);
      setFlags(paramActionData, paramInt);
      return;
      label43:
      bool2 = false;
      break;
      label49:
      bool1 = false;
    }
  }
  
  public void setPumpkinLoggableEvent(ActionData paramActionData, int paramInt)
  {
    if (paramInt == 65536) {
      setFlags(paramActionData, paramInt);
    }
  }
  
  public void suppressGwsLoggableEvent(ActionData paramActionData, int paramInt)
  {
    boolean bool1 = true;
    boolean bool2;
    if (paramActionData.isGwsLoggable())
    {
      if ((paramInt & 0xFF00) == 0) {
        break label49;
      }
      bool2 = bool1;
      Preconditions.checkArgument(bool2);
      if ((0xFFFF00FF & paramInt) != 0) {
        break label55;
      }
    }
    for (;;)
    {
      Preconditions.checkArgument(bool1);
      setFlags(paramActionData, (paramInt & 0xFF00) << 12);
      return;
      label49:
      bool2 = false;
      break;
      label55:
      bool1 = false;
    }
  }
  
  public int takeGwsUnloggedEvents(ActionData paramActionData)
  {
    if (this.mFlags.containsKey(paramActionData))
    {
      int i = ((Integer)this.mFlags.get(paramActionData)).intValue();
      int j = 0xFF00 & i >>> 12;
      int k = i & 0xFF00 & (j ^ 0xFFFFFFFF);
      setFlags(paramActionData, (i & 0xFF00) << 12);
      return k;
    }
    return 0;
  }
  
  public int takePumpkinUnloggedEvents(ActionData paramActionData)
  {
    if (this.mFlags.containsKey(paramActionData))
    {
      int i = ((Integer)this.mFlags.get(paramActionData)).intValue();
      int j = 0x10000 & i >>> 12;
      int k = i & 0x10000 & (j ^ 0xFFFFFFFF);
      setFlags(paramActionData, (i & 0x10000) << 12);
      return k;
    }
    return 0;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.state.LoggingState
 * JD-Core Version:    0.7.0.1
 */