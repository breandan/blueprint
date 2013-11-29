package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class GlobalSearchApplicationInfo
  implements SafeParcelable
{
  public static final e CREATOR = new e();
  public final String defaultIntentAction;
  public final String defaultIntentActivity;
  public final String defaultIntentData;
  public final int iconId;
  final int is;
  public final int labelId;
  final String packageName;
  public final int settingsDescriptionId;
  
  public GlobalSearchApplicationInfo(int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2, String paramString3)
  {
    this(null, paramInt1, paramInt2, paramInt3, paramString1, paramString2, paramString3);
  }
  
  GlobalSearchApplicationInfo(int paramInt1, String paramString1, int paramInt2, int paramInt3, int paramInt4, String paramString2, String paramString3, String paramString4)
  {
    this.is = paramInt1;
    this.packageName = paramString1;
    this.labelId = paramInt2;
    this.settingsDescriptionId = paramInt3;
    this.iconId = paramInt4;
    this.defaultIntentAction = paramString2;
    this.defaultIntentData = paramString3;
    this.defaultIntentActivity = paramString4;
  }
  
  public GlobalSearchApplicationInfo(String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2, String paramString3, String paramString4)
  {
    this(2, paramString1, paramInt1, paramInt2, paramInt3, paramString2, paramString3, paramString4);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    GlobalSearchApplicationInfo localGlobalSearchApplicationInfo;
    do
    {
      return true;
      if (!(paramObject instanceof GlobalSearchApplicationInfo)) {
        break;
      }
      localGlobalSearchApplicationInfo = (GlobalSearchApplicationInfo)paramObject;
    } while ((TextUtils.equals(this.packageName, localGlobalSearchApplicationInfo.packageName)) && (this.labelId == localGlobalSearchApplicationInfo.labelId) && (this.settingsDescriptionId == localGlobalSearchApplicationInfo.settingsDescriptionId) && (this.iconId == localGlobalSearchApplicationInfo.iconId) && (TextUtils.equals(this.defaultIntentAction, localGlobalSearchApplicationInfo.defaultIntentAction)) && (TextUtils.equals(this.defaultIntentData, localGlobalSearchApplicationInfo.defaultIntentData)) && (TextUtils.equals(this.defaultIntentActivity, localGlobalSearchApplicationInfo.defaultIntentActivity)));
    return false;
    return false;
  }
  
  public String getPackageName()
  {
    return this.packageName;
  }
  
  GlobalSearchApplicationInfo r(String paramString)
  {
    return new GlobalSearchApplicationInfo(this.is, paramString, this.labelId, this.settingsDescriptionId, this.iconId, this.defaultIntentAction, this.defaultIntentData, this.defaultIntentActivity);
  }
  
  public String toString()
  {
    return getClass().getSimpleName() + "{" + this.packageName + ";labelId=" + Integer.toHexString(this.labelId) + ";settingsDescriptionId=" + Integer.toHexString(this.settingsDescriptionId) + ";iconId=" + Integer.toHexString(this.iconId) + ";defaultIntentAction=" + this.defaultIntentAction + ";defaultIntentData=" + this.defaultIntentData + ";defaultIntentActivity=" + this.defaultIntentActivity + "}";
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    e.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.GlobalSearchApplicationInfo
 * JD-Core Version:    0.7.0.1
 */