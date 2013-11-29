package com.google.android.search.shared.ui;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;
import com.google.android.search.shared.api.Suggestion;
import com.google.common.base.Preconditions;

public class SuggestionUiUtils
{
  public static void showRemoveFromHistoryDialog(Context paramContext, Suggestion paramSuggestion, Runnable paramRunnable)
  {
    Preconditions.checkArgument(paramSuggestion.isHistorySuggestion());
    new AlertDialog.Builder(paramContext).setTitle(paramSuggestion.getSuggestionText1()).setMessage(2131363203).setPositiveButton(2131363205, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        this.val$callback.run();
      }
    }).setNegativeButton(2131363206, null).show();
  }
  
  public static void showRemoveFromHistoryFailedToast(Context paramContext)
  {
    Toast.makeText(paramContext, 2131363204, 0).show();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.SuggestionUiUtils
 * JD-Core Version:    0.7.0.1
 */