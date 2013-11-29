package com.google.android.sidekick.main.actions;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.velvet.VelvetServices;
import com.google.common.collect.ImmutableList;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ResponsePayload;

public class DeletePlaceWorkerFragment
  extends Fragment
{
  private SendDeleteActionTask mTask;
  
  private void handleResponse(boolean paramBoolean)
  {
    Sidekick.Entry localEntry = ProtoUtils.getEntryFromByteArray(getArguments().getByteArray("entry_key"));
    WorkerFragmentSpinnerDialog.hide(getFragmentManager());
    if (paramBoolean) {
      VelvetServices.get().getSidekickInjector().getEntryProvider().handleDismissedEntries(ImmutableList.of(localEntry));
    }
    this.mTask = null;
    getFragmentManager().popBackStack("deletePlaceWorkerFragment", 1);
  }
  
  public static DeletePlaceWorkerFragment newInstance(Sidekick.Entry paramEntry, Sidekick.Action paramAction)
  {
    Bundle localBundle = new Bundle();
    localBundle.putByteArray("entry_key", paramEntry.toByteArray());
    localBundle.putByteArray("action_key", paramAction.toByteArray());
    DeletePlaceWorkerFragment localDeletePlaceWorkerFragment = new DeletePlaceWorkerFragment();
    localDeletePlaceWorkerFragment.setArguments(localBundle);
    return localDeletePlaceWorkerFragment;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    VelvetServices localVelvetServices = VelvetServices.get();
    NetworkClient localNetworkClient = localVelvetServices.getSidekickInjector().getNetworkClient();
    Clock localClock = localVelvetServices.getCoreServices().getClock();
    Bundle localBundle = getArguments();
    Sidekick.Entry localEntry = ProtoUtils.getEntryFromByteArray(localBundle.getByteArray("entry_key"));
    Sidekick.Action localAction = ProtoUtils.getActionFromByteArray(localBundle.getByteArray("action_key"));
    setRetainInstance(true);
    WorkerFragmentSpinnerDialog.show(getFragmentManager(), this);
    this.mTask = new SendDeleteActionTask(localEntry, localAction, localNetworkClient, localClock);
    this.mTask.execute(new Void[0]);
  }
  
  public void onDestroy()
  {
    if (this.mTask != null) {
      this.mTask.cancel(true);
    }
    this.mTask = null;
    super.onDestroy();
  }
  
  class SendDeleteActionTask
    extends InvalidatingRecordActionTask
  {
    public SendDeleteActionTask(Sidekick.Entry paramEntry, Sidekick.Action paramAction, NetworkClient paramNetworkClient, Clock paramClock)
    {
      super(null, paramEntry, paramAction, paramClock);
    }
    
    protected void onPostExecute(Sidekick.ResponsePayload paramResponsePayload)
    {
      super.onPostExecute(paramResponsePayload);
      DeletePlaceWorkerFragment localDeletePlaceWorkerFragment = DeletePlaceWorkerFragment.this;
      if (paramResponsePayload != null) {}
      for (boolean bool = true;; bool = false)
      {
        localDeletePlaceWorkerFragment.handleResponse(bool);
        return;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.DeletePlaceWorkerFragment
 * JD-Core Version:    0.7.0.1
 */