package com.google.android.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SoundSearchUnavailableError
  extends SearchError
{
  public static final Parcelable.Creator<SoundSearchUnavailableError> CREATOR = new Parcelable.Creator()
  {
    public SoundSearchUnavailableError createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SoundSearchUnavailableError();
    }
    
    public SoundSearchUnavailableError[] newArray(int paramAnonymousInt)
    {
      return new SoundSearchUnavailableError[paramAnonymousInt];
    }
  };
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getErrorImageResId()
  {
    return 2130837774;
  }
  
  public int getErrorMessageResId()
  {
    return 2131363313;
  }
  
  public int getErrorTypeForLogs()
  {
    return -1;
  }
  
  public boolean isRetriable()
  {
    return false;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.SoundSearchUnavailableError
 * JD-Core Version:    0.7.0.1
 */