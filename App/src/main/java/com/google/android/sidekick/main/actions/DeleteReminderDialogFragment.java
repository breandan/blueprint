package com.google.android.sidekick.main.actions;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.DataBackendVersionStore;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.notifications.NotificationRefreshService;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.velvet.VelvetServices;
import com.google.common.collect.ImmutableSet;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ActionsResponse;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ReminderEntry;
import com.google.geo.sidekick.Sidekick.ResponsePayload;

public class DeleteReminderDialogFragment
  extends DialogFragment
{
  private Sidekick.Entry mEntry;
  private DeleteReminderDialogListener mListener;
  
  public static DeleteReminderDialogFragment newInstance(Fragment paramFragment, Sidekick.Entry paramEntry)
  {
    Bundle localBundle = new Bundle();
    localBundle.putByteArray("ENTRY_KEY", paramEntry.toByteArray());
    DeleteReminderDialogFragment localDeleteReminderDialogFragment = new DeleteReminderDialogFragment();
    localDeleteReminderDialogFragment.setArguments(localBundle);
    localDeleteReminderDialogFragment.setTargetFragment(paramFragment, 0);
    return localDeleteReminderDialogFragment;
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    VelvetServices localVelvetServices = VelvetServices.get();
    final NetworkClient localNetworkClient = localVelvetServices.getSidekickInjector().getNetworkClient();
    final Clock localClock = localVelvetServices.getCoreServices().getClock();
    final DataBackendVersionStore localDataBackendVersionStore = localVelvetServices.getSidekickInjector().getDataBackendVersionStore();
    if (paramBundle != null) {}
    for (this.mEntry = ProtoUtils.getEntryFromByteArray(paramBundle.getByteArray("ENTRY_KEY"));; this.mEntry = ProtoUtils.getEntryFromByteArray(getArguments().getByteArray("ENTRY_KEY")))
    {
      Fragment localFragment = getTargetFragment();
      if ((localFragment instanceof DeleteReminderDialogListener)) {
        this.mListener = ((DeleteReminderDialogListener)localFragment);
      }
      final Context localContext = getActivity().getApplicationContext();
      final FragmentLaunchingAlertDialog localFragmentLaunchingAlertDialog = new FragmentLaunchingAlertDialog(getActivity(), getFragmentManager(), 2131362734);
      localFragmentLaunchingAlertDialog.setPositiveButton(17039379, new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          localContext.startService(NotificationRefreshService.getDeleteNotificationIntent(localContext, ImmutableSet.of(DeleteReminderDialogFragment.this.mEntry)));
          Sidekick.Action localAction = ProtoUtils.findAction(DeleteReminderDialogFragment.this.mEntry, 32, new int[] { 146 });
          if (localAction != null)
          {
            new RecordActionTask(localNetworkClient, DeleteReminderDialogFragment.this.mEntry, localAction, localClock)
            {
              protected void onPostExecute(Sidekick.ResponsePayload paramAnonymous2ResponsePayload)
              {
                if (paramAnonymous2ResponsePayload == null) {
                  Toast.makeText(DeleteReminderDialogFragment.1.this.val$context, 2131363424, 0).show();
                }
                for (;;)
                {
                  super.onPostExecute(paramAnonymous2ResponsePayload);
                  if ((paramAnonymous2ResponsePayload != null) && (DeleteReminderDialogFragment.this.mListener != null)) {
                    DeleteReminderDialogFragment.this.mListener.onReminderDeleted(DeleteReminderDialogFragment.this.mEntry.getReminderEntry().getTaskId());
                  }
                  DeleteReminderDialogFragment.1.this.val$dialog.dismiss();
                  return;
                  if (paramAnonymous2ResponsePayload.hasActionsResponse()) {
                    DeleteReminderDialogFragment.1.this.val$dataBackendVersionStore.requireDataVersions(paramAnonymous2ResponsePayload.getActionsResponse().getMinimumDataVersionList());
                  }
                }
              }
            }.execute(new Void[0]);
            return;
          }
          localFragmentLaunchingAlertDialog.dismiss();
        }
      });
      localFragmentLaunchingAlertDialog.setNegativeButton(17039369, new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          localFragmentLaunchingAlertDialog.dismiss();
        }
      });
      return localFragmentLaunchingAlertDialog;
    }
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putByteArray("ENTRY_KEY", this.mEntry.toByteArray());
  }
  
  public static abstract interface DeleteReminderDialogListener
  {
    public abstract void onReminderDeleted(String paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.DeleteReminderDialogFragment
 * JD-Core Version:    0.7.0.1
 */