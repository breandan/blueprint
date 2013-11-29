package com.google.android.gms.appdatasearch;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class u
  implements Parcelable.Creator<SearchResults>
{
  static void a(SearchResults paramSearchResults, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramSearchResults.mErrorMessage, false);
    b.c(paramParcel, 1000, paramSearchResults.is);
    b.a(paramParcel, 2, paramSearchResults.jw, false);
    b.a(paramParcel, 3, paramSearchResults.jx, false);
    b.a(paramParcel, 4, paramSearchResults.jy, paramInt, false);
    b.a(paramParcel, 5, paramSearchResults.jz, paramInt, false);
    b.a(paramParcel, 6, paramSearchResults.jA, paramInt, false);
    b.c(paramParcel, 7, paramSearchResults.jB);
    b.a(paramParcel, 8, paramSearchResults.jC, false);
    b.a(paramParcel, 9, paramSearchResults.jD, false);
    b.C(paramParcel, i);
  }
  
  public SearchResults A(Parcel paramParcel)
  {
    int i = 0;
    String[] arrayOfString = null;
    int j = a.av(paramParcel);
    int[] arrayOfInt1 = null;
    Bundle[] arrayOfBundle1 = null;
    Bundle[] arrayOfBundle2 = null;
    Bundle[] arrayOfBundle3 = null;
    byte[] arrayOfByte = null;
    int[] arrayOfInt2 = null;
    String str = null;
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
        str = a.l(paramParcel, m);
        break;
      case 1000: 
        k = a.f(paramParcel, m);
        break;
      case 2: 
        arrayOfInt2 = a.q(paramParcel, m);
        break;
      case 3: 
        arrayOfByte = a.o(paramParcel, m);
        break;
      case 4: 
        arrayOfBundle3 = (Bundle[])a.b(paramParcel, m, Bundle.CREATOR);
        break;
      case 5: 
        arrayOfBundle2 = (Bundle[])a.b(paramParcel, m, Bundle.CREATOR);
        break;
      case 6: 
        arrayOfBundle1 = (Bundle[])a.b(paramParcel, m, Bundle.CREATOR);
        break;
      case 7: 
        i = a.f(paramParcel, m);
        break;
      case 8: 
        arrayOfInt1 = a.q(paramParcel, m);
        break;
      case 9: 
        arrayOfString = a.w(paramParcel, m);
      }
    }
    if (paramParcel.dataPosition() != j) {
      throw new a.a("Overread allowed size end=" + j, paramParcel);
    }
    return new SearchResults(k, str, arrayOfInt2, arrayOfByte, arrayOfBundle3, arrayOfBundle2, arrayOfBundle1, i, arrayOfInt1, arrayOfString);
  }
  
  public SearchResults[] G(int paramInt)
  {
    return new SearchResults[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.u
 * JD-Core Version:    0.7.0.1
 */