package com.google.android.search.core.preferences;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.apps.sidekick.training.Training.QuestionWithEntry;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.inject.TrainingQuestionManager;
import com.google.android.sidekick.shared.training.TrainingRequestHelper;
import com.google.android.sidekick.shared.util.PlaceUtils;
import com.google.android.velvet.VelvetServices;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.geo.sidekick.Sidekick.TrainingModeClosetQuery;
import com.google.geo.sidekick.Sidekick.TrainingModeClosetResponse;
import com.google.geo.sidekick.Sidekick.TrainingModeDataQuery;
import java.util.Iterator;
import javax.annotation.Nullable;

public class NowTrainingQuestionsFetcherFragment
  extends Fragment
{
  private Sidekick.TrainingModeClosetResponse mCloset;
  private FetchClosetTask mFetchTask;
  private boolean mHasError;
  private NetworkClient mNetworkClient;
  private int mNumPlaces;
  private TrainingQuestionManager mTrainingQuestionManager;
  
  public NowTrainingQuestionsFetcherFragment()
  {
    setRetainInstance(true);
  }
  
  private static int getFrequentPlacesCount(Sidekick.EntryResponse paramEntryResponse)
  {
    Pair localPair = MyPlacesUtil.getHomeWorkEntries(paramEntryResponse);
    int i = 0;
    if (localPair != null)
    {
      Object localObject = localPair.first;
      i = 0;
      if (localObject != null)
      {
        boolean bool = TextUtils.isEmpty(PlaceUtils.getAddress((Sidekick.Entry)localPair.first));
        i = 0;
        if (!bool) {
          i = 0 + 1;
        }
      }
      if ((localPair.second != null) && (!TextUtils.isEmpty(PlaceUtils.getAddress((Sidekick.Entry)localPair.second)))) {
        i++;
      }
    }
    return i;
  }
  
  @Nullable
  public Sidekick.TrainingModeClosetResponse getCloset()
  {
    return this.mCloset;
  }
  
  public int getNumPlaces()
  {
    return this.mNumPlaces;
  }
  
  public boolean hasError()
  {
    return this.mHasError;
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    SidekickInjector localSidekickInjector = VelvetServices.get().getSidekickInjector();
    this.mNetworkClient = localSidekickInjector.getNetworkClient();
    this.mTrainingQuestionManager = localSidekickInjector.getTrainingQuestionManager();
    if ((this.mCloset == null) && (this.mFetchTask == null))
    {
      this.mFetchTask = new FetchClosetTask(null);
      this.mFetchTask.execute(new Void[0]);
    }
  }
  
  public void onDetach()
  {
    if (this.mFetchTask != null)
    {
      this.mFetchTask.cancel(true);
      this.mFetchTask = null;
    }
    super.onDetach();
  }
  
  public void setNumPlaces(int paramInt)
  {
    this.mNumPlaces = paramInt;
  }
  
  void updateClosetData(Sidekick.TrainingModeClosetResponse paramTrainingModeClosetResponse)
  {
    this.mCloset = paramTrainingModeClosetResponse;
  }
  
  private class FetchClosetTask
    extends AsyncTask<Void, Void, Sidekick.ResponsePayload>
  {
    private FetchClosetTask() {}
    
    private Sidekick.ResponsePayload fetchClosetData(Iterable<Training.QuestionWithEntry> paramIterable)
    {
      Sidekick.TrainingModeDataQuery localTrainingModeDataQuery = new Sidekick.TrainingModeDataQuery().setMetadata(NowTrainingQuestionsFetcherFragment.this.mTrainingQuestionManager.getTrainingModeMetadata());
      Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload().setEntryQuery(MyPlacesUtil.buildQuery()).setTrainingModeDataQuery(localTrainingModeDataQuery).setTrainingModeClosetQuery(new Sidekick.TrainingModeClosetQuery());
      if (paramIterable.iterator().hasNext()) {
        localRequestPayload.setActionsQuery(TrainingRequestHelper.buildAnsweredQuestionsQuery(paramIterable));
      }
      return NowTrainingQuestionsFetcherFragment.this.mNetworkClient.sendRequestWithoutLocation(localRequestPayload);
    }
    
    protected Sidekick.ResponsePayload doInBackground(Void... paramVarArgs)
    {
      Iterable localIterable = NowTrainingQuestionsFetcherFragment.this.mTrainingQuestionManager.getPendingAnsweredQuestionsWithEntries();
      Sidekick.ResponsePayload localResponsePayload = fetchClosetData(localIterable);
      if ((localResponsePayload == null) || (!localResponsePayload.hasTrainingModeClosetResponse())) {
        NowTrainingQuestionsFetcherFragment.access$202(NowTrainingQuestionsFetcherFragment.this, true);
      }
      for (;;)
      {
        NowTrainingQuestionsFetcherFragment.access$602(NowTrainingQuestionsFetcherFragment.this, null);
        return localResponsePayload;
        if (localResponsePayload.hasTrainingModeDataResponse()) {
          NowTrainingQuestionsFetcherFragment.this.mTrainingQuestionManager.updateFromServerResponse(localResponsePayload.getTrainingModeDataResponse(), localIterable);
        }
        NowTrainingQuestionsFetcherFragment.access$302(NowTrainingQuestionsFetcherFragment.this, localResponsePayload.getTrainingModeClosetResponse());
        if (localResponsePayload.hasEntryResponse()) {
          NowTrainingQuestionsFetcherFragment.access$402(NowTrainingQuestionsFetcherFragment.this, NowTrainingQuestionsFetcherFragment.getFrequentPlacesCount(localResponsePayload.getEntryResponse()));
        }
      }
    }
    
    protected void onPostExecute(Sidekick.ResponsePayload paramResponsePayload)
    {
      NowTrainingQuestionsFetcherFragment.Listener localListener = (NowTrainingQuestionsFetcherFragment.Listener)NowTrainingQuestionsFetcherFragment.this.getTargetFragment();
      if (localListener != null)
      {
        if (NowTrainingQuestionsFetcherFragment.this.mHasError) {
          localListener.onError();
        }
      }
      else {
        return;
      }
      localListener.onClosetLoaded(NowTrainingQuestionsFetcherFragment.this.mCloset, NowTrainingQuestionsFetcherFragment.this.mNumPlaces);
    }
  }
  
  public static abstract interface Listener
  {
    public abstract void onClosetLoaded(Sidekick.TrainingModeClosetResponse paramTrainingModeClosetResponse, int paramInt);
    
    public abstract void onError();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.NowTrainingQuestionsFetcherFragment
 * JD-Core Version:    0.7.0.1
 */