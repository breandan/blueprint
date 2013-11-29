package com.google.android.voicesearch.fragments.action;

import android.annotation.SuppressLint;
import android.os.Parcelable;

@SuppressLint({"ParcelCreator"})
public abstract interface VoiceAction
  extends Parcelable
{
  public abstract <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor);
  
  public abstract boolean canExecute();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.action.VoiceAction
 * JD-Core Version:    0.7.0.1
 */