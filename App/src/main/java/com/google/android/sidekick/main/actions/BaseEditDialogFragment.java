package com.google.android.sidekick.main.actions;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import com.google.android.shared.util.Util;

public class BaseEditDialogFragment
  extends DialogFragment
{
  protected void hideSoftKeyboard(View paramView)
  {
    Util.hideSoftKeyboard(getActivity(), paramView);
  }
  
  public void onDismiss(DialogInterface paramDialogInterface)
  {
    super.onDismiss(paramDialogInterface);
    Activity localActivity = getActivity();
    if (localActivity != null)
    {
      Window localWindow = localActivity.getWindow();
      if (localWindow != null) {
        localWindow.setSoftInputMode(2);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.BaseEditDialogFragment
 * JD-Core Version:    0.7.0.1
 */