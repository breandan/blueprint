package com.google.android.sidekick.main.actions;

import android.content.Context;
import android.widget.Toast;
import com.google.android.sidekick.shared.util.EntryAction;
import com.google.android.sidekick.shared.util.EntryAction.Callback;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ResponsePayload;

public abstract class EntryActionBase
  implements EntryAction
{
  protected final Sidekick.Action mAction;
  private EntryAction.Callback mCallback;
  protected final Context mContext;
  protected final Sidekick.Entry mEntry;
  
  public EntryActionBase(Context paramContext, Sidekick.Action paramAction, Sidekick.Entry paramEntry, EntryAction.Callback paramCallback)
  {
    this.mContext = paramContext;
    this.mAction = paramAction;
    this.mEntry = paramEntry;
    this.mCallback = paramCallback;
  }
  
  public void failure()
  {
    if (this.mCallback != null)
    {
      this.mCallback.onFailure(this);
      return;
    }
    Toast.makeText(this.mContext, 2131362211, 0).show();
  }
  
  public void success(Sidekick.ResponsePayload paramResponsePayload)
  {
    if (this.mCallback != null) {
      this.mCallback.onSuccess(this, paramResponsePayload);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.EntryActionBase
 * JD-Core Version:    0.7.0.1
 */