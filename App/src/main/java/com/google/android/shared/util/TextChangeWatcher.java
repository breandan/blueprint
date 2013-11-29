package com.google.android.shared.util;

public abstract interface TextChangeWatcher
{
  public abstract void onCorrectionEnd();
  
  public abstract void onTextChanged(CharSequence paramCharSequence, int paramInt);
  
  public abstract void onTextEditStarted();
  
  public abstract void onTextSelected(CharSequence paramCharSequence, boolean paramBoolean, int paramInt1, int paramInt2);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.TextChangeWatcher
 * JD-Core Version:    0.7.0.1
 */