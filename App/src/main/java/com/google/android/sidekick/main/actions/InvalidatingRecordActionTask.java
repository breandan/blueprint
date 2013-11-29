package com.google.android.sidekick.main.actions;

import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.common.collect.ImmutableSet;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.PlaceData;
import com.google.geo.sidekick.Sidekick.ResponsePayload;

public class InvalidatingRecordActionTask
  extends RecordActionTask
{
  private final EntryProvider mEntryProvider;
  
  public InvalidatingRecordActionTask(NetworkClient paramNetworkClient, EntryProvider paramEntryProvider, Sidekick.Entry paramEntry, Sidekick.Action paramAction, Clock paramClock)
  {
    super(paramNetworkClient, paramEntry, paramAction, paramClock);
    this.mEntryProvider = paramEntryProvider;
  }
  
  public InvalidatingRecordActionTask(NetworkClient paramNetworkClient, EntryProvider paramEntryProvider, Sidekick.Entry paramEntry, Sidekick.Action paramAction, Sidekick.PlaceData paramPlaceData, Clock paramClock)
  {
    super(paramNetworkClient, paramEntry, ImmutableSet.of(paramAction), paramPlaceData, paramClock);
    this.mEntryProvider = paramEntryProvider;
  }
  
  protected void onPostExecute(Sidekick.ResponsePayload paramResponsePayload)
  {
    super.onPostExecute(paramResponsePayload);
    if ((paramResponsePayload != null) && (this.mEntryProvider != null)) {
      this.mEntryProvider.invalidate();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.InvalidatingRecordActionTask
 * JD-Core Version:    0.7.0.1
 */