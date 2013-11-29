package com.google.android.voicesearch.ime;

import android.content.res.Resources;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import com.google.android.speech.Recognizer;

public abstract interface VoiceImeInputMethodService
{
  public abstract InputConnection getCurrentInputConnection();
  
  public abstract EditorInfo getCurrentInputEditorInfo();
  
  public abstract Recognizer getRecognizer();
  
  public abstract Resources getResources();
  
  public abstract boolean isScreenOn();
  
  public abstract void scheduleSendEvents();
  
  public abstract void switchToLastInputMethod();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.ime.VoiceImeInputMethodService
 * JD-Core Version:    0.7.0.1
 */