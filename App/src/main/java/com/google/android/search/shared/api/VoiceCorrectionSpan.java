package com.google.android.search.shared.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class VoiceCorrectionSpan
  implements Parcelable
{
  public static final Parcelable.Creator<VoiceCorrectionSpan> CREATOR = new Parcelable.Creator()
  {
    public VoiceCorrectionSpan createFromParcel(Parcel paramAnonymousParcel)
    {
      ArrayList localArrayList = Lists.newArrayList();
      paramAnonymousParcel.readStringList(localArrayList);
      return new VoiceCorrectionSpan(localArrayList);
    }
    
    public VoiceCorrectionSpan[] newArray(int paramAnonymousInt)
    {
      return new VoiceCorrectionSpan[paramAnonymousInt];
    }
  };
  private final List<String> mCorrection;
  
  public VoiceCorrectionSpan(List<String> paramList)
  {
    this.mCorrection = ((List)Preconditions.checkNotNull(paramList));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof VoiceCorrectionSpan))
    {
      VoiceCorrectionSpan localVoiceCorrectionSpan = (VoiceCorrectionSpan)paramObject;
      return this.mCorrection.equals(localVoiceCorrectionSpan.mCorrection);
    }
    return false;
  }
  
  public List<String> getCorrection()
  {
    return Collections.unmodifiableList(this.mCorrection);
  }
  
  public int hashCode()
  {
    return this.mCorrection.hashCode();
  }
  
  public String toString()
  {
    return "VoiceCorrectionSpan[" + this.mCorrection + "]";
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStringList(this.mCorrection);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.api.VoiceCorrectionSpan
 * JD-Core Version:    0.7.0.1
 */