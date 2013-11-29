package com.google.android.sidekick.main.actions;

import android.content.Context;
import android.widget.Toast;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.DataBackendVersionStore;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.notifications.NotificationRefreshService;
import com.google.common.collect.ImmutableSet;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ActionsResponse;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ResponsePayload;

public class SnoozeReminderTask
  extends RecordActionTask
{
  private final Context mContext;
  private final DataBackendVersionStore mDataBackendVersionStore;
  private final Sidekick.Entry mEntry;
  
  public SnoozeReminderTask(NetworkClient paramNetworkClient, Context paramContext, Sidekick.Entry paramEntry, Sidekick.Action paramAction, Clock paramClock, DataBackendVersionStore paramDataBackendVersionStore)
  {
    super(paramNetworkClient, paramEntry, paramAction, paramClock);
    this.mContext = paramContext;
    this.mEntry = paramEntry;
    this.mDataBackendVersionStore = paramDataBackendVersionStore;
  }
  
  protected void onPostExecute(Sidekick.ResponsePayload paramResponsePayload)
  {
    super.onPostExecute(paramResponsePayload);
    if ((paramResponsePayload != null) && (paramResponsePayload.hasActionsResponse())) {
      this.mDataBackendVersionStore.requireDataVersions(paramResponsePayload.getActionsResponse().getMinimumDataVersionList());
    }
    this.mContext.startService(NotificationRefreshService.getDeleteNotificationIntent(this.mContext, ImmutableSet.of(this.mEntry)));
    if (paramResponsePayload == null) {
      Toast.makeText(this.mContext, 2131362725, 0).show();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.SnoozeReminderTask
 * JD-Core Version:    0.7.0.1
 */