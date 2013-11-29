package com.google.android.search.core.ears;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.search.core.SearchError;
import com.google.android.speech.exception.NoMatchRecognizeException;
import com.google.android.speech.exception.SoundSearchRecognizeException;
import com.google.android.voicesearch.util.ErrorUtils;

public class SoundSearchError
  extends SearchError
{
  public static final Parcelable.Creator<SoundSearchError> CREATOR = new Parcelable.Creator()
  {
    public SoundSearchError createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SoundSearchError(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), null);
    }
    
    public SoundSearchError[] newArray(int paramAnonymousInt)
    {
      return new SoundSearchError[paramAnonymousInt];
    }
  };
  private final int mErrorType;
  private final int mImageId;
  private final int mMessageId;
  
  private SoundSearchError(int paramInt1, int paramInt2, int paramInt3)
  {
    this.mMessageId = paramInt1;
    this.mImageId = paramInt2;
    this.mErrorType = paramInt3;
  }
  
  public SoundSearchError(SoundSearchRecognizeException paramSoundSearchRecognizeException)
  {
    if ((paramSoundSearchRecognizeException.getOriginalException() instanceof NoMatchRecognizeException)) {
      this.mMessageId = 2131363309;
    }
    for (this.mImageId = 2130837905;; this.mImageId = ErrorUtils.getErrorImage(paramSoundSearchRecognizeException.getOriginalException()))
    {
      this.mErrorType = ErrorUtils.getErrorTypeForLogs(paramSoundSearchRecognizeException.getOriginalException());
      return;
      this.mMessageId = ErrorUtils.getErrorMessage(paramSoundSearchRecognizeException.getOriginalException());
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getErrorImageResId()
  {
    return this.mImageId;
  }
  
  public int getErrorMessageResId()
  {
    return this.mMessageId;
  }
  
  public int getErrorTypeForLogs()
  {
    return this.mErrorType;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.mMessageId);
    paramParcel.writeInt(this.mImageId);
    paramParcel.writeInt(this.mErrorType);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.ears.SoundSearchError
 * JD-Core Version:    0.7.0.1
 */