package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;
import java.util.ArrayList;

public class m
  implements Parcelable.Creator<QuerySpecification>
{
  static void a(QuerySpecification paramQuerySpecification, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramQuerySpecification.wantUris);
    b.c(paramParcel, 1000, paramQuerySpecification.is);
    b.a(paramParcel, 2, paramQuerySpecification.wantedTags, false);
    b.b(paramParcel, 3, paramQuerySpecification.wantedSections, false);
    b.a(paramParcel, 4, paramQuerySpecification.prefixMatch);
    b.C(paramParcel, i);
  }
  
  public QuerySpecification t(Parcel paramParcel)
  {
    ArrayList localArrayList1 = null;
    boolean bool1 = false;
    int i = a.av(paramParcel);
    ArrayList localArrayList2 = null;
    boolean bool2 = false;
    int j = 0;
    while (paramParcel.dataPosition() < i)
    {
      int k = a.au(paramParcel);
      switch (a.aL(k))
      {
      default: 
        a.b(paramParcel, k);
        break;
      case 1: 
        bool2 = a.c(paramParcel, k);
        break;
      case 1000: 
        j = a.f(paramParcel, k);
        break;
      case 2: 
        localArrayList2 = a.x(paramParcel, k);
        break;
      case 3: 
        localArrayList1 = a.c(paramParcel, k, Section.CREATOR);
        break;
      case 4: 
        bool1 = a.c(paramParcel, k);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new QuerySpecification(j, bool2, localArrayList2, localArrayList1, bool1);
  }
  
  public QuerySpecification[] y(int paramInt)
  {
    return new QuerySpecification[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.m
 * JD-Core Version:    0.7.0.1
 */