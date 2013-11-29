package com.google.android.search.core.preferences;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

public class OptOutDialogFragment
  extends DialogFragment
{
  private OptOutSwitchHandler mTarget;
  
  private void optOut(boolean paramBoolean)
  {
    this.mTarget.updatePredictiveCardsEnabledSwitch(null);
    this.mTarget.startOptOutTask(paramBoolean);
  }
  
  public void onCancel(DialogInterface paramDialogInterface)
  {
    this.mTarget.updatePredictiveCardsEnabledSwitch(null);
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    this.mTarget = OptOutSwitchHandler.findOptOutHandler(this);
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
    View localView = getActivity().getLayoutInflater().inflate(2130968767, null);
    final CheckBox localCheckBox = (CheckBox)localView.findViewById(2131296825);
    localBuilder.setTitle(2131362543).setView(localView).setPositiveButton(2131362542, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        OptOutDialogFragment.this.optOut(localCheckBox.isChecked());
        OptOutDialogFragment.this.dismiss();
      }
    }).setNegativeButton(17039360, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        OptOutDialogFragment.this.mTarget.updatePredictiveCardsEnabledSwitch(null);
        OptOutDialogFragment.this.dismiss();
      }
    });
    return localBuilder.create();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.OptOutDialogFragment
 * JD-Core Version:    0.7.0.1
 */