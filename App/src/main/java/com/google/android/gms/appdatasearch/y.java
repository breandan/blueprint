package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class y
  implements Parcelable.Creator<SuggestionResults>
{
  static void a(SuggestionResults paramSuggestionResults, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramSuggestionResults.mErrorMessage, false);
    b.c(paramParcel, 1000, paramSuggestionResults.is);
    b.a(paramParcel, 2, paramSuggestionResults.jN, false);
    b.a(paramParcel, 3, paramSuggestionResults.jO, false);
    b.C(paramParcel, i);
  }
  
  public SuggestionResults E(Parcel paramParcel)
  {
    String[] arrayOfString1 = null;
    int i = a.av(paramParcel);
    int j = 0;
    String[] arrayOfString2 = null;
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
        arrayOfString2 = a.w(paramParcel, k);
        break;
      case 3: 
        arrayOfString1 = a.w(paramParcel, k);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new SuggestionResults(j, str, arrayOfString2, arrayOfString1);
  }
  
  public SuggestionResults[] K(int paramInt)
  {
    return new SuggestionResults[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.y
 * JD-Core Version:    0.7.0.1
 */