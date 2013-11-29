package com.google.android.voicesearch.audio;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import javax.annotation.Nonnull;

public class TtsAudioPlayer
{
  private final AudioRouter mAudioRouter;
  private ByteArrayPlayer mByteArrayPlayer;
  private ByteArrayPlayer.Callback mCallback;
  private final Executor mExecutor;
  private boolean mPlayAudioWhenReady;
  
  public TtsAudioPlayer(AudioRouter paramAudioRouter, Executor paramExecutor)
  {
    this.mExecutor = ((Executor)Preconditions.checkNotNull(paramExecutor));
    this.mAudioRouter = paramAudioRouter;
    this.mPlayAudioWhenReady = false;
  }
  
  public void requestPlayback(Callback paramCallback)
  {
    this.mCallback = paramCallback;
    this.mPlayAudioWhenReady = true;
    if (this.mByteArrayPlayer != null) {
      this.mByteArrayPlayer.start(this.mCallback);
    }
  }
  
  public void setAudio(@Nonnull byte[] paramArrayOfByte)
  {
    if (this.mByteArrayPlayer == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mByteArrayPlayer = new ByteArrayPlayer(this.mAudioRouter, paramArrayOfByte, this.mExecutor);
      if (this.mPlayAudioWhenReady) {
        this.mByteArrayPlayer.start(this.mCallback);
      }
      return;
    }
  }
  
  public void stopAudioPlayback()
  {
    if (this.mByteArrayPlayer != null)
    {
      this.mByteArrayPlayer.stop();
      this.mByteArrayPlayer = null;
    }
    this.mPlayAudioWhenReady = false;
  }
  
  public static abstract interface Callback
    extends ByteArrayPlayer.Callback
  {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.audio.TtsAudioPlayer
 * JD-Core Version:    0.7.0.1
 */