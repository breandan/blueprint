package com.google.android.voicesearch.fragments;

import android.content.Intent;
import android.net.Uri;
import com.google.android.voicesearch.util.MapUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResults;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalResultUtils
{
  private static Intent createCallIntent(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.CALL");
    localIntent.setData(Uri.fromParts("tel", paramString, null));
    return localIntent;
  }
  
  public static Intent[] createIntentsForAction(int paramInt1, EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult1, EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult2, int paramInt2)
  {
    Intent[] arrayOfIntent1;
    if (paramInt1 == 4)
    {
      arrayOfIntent1 = new Intent[1];
      arrayOfIntent1[0] = createCallIntent(paramEcoutezLocalResult2.getPhoneNumber());
    }
    for (;;)
    {
      Preconditions.checkNotNull(arrayOfIntent1);
      Intent[] arrayOfIntent2 = arrayOfIntent1;
      int i = arrayOfIntent2.length;
      for (int j = 0; j < i; j++) {
        arrayOfIntent2[j].addFlags(268435456);
      }
      arrayOfIntent1 = MapUtil.getMapsIntents(paramInt1, paramEcoutezLocalResult1, paramEcoutezLocalResult2, paramInt2);
    }
    return arrayOfIntent1;
  }
  
  public static List<Intent> createProbeIntentsForAction(int paramInt, EcoutezStructuredResponse.EcoutezLocalResults paramEcoutezLocalResults)
  {
    ArrayList localArrayList = Lists.newArrayList(MapUtil.getMapsIntents(paramEcoutezLocalResults.getMapsUrl()));
    if (paramInt == 4) {
      localArrayList.add(createCallIntent(""));
    }
    while (paramInt != 3) {
      return localArrayList;
    }
    Collections.addAll(localArrayList, MapUtil.getProbeIntents());
    return localArrayList;
  }
  
  public static int getActionIconImageResource(int paramInt)
  {
    if (paramInt == -1) {
      paramInt = 1;
    }
    switch (paramInt)
    {
    default: 
      return 2130837676;
    case 4: 
      return 2130837636;
    case 2: 
      return 2130837639;
    }
    return 2130837665;
  }
  
  public static int getActionLabelStringId(int paramInt)
  {
    if (paramInt == -1) {
      paramInt = 1;
    }
    switch (paramInt)
    {
    default: 
      return 2131361977;
    case 2: 
      return 2131361976;
    case 4: 
      return 2131361979;
    }
    return 2131361978;
  }
  
  public static int getEventTypeLog(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 50;
    case 4: 
      return 113;
    case 2: 
      return 114;
    }
    return 115;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.LocalResultUtils
 * JD-Core Version:    0.7.0.1
 */