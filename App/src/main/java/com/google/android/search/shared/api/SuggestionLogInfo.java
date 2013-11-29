package com.google.android.search.shared.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SuggestionLogInfo
  implements Parcelable
{
  public static final Parcelable.Creator<SuggestionLogInfo> CREATOR = new Parcelable.Creator()
  {
    public SuggestionLogInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SuggestionLogInfo(paramAnonymousParcel);
    }
    
    public SuggestionLogInfo[] newArray(int paramAnonymousInt)
    {
      return new SuggestionLogInfo[paramAnonymousInt];
    }
  };
  public static SuggestionLogInfo EMPTY = new SuggestionLogInfo("", "");
  private final String mSuggestionsEncoding;
  private final String mSummonsEncoding;
  
  public SuggestionLogInfo(Parcel paramParcel)
  {
    this.mSuggestionsEncoding = paramParcel.readString();
    this.mSummonsEncoding = paramParcel.readString();
  }
  
  public SuggestionLogInfo(String paramString1, String paramString2)
  {
    this.mSuggestionsEncoding = paramString1;
    this.mSummonsEncoding = paramString2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getSuggestionsEncoding()
  {
    return this.mSuggestionsEncoding;
  }
  
  public String getSummonEncoding()
  {
    return this.mSummonsEncoding;
  }
  
  public String toString()
  {
    return "SuggestionLogInfo: \n    SuggestionsEncoding: " + this.mSuggestionsEncoding + "\n    SummonsEncoding: " + this.mSummonsEncoding;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.mSuggestionsEncoding);
    paramParcel.writeString(this.mSummonsEncoding);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.api.SuggestionLogInfo
 * JD-Core Version:    0.7.0.1
 */