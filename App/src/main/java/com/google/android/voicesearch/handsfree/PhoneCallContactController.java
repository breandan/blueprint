package com.google.android.voicesearch.handsfree;

import android.content.res.Resources;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.listeners.RecognitionEventListenerAdapter;
import com.google.android.voicesearch.audio.AudioTrackSoundManager;
import com.google.android.voicesearch.fragments.HandsFreeRecognizerController;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;
import com.google.speech.recognizer.api.RecognizerProtos.RecognitionEvent;
import javax.annotation.Nullable;

class PhoneCallContactController
{
  private int mActionType;
  @Nullable
  private Contact mContact;
  private final InterpretationProcessor mInterpretationProcessor;
  @Nullable
  private MainController mMainController;
  private final HandsFreeRecognizerController mRecognizerController;
  private final Resources mResources;
  private final AudioTrackSoundManager mSoundManager;
  @Nullable
  private Ui mUi;
  private final ViewDisplayer mViewDisplayer;
  
  public PhoneCallContactController(HandsFreeRecognizerController paramHandsFreeRecognizerController, Resources paramResources, ViewDisplayer paramViewDisplayer, AudioTrackSoundManager paramAudioTrackSoundManager)
  {
    this.mResources = paramResources;
    this.mRecognizerController = paramHandsFreeRecognizerController;
    this.mInterpretationProcessor = new InterpretationProcessor(new InterpretationProcessorListener(null));
    this.mViewDisplayer = paramViewDisplayer;
    this.mSoundManager = paramAudioTrackSoundManager;
  }
  
  private String getTtsPrompt(Contact paramContact)
  {
    if (paramContact.hasName())
    {
      Resources localResources2 = this.mResources;
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = paramContact.getName();
      arrayOfObject2[1] = paramContact.getLabel(this.mResources);
      return localResources2.getString(2131363232, arrayOfObject2);
    }
    Resources localResources1 = this.mResources;
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = PhoneActionUtils.spaceOutDigits(paramContact.getValue());
    return localResources1.getString(2131363233, arrayOfObject1);
  }
  
  public void callContactByTouch()
  {
    EventLogger.recordClientEventWithSource(13, 16777216, Integer.valueOf(this.mActionType));
    this.mMainController.startActivity(PhoneActionUtils.getCallIntent(this.mContact.getValue()));
    this.mMainController.exit();
  }
  
  public void callContactByVoice()
  {
    EventLogger.recordClientEventWithSource(13, 33554432, Integer.valueOf(this.mActionType));
    this.mSoundManager.playRecognitionDoneSound();
    this.mMainController.startActivity(PhoneActionUtils.getCallIntent(this.mContact.getValue()));
    this.mMainController.exit();
  }
  
  public void cancelByTouch()
  {
    EventLogger.recordClientEventWithSource(14, 16777216, Integer.valueOf(this.mActionType));
    this.mContact = null;
    this.mMainController.exit();
  }
  
  public void cancelByVoice()
  {
    EventLogger.recordClientEventWithSource(14, 33554432, Integer.valueOf(this.mActionType));
    this.mSoundManager.playHandsFreeShutDownSound();
    this.mContact = null;
    this.mMainController.exit();
  }
  
  public void handleVoiceError()
  {
    EventLogger.recordClientEventWithSource(14, 50331648, Integer.valueOf(this.mActionType));
    this.mMainController.showError(2131363633, 2131363633);
  }
  
  public void setMainController(MainController paramMainController)
  {
    if (this.mMainController == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mMainController = ((MainController)Preconditions.checkNotNull(paramMainController));
      return;
    }
  }
  
  public void start(Contact paramContact)
  {
    ExtraPreconditions.checkMainThread();
    this.mContact = ((Contact)Preconditions.checkNotNull(paramContact));
    EventLogger.recordSpeechEvent(13, paramContact);
    this.mRecognizerController.startCommandRecognitionNoUi(new RecognizerListener(), 3, getTtsPrompt(paramContact));
    if (paramContact.hasName()) {
      this.mActionType = 10;
    }
    for (this.mUi = this.mViewDisplayer.showPhoneCallContact();; this.mUi = this.mViewDisplayer.showPhoneCallNumber())
    {
      this.mUi.setController(this);
      this.mUi.setContact(paramContact);
      this.mUi.setLanguage(this.mMainController.getSpokenLanguageName());
      return;
      this.mActionType = 28;
    }
  }
  
  private class InterpretationProcessorListener
    implements InterpretationProcessor.Listener
  {
    private InterpretationProcessorListener() {}
    
    public void onCancel()
    {
      PhoneCallContactController.this.cancelByVoice();
    }
    
    public void onConfirm()
    {
      PhoneCallContactController.this.callContactByVoice();
    }
    
    public void onSelect(int paramInt)
    {
      PhoneCallContactController.this.handleVoiceError();
    }
  }
  
  private class RecognizerListener
    extends RecognitionEventListenerAdapter
  {
    private boolean mResponseDispatched = false;
    
    public RecognizerListener() {}
    
    public void onDone()
    {
      
      if (!this.mResponseDispatched)
      {
        this.mResponseDispatched = true;
        PhoneCallContactController.this.handleVoiceError();
      }
    }
    
    public void onEndOfSpeech()
    {
      PhoneCallContactController.this.mUi.showNotListening();
    }
    
    public void onError(RecognizeException paramRecognizeException)
    {
      ExtraPreconditions.checkMainThread();
      PhoneCallContactController.this.handleVoiceError();
      this.mResponseDispatched = true;
    }
    
    public void onNoSpeechDetected()
    {
      ExtraPreconditions.checkMainThread();
      PhoneCallContactController.this.handleVoiceError();
      this.mResponseDispatched = true;
    }
    
    public void onReadyForSpeech()
    {
      PhoneCallContactController.this.mUi.showListening();
    }
    
    public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent)
    {
      
      if ((!this.mResponseDispatched) && (PhoneCallContactController.this.mInterpretationProcessor.handleRecognitionEvent(paramRecognitionEvent))) {
        this.mResponseDispatched = true;
      }
    }
  }
  
  public static abstract interface Ui
    extends HandsFreeRecognitionUi
  {
    public abstract void setContact(Contact paramContact);
    
    public abstract void setController(PhoneCallContactController paramPhoneCallContactController);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.handsfree.PhoneCallContactController
 * JD-Core Version:    0.7.0.1
 */