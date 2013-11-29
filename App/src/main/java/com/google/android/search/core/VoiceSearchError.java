package com.google.android.search.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.shared.api.Query;
import com.google.android.speech.audio.AudioStore;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.voicesearch.util.ErrorUtils;
import javax.annotation.Nullable;

public class VoiceSearchError
  extends SearchError
{
  public static final Parcelable.Creator<VoiceSearchError> CREATOR = new Parcelable.Creator()
  {
    public VoiceSearchError createFromParcel(Parcel paramAnonymousParcel)
    {
      return new VoiceSearchError(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), false, paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), null, paramAnonymousParcel.readString(), null);
    }
    
    public VoiceSearchError[] newArray(int paramAnonymousInt)
    {
      return new VoiceSearchError[paramAnonymousInt];
    }
  };
  @Nullable
  private final AudioStore mAudioStore;
  private final boolean mCanResendSameAudio;
  private final int mErrorType;
  private final int mExplanationId;
  private final int mImageId;
  private final int mMessageId;
  @Nullable
  private final String mRequestId;
  private final int mTitleId;
  
  private VoiceSearchError(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, int paramInt5, @Nullable AudioStore paramAudioStore, String paramString)
  {
    this.mMessageId = paramInt1;
    this.mTitleId = paramInt2;
    this.mExplanationId = paramInt3;
    if ((paramString != null) && (paramBoolean)) {}
    for (boolean bool = true;; bool = false)
    {
      this.mCanResendSameAudio = bool;
      this.mImageId = paramInt4;
      this.mErrorType = paramInt5;
      this.mAudioStore = paramAudioStore;
      this.mRequestId = paramString;
      return;
    }
  }
  
  public VoiceSearchError(RecognizeException paramRecognizeException, AudioStore paramAudioStore, @Nullable String paramString)
  {
    this(ErrorUtils.getErrorMessage(paramRecognizeException), ErrorUtils.getErrorTitle(paramRecognizeException), ErrorUtils.getErrorExplanation(paramRecognizeException), ErrorUtils.canResendSameAudio(paramRecognizeException), ErrorUtils.getErrorImage(paramRecognizeException), ErrorUtils.getErrorTypeForLogs(paramRecognizeException), paramAudioStore, paramString);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getButtonTextId()
  {
    if ((this.mCanResendSameAudio) && (this.mAudioStore != null) && (this.mAudioStore.hasAudio(this.mRequestId))) {
      return 2131363289;
    }
    return super.getButtonTextId();
  }
  
  public int getErrorExplanationResId()
  {
    return this.mExplanationId;
  }
  
  public int getErrorImageResId()
  {
    return this.mImageId;
  }
  
  public int getErrorMessageResId()
  {
    return this.mMessageId;
  }
  
  public int getErrorTitleResId()
  {
    return this.mTitleId;
  }
  
  public int getErrorTypeForLogs()
  {
    return this.mErrorType;
  }
  
  public void retry(QueryState paramQueryState, Query paramQuery)
  {
    if ((this.mCanResendSameAudio) && (this.mAudioStore != null) && (this.mAudioStore.hasAudio(this.mRequestId))) {
      paramQuery = paramQuery.voiceSearchWithLastRecording();
    }
    super.retry(paramQueryState, paramQuery);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.mMessageId);
    paramParcel.writeInt(this.mTitleId);
    paramParcel.writeInt(this.mExplanationId);
    paramParcel.writeInt(this.mImageId);
    paramParcel.writeString(this.mRequestId);
    paramParcel.writeInt(this.mErrorType);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.VoiceSearchError
 * JD-Core Version:    0.7.0.1
 */