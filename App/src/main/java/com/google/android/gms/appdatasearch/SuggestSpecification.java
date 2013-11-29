package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class SuggestSpecification
  implements SafeParcelable
{
  public static final x CREATOR = new x();
  final int is;
  
  public SuggestSpecification()
  {
    this(2);
  }
  
  SuggestSpecification(int paramInt)
  {
    this.is = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    x.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.SuggestSpecification
 * JD-Core Version:    0.7.0.1
 */