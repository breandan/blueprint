package com.google.android.voicesearch.ime;

import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import java.util.Iterator;
import java.util.List;

public class ImeUtils
{
  private static String getImeId(Context paramContext)
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)paramContext.getSystemService("input_method");
    String str = paramContext.getPackageName();
    Iterator localIterator = localInputMethodManager.getEnabledInputMethodList().iterator();
    while (localIterator.hasNext())
    {
      InputMethodInfo localInputMethodInfo = (InputMethodInfo)localIterator.next();
      if (str.equals(localInputMethodInfo.getPackageName())) {
        return localInputMethodInfo.getId();
      }
    }
    return null;
  }
  
  public static void showImeSubtypeSetting(Context paramContext)
  {
    Intent localIntent = new Intent("android.settings.INPUT_METHOD_SUBTYPE_SETTINGS");
    localIntent.putExtra("input_method_id", getImeId(paramContext));
    localIntent.setFlags(337641472);
    paramContext.startActivity(localIntent);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.ime.ImeUtils
 * JD-Core Version:    0.7.0.1
 */