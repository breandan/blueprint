package com.google.android.sidekick.shared.util;

import com.google.geo.sidekick.Sidekick.ResponsePayload;

public abstract interface EntryAction
  extends Runnable
{
  public static abstract interface Callback
  {
    public abstract void onFailure(EntryAction paramEntryAction);
    
    public abstract void onSuccess(EntryAction paramEntryAction, Sidekick.ResponsePayload paramResponsePayload);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.EntryAction
 * JD-Core Version:    0.7.0.1
 */