package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class RequestIndexingSpecification
  implements SafeParcelable
{
  public static final q CREATOR = new q();
  final int is;
  
  RequestIndexingSpecification(int paramInt)
  {
    this.is = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    q.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.RequestIndexingSpecification
 * JD-Core Version:    0.7.0.1
 */