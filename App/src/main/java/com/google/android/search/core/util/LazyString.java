package com.google.android.search.core.util;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class LazyString
{
  private final Supplier<String> mToStringSupplier;
  
  public LazyString(String paramString, Object... paramVarArgs)
  {
    this.mToStringSupplier = Suppliers.memoize(new ToStringSupplier(paramString, paramVarArgs));
  }
  
  public String toString()
  {
    return (String)this.mToStringSupplier.get();
  }
  
  private static class ToStringSupplier
    implements Supplier<String>
  {
    private final Object[] mArguments;
    private final String mFormat;
    
    public ToStringSupplier(String paramString, Object[] paramArrayOfObject)
    {
      this.mFormat = paramString;
      this.mArguments = paramArrayOfObject;
    }
    
    public String get()
    {
      return String.format(this.mFormat, this.mArguments);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.LazyString
 * JD-Core Version:    0.7.0.1
 */