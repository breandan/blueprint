package com.google.android.gms.location.reporting;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class e
  implements Parcelable.Creator<UploadRequest>
{
  static void a(UploadRequest paramUploadRequest, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.c(paramParcel, 1, paramUploadRequest.getVersionCode());
    b.a(paramParcel, 2, paramUploadRequest.getAccount(), paramInt, false);
    b.a(paramParcel, 3, paramUploadRequest.getReason(), false);
    b.a(paramParcel, 4, paramUploadRequest.getDurationMillis());
    b.a(paramParcel, 5, paramUploadRequest.getMovingLatencyMillis());
    b.a(paramParcel, 6, paramUploadRequest.getStationaryLatencyMillis());
    b.a(paramParcel, 7, paramUploadRequest.getAppSpecificKey(), false);
    b.C(paramParcel, i);
  }
  
  public UploadRequest aS(Parcel paramParcel)
  {
    long l1 = 0L;
    String str1 = null;
    int i = a.av(paramParcel);
    int j = 0;
    long l2 = l1;
    long l3 = l1;
    String str2 = null;
    Account localAccount = null;
    while (paramParcel.dataPosition() < i)
    {
      int k = a.au(paramParcel);
      switch (a.aL(k))
      {
      default: 
        a.b(paramParcel, k);
        break;
      case 1: 
        j = a.f(paramParcel, k);
        break;
      case 2: 
        localAccount = (Account)a.a(paramParcel, k, Account.CREATOR);
        break;
      case 3: 
        str2 = a.l(paramParcel, k);
        break;
      case 4: 
        l3 = a.g(paramParcel, k);
        break;
      case 5: 
        l2 = a.g(paramParcel, k);
        break;
      case 6: 
        l1 = a.g(paramParcel, k);
        break;
      case 7: 
        str1 = a.l(paramParcel, k);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new UploadRequest(j, localAccount, str2, l3, l2, l1, str1);
  }
  
  public UploadRequest[] bw(int paramInt)
  {
    return new UploadRequest[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.location.reporting.e
 * JD-Core Version:    0.7.0.1
 */