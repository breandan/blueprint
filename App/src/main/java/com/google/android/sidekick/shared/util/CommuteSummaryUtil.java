package com.google.android.sidekick.shared.util;

import com.google.geo.sidekick.Sidekick.CommuteSummary;
import javax.annotation.Nullable;

public class CommuteSummaryUtil
{
  @Nullable
  public static Integer getTravelMode(@Nullable Sidekick.CommuteSummary paramCommuteSummary)
  {
    if ((paramCommuteSummary != null) && (paramCommuteSummary.hasTravelMode()) && (paramCommuteSummary.getTravelMode() != -1)) {
      return Integer.valueOf(paramCommuteSummary.getTravelMode());
    }
    return null;
  }
  
  public static int getTravelModeSetting(@Nullable Sidekick.CommuteSummary paramCommuteSummary)
  {
    if ((paramCommuteSummary != null) && (paramCommuteSummary.hasTravelModeSetting()) && (paramCommuteSummary.getTravelModeSetting() != -1)) {
      return paramCommuteSummary.getTravelModeSetting();
    }
    return 2;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.CommuteSummaryUtil
 * JD-Core Version:    0.7.0.1
 */