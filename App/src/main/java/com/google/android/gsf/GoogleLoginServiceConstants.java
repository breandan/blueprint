package com.google.android.gsf;

import android.content.Intent;

public class GoogleLoginServiceConstants
{
  public static final Intent SERVICE_INTENT = new Intent().setPackage("com.google.android.gsf.login").setAction("com.google.android.gsf.action.GET_GLS").addCategory("android.intent.category.DEFAULT");
  
  public static String featureForService(String paramString)
  {
    return "service_" + paramString;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gsf.GoogleLoginServiceConstants
 * JD-Core Version:    0.7.0.1
 */