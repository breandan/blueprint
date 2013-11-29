package com.google.android.gms.appdatasearch;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class DocumentResults
  implements SafeParcelable
{
  public static final d CREATOR = new d();
  final Bundle iA;
  final Bundle iB;
  final int is;
  final Bundle iz;
  final String mErrorMessage;
  
  DocumentResults(int paramInt, String paramString, Bundle paramBundle1, Bundle paramBundle2, Bundle paramBundle3)
  {
    this.is = paramInt;
    this.mErrorMessage = paramString;
    this.iz = paramBundle1;
    this.iA = paramBundle2;
    this.iB = paramBundle3;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    d.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.DocumentResults
 * JD-Core Version:    0.7.0.1
 */