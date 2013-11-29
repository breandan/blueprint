package com.google.android.search.shared.api;

import javax.annotation.Nonnull;

public abstract interface RecognitionUi
{
  public abstract void setFinalRecognizedText(@Nonnull CharSequence paramCharSequence);
  
  public abstract void showRecognitionState(int paramInt);
  
  public abstract void updateRecognizedText(String paramString1, String paramString2);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.api.RecognitionUi
 * JD-Core Version:    0.7.0.1
 */