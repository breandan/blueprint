package com.google.android.voicesearch.handsfree;

import com.google.speech.recognizer.api.RecognizerProtos.Hypothesis;
import com.google.speech.recognizer.api.RecognizerProtos.RecognitionEvent;
import com.google.speech.recognizer.api.RecognizerProtos.RecognitionResult;
import com.google.speech.recognizer.api.RecognizerProtos.SemanticResult;
import speech.InterpretationProto.Interpretation;
import speech.InterpretationProto.Slot;

class InterpretationProcessor
{
  private final Listener mListener;
  
  public InterpretationProcessor(Listener paramListener)
  {
    this.mListener = paramListener;
  }
  
  private String getInterpretationValue(RecognizerProtos.RecognitionEvent paramRecognitionEvent)
  {
    String str;
    if (paramRecognitionEvent.getEventType() != 1)
    {
      str = null;
      return str;
    }
    if (!paramRecognitionEvent.hasResult()) {
      return null;
    }
    RecognizerProtos.RecognitionResult localRecognitionResult = paramRecognitionEvent.getResult();
    for (int i = 0;; i++)
    {
      if (i >= localRecognitionResult.getHypothesisCount()) {
        break label75;
      }
      RecognizerProtos.Hypothesis localHypothesis = localRecognitionResult.getHypothesis(i);
      if (localHypothesis.hasSemanticResult())
      {
        str = getInterpretationValue(localHypothesis.getSemanticResult());
        if (str != null) {
          break;
        }
      }
    }
    label75:
    return null;
  }
  
  private String getInterpretationValue(RecognizerProtos.SemanticResult paramSemanticResult)
  {
    if (paramSemanticResult.getInterpretationCount() == 0) {}
    InterpretationProto.Interpretation localInterpretation;
    do
    {
      return null;
      localInterpretation = paramSemanticResult.getInterpretation(0);
    } while ((localInterpretation.getSlotCount() == 0) || (!localInterpretation.getSlot(0).hasValue()));
    return localInterpretation.getSlot(0).getValue();
  }
  
  private String[] getInterpretations(RecognizerProtos.RecognitionEvent paramRecognitionEvent)
  {
    String str = getInterpretationValue(paramRecognitionEvent);
    if (str != null) {
      return str.trim().split(" ");
    }
    return null;
  }
  
  public boolean handleRecognitionEvent(RecognizerProtos.RecognitionEvent paramRecognitionEvent)
  {
    String[] arrayOfString = getInterpretations(paramRecognitionEvent);
    int i = arrayOfString.length;
    int j = 0;
    for (;;)
    {
      String str;
      if (j < i)
      {
        str = arrayOfString[j];
        if ("_cancel".equals(str))
        {
          this.mListener.onCancel();
          return true;
        }
        if ("_okay".equals(str))
        {
          this.mListener.onConfirm();
          return true;
        }
        if (!str.startsWith("_select")) {}
      }
      try
      {
        int k = Integer.parseInt(str.substring(1 + "_select".length()));
        this.mListener.onSelect(k);
        label104:
        j++;
        continue;
        return false;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        break label104;
      }
    }
  }
  
  public static abstract interface Listener
  {
    public abstract void onCancel();
    
    public abstract void onConfirm();
    
    public abstract void onSelect(int paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.handsfree.InterpretationProcessor
 * JD-Core Version:    0.7.0.1
 */