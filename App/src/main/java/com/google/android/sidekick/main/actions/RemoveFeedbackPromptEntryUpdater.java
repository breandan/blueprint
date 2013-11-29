package com.google.android.sidekick.main.actions;

import com.google.android.shared.util.ProtoUtils;
import com.google.android.sidekick.main.entry.EntryUpdater.EntryUpdaterFunc;
import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.geo.sidekick.Sidekick.Entry;
import javax.annotation.Nullable;

public class RemoveFeedbackPromptEntryUpdater
  implements EntryUpdater.EntryUpdaterFunc
{
  private final ProtoKey<Sidekick.Entry> mTargetEntryKey;
  
  public RemoveFeedbackPromptEntryUpdater(Sidekick.Entry paramEntry)
  {
    this.mTargetEntryKey = new ProtoKey(paramEntry);
  }
  
  @Nullable
  public Sidekick.Entry apply(ProtoKey<Sidekick.Entry> paramProtoKey)
  {
    if (this.mTargetEntryKey.equals(paramProtoKey))
    {
      Sidekick.Entry localEntry1 = (Sidekick.Entry)paramProtoKey.getProto();
      Sidekick.Entry localEntry2 = (Sidekick.Entry)ProtoUtils.copyOf(localEntry1);
      localEntry1.clearUserPrompt();
      return localEntry2;
    }
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.RemoveFeedbackPromptEntryUpdater
 * JD-Core Version:    0.7.0.1
 */