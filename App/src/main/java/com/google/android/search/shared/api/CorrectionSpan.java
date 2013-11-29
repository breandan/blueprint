package com.google.android.search.shared.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;
import com.google.common.base.Preconditions;

public final class CorrectionSpan
  extends CharacterStyle
  implements Parcelable, UpdateAppearance
{
  public static final Parcelable.Creator<CorrectionSpan> CREATOR = new Parcelable.Creator()
  {
    public CorrectionSpan createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CorrectionSpan(paramAnonymousParcel.readString());
    }
    
    public CorrectionSpan[] newArray(int paramAnonymousInt)
    {
      return new CorrectionSpan[paramAnonymousInt];
    }
  };
  private final String mCorrection;
  
  public CorrectionSpan(String paramString)
  {
    this.mCorrection = ((String)Preconditions.checkNotNull(paramString));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof CorrectionSpan))
    {
      CorrectionSpan localCorrectionSpan = (CorrectionSpan)paramObject;
      return this.mCorrection.equals(localCorrectionSpan.mCorrection);
    }
    return false;
  }
  
  public String getCorrection()
  {
    return this.mCorrection;
  }
  
  public int hashCode()
  {
    return this.mCorrection.hashCode();
  }
  
  public String toString()
  {
    return "CorrectionSpan[" + this.mCorrection + "]";
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    paramTextPaint.setUnderlineText(true);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.mCorrection);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.api.CorrectionSpan
 * JD-Core Version:    0.7.0.1
 */