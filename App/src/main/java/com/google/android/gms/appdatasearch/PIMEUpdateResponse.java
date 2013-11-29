package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class PIMEUpdateResponse
  implements SafeParcelable
{
  public static final i CREATOR = new i();
  final int is;
  final String mErrorMessage;
  public final byte[] nextIterToken;
  public final PIMEUpdate[] updates;
  
  PIMEUpdateResponse(int paramInt, String paramString, byte[] paramArrayOfByte, PIMEUpdate[] paramArrayOfPIMEUpdate)
  {
    this.is = paramInt;
    this.mErrorMessage = paramString;
    this.nextIterToken = paramArrayOfByte;
    this.updates = paramArrayOfPIMEUpdate;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    i.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.PIMEUpdateResponse
 * JD-Core Version:    0.7.0.1
 */