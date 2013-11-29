package com.google.android.gms.location.reporting;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class d
  implements Parcelable.Creator<ReportingState>
{
  static void a(ReportingState paramReportingState, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.c(paramParcel, 1, paramReportingState.getVersionCode());
    b.c(paramParcel, 2, paramReportingState.getReportingEnabled());
    b.c(paramParcel, 3, paramReportingState.getHistoryEnabled());
    b.a(paramParcel, 4, paramReportingState.isAllowed());
    b.a(paramParcel, 5, paramReportingState.isActive());
    b.a(paramParcel, 6, paramReportingState.isDeferringToMaps());
    b.c(paramParcel, 7, paramReportingState.getExpectedOptInResult());
    b.C(paramParcel, i);
  }
  
  public ReportingState aR(Parcel paramParcel)
  {
    int i = 0;
    int j = a.av(paramParcel);
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    int k = 0;
    int m = 0;
    int n = 0;
    while (paramParcel.dataPosition() < j)
    {
      int i1 = a.au(paramParcel);
      switch (a.aL(i1))
      {
      default: 
        a.b(paramParcel, i1);
        break;
      case 1: 
        n = a.f(paramParcel, i1);
        break;
      case 2: 
        m = a.f(paramParcel, i1);
        break;
      case 3: 
        k = a.f(paramParcel, i1);
        break;
      case 4: 
        bool3 = a.c(paramParcel, i1);
        break;
      case 5: 
        bool2 = a.c(paramParcel, i1);
        break;
      case 6: 
        bool1 = a.c(paramParcel, i1);
        break;
      case 7: 
        i = a.f(paramParcel, i1);
      }
    }
    if (paramParcel.dataPosition() != j) {
      throw new a.a("Overread allowed size end=" + j, paramParcel);
    }
    return new ReportingState(n, m, k, bool3, bool2, bool1, i);
  }
  
  public ReportingState[] bv(int paramInt)
  {
    return new ReportingState[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.location.reporting.d
 * JD-Core Version:    0.7.0.1
 */