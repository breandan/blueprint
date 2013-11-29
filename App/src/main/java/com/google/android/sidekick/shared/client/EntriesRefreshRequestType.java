package com.google.android.sidekick.shared.client;

public class EntriesRefreshRequestType
{
  public static boolean isFullRefresh(int paramInt)
  {
    return (paramInt == 0) || (paramInt == 2) || (paramInt == 3) || (paramInt == 1);
  }
  
  public static boolean isIncremental(int paramInt)
  {
    return paramInt == 4;
  }
  
  public static boolean isMore(int paramInt)
  {
    return (paramInt == 3) || (paramInt == 4);
  }
  
  public static boolean isUserInitiated(int paramInt)
  {
    return (paramInt == 2) || (paramInt == 3) || (paramInt == 4);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.EntriesRefreshRequestType
 * JD-Core Version:    0.7.0.1
 */