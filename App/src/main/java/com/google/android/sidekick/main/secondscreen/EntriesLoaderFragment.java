package com.google.android.sidekick.main.secondscreen;

import android.app.Activity;
import android.app.Fragment;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.sidekick.main.contextprovider.RenderingContextPopulator;
import com.google.android.sidekick.main.entry.ChildEntryRemover;
import com.google.android.sidekick.main.entry.EntryRemover;
import com.google.android.sidekick.main.entry.EntryTreePruner;
import com.google.android.sidekick.main.entry.EntryUpdater;
import com.google.android.sidekick.main.entry.EntryUpdater.EntryUpdaterFunc;
import com.google.android.sidekick.main.entry.EntryValidator;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.common.collect.ImmutableSet;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryQuery;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.Interest;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import javax.annotation.Nullable;

public class EntriesLoaderFragment
  extends Fragment
{
  private static final String TAG = Tag.getTag(EntriesLoaderFragment.class);
  private CardRenderingContext mCardRenderingContext;
  private Sidekick.EntryTree mEntryTree;
  private boolean mHasError;
  private EntriesLoaderListener mListener;
  private LoaderTask mLoader;
  private LocationOracle mLocationOracle;
  private NetworkClient mNetworkClient;
  private Sidekick.Interest mPendingInterest;
  private EntryTreePruner mPruner;
  private RenderingContextPopulator mRenderingContextPopulator;
  
  public EntriesLoaderFragment()
  {
    setRetainInstance(true);
    this.mCardRenderingContext = new CardRenderingContext();
  }
  
  private void injectDependencies(LocationOracle paramLocationOracle, NetworkClient paramNetworkClient, EntryTreePruner paramEntryTreePruner, RenderingContextPopulator paramRenderingContextPopulator)
  {
    this.mLocationOracle = paramLocationOracle;
    this.mNetworkClient = paramNetworkClient;
    this.mPruner = paramEntryTreePruner;
    this.mRenderingContextPopulator = paramRenderingContextPopulator;
  }
  
  private void startLoading(Sidekick.Interest paramInterest)
  {
    if (this.mLoader != null) {
      this.mLoader.cancel(true);
    }
    this.mLoader = new LoaderTask(null);
    this.mLoader.execute(new Sidekick.Interest[] { paramInterest });
  }
  
  @Nullable
  public CardRenderingContext getCardRenderingContext()
  {
    return this.mCardRenderingContext;
  }
  
  @Nullable
  public Sidekick.EntryTree getEntryTree()
  {
    return this.mEntryTree;
  }
  
  public boolean hasError()
  {
    return this.mHasError;
  }
  
  public void load(Sidekick.Interest paramInterest)
  {
    if (isAdded())
    {
      startLoading(paramInterest);
      return;
    }
    this.mPendingInterest = paramInterest;
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    SidekickInjector localSidekickInjector = VelvetServices.get().getSidekickInjector();
    injectDependencies(localSidekickInjector.getLocationOracle(), localSidekickInjector.getNetworkClient(), new EntryTreePruner(new EntryValidator(localSidekickInjector.getCalendarDataProvider(), paramActivity.getApplicationContext())), localSidekickInjector.getRenderingContextPopulator());
    this.mListener = ((EntriesLoaderListener)paramActivity);
    if (this.mPendingInterest != null)
    {
      startLoading(this.mPendingInterest);
      this.mPendingInterest = null;
    }
  }
  
  public void onDetach()
  {
    super.onDetach();
    this.mListener = null;
  }
  
  public void removeEntry(Sidekick.Entry paramEntry)
  {
    if (this.mEntryTree == null) {
      return;
    }
    new EntryRemover(null, ImmutableSet.of(new ProtoKey(paramEntry))).visitWithoutNotifying(this.mEntryTree.getRoot());
  }
  
  public void removeGroupChildEntry(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2)
  {
    if (this.mEntryTree == null) {
      return;
    }
    new ChildEntryRemover(null, new ProtoKey(paramEntry1), ImmutableSet.of(new ProtoKey(paramEntry2))).visitWithoutNotifying(this.mEntryTree.getRoot());
  }
  
  public void setCardRenderingContext(CardRenderingContext paramCardRenderingContext)
  {
    this.mCardRenderingContext = paramCardRenderingContext;
  }
  
  public void updateEntries(EntryUpdater.EntryUpdaterFunc paramEntryUpdaterFunc)
  {
    if (this.mEntryTree == null) {
      return;
    }
    new EntryUpdater(null, paramEntryUpdaterFunc).visitWithoutNotifying(this.mEntryTree.getRoot());
  }
  
  public static abstract interface EntriesLoaderListener
  {
    public abstract void onEntriesUpdated();
    
    public abstract void onError();
  }
  
  private static class LoadResults
  {
    final CardRenderingContext mCardRenderingContext;
    final Sidekick.EntryTree mEntryTree;
    final boolean mError;
    
    private LoadResults(boolean paramBoolean, Sidekick.EntryTree paramEntryTree, @Nullable CardRenderingContext paramCardRenderingContext)
    {
      this.mError = paramBoolean;
      this.mEntryTree = paramEntryTree;
      this.mCardRenderingContext = paramCardRenderingContext;
    }
  }
  
  private class LoaderTask
    extends AsyncTask<Sidekick.Interest, Void, EntriesLoaderFragment.LoadResults>
  {
    private LoaderTask() {}
    
    protected EntriesLoaderFragment.LoadResults doInBackground(Sidekick.Interest... paramVarArgs)
    {
      if (paramVarArgs.length == 0)
      {
        Log.w(EntriesLoaderFragment.TAG, "Expected at least one interest");
        return new EntriesLoaderFragment.LoadResults(false, new Sidekick.EntryTree(), null, null);
      }
      Location localLocation = EntriesLoaderFragment.this.mLocationOracle.blockingUpdateBestLocation();
      Sidekick.EntryQuery localEntryQuery = new Sidekick.EntryQuery();
      int i = paramVarArgs.length;
      for (int j = 0; j < i; j++) {
        localEntryQuery.addInterest(paramVarArgs[j]);
      }
      Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload().setEntryQuery(localEntryQuery);
      Sidekick.ResponsePayload localResponsePayload = EntriesLoaderFragment.this.mNetworkClient.sendRequestWithLocation(localRequestPayload);
      if ((localResponsePayload != null) && (localResponsePayload.hasEntryResponse()) && (localResponsePayload.getEntryResponse().getEntryTreeCount() > 0))
      {
        Sidekick.EntryResponse localEntryResponse = localResponsePayload.getEntryResponse();
        EntriesLoaderFragment.this.mPruner.prune(localEntryResponse);
        Sidekick.EntryTree localEntryTree = localEntryResponse.getEntryTree(0);
        CardRenderingContext localCardRenderingContext = new CardRenderingContext(localLocation, localLocation);
        EntriesLoaderFragment.this.mRenderingContextPopulator.populate(localCardRenderingContext, localEntryTree);
        return new EntriesLoaderFragment.LoadResults(false, localEntryTree, localCardRenderingContext, null);
      }
      Log.w(EntriesLoaderFragment.TAG, "Failed to retrieve entries");
      return new EntriesLoaderFragment.LoadResults(true, new Sidekick.EntryTree(), null, null);
    }
    
    protected void onPostExecute(EntriesLoaderFragment.LoadResults paramLoadResults)
    {
      super.onPostExecute(paramLoadResults);
      EntriesLoaderFragment.access$702(EntriesLoaderFragment.this, paramLoadResults.mError);
      EntriesLoaderFragment.access$802(EntriesLoaderFragment.this, paramLoadResults.mEntryTree);
      EntriesLoaderFragment.access$902(EntriesLoaderFragment.this, paramLoadResults.mCardRenderingContext);
      if (EntriesLoaderFragment.this.mListener != null)
      {
        if (!EntriesLoaderFragment.this.mHasError) {
          break label83;
        }
        EntriesLoaderFragment.this.mListener.onError();
      }
      for (;;)
      {
        EntriesLoaderFragment.access$1102(EntriesLoaderFragment.this, null);
        return;
        label83:
        EntriesLoaderFragment.this.mListener.onEntriesUpdated();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.secondscreen.EntriesLoaderFragment
 * JD-Core Version:    0.7.0.1
 */