package com.google.android.speech.audio;

import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class AudioRecorder
{
  private AudioStore mAudioStore = null;
  private int mBytesPerMsec;
  private int mEndPos;
  private int mMaxFlattenedBufferSize;
  private ClampedLengthRecordingThread mRecordingThread;
  private String mRequestId;
  private int mSampleRate;
  private int mStartPos;
  
  private void doStopRecording(boolean paramBoolean)
  {
    if (this.mRecordingThread == null) {
      return;
    }
    if (paramBoolean) {
      this.mRecordingThread.requestStop();
    }
    for (;;)
    {
      try
      {
        this.mRecordingThread.join();
        if (this.mRecordingThread.isGood())
        {
          arrayOfByte = this.mRecordingThread.getBuffer();
          this.mEndPos = Math.min(this.mRecordingThread.getTotalLength(), this.mEndPos);
          if (this.mStartPos >= this.mEndPos) {
            arrayOfByte = null;
          }
          this.mAudioStore.put(this.mRequestId, new AudioStore.AudioRecording(this.mSampleRate, getLastAudio(arrayOfByte)));
          this.mAudioStore = null;
          this.mRequestId = null;
          this.mRecordingThread = null;
          return;
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        this.mAudioStore = null;
        this.mRequestId = null;
        return;
      }
      boolean bool = this.mRecordingThread.isOverflown();
      byte[] arrayOfByte = null;
      if (bool)
      {
        int i = this.mEndPos;
        int j = this.mMaxFlattenedBufferSize;
        arrayOfByte = null;
        if (i <= j) {
          arrayOfByte = this.mRecordingThread.getBuffer();
        }
      }
    }
  }
  
  private byte[] getLastAudio(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte == null) || ((this.mStartPos == 0) && (this.mEndPos == paramArrayOfByte.length))) {
      return paramArrayOfByte;
    }
    return Arrays.copyOfRange(paramArrayOfByte, 2 * (this.mStartPos / 2), this.mEndPos);
  }
  
  public boolean isRecording()
  {
    return this.mRecordingThread != null;
  }
  
  public void setRecordingStartTime(long paramLong)
  {
    boolean bool1 = true;
    boolean bool2;
    if (this.mRecordingThread != null)
    {
      bool2 = bool1;
      Preconditions.checkState(bool2);
      if (paramLong < 0L) {
        break label50;
      }
    }
    for (;;)
    {
      Preconditions.checkArgument(bool1);
      this.mStartPos = ((int)(paramLong * this.mBytesPerMsec / 1000L));
      return;
      bool2 = false;
      break;
      label50:
      bool1 = false;
    }
  }
  
  public void startRecording(InputStream paramInputStream, int paramInt1, int paramInt2, AudioStore paramAudioStore, String paramString)
  {
    if (this.mRecordingThread == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mAudioStore = paramAudioStore;
      this.mSampleRate = paramInt1;
      this.mBytesPerMsec = MicrophoneInputStreamFactory.getBytesPerMsec(paramInt1);
      this.mRequestId = paramString;
      this.mStartPos = 0;
      this.mEndPos = 2147483647;
      int i = 10000 * this.mBytesPerMsec;
      this.mMaxFlattenedBufferSize = (60000 * this.mBytesPerMsec);
      this.mRecordingThread = new ClampedLengthRecordingThread(i, this.mMaxFlattenedBufferSize, paramInputStream, paramInt2);
      this.mRecordingThread.start();
      return;
    }
  }
  
  public void waitForRecording()
  {
    doStopRecording(false);
  }
  
  private static class ClampedLengthRecordingThread
    extends Thread
  {
    private byte[] mBuf;
    private final InputStream mInput;
    private final int mMaxSize;
    private final int mReadSize;
    private int mState;
    private int mTotalLength;
    
    ClampedLengthRecordingThread(int paramInt1, int paramInt2, InputStream paramInputStream, int paramInt3)
    {
      this.mMaxSize = paramInt2;
      this.mReadSize = paramInt3;
      this.mInput = paramInputStream;
      this.mBuf = new byte[paramInt1];
      this.mState = 1;
    }
    
    public byte[] getBuffer()
    {
      return this.mBuf;
    }
    
    public int getTotalLength()
    {
      return this.mTotalLength;
    }
    
    public boolean isGood()
    {
      return this.mState == 3;
    }
    
    public boolean isOverflown()
    {
      return this.mState == -2;
    }
    
    public void requestStop()
    {
      try
      {
        if (this.mState == 1)
        {
          this.mState = 2;
          interrupt();
        }
        return;
      }
      finally {}
    }
    
    public void run()
    {
      int i = 0;
      int j = 0;
      try
      {
        for (;;)
        {
          if (j != -1) {}
          try
          {
            if (this.mState == 2)
            {
              this.mTotalLength = i;
              this.mState = 3;
              return;
            }
            int m = i + j;
            if (m > this.mMaxSize)
            {
              this.mTotalLength = this.mMaxSize;
              this.mState = -2;
              return;
            }
            i += j;
            if (i < this.mMaxSize)
            {
              int i1 = Math.min(i + this.mReadSize, this.mMaxSize);
              if (i1 > this.mBuf.length)
              {
                byte[] arrayOfByte = new byte[Math.min(i1 * 2, this.mMaxSize)];
                System.arraycopy(this.mBuf, 0, arrayOfByte, 0, i);
                this.mBuf = arrayOfByte;
              }
              int i2 = i1 - i;
              int i3 = this.mInput.read(this.mBuf, i, i2);
              j = i3;
              continue;
            }
          }
          finally {}
        }
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          try
          {
            this.mTotalLength = i;
            if (this.mState == 2) {}
            for (int k = 3;; k = -1)
            {
              this.mState = k;
              return;
              int n = this.mInput.read(new byte[1]);
              j = n;
              break;
            }
            localObject1 = finally;
          }
          finally {}
        }
      }
      finally
      {
        Closeables.closeQuietly(this.mInput);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.audio.AudioRecorder
 * JD-Core Version:    0.7.0.1
 */