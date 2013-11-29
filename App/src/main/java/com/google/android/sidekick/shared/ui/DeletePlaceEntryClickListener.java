package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.Intent;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;

public class DeletePlaceEntryClickListener
  extends ActionLauncherActivityEntryClickListener
{
  private final Sidekick.Action mDeleteAction;
  
  public DeletePlaceEntryClickListener(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, Sidekick.Entry paramEntry, Sidekick.Action paramAction)
  {
    super(paramContext, paramPredictiveCardContainer, paramEntry, 62);
    this.mDeleteAction = paramAction;
  }
  
  protected void customizeIntent(Intent paramIntent)
  {
    ProtoUtils.putProtoExtra(paramIntent, "action", this.mDeleteAction);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.DeletePlaceEntryClickListener
 * JD-Core Version:    0.7.0.1
 */