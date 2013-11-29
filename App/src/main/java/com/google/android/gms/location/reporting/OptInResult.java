package com.google.android.gms.location.reporting;

public class OptInResult
{
  public static int sanitize(int paramInt)
  {
    switch (paramInt)
    {
    case 7: 
    default: 
      paramInt = 1;
    }
    return paramInt;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.location.reporting.OptInResult
 * JD-Core Version:    0.7.0.1
 */