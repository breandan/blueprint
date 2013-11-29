package com.google.android.search.shared.ui;

import java.util.regex.Pattern;

public abstract interface StreamingTextView
{
  public static final Pattern SPLIT_PATTERN = Pattern.compile("\\S+");
  
  public abstract void reset();
  
  public abstract void setFinalRecognizedText(String paramString);
  
  public abstract void setGravity(int paramInt);
  
  public abstract void setTextSize(int paramInt, float paramFloat);
  
  public abstract void updateRecognizedText(String paramString1, String paramString2);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.StreamingTextView
 * JD-Core Version:    0.7.0.1
 */