package com.google.android.gms.appdatasearch;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class d
  implements Parcelable.Creator<DocumentResults>
{
  static void a(DocumentResults paramDocumentResults, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramDocumentResults.mErrorMessage, false);
    b.c(paramParcel, 1000, paramDocumentResults.is);
    b.a(paramParcel, 2, paramDocumentResults.iz, false);
    b.a(paramParcel, 3, paramDocumentResults.iA, false);
    b.a(paramParcel, 4, paramDocumentResults.iB, false);
    b.C(paramParcel, i);
  }
  
  public DocumentResults k(Parcel paramParcel)
  {
    Bundle localBundle1 = null;
    int i = a.av(paramParcel);
    int j = 0;
    Bundle localBundle2 = null;
    Bundle localBundle3 = null;
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
        localBundle3 = a.n(paramParcel, k);
        break;
      case 3: 
        localBundle2 = a.n(paramParcel, k);
        break;
      case 4: 
        localBundle1 = a.n(paramParcel, k);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new DocumentResults(j, str, localBundle3, localBundle2, localBundle1);
  }
  
  public DocumentResults[] p(int paramInt)
  {
    return new DocumentResults[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.d
 * JD-Core Version:    0.7.0.1
 */