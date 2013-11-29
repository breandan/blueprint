package com.google.android.sidekick.main.actions;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.shared.util.ExecutedUserActionBuilder;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.collect.ImmutableSet;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ActionsQuery;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import com.google.geo.sidekick.Sidekick.PlaceData;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import java.util.Collection;
import java.util.Iterator;

public class RecordActionTask
  extends AsyncTask<Void, Void, Sidekick.ResponsePayload>
{
  private static final String sTag = Tag.getTag(RecordActionTask.class);
  private final Collection<Sidekick.Action> mActions;
  private final Clock mClock;
  private final Sidekick.Entry mEntry;
  private final NetworkClient mNetworkClient;
  private final Sidekick.PlaceData mNewPlace;
  
  public RecordActionTask(NetworkClient paramNetworkClient, Sidekick.Entry paramEntry, Sidekick.Action paramAction, Clock paramClock)
  {
    this(paramNetworkClient, paramEntry, ImmutableSet.of(paramAction), null, paramClock);
  }
  
  RecordActionTask(NetworkClient paramNetworkClient, Sidekick.Entry paramEntry, Collection<Sidekick.Action> paramCollection, Sidekick.PlaceData paramPlaceData, Clock paramClock)
  {
    this.mNetworkClient = paramNetworkClient;
    this.mEntry = paramEntry;
    this.mActions = paramCollection;
    this.mNewPlace = paramPlaceData;
    this.mClock = paramClock;
  }
  
  protected Sidekick.ExecutedUserAction buildExecutedAction(Sidekick.Action paramAction, long paramLong)
  {
    ExecutedUserActionBuilder localExecutedUserActionBuilder = new ExecutedUserActionBuilder(this.mEntry, paramAction, paramLong);
    if (this.mNewPlace != null) {
      localExecutedUserActionBuilder = localExecutedUserActionBuilder.withCustomPlace(this.mNewPlace);
    }
    return localExecutedUserActionBuilder.build();
  }
  
  protected Sidekick.ResponsePayload doInBackground(Void... paramVarArgs)
  {
    Sidekick.ResponsePayload localResponsePayload = recordAction(this.mClock.currentTimeMillis());
    if (localResponsePayload == null) {
      Log.e(sTag, "Error sending request to the server");
    }
    return localResponsePayload;
  }
  
  Sidekick.ResponsePayload recordAction(long paramLong)
  {
    Sidekick.ActionsQuery localActionsQuery = new Sidekick.ActionsQuery();
    Iterator localIterator = this.mActions.iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Action localAction = (Sidekick.Action)localIterator.next();
      if (localAction != null) {
        localActionsQuery.addExecutedUserAction(buildExecutedAction(localAction, paramLong));
      }
    }
    Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload().setActionsQuery(localActionsQuery);
    return this.mNetworkClient.sendRequestWithLocation(localRequestPayload);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.RecordActionTask
 * JD-Core Version:    0.7.0.1
 */