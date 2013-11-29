package com.squareup.okhttp.internal.spdy;

final class Settings
{
  private int persistValue;
  private int persisted;
  private int set;
  private final int[] values = new int[9];
  
  int flags(int paramInt)
  {
    boolean bool = isPersisted(paramInt);
    int i = 0;
    if (bool) {
      i = 0x0 | 0x2;
    }
    if (persistValue(paramInt)) {
      i |= 0x1;
    }
    return i;
  }
  
  int get(int paramInt)
  {
    return this.values[paramInt];
  }
  
  int getInitialWindowSize(int paramInt)
  {
    if ((0x80 & this.set) != 0) {
      paramInt = this.values[7];
    }
    return paramInt;
  }
  
  boolean isPersisted(int paramInt)
  {
    return (1 << paramInt & this.persisted) != 0;
  }
  
  boolean isSet(int paramInt)
  {
    return (1 << paramInt & this.set) != 0;
  }
  
  void merge(Settings paramSettings)
  {
    int i = 0;
    if (i < 9)
    {
      if (!paramSettings.isSet(i)) {}
      for (;;)
      {
        i++;
        break;
        set(i, paramSettings.flags(i), paramSettings.get(i));
      }
    }
  }
  
  boolean persistValue(int paramInt)
  {
    return (1 << paramInt & this.persistValue) != 0;
  }
  
  void set(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 >= this.values.length) {
      return;
    }
    int i = 1 << paramInt1;
    this.set = (i | this.set);
    if ((paramInt2 & 0x1) != 0)
    {
      this.persistValue = (i | this.persistValue);
      if ((paramInt2 & 0x2) == 0) {
        break label84;
      }
    }
    label84:
    for (this.persisted = (i | this.persisted);; this.persisted &= (i ^ 0xFFFFFFFF))
    {
      this.values[paramInt1] = paramInt3;
      return;
      this.persistValue &= (i ^ 0xFFFFFFFF);
      break;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.squareup.okhttp.internal.spdy.Settings
 * JD-Core Version:    0.7.0.1
 */