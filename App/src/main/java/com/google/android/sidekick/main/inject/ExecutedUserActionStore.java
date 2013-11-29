package com.google.android.sidekick.main.inject;

import com.google.android.sidekick.shared.ExecutedUserActionWriter;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import java.util.List;

public abstract interface ExecutedUserActionStore
  extends ExecutedUserActionWriter
{
  public abstract void addDeferredAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction);
  
  public abstract void commitDeferredActions();
  
  public abstract List<Sidekick.ExecutedUserAction> flush();
  
  public abstract boolean hasExecutedUserAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction);
  
  public abstract void persist();
  
  public abstract void postDeleteStore();
  
  public abstract boolean removeDeferredAction(Sidekick.Entry paramEntry, Sidekick.Action paramAction);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.ExecutedUserActionStore
 * JD-Core Version:    0.7.0.1
 */