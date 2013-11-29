package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class i
  implements Parcelable.Creator<PIMEUpdateResponse>
{
  static void a(PIMEUpdateResponse paramPIMEUpdateResponse, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramPIMEUpdateResponse.mErrorMessage, false);
    b.c(paramParcel, 1000, paramPIMEUpdateResponse.is);
    b.a(paramParcel, 2, paramPIMEUpdateResponse.nextIterToken, false);
    b.a(paramParcel, 3, paramPIMEUpdateResponse.updates, paramInt, false);
    b.C(paramParcel, i);
  }
  
  public PIMEUpdateResponse p(Parcel paramParcel)
  {
    PIMEUpdate[] arrayOfPIMEUpdate = null;
    int i = a.av(paramParcel);
    int j = 0;
    byte[] arrayOfByte = null;
    String str = null;
    while (paramParcel.dataPosition() < i)
    {
      int k = a.au(paramParcel);
      switch (a.aL(k))
      {
      default: 
        a.b(paramParcel, k);
        break;
      case 1: 
        str = a.l(paramParcel, k);
        break;
      case 1000: 
        j = a.f(paramParcel, k);
        break;
      case 2: 
        arrayOfByte = a.o(paramParcel, k);
        break;
      case 3: 
        arrayOfPIMEUpdate = (PIMEUpdate[])a.b(paramParcel, k, PIMEUpdate.CREATOR);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new PIMEUpdateResponse(j, str, arrayOfByte, arrayOfPIMEUpdate);
  }
  
  public PIMEUpdateResponse[] u(int paramInt)
  {
    return new PIMEUpdateResponse[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.i
 * JD-Core Version:    0.7.0.1
 */