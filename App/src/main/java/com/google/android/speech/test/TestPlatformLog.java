package com.google.android.speech.test;

import android.util.Log;
import com.google.speech.recognizer.api.RecognizerProtos.Hypothesis;
import com.google.speech.recognizer.api.RecognizerProtos.RecognitionEvent;
import com.google.speech.recognizer.api.RecognizerProtos.RecognitionResult;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class TestPlatformLog
{
  private static boolean sEnableTestPlatformLogging = false;
  
  public static void log(String paramString)
  {
    if (sEnableTestPlatformLogging) {
      Log.i("TestPlatformLog", "TEST_PLATFORM: " + paramString);
    }
  }
  
  public static void logAudioPath(String paramString)
  {
    log("LOGGING_AUDIO: " + paramString);
  }
  
  public static void logError(String paramString)
  {
    log("ERROR: " + paramString);
  }
  
  public static void logResults(RecognizerProtos.RecognitionEvent paramRecognitionEvent)
  {
    if (sEnableTestPlatformLogging)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append("RESULTS: ");
      if (paramRecognitionEvent.getEventType() == 1)
      {
        RecognizerProtos.RecognitionResult localRecognitionResult = paramRecognitionEvent.getCombinedResult();
        if ((localRecognitionResult != null) && (localRecognitionResult.getHypothesisCount() > 0))
        {
          Iterator localIterator = localRecognitionResult.getHypothesisList().iterator();
          while (localIterator.hasNext())
          {
            RecognizerProtos.Hypothesis localHypothesis = (RecognizerProtos.Hypothesis)localIterator.next();
            Locale localLocale = Locale.US;
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = localHypothesis.getText();
            localStringBuffer.append(String.format(localLocale, "result:\"%s\",", arrayOfObject));
          }
          log(localStringBuffer.toString());
        }
      }
    }
  }
  
  public static void setEnabled(boolean paramBoolean)
  {
    sEnableTestPlatformLogging = paramBoolean;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.test.TestPlatformLog
 * JD-Core Version:    0.7.0.1
 */