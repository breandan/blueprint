package com.google.android.search.core.preferences;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class OptOutSpinnerDialog
  extends DialogFragment
{
  public void onCancel(DialogInterface paramDialogInterface)
  {
    dismiss();
    Fragment localFragment = getFragmentManager().findFragmentByTag("opt_out_worker_fragment");
    if (localFragment != null) {
      getFragmentManager().beginTransaction().remove(localFragment).commitAllowingStateLoss();
    }
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    ProgressDialog localProgressDialog = new ProgressDialog(getActivity());
    localProgressDialog.setTitle(2131362495);
    localProgressDialog.setIndeterminate(true);
    return localProgressDialog;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.OptOutSpinnerDialog
 * JD-Core Version:    0.7.0.1
 */