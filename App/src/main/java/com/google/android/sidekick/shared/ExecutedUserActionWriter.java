package com.google.android.sidekick.shared;

import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import java.util.List;

public abstract interface ExecutedUserActionWriter
{
  public abstract void saveAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction);
  
  public abstract void saveClickAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction, Sidekick.ClickAction paramClickAction);
  
  public abstract void saveExecutedUserActions(List<Sidekick.ExecutedUserAction> paramList);
  
  public abstract void saveViewAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction, long paramLong, int paramInt, boolean paramBoolean);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ExecutedUserActionWriter
 * JD-Core Version:    0.7.0.1
 */