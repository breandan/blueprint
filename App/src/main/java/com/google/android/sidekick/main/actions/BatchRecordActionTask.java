package com.google.android.sidekick.main.actions;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.DataBackendVersionStore;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.shared.util.ExecutedUserActionBuilder;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ActionsQuery;
import com.google.geo.sidekick.Sidekick.ActionsResponse;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class BatchRecordActionTask
  extends AsyncTask<Void, Void, Sidekick.ResponsePayload>
{
  private static final String TAG = Tag.getTag(BatchRecordActionTask.class);
  private final int mActionType;
  private final Clock mClock;
  private DataBackendVersionStore mDataBackendVersionStore;
  private final Collection<Sidekick.Entry> mEntries;
  private EntryProvider mEntryProvider;
  private final NetworkClient mNetworkClient;
  
  public BatchRecordActionTask(NetworkClient paramNetworkClient, Collection<Sidekick.Entry> paramCollection, int paramInt, Clock paramClock)
  {
    this.mNetworkClient = paramNetworkClient;
    this.mEntries = paramCollection;
    this.mActionType = paramInt;
    this.mClock = paramClock;
  }
  
  protected Sidekick.ResponsePayload doInBackground(Void... paramVarArgs)
  {
    long l = this.mClock.currentTimeMillis();
    Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload();
    Sidekick.ActionsQuery localActionsQuery = new Sidekick.ActionsQuery();
    Iterator localIterator1 = this.mEntries.iterator();
    for (;;)
    {
      if (!localIterator1.hasNext()) {
        break label131;
      }
      Sidekick.Entry localEntry = (Sidekick.Entry)localIterator1.next();
      Iterator localIterator2 = localEntry.getEntryActionList().iterator();
      if (localIterator2.hasNext())
      {
        Sidekick.Action localAction = (Sidekick.Action)localIterator2.next();
        if (localAction.getType() != this.mActionType) {
          break;
        }
        localActionsQuery.addExecutedUserAction(new ExecutedUserActionBuilder(localEntry, localAction, l).build());
      }
    }
    label131:
    Sidekick.ResponsePayload localResponsePayload;
    if (localActionsQuery.getExecutedUserActionCount() == 0) {
      localResponsePayload = null;
    }
    do
    {
      return localResponsePayload;
      localRequestPayload.setActionsQuery(localActionsQuery);
      localResponsePayload = this.mNetworkClient.sendRequestWithLocation(localRequestPayload);
    } while (localResponsePayload != null);
    Log.e(TAG, "Error sending request to the server");
    return localResponsePayload;
  }
  
  protected void onPostExecute(Sidekick.ResponsePayload paramResponsePayload)
  {
    super.onPostExecute(paramResponsePayload);
    if ((paramResponsePayload != null) && (this.mEntryProvider != null))
    {
      if (paramResponsePayload.hasActionsResponse()) {
        this.mDataBackendVersionStore.requireDataVersions(paramResponsePayload.getActionsResponse().getMinimumDataVersionList());
      }
      this.mEntryProvider.invalidate();
    }
  }
  
  public void setInvalidateOnSuccess(EntryProvider paramEntryProvider, DataBackendVersionStore paramDataBackendVersionStore)
  {
    this.mEntryProvider = ((EntryProvider)Preconditions.checkNotNull(paramEntryProvider));
    this.mDataBackendVersionStore = ((DataBackendVersionStore)Preconditions.checkNotNull(paramDataBackendVersionStore));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.BatchRecordActionTask
 * JD-Core Version:    0.7.0.1
 */