package com.google.android.search.core.ears;

import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ExtraPreconditions.ThreadCheck;
import com.google.android.speech.Recognizer;
import com.google.android.speech.exception.NoMatchRecognizeException;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.exception.SoundSearchRecognizeException;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.android.speech.listeners.RecognitionEventListenerAdapter;
import com.google.android.speech.params.AudioInputParams;
import com.google.android.speech.params.AudioInputParams.Builder;
import com.google.android.speech.params.SessionParams;
import com.google.android.speech.params.SessionParams.Builder;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.audio.AudioTrackSoundManager;
import com.google.audio.ears.proto.EarsService.EarsResult;
import com.google.audio.ears.proto.EarsService.EarsResultsResponse;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class SoundSearchController
{
  private boolean mDisableBeeps;
  private SoundSearchListener mListener;
  private final Executor mMainThreadExecutor;
  private RecognitionEventListener mRecognitionListener;
  private boolean mStarted;
  private final ExtraPreconditions.ThreadCheck mThreadCheck;
  private final VoiceSearchServices mVss;
  
  public SoundSearchController(VoiceSearchServices paramVoiceSearchServices, Executor paramExecutor)
  {
    this.mVss = paramVoiceSearchServices;
    this.mMainThreadExecutor = paramExecutor;
    this.mThreadCheck = ExtraPreconditions.createSameThreadCheck();
  }
  
  private void cancelInternal(boolean paramBoolean)
  {
    if (this.mStarted)
    {
      this.mVss.getRecognizer().cancel(this.mRecognitionListener);
      this.mListener = null;
      this.mRecognitionListener = null;
      this.mStarted = false;
      if ((paramBoolean) && (!this.mDisableBeeps)) {
        this.mVss.getSoundManager().playNoInputSound();
      }
    }
  }
  
  private SessionParams createSessionParams(Query paramQuery)
  {
    AudioInputParams localAudioInputParams = new AudioInputParams.Builder().setNoiseSuppressionEnabled(false).setPlayBeepEnabled(false).setSamplingRate(11025).build();
    SessionParams.Builder localBuilder = new SessionParams.Builder().setSpokenBcp47Locale("en-US");
    if (paramQuery.isMusicSearch()) {}
    for (int i = 7;; i = 8) {
      return localBuilder.setMode(i).setSoundSearchTtsEnabled(paramQuery.shouldPlayTts()).setAudioInputParams(localAudioInputParams).build();
    }
  }
  
  public void cancel()
  {
    this.mThreadCheck.check();
    cancelInternal(true);
  }
  
  public void start(SoundSearchListener paramSoundSearchListener, Query paramQuery)
  {
    if (paramSoundSearchListener != null) {}
    for (boolean bool1 = true;; bool1 = false)
    {
      Preconditions.checkArgument(bool1);
      boolean bool2;
      if (!paramQuery.isMusicSearch())
      {
        boolean bool3 = paramQuery.isTvSearch();
        bool2 = false;
        if (!bool3) {}
      }
      else
      {
        bool2 = true;
      }
      Preconditions.checkArgument(bool2);
      this.mThreadCheck.check();
      if (!this.mStarted)
      {
        this.mListener = paramSoundSearchListener;
        this.mStarted = true;
        this.mDisableBeeps = paramQuery.isPredictiveTvSearch();
        this.mRecognitionListener = new MyListener(null);
        this.mVss.getRecognizer().startListening(createSessionParams(paramQuery), this.mRecognitionListener, this.mMainThreadExecutor, null);
      }
      return;
    }
  }
  
  private final class MyListener
    extends RecognitionEventListenerAdapter
  {
    private boolean mHasResult;
    
    private MyListener() {}
    
    public void onDone()
    {
      SoundSearchController.this.mThreadCheck.check();
      SoundSearchController.SoundSearchListener localSoundSearchListener;
      SoundSearchController localSoundSearchController;
      if (SoundSearchController.this.mStarted)
      {
        localSoundSearchListener = SoundSearchController.this.mListener;
        localSoundSearchController = SoundSearchController.this;
        if (this.mHasResult) {
          break label84;
        }
      }
      label84:
      for (boolean bool = true;; bool = false)
      {
        localSoundSearchController.cancelInternal(bool);
        if (!this.mHasResult) {
          localSoundSearchListener.onNoSoundSearchMatch(new SoundSearchRecognizeException(new NoMatchRecognizeException()));
        }
        localSoundSearchListener.onDone();
        return;
      }
    }
    
    public void onError(RecognizeException paramRecognizeException)
    {
      SoundSearchController.this.mThreadCheck.check();
      if (SoundSearchController.this.mStarted)
      {
        if (!SoundSearchController.this.mDisableBeeps) {
          SoundSearchController.this.mVss.getSoundManager().playErrorSound();
        }
        SoundSearchController.SoundSearchListener localSoundSearchListener = SoundSearchController.this.mListener;
        SoundSearchController.this.cancelInternal(false);
        localSoundSearchListener.onSoundSearchError(new SoundSearchRecognizeException(paramRecognizeException));
      }
    }
    
    public void onMediaDataResult(byte[] paramArrayOfByte)
    {
      SoundSearchController.this.mThreadCheck.check();
      if (SoundSearchController.this.mStarted) {
        SoundSearchController.this.mListener.onTtsAvailable(paramArrayOfByte);
      }
    }
    
    public void onReadyForSpeech()
    {
      if (SoundSearchController.this.mStarted) {
        SoundSearchController.this.mListener.onListening();
      }
    }
    
    public void onSoundSearchResult(final EarsService.EarsResultsResponse paramEarsResultsResponse)
    {
      SoundSearchController.this.mThreadCheck.check();
      EarsService.EarsResult localEarsResult;
      if ((SoundSearchController.this.mStarted) && (!this.mHasResult))
      {
        localEarsResult = EarsResultParser.getFirstEarsResultWithMusic(paramEarsResultsResponse.getResultList());
        if (localEarsResult == null) {
          break label109;
        }
        SoundSearchController.this.mVss.getExecutorService().execute(new Runnable()
        {
          public void run()
          {
            SoundSearchController.this.mVss.getEarsProviderHelper().insertHeardMatch(paramEarsResultsResponse);
          }
        });
      }
      for (;;)
      {
        if (localEarsResult != null)
        {
          this.mHasResult = true;
          SoundSearchController.this.mVss.getRecognizer().stopListening(SoundSearchController.this.mRecognitionListener);
          SoundSearchController.this.mListener.onSoundSearchResult(paramEarsResultsResponse);
        }
        return;
        label109:
        localEarsResult = EarsResultParser.getFirstEarsResultWithTv(paramEarsResultsResponse.getResultList());
      }
    }
  }
  
  public static abstract interface SoundSearchListener
  {
    public abstract void onDone();
    
    public abstract void onListening();
    
    public abstract void onNoSoundSearchMatch(SoundSearchRecognizeException paramSoundSearchRecognizeException);
    
    public abstract void onSoundSearchError(SoundSearchRecognizeException paramSoundSearchRecognizeException);
    
    public abstract void onSoundSearchResult(EarsService.EarsResultsResponse paramEarsResultsResponse);
    
    public abstract void onTtsAvailable(byte[] paramArrayOfByte);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.ears.SoundSearchController
 * JD-Core Version:    0.7.0.1
 */