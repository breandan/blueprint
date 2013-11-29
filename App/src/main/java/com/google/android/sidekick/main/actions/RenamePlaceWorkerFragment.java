package com.google.android.sidekick.main.actions;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.velvet.VelvetServices;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.PlaceData;
import com.google.geo.sidekick.Sidekick.ResponsePayload;

public class RenamePlaceWorkerFragment
  extends Fragment
{
  private EntryProvider mEntryProvider;
  private SidekickInjector mSidekickInjector;
  private SendRenameActionTask mTask;
  
  private void handleResponse(boolean paramBoolean)
  {
    Bundle localBundle = getArguments();
    Sidekick.Entry localEntry = ProtoUtils.getEntryFromByteArray(localBundle.getByteArray("entry_key"));
    Sidekick.PlaceData localPlaceData = (Sidekick.PlaceData)ProtoUtils.getFromByteArray(new Sidekick.PlaceData(), localBundle.getByteArray("renamed_place_key"));
    WorkerFragmentSpinnerDialog.hide(getFragmentManager());
    if (paramBoolean) {
      this.mEntryProvider.updateEntries(new RenamePlaceEntryUpdater(localEntry, localPlaceData));
    }
    this.mTask = null;
    getFragmentManager().popBackStack("editPlaceWorkerFragment", 1);
  }
  
  public static RenamePlaceWorkerFragment newInstance(Sidekick.Entry paramEntry, Sidekick.Action paramAction, Sidekick.PlaceData paramPlaceData)
  {
    Bundle localBundle = new Bundle();
    localBundle.putByteArray("entry_key", paramEntry.toByteArray());
    localBundle.putByteArray("action_key", paramAction.toByteArray());
    localBundle.putByteArray("renamed_place_key", paramPlaceData.toByteArray());
    RenamePlaceWorkerFragment localRenamePlaceWorkerFragment = new RenamePlaceWorkerFragment();
    localRenamePlaceWorkerFragment.setArguments(localBundle);
    return localRenamePlaceWorkerFragment;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mSidekickInjector = VelvetServices.get().getSidekickInjector();
    this.mEntryProvider = this.mSidekickInjector.getEntryProvider();
    Bundle localBundle = getArguments();
    Sidekick.Entry localEntry = ProtoUtils.getEntryFromByteArray(localBundle.getByteArray("entry_key"));
    Sidekick.Action localAction = ProtoUtils.getActionFromByteArray(localBundle.getByteArray("action_key"));
    Sidekick.PlaceData localPlaceData = (Sidekick.PlaceData)ProtoUtils.getFromByteArray(new Sidekick.PlaceData(), localBundle.getByteArray("renamed_place_key"));
    setRetainInstance(true);
    WorkerFragmentSpinnerDialog.show(getFragmentManager(), this);
    this.mTask = new SendRenameActionTask(localEntry, localAction, localPlaceData, VelvetServices.get().getCoreServices().getClock(), this.mSidekickInjector.getNetworkClient());
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
  
  class SendRenameActionTask
    extends InvalidatingRecordActionTask
  {
    public SendRenameActionTask(Sidekick.Entry paramEntry, Sidekick.Action paramAction, Sidekick.PlaceData paramPlaceData, Clock paramClock, NetworkClient paramNetworkClient)
    {
      super(null, paramEntry, paramAction, paramPlaceData, paramClock);
    }
    
    protected void onPostExecute(Sidekick.ResponsePayload paramResponsePayload)
    {
      super.onPostExecute(paramResponsePayload);
      if (paramResponsePayload != null)
      {
        Toast.makeText(RenamePlaceWorkerFragment.this.getActivity(), 2131362210, 0).show();
        RenamePlaceWorkerFragment.this.handleResponse(true);
        return;
      }
      RenamePlaceWorkerFragment.this.handleResponse(false);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.RenamePlaceWorkerFragment
 * JD-Core Version:    0.7.0.1
 */