package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class Section
  implements SafeParcelable
{
  public static final v CREATOR = new v();
  final int is;
  public final String name;
  public final int snippetLength;
  public final boolean snippeted;
  
  Section(int paramInt1, String paramString, boolean paramBoolean, int paramInt2)
  {
    this.is = paramInt1;
    this.name = paramString;
    this.snippeted = paramBoolean;
    this.snippetLength = paramInt2;
  }
  
  public Section(String paramString)
  {
    this(paramString, false, 0);
  }
  
  public Section(String paramString, boolean paramBoolean, int paramInt)
  {
    this(2, paramString, paramBoolean, paramInt);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    v.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.Section
 * JD-Core Version:    0.7.0.1
 */