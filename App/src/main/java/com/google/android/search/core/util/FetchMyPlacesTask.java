package com.google.android.search.core.util;

import android.os.AsyncTask;
import android.util.Pair;
import com.google.android.search.core.preferences.MyPlacesUtil;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.speech.callback.SimpleCallback;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryQuery;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import javax.annotation.Nullable;

public class FetchMyPlacesTask
  extends AsyncTask<Void, Void, Sidekick.EntryResponse>
{
  private final SimpleCallback<Pair<Sidekick.Entry, Sidekick.Entry>> mCallback;
  private final NetworkClient mNetworkClient;
  
  public FetchMyPlacesTask(NetworkClient paramNetworkClient, SimpleCallback<Pair<Sidekick.Entry, Sidekick.Entry>> paramSimpleCallback)
  {
    this.mCallback = ((SimpleCallback)Preconditions.checkNotNull(paramSimpleCallback));
    this.mNetworkClient = ((NetworkClient)Preconditions.checkNotNull(paramNetworkClient));
  }
  
  @Nullable
  protected Sidekick.EntryResponse doInBackground(Void... paramVarArgs)
  {
    Sidekick.EntryQuery localEntryQuery = MyPlacesUtil.buildQuery();
    Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload();
    localRequestPayload.setEntryQuery(localEntryQuery);
    Sidekick.ResponsePayload localResponsePayload = this.mNetworkClient.sendRequestWithoutLocation(localRequestPayload);
    if (localResponsePayload == null) {
      return null;
    }
    return localResponsePayload.getEntryResponse();
  }
  
  protected void onPostExecute(Sidekick.EntryResponse paramEntryResponse)
  {
    this.mCallback.onResult(MyPlacesUtil.getHomeWorkEntries(paramEntryResponse));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.FetchMyPlacesTask
 * JD-Core Version:    0.7.0.1
 */