package com.google.android.sidekick.shared.client;

import com.google.android.shared.util.SpeechLevelSource;
import java.util.Observable;
import javax.annotation.Nullable;

public abstract class TvRecognitionManager
  extends Observable
{
  private int mState = 1;
  
  public int getRecognitionState()
  {
    return this.mState;
  }
  
  @Nullable
  public abstract SpeechLevelSource getSpeechLevelSource();
  
  protected final void setRecognitionState(int paramInt)
  {
    if (this.mState == paramInt) {
      return;
    }
    this.mState = paramInt;
    setChanged();
    notifyObservers(Integer.valueOf(this.mState));
  }
  
  public abstract void startRecognition();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.TvRecognitionManager
 * JD-Core Version:    0.7.0.1
 */