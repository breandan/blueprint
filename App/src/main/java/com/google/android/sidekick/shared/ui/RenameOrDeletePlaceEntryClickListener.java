package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.Intent;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;

public class RenameOrDeletePlaceEntryClickListener
  extends ActionLauncherActivityEntryClickListener
{
  private final Sidekick.Action mDeleteAction;
  private final Sidekick.Action mRenameAction;
  
  public RenameOrDeletePlaceEntryClickListener(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, Sidekick.Entry paramEntry, Sidekick.Action paramAction1, Sidekick.Action paramAction2)
  {
    super(paramContext, paramPredictiveCardContainer, paramEntry, 64);
    this.mRenameAction = paramAction1;
    this.mDeleteAction = paramAction2;
  }
  
  protected void customizeIntent(Intent paramIntent)
  {
    paramIntent.putExtra("action", this.mRenameAction.toByteArray());
    paramIntent.putExtra("delete_action", this.mDeleteAction.toByteArray());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.RenameOrDeletePlaceEntryClickListener
 * JD-Core Version:    0.7.0.1
 */