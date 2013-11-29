package com.google.android.sidekick.main.actions;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;

public class DeletePlaceDialogFragment
  extends DialogFragment
{
  private Sidekick.Action mDeleteAction;
  private Sidekick.Entry mEntry;
  
  public static DeletePlaceDialogFragment newInstance(Intent paramIntent)
  {
    Bundle localBundle = paramIntent.getExtras();
    Sidekick.Entry localEntry = (Sidekick.Entry)ProtoUtils.getProtoExtra(localBundle, "entry", Sidekick.Entry.class);
    Sidekick.Action localAction = (Sidekick.Action)ProtoUtils.getProtoExtra(localBundle, "action", Sidekick.Action.class);
    if ((localEntry == null) || (localAction == null)) {
      return null;
    }
    return newInstance(localEntry, localAction);
  }
  
  public static DeletePlaceDialogFragment newInstance(Sidekick.Entry paramEntry, Sidekick.Action paramAction)
  {
    Bundle localBundle = new Bundle();
    localBundle.putByteArray("entry_key", paramEntry.toByteArray());
    localBundle.putByteArray("delete_action_key", paramAction.toByteArray());
    DeletePlaceDialogFragment localDeletePlaceDialogFragment = new DeletePlaceDialogFragment();
    localDeletePlaceDialogFragment.setArguments(localBundle);
    return localDeletePlaceDialogFragment;
  }
  
  private void startDeletePlaceTask()
  {
    FragmentManager localFragmentManager = getFragmentManager();
    if (localFragmentManager == null) {}
    while (localFragmentManager.findFragmentByTag("deletePlaceWorkerFragment") != null) {
      return;
    }
    DeletePlaceWorkerFragment localDeletePlaceWorkerFragment = DeletePlaceWorkerFragment.newInstance(this.mEntry, this.mDeleteAction);
    localFragmentManager.beginTransaction().addToBackStack("deletePlaceWorkerFragment").add(localDeletePlaceWorkerFragment, "deletePlaceWorkerFragment").commit();
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    Bundle localBundle = getArguments();
    this.mEntry = ProtoUtils.getEntryFromByteArray(localBundle.getByteArray("entry_key"));
    this.mDeleteAction = ProtoUtils.getActionFromByteArray(localBundle.getByteArray("delete_action_key"));
    final FragmentLaunchingAlertDialog localFragmentLaunchingAlertDialog = new FragmentLaunchingAlertDialog(getActivity(), getFragmentManager(), 2131362539);
    localFragmentLaunchingAlertDialog.setPositiveButton(17039370, new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        DeletePlaceDialogFragment.this.startDeletePlaceTask();
      }
    });
    localFragmentLaunchingAlertDialog.setNegativeButton(17039360, new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        localFragmentLaunchingAlertDialog.cancel();
      }
    });
    localFragmentLaunchingAlertDialog.getWindow().setSoftInputMode(2);
    return localFragmentLaunchingAlertDialog;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.DeletePlaceDialogFragment
 * JD-Core Version:    0.7.0.1
 */