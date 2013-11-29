package com.google.android.voicesearch.intentapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.fragments.IntentApiController;
import com.google.android.voicesearch.fragments.IntentApiController.Ui;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.logger.EventLoggerService;

public class IntentApiActivity
  extends Activity
  implements IntentApiController.Ui
{
  private IntentApiController mController;
  private IntentApiViewHelper mViewHelper;
  
  private View.OnClickListener buildFinishListener(final int paramInt)
  {
    new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        IntentApiActivity.this.mController.finishWithReturnCode(paramInt);
      }
    };
  }
  
  private void setIntentApiViewAndAttachCallbacks()
  {
    setContentView(2130968729);
    this.mViewHelper = new IntentApiViewHelper(findViewById(2131296677));
    this.mViewHelper.setCallback(new IntentApiViewHelper.Callback()
    {
      public void onCancelRecordingClicked()
      {
        EventLogger.recordClientEvent(18);
        IntentApiActivity.this.mController.cancel();
      }
      
      public void onStopRecordingClicked()
      {
        IntentApiActivity.this.mController.stopListening();
      }
    });
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    VoiceSearchServices localVoiceSearchServices = VelvetServices.get().getVoiceSearchServices();
    this.mController = new IntentApiController(this, localVoiceSearchServices.getSettings(), localVoiceSearchServices.getSpeechLevelSource(), localVoiceSearchServices.getSoundManager(), new IntentApiRecognizerController(localVoiceSearchServices));
    setIntentApiViewAndAttachCallbacks();
  }
  
  protected void onPause()
  {
    this.mController.cancel();
    if (!isChangingConfigurations())
    {
      EventLogger.recordClientEvent(62);
      EventLoggerService.scheduleSendEvents(this);
    }
    super.onPause();
  }
  
  protected void onResume()
  {
    super.onResume();
    IntentApiParams localIntentApiParams = new IntentApiParams(getIntent(), getCallingPackage());
    if (localIntentApiParams.getCallingPackage() == null) {
      finish();
    }
    EventLoggerService.cancelSendEvents(this);
    EventLogger.recordClientEvent(61, localIntentApiParams.getCallingPackage());
    this.mController.onResume(localIntentApiParams);
  }
  
  public void onStart()
  {
    super.onStart();
    this.mController.attachUi(this);
  }
  
  public void onStop()
  {
    this.mController.detachUi(this);
    super.onStop();
  }
  
  public void setLanguage(String paramString)
  {
    this.mViewHelper.setLanguage(paramString);
  }
  
  public void setPromptText(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      this.mViewHelper.setText(2131363461);
      return;
    }
    this.mViewHelper.setText(paramString);
  }
  
  public void setResultInternal(int paramInt)
  {
    setResult(paramInt);
  }
  
  public void setResultInternal(int paramInt, Intent paramIntent)
  {
    setResult(paramInt, paramIntent);
  }
  
  public void setSpeechLevelSource(SpeechLevelSource paramSpeechLevelSource)
  {
    this.mViewHelper.setSpeechLevelSource(paramSpeechLevelSource);
  }
  
  public void showError(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    setContentView(2130968730);
    ((TextView)findViewById(2131296681)).setText(paramInt1);
    Button localButton1 = (Button)findViewById(2131296734);
    Button localButton2 = (Button)findViewById(2131296735);
    Button localButton3 = (Button)findViewById(2131296733);
    if (paramBoolean)
    {
      localButton1.setVisibility(0);
      localButton1.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          IntentApiActivity.this.setIntentApiViewAndAttachCallbacks();
          IntentApiActivity.this.mController.retryLastRecognition();
        }
      });
      localButton2.setVisibility(8);
      localButton3.setVisibility(0);
      localButton3.setOnClickListener(buildFinishListener(paramInt2));
      return;
    }
    localButton3.setVisibility(8);
    localButton1.setVisibility(8);
    localButton2.setVisibility(0);
    localButton2.setOnClickListener(buildFinishListener(paramInt2));
  }
  
  public void showInitializing()
  {
    this.mViewHelper.showInitializing();
  }
  
  public void showListening()
  {
    this.mViewHelper.showListening();
  }
  
  public void showNoMatch(int paramInt)
  {
    setContentView(2130968730);
    ((TextView)findViewById(2131296681)).setText(2131363423);
    Button localButton1 = (Button)findViewById(2131296734);
    Button localButton2 = (Button)findViewById(2131296735);
    Button localButton3 = (Button)findViewById(2131296733);
    localButton1.setVisibility(0);
    localButton1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        IntentApiActivity.this.mController.detachUi(IntentApiActivity.this);
        IntentApiActivity.this.setIntentApiViewAndAttachCallbacks();
        IntentApiActivity.this.mController.attachUi(IntentApiActivity.this);
        IntentApiActivity.this.mController.startOver();
      }
    });
    localButton2.setVisibility(8);
    localButton3.setVisibility(0);
    localButton3.setOnClickListener(buildFinishListener(paramInt));
  }
  
  public void showRecognizing()
  {
    this.mViewHelper.showRecognizing();
  }
  
  public void showRecording()
  {
    this.mViewHelper.showRecording();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.intentapi.IntentApiActivity
 * JD-Core Version:    0.7.0.1
 */