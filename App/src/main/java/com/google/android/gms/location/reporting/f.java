package com.google.android.gms.location.reporting;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class f
  implements Parcelable.Creator<UploadRequestResult>
{
  static void a(UploadRequestResult paramUploadRequestResult, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.c(paramParcel, 1, paramUploadRequestResult.getVersionCode());
    b.c(paramParcel, 2, paramUploadRequestResult.getResultCode());
    b.a(paramParcel, 3, paramUploadRequestResult.getRequestId());
    b.C(paramParcel, i);
  }
  
  public UploadRequestResult aT(Parcel paramParcel)
  {
    int i = 0;
    int j = a.av(paramParcel);
    long l = 0L;
    int k = 0;
    while (paramParcel.dataPosition() < j)
    {
      int m = a.au(paramParcel);
      switch (a.aL(m))
      {
      default: 
        a.b(paramParcel, m);
        break;
      case 1: 
        k = a.f(paramParcel, m);
        break;
      case 2: 
        i = a.f(paramParcel, m);
        break;
      case 3: 
        l = a.g(paramParcel, m);
      }
    }
    if (paramParcel.dataPosition() != j) {
      throw new a.a("Overread allowed size end=" + j, paramParcel);
    }
    return new UploadRequestResult(k, i, l);
  }
  
  public UploadRequestResult[] bx(int paramInt)
  {
    return new UploadRequestResult[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.location.reporting.f
 * JD-Core Version:    0.7.0.1
 */