package com.embryo.android.voicesearch.serviceapi;

import android.content.Intent;
import android.speech.RecognitionService;
import android.util.Log;

import com.google.android.velvet.VelvetServices;
import com.embryo.android.voicesearch.VoiceSearchServices;
import com.embryo.android.voicesearch.settings.Settings;

public class GoogleRecognitionService
        extends RecognitionService {
    private com.embryo.android.voicesearch.serviceapi.GoogleRecognitionServiceImpl mGoogleRecognitionServiceImpl;
    private Settings mSettings;

    private void initGooogleRecognitionImpl() {
        VoiceSearchServices localVoiceSearchServices = VelvetServices.get().getVoiceSearchServices();
        LevelsGenerator localLevelsGenerator = new LevelsGenerator(VelvetServices.get().getAsyncServices().getUiThreadExecutor(), localVoiceSearchServices.getSpeechLevelSource());
        this.mGoogleRecognitionServiceImpl = new com.embryo.android.voicesearch.serviceapi.GoogleRecognitionServiceImpl(localVoiceSearchServices.getRecognizer(), localLevelsGenerator, localVoiceSearchServices.getMainThreadExecutor());
    }

    protected void onCancel(RecognitionService.Callback paramCallback) {
        if (this.mGoogleRecognitionServiceImpl == null) {
            Log.w("GoogleRecognitionService", "Cancel is called before startListening");
            return;
        }
        this.mGoogleRecognitionServiceImpl.cancel();
    }

    public void onCreate() {
        super.onCreate();
        this.mSettings = VelvetServices.get().getVoiceSearchServices().getSettings();
    }

    public void onDestroy() {
        if (this.mGoogleRecognitionServiceImpl != null) {
            this.mGoogleRecognitionServiceImpl.destroy();
        }
        super.onDestroy();
    }

    protected void onStartListening(Intent paramIntent, RecognitionService.Callback paramCallback) {
        if (this.mGoogleRecognitionServiceImpl == null) {
            initGooogleRecognitionImpl();
        }
        this.mGoogleRecognitionServiceImpl.startListening(new com.embryo.android.voicesearch.serviceapi.GoogleRecognitionParams(paramIntent, this.mSettings), paramCallback);
    }

    protected void onStopListening(RecognitionService.Callback paramCallback) {
        if (this.mGoogleRecognitionServiceImpl == null) {
            Log.w("GoogleRecognitionService", "StopListening is called before startListening");
            return;
        }
        this.mGoogleRecognitionServiceImpl.stopListening();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     GoogleRecognitionService

 * JD-Core Version:    0.7.0.1

 */