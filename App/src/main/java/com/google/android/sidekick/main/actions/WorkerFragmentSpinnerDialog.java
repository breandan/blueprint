package com.google.android.sidekick.main.actions;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class WorkerFragmentSpinnerDialog
  extends DialogFragment
{
  private String mWorkerFragmentTag;
  
  static void hide(FragmentManager paramFragmentManager)
  {
    WorkerFragmentSpinnerDialog localWorkerFragmentSpinnerDialog = (WorkerFragmentSpinnerDialog)paramFragmentManager.findFragmentByTag("spinner_dialog");
    if (localWorkerFragmentSpinnerDialog != null) {
      localWorkerFragmentSpinnerDialog.dismissAllowingStateLoss();
    }
  }
  
  static void show(FragmentManager paramFragmentManager, Fragment paramFragment)
  {
    if ((WorkerFragmentSpinnerDialog)paramFragmentManager.findFragmentByTag("spinner_dialog") == null)
    {
      WorkerFragmentSpinnerDialog localWorkerFragmentSpinnerDialog = new WorkerFragmentSpinnerDialog();
      Bundle localBundle = new Bundle();
      localBundle.putString("worker_tag_key", paramFragment.getTag());
      localWorkerFragmentSpinnerDialog.setArguments(localBundle);
      localWorkerFragmentSpinnerDialog.show(paramFragmentManager, "spinner_dialog");
    }
  }
  
  public void onCancel(DialogInterface paramDialogInterface)
  {
    dismiss();
    Fragment localFragment = getFragmentManager().findFragmentByTag(this.mWorkerFragmentTag);
    if (localFragment != null) {
      getFragmentManager().beginTransaction().remove(localFragment).commitAllowingStateLoss();
    }
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    this.mWorkerFragmentTag = getArguments().getString("worker_tag_key");
    ProgressDialog localProgressDialog = new ProgressDialog(getActivity());
    localProgressDialog.setTitle(2131363214);
    localProgressDialog.setIndeterminate(true);
    return localProgressDialog;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.WorkerFragmentSpinnerDialog
 * JD-Core Version:    0.7.0.1
 */