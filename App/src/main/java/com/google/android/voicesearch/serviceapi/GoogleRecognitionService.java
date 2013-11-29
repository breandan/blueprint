package com.google.android.voicesearch.serviceapi;

import android.content.Intent;
import android.speech.RecognitionService;
import android.speech.RecognitionService.Callback;
import android.util.Log;
import com.google.android.search.core.AsyncServices;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.settings.Settings;

public class GoogleRecognitionService
  extends RecognitionService
{
  private GoogleRecognitionServiceImpl mGoogleRecognitionServiceImpl;
  private Settings mSettings;
  
  private void initGooogleRecognitionImpl()
  {
    VoiceSearchServices localVoiceSearchServices = VelvetServices.get().getVoiceSearchServices();
    LevelsGenerator localLevelsGenerator = new LevelsGenerator(VelvetServices.get().getAsyncServices().getUiThreadExecutor(), localVoiceSearchServices.getSpeechLevelSource());
    this.mGoogleRecognitionServiceImpl = new GoogleRecognitionServiceImpl(localVoiceSearchServices.getRecognizer(), localLevelsGenerator, localVoiceSearchServices.getMainThreadExecutor());
  }
  
  protected void onCancel(RecognitionService.Callback paramCallback)
  {
    if (this.mGoogleRecognitionServiceImpl == null)
    {
      Log.w("GoogleRecognitionService", "Cancel is called before startListening");
      return;
    }
    this.mGoogleRecognitionServiceImpl.cancel();
  }
  
  public void onCreate()
  {
    super.onCreate();
    this.mSettings = VelvetServices.get().getVoiceSearchServices().getSettings();
  }
  
  public void onDestroy()
  {
    if (this.mGoogleRecognitionServiceImpl != null) {
      this.mGoogleRecognitionServiceImpl.destroy();
    }
    super.onDestroy();
  }
  
  protected void onStartListening(Intent paramIntent, RecognitionService.Callback paramCallback)
  {
    if (this.mGoogleRecognitionServiceImpl == null) {
      initGooogleRecognitionImpl();
    }
    this.mGoogleRecognitionServiceImpl.startListening(new GoogleRecognitionParams(paramIntent, this.mSettings), paramCallback);
  }
  
  protected void onStopListening(RecognitionService.Callback paramCallback)
  {
    if (this.mGoogleRecognitionServiceImpl == null)
    {
      Log.w("GoogleRecognitionService", "StopListening is called before startListening");
      return;
    }
    this.mGoogleRecognitionServiceImpl.stopListening();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.serviceapi.GoogleRecognitionService
 * JD-Core Version:    0.7.0.1
 */