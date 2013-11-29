package com.google.android.gms.appdatasearch;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class SectionFeature
  implements SafeParcelable
{
  public static final w CREATOR = new w();
  public final int id;
  final int is;
  final Bundle jM;
  
  private SectionFeature(int paramInt)
  {
    this(1, paramInt, new Bundle());
  }
  
  SectionFeature(int paramInt1, int paramInt2, Bundle paramBundle)
  {
    this.is = paramInt1;
    this.id = paramInt2;
    this.jM = paramBundle;
  }
  
  public static SectionFeature matchGlobalNicknames()
  {
    return new SectionFeature(1);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    w.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.SectionFeature
 * JD-Core Version:    0.7.0.1
 */