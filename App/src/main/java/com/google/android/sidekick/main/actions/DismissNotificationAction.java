package com.google.android.sidekick.main.actions;

import android.content.Context;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ResponsePayload;

public class DismissNotificationAction
  extends EntryActionBase
{
  private final Clock mClock;
  private final NetworkClient mNetworkClient;
  
  public DismissNotificationAction(Context paramContext, Sidekick.Entry paramEntry, Sidekick.Action paramAction, NetworkClient paramNetworkClient, Clock paramClock)
  {
    super(paramContext, paramAction, paramEntry, null);
    this.mNetworkClient = paramNetworkClient;
    this.mClock = paramClock;
  }
  
  public void run()
  {
    new SendDismissActionTask().execute(new Void[0]);
  }
  
  class SendDismissActionTask
    extends RecordActionTask
  {
    public SendDismissActionTask()
    {
      super(DismissNotificationAction.this.mEntry, DismissNotificationAction.this.mAction, DismissNotificationAction.this.mClock);
    }
    
    protected void onPostExecute(Sidekick.ResponsePayload paramResponsePayload)
    {
      super.onPostExecute(paramResponsePayload);
      if (paramResponsePayload != null)
      {
        DismissNotificationAction.this.success(paramResponsePayload);
        return;
      }
      DismissNotificationAction.this.failure();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.DismissNotificationAction
 * JD-Core Version:    0.7.0.1
 */