package com.google.android.search.core.preferences.cards;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.android.apps.sidekick.StaticEntitiesData;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.file.AsyncFileStorage;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Function;
import com.google.geo.sidekick.Sidekick.FetchStaticEntitiesQuery;
import com.google.geo.sidekick.Sidekick.FetchStaticEntitiesResponse;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.geo.sidekick.Sidekick.SportsTeams;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;

public class SportsEntitiesProvider
{
  private static final String TAG = Tag.getTag(SportsEntitiesProvider.class);
  private final AsyncFileStorage mAsyncFileStorage;
  private final Clock mClock;
  private final NetworkClient mNetworkClient;
  private Sidekick.SportsTeams mSportsEntities;
  private final Fragment mTargetFragment;
  
  public SportsEntitiesProvider(Fragment paramFragment)
  {
    VelvetServices localVelvetServices = VelvetServices.get();
    this.mClock = localVelvetServices.getCoreServices().getClock();
    this.mNetworkClient = localVelvetServices.getSidekickInjector().getNetworkClient();
    this.mAsyncFileStorage = localVelvetServices.getSidekickInjector().getAsyncFileStorage();
    this.mTargetFragment = paramFragment;
  }
  
  public Sidekick.SportsTeams getSportsEntities()
  {
    try
    {
      Sidekick.SportsTeams localSportsTeams = this.mSportsEntities;
      return localSportsTeams;
    }
    finally {}
  }
  
  public boolean isReady()
  {
    for (;;)
    {
      try
      {
        if ((this.mSportsEntities != null) && (this.mSportsEntities.getSportTeamPlayerCount() > 0))
        {
          bool = true;
          return bool;
        }
      }
      finally {}
      boolean bool = false;
    }
  }
  
  public void loadSportsData()
  {
    final Handler local1 = new Handler()
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        SportsEntitiesProvider.this.updateSportsEntities();
      }
    };
    this.mAsyncFileStorage.readFromFile("static_entities", new Function()
    {
      @SuppressLint({"HandlerLeak"})
      public Void apply(byte[] paramAnonymousArrayOfByte)
      {
        int i = 1;
        if (paramAnonymousArrayOfByte != null) {}
        for (;;)
        {
          try
          {
            localStaticEntitiesData = new StaticEntitiesData();
            localStaticEntitiesData.mergeFrom(paramAnonymousArrayOfByte);
            if ((localStaticEntitiesData.hasStaticEntities()) && (localStaticEntitiesData.getStaticEntities().hasSportsEntities())) {
              localSportsTeams = localStaticEntitiesData.getStaticEntities().getSportsEntities();
            }
          }
          catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
          {
            StaticEntitiesData localStaticEntitiesData;
            Sidekick.SportsTeams localSportsTeams;
            Log.e(SportsEntitiesProvider.TAG, "File storage contained invalid data");
            continue;
          }
          synchronized (SportsEntitiesProvider.this)
          {
            SportsEntitiesProvider.access$002(SportsEntitiesProvider.this, localSportsTeams);
            local1.sendEmptyMessage(0);
            i = 0;
            if (localStaticEntitiesData.hasLastRefreshMillis())
            {
              long l1 = localStaticEntitiesData.getLastRefreshMillis();
              long l2 = SportsEntitiesProvider.this.mClock.currentTimeMillis();
              if (l2 - l1 > 604800000L) {
                i = 1;
              }
            }
            if (i != 0) {
              new SportsEntitiesProvider.FetchSportsEntities(SportsEntitiesProvider.this.mNetworkClient, SportsEntitiesProvider.this.mClock, SportsEntitiesProvider.this.mAsyncFileStorage, jdField_this).execute(new Void[0]);
            }
            return null;
          }
        }
      }
    });
  }
  
  public void updateSportsEntities()
  {
    if (this.mTargetFragment.getFragmentManager() == null) {}
    AddTeamDialogFragment localAddTeamDialogFragment;
    do
    {
      return;
      localAddTeamDialogFragment = (AddTeamDialogFragment)this.mTargetFragment.getFragmentManager().findFragmentByTag("add_team_dialog_tag");
    } while (localAddTeamDialogFragment == null);
    localAddTeamDialogFragment.updateSportsEntities(this.mSportsEntities);
  }
  
  void updateSportsEntities(Sidekick.SportsTeams paramSportsTeams)
  {
    try
    {
      this.mSportsEntities = paramSportsTeams;
      updateSportsEntities();
      return;
    }
    finally {}
  }
  
  private static class FetchSportsEntities
    extends AsyncTask<Void, Void, Sidekick.SportsTeams>
  {
    private final AsyncFileStorage mAsyncFileStorage;
    private final Clock mClock;
    private final SportsEntitiesProvider mEntitiesUpdater;
    private final NetworkClient mNetworkClient;
    
    public FetchSportsEntities(NetworkClient paramNetworkClient, Clock paramClock, AsyncFileStorage paramAsyncFileStorage, SportsEntitiesProvider paramSportsEntitiesProvider)
    {
      this.mAsyncFileStorage = paramAsyncFileStorage;
      this.mNetworkClient = paramNetworkClient;
      this.mClock = paramClock;
      this.mEntitiesUpdater = paramSportsEntitiesProvider;
    }
    
    protected Sidekick.SportsTeams doInBackground(Void... paramVarArgs)
    {
      Sidekick.FetchStaticEntitiesQuery localFetchStaticEntitiesQuery = new Sidekick.FetchStaticEntitiesQuery();
      localFetchStaticEntitiesQuery.addStaticEntities(0);
      Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload().setFetchStaticEntitiesQuery(localFetchStaticEntitiesQuery);
      Sidekick.ResponsePayload localResponsePayload = this.mNetworkClient.sendRequestWithoutLocation(localRequestPayload);
      if ((localResponsePayload != null) && (localResponsePayload.hasFetchStaticEntitiesResponse()))
      {
        Sidekick.FetchStaticEntitiesResponse localFetchStaticEntitiesResponse = localResponsePayload.getFetchStaticEntitiesResponse();
        if (localFetchStaticEntitiesResponse.hasSportsEntities())
        {
          long l = this.mClock.currentTimeMillis();
          StaticEntitiesData localStaticEntitiesData = new StaticEntitiesData();
          localStaticEntitiesData.setLastRefreshMillis(l);
          localStaticEntitiesData.setStaticEntities(localFetchStaticEntitiesResponse);
          this.mAsyncFileStorage.writeToFile("static_entities", localStaticEntitiesData.toByteArray());
          return localFetchStaticEntitiesResponse.getSportsEntities();
        }
      }
      return null;
    }
    
    protected void onPostExecute(Sidekick.SportsTeams paramSportsTeams)
    {
      if (paramSportsTeams == null) {
        return;
      }
      this.mEntitiesUpdater.updateSportsEntities(paramSportsTeams);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.SportsEntitiesProvider
 * JD-Core Version:    0.7.0.1
 */