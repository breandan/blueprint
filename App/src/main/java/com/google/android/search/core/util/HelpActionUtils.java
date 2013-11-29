package com.google.android.search.core.util;

import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import com.google.android.search.core.DeviceCapabilityManager;
import com.google.android.voicesearch.fragments.action.HelpAction;
import com.google.android.voicesearch.util.ExampleContactHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.majel.proto.ActionV2Protos.HelpAction;
import com.google.majel.proto.ActionV2Protos.HelpAction.Feature;
import com.google.majel.proto.ActionV2Protos.HelpAction.Feature.Example;
import com.google.majel.proto.ActionV2Protos.TranslationConsoleString;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

public class HelpActionUtils
{
  static Uri convertToUri(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new AssertionError("Unknown HelpAction.CONTACT_SUBSTITUTION value" + paramInt);
    case 1: 
      return ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    }
    return ContactsContract.CommonDataKinds.Email.CONTENT_URI;
  }
  
  static boolean isExampleSupported(ActionV2Protos.HelpAction.Feature.Example paramExample, DeviceCapabilityManager paramDeviceCapabilityManager, int paramInt)
  {
    if (!paramExample.hasQuery()) {
      return false;
    }
    if (((paramExample.hasMinVersion()) && (paramInt < paramExample.getMinVersion())) || ((paramExample.hasRetireVersion()) && (paramInt >= paramExample.getRetireVersion())))
    {
      Log.d("HelpActionUtils", paramExample.getQuery() + " skipped due to incompatible version");
      return false;
    }
    Iterator localIterator = paramExample.getRequiredCapabilityList().iterator();
    while (localIterator.hasNext())
    {
      int i = ((Integer)localIterator.next()).intValue();
      if (i == 0)
      {
        if (!paramDeviceCapabilityManager.isTelephoneCapable()) {
          return false;
        }
      }
      else if (i == 1)
      {
        if (!paramDeviceCapabilityManager.hasRearFacingCamera()) {
          return false;
        }
      }
      else {
        return false;
      }
    }
    return true;
  }
  
  @Nonnull
  public static void process(ActionV2Protos.HelpAction paramHelpAction, ExampleContactHelper paramExampleContactHelper, DeviceCapabilityManager paramDeviceCapabilityManager, int paramInt, List<? super HelpAction> paramList)
  {
    processIntroduction(paramHelpAction, paramList);
    processFeatures(paramHelpAction, paramExampleContactHelper, paramDeviceCapabilityManager, paramInt, paramList);
  }
  
  static HelpAction processFeature(ActionV2Protos.HelpAction.Feature paramFeature, ExampleContactHelper paramExampleContactHelper, DeviceCapabilityManager paramDeviceCapabilityManager, int paramInt)
  {
    ArrayList localArrayList = Lists.newArrayListWithExpectedSize(paramFeature.getExampleCount());
    HashMap localHashMap = Maps.newHashMap();
    String str = paramFeature.getHeadline().getText();
    Iterator localIterator = paramFeature.getExampleList().iterator();
    while (localIterator.hasNext())
    {
      ActionV2Protos.HelpAction.Feature.Example localExample = (ActionV2Protos.HelpAction.Feature.Example)localIterator.next();
      if (isExampleSupported(localExample, paramDeviceCapabilityManager, paramInt))
      {
        int i = HelpAction.getContactSubstitutionType(localExample);
        if (i != 0)
        {
          List localList = (List)localHashMap.get(Integer.valueOf(i));
          if (localList == null) {
            localList = paramExampleContactHelper.getContacts(convertToUri(i));
          }
          if (!localList.isEmpty()) {
            localHashMap.put(Integer.valueOf(i), localList);
          }
        }
        else
        {
          localArrayList.add(localExample);
        }
      }
    }
    if (localArrayList.size() > 0)
    {
      Collections.shuffle(localArrayList);
      Log.d("HelpActionUtils", "Initializing controller for feature: " + str + " containing " + localArrayList.size() + " examples.");
      return new HelpAction(str, localArrayList, localHashMap);
    }
    Log.i("HelpActionUtils", str + " has no applicable examples.");
    return null;
  }
  
  public static void processFeatures(ActionV2Protos.HelpAction paramHelpAction, ExampleContactHelper paramExampleContactHelper, DeviceCapabilityManager paramDeviceCapabilityManager, int paramInt, List<? super HelpAction> paramList)
  {
    Iterator localIterator = paramHelpAction.getFeatureList().iterator();
    while (localIterator.hasNext())
    {
      HelpAction localHelpAction = processFeature((ActionV2Protos.HelpAction.Feature)localIterator.next(), paramExampleContactHelper, paramDeviceCapabilityManager, paramInt);
      if (localHelpAction != null) {
        paramList.add(localHelpAction);
      }
    }
  }
  
  public static void processIntroduction(ActionV2Protos.HelpAction paramHelpAction, List<? super HelpAction> paramList)
  {
    paramList.add(new HelpAction(paramHelpAction.getTitle().getText(), paramHelpAction.getIntroduction().getText()));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.HelpActionUtils
 * JD-Core Version:    0.7.0.1
 */