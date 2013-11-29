package com.google.android.sidekick.main.actions;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.velvet.VelvetServices;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Location;
import javax.annotation.Nullable;

public class EditHomeWorkWorkerFragment
  extends Fragment
  implements SimpleCallback<Boolean>
{
  private EntryProvider mEntryProvider;
  private SendEditActionTask mTask;
  
  public static Bundle buildArguments(Sidekick.Entry paramEntry, Sidekick.Action paramAction, @Nullable Sidekick.Location paramLocation, @Nullable String paramString1, @Nullable String paramString2)
  {
    Bundle localBundle = new Bundle();
    localBundle.putByteArray("entry_key", paramEntry.toByteArray());
    localBundle.putByteArray("action_key", paramAction.toByteArray());
    if (paramLocation != null) {
      localBundle.putByteArray("edited_location_key", paramLocation.toByteArray());
    }
    localBundle.putString("old_name_key", paramString1);
    localBundle.putString("old_address_key", paramString2);
    return localBundle;
  }
  
  public static EditHomeWorkWorkerFragment newInstance(Intent paramIntent)
  {
    Bundle localBundle = paramIntent.getExtras();
    Sidekick.Entry localEntry = (Sidekick.Entry)ProtoUtils.getProtoExtra(localBundle, "entry", Sidekick.Entry.class);
    Sidekick.Action localAction = (Sidekick.Action)ProtoUtils.getProtoExtra(localBundle, "action", Sidekick.Action.class);
    if ((localEntry == null) || (localAction == null)) {
      return null;
    }
    return newInstance(localEntry, localAction, null, null, null);
  }
  
  public static EditHomeWorkWorkerFragment newInstance(Sidekick.Entry paramEntry, Sidekick.Action paramAction, @Nullable Sidekick.Location paramLocation, @Nullable String paramString1, @Nullable String paramString2)
  {
    EditHomeWorkWorkerFragment localEditHomeWorkWorkerFragment = new EditHomeWorkWorkerFragment();
    localEditHomeWorkWorkerFragment.setArguments(buildArguments(paramEntry, paramAction, paramLocation, paramString1, paramString2));
    return localEditHomeWorkWorkerFragment;
  }
  
  protected void editPlaceTaskFinished(boolean paramBoolean, Sidekick.Entry paramEntry, Sidekick.Action paramAction, Sidekick.Location paramLocation, String paramString1, String paramString2)
  {
    if (paramBoolean) {
      this.mEntryProvider.updateEntries(new EditHomeWorkEntryUpdater(paramEntry, paramLocation));
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    VelvetServices localVelvetServices = VelvetServices.get();
    SidekickInjector localSidekickInjector = localVelvetServices.getSidekickInjector();
    this.mEntryProvider = localSidekickInjector.getEntryProvider();
    NetworkClient localNetworkClient = localSidekickInjector.getNetworkClient();
    Clock localClock = localVelvetServices.getCoreServices().getClock();
    Bundle localBundle = getArguments();
    Sidekick.Entry localEntry = ProtoUtils.getEntryFromByteArray(localBundle.getByteArray("entry_key"));
    Sidekick.Action localAction = ProtoUtils.getActionFromByteArray(localBundle.getByteArray("action_key"));
    Sidekick.Location localLocation = (Sidekick.Location)ProtoUtils.getFromByteArray(new Sidekick.Location(), localBundle.getByteArray("edited_location_key"));
    setRetainInstance(true);
    WorkerFragmentSpinnerDialog.show(getFragmentManager(), this);
    this.mTask = new SendEditActionTask(getActivity(), localEntry, localAction, localLocation, localNetworkClient, null, this, localClock);
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
  
  public void onResult(Boolean paramBoolean)
  {
    Bundle localBundle = getArguments();
    Sidekick.Entry localEntry = ProtoUtils.getEntryFromByteArray(localBundle.getByteArray("entry_key"));
    Sidekick.Action localAction = ProtoUtils.getActionFromByteArray(localBundle.getByteArray("action_key"));
    String str1 = localBundle.getString("old_name_key");
    String str2 = localBundle.getString("old_address_key");
    Sidekick.Location localLocation = (Sidekick.Location)ProtoUtils.getFromByteArray(new Sidekick.Location(), localBundle.getByteArray("edited_location_key"));
    editPlaceTaskFinished(paramBoolean.booleanValue(), localEntry, localAction, localLocation, str1, str2);
    WorkerFragmentSpinnerDialog.hide(getFragmentManager());
    this.mTask = null;
    getFragmentManager().popBackStack("editPlaceWorkerFragment", 1);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.EditHomeWorkWorkerFragment
 * JD-Core Version:    0.7.0.1
 */