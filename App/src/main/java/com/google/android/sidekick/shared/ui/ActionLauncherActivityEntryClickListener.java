package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActionLauncherUtil;
import com.google.geo.sidekick.Sidekick.Entry;

public class ActionLauncherActivityEntryClickListener
  extends EntryClickListener
{
  private final Context mContext;
  
  public ActionLauncherActivityEntryClickListener(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, Sidekick.Entry paramEntry, int paramInt)
  {
    super(paramPredictiveCardContainer, paramEntry, paramInt);
    this.mContext = paramContext;
  }
  
  protected void customizeIntent(Intent paramIntent) {}
  
  protected void onEntryClick(View paramView)
  {
    Intent localIntent = ActionLauncherUtil.createActionLauncherIntent(this.mContext);
    localIntent.putExtra("action_type", this.mActionType);
    localIntent.putExtra("entry", this.mEntry.toByteArray());
    customizeIntent(localIntent);
    this.mContext.startActivity(localIntent);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.ActionLauncherActivityEntryClickListener
 * JD-Core Version:    0.7.0.1
 */