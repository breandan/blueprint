package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.Intent;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;

public class ConfirmHomeWorkEntryClickListener
  extends ActionLauncherActivityEntryClickListener
{
  private final Sidekick.Action mEditAction;
  
  public ConfirmHomeWorkEntryClickListener(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, Sidekick.Entry paramEntry, Sidekick.Action paramAction)
  {
    super(paramContext, paramPredictiveCardContainer, paramEntry, 61);
    this.mEditAction = paramAction;
  }
  
  protected void customizeIntent(Intent paramIntent)
  {
    ProtoUtils.putProtoExtra(paramIntent, "action", this.mEditAction);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.ConfirmHomeWorkEntryClickListener
 * JD-Core Version:    0.7.0.1
 */