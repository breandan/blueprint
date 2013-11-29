package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.List;

public class QuerySpecification
  implements SafeParcelable
{
  public static final m CREATOR = new m();
  final int is;
  public final boolean prefixMatch;
  public final boolean wantUris;
  public final List<Section> wantedSections;
  public final List<String> wantedTags;
  
  QuerySpecification(int paramInt, boolean paramBoolean1, List<String> paramList, List<Section> paramList1, boolean paramBoolean2)
  {
    this.is = paramInt;
    this.wantUris = paramBoolean1;
    this.wantedTags = paramList;
    this.wantedSections = paramList1;
    this.prefixMatch = paramBoolean2;
  }
  
  public QuerySpecification(boolean paramBoolean1, boolean paramBoolean2, List<Section> paramList, List<String> paramList1)
  {
    this(2, paramBoolean2, paramList1, paramList, paramBoolean1);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    m.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.QuerySpecification
 * JD-Core Version:    0.7.0.1
 */