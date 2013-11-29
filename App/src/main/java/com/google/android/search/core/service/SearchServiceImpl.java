package com.google.android.search.core.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.Feature;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.SearchBoxStats;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.api.SuggestionLogInfo;
import com.google.android.search.shared.service.ClientConfig;
import com.google.android.search.shared.service.ISearchService.Stub;
import com.google.android.search.shared.service.ISearchServiceUiCallback;
import com.google.android.search.shared.service.SearchServiceUiCallback;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.ThreadChanger;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.ui.VelvetActivity;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Executor;

public class SearchServiceImpl
  extends Service
{
  private static final String VELVET_ACTIVITY_CLASS_NAME = VelvetActivity.class.getName();
  private ISearchServiceUiCallback mCallback;
  private final IBinder.DeathRecipient mCallbackDeathRecipient = new IBinder.DeathRecipient()
  {
    public void binderDied()
    {
      Log.i("SearchServiceImpl", "Search service listener died");
      SearchServiceImpl.access$002(SearchServiceImpl.this, null);
    }
  };
  private ClientConfig mHeadlessConfig = new ClientConfig(2);
  private LocalSearchService mImpl;
  private SearchServiceBinder mSearchBinder;
  private final SearchServiceListener mSearchServiceListener = new SearchServiceListener(null);
  
  private void setClientCallback(ISearchServiceUiCallback paramISearchServiceUiCallback)
  {
    if (this.mCallback != null) {
      this.mCallback.asBinder().unlinkToDeath(this.mCallbackDeathRecipient, 0);
    }
    this.mCallback = paramISearchServiceUiCallback;
    if (this.mCallback != null) {}
    try
    {
      this.mCallback.asBinder().linkToDeath(this.mCallbackDeathRecipient, 0);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.i("SearchServiceImpl", "Client died before linkToDeath()");
    }
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    super.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    this.mImpl.dump(paramPrintWriter);
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    this.mImpl.ensureAttachedToSearchController();
    return this.mSearchBinder;
  }
  
  public void onCreate()
  {
    super.onCreate();
    VelvetServices localVelvetServices = VelvetServices.get();
    ScheduledSingleThreadedExecutor localScheduledSingleThreadedExecutor = localVelvetServices.getAsyncServices().getUiThreadExecutor();
    this.mSearchBinder = new SearchServiceBinder(this, this.mSearchServiceListener, localScheduledSingleThreadedExecutor, null);
    this.mImpl = new LocalSearchService(getApplicationContext(), localVelvetServices, this.mSearchBinder, new ServiceForegroundHelper(this, getApplicationContext()));
    this.mSearchBinder.setService(this.mImpl);
    this.mImpl.setSearchServiceUiCallback(this.mSearchServiceListener, this.mHeadlessConfig);
    this.mImpl.create();
  }
  
  public void onDestroy()
  {
    if (this.mCallback != null)
    {
      this.mCallback.asBinder().unlinkToDeath(this.mCallbackDeathRecipient, 0);
      this.mCallback = null;
    }
    this.mSearchBinder.destroy();
    this.mImpl.destroy();
    super.onDestroy();
  }
  
  public void onRebind(Intent paramIntent)
  {
    this.mImpl.ensureAttachedToSearchController();
    this.mSearchBinder.setDetached(false);
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    if (paramIntent == null) {}
    do
    {
      return super.onStartCommand(paramIntent, paramInt1, paramInt2);
      if ("com.google.android.search.core.BTSTARTUP".equals(paramIntent.getAction()))
      {
        this.mImpl.setBluetoothShouldForeground(true);
        this.mImpl.registerE100Listeners();
        return 1;
      }
      if ("com.google.android.search.core.BTSTOP".equals(paramIntent.getAction()))
      {
        this.mImpl.unregisterE100Listeners();
        this.mImpl.setBluetoothShouldForeground(false);
        return 1;
      }
    } while (!"com.google.android.search.core.VOICE_ACTION".equals(paramIntent.getAction()));
    this.mImpl.forceForegroundOnEyesFreeCommitHack();
    boolean bool = paramIntent.getBooleanExtra("com.google.android.search.core.handsfreesource", false);
    Query localQuery = Query.EMPTY.withSearchBoxStats(this.mImpl.getSearchBoxStats());
    if (bool)
    {
      this.mImpl.commit(localQuery.voiceSearchFromWiredHeadsetButton());
      return 1;
    }
    this.mImpl.commit(localQuery.voiceSearchFromBluetoothHeadsetButton());
    return 1;
  }
  
  public void onTrimMemory(int paramInt)
  {
    super.onTrimMemory(paramInt);
    this.mImpl.onTrimMemory();
  }
  
  public boolean onUnbind(Intent paramIntent)
  {
    super.onUnbind(paramIntent);
    this.mImpl.setSearchServiceUiCallback(this.mSearchServiceListener, this.mHeadlessConfig);
    return true;
  }
  
  protected static class SearchServiceBinder
    extends ISearchService.Stub
  {
    private boolean mDetached;
    private LocalSearchService mImpl;
    private SearchServiceImpl mSearchServiceImpl;
    private SearchServiceImpl.SearchServiceListener mSearchServiceListener;
    private SearchService mThreadChangedImpl;
    private Executor mUiThread;
    
    private SearchServiceBinder(SearchServiceImpl paramSearchServiceImpl, SearchServiceImpl.SearchServiceListener paramSearchServiceListener, Executor paramExecutor)
    {
      this.mSearchServiceImpl = paramSearchServiceImpl;
      this.mSearchServiceListener = paramSearchServiceListener;
      this.mUiThread = paramExecutor;
    }
    
    private void setService(LocalSearchService paramLocalSearchService)
    {
      this.mImpl = paramLocalSearchService;
      this.mThreadChangedImpl = ((SearchService)ThreadChanger.createNonBlockingThreadChangeProxy(this.mUiThread, SearchService.class, paramLocalSearchService));
    }
    
    public void cancel()
      throws RemoteException
    {
      if (this.mDetached) {
        return;
      }
      this.mThreadChangedImpl.cancel();
    }
    
    public void commit(Query paramQuery)
      throws RemoteException
    {
      if (this.mDetached) {
        return;
      }
      if (paramQuery.isEyesFree()) {
        this.mImpl.forceForegroundOnEyesFreeCommitHack();
      }
      this.mThreadChangedImpl.commit(paramQuery);
    }
    
    public void destroy()
    {
      this.mDetached = true;
      this.mSearchServiceImpl = null;
      this.mUiThread = null;
      this.mThreadChangedImpl = null;
    }
    
    public boolean isDetached()
    {
      return this.mDetached;
    }
    
    public void onQuickContactClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats)
      throws RemoteException
    {
      if (this.mDetached) {
        return;
      }
      this.mThreadChangedImpl.onQuickContactClicked(paramSuggestion, paramSearchBoxStats);
    }
    
    public void onSuggestionClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats)
      throws RemoteException
    {
      if (this.mDetached) {
        return;
      }
      this.mThreadChangedImpl.onSuggestionClicked(paramSuggestion, paramSearchBoxStats);
    }
    
    public void removeSuggestionFromHistory(Suggestion paramSuggestion)
      throws RemoteException
    {
      if (this.mDetached) {
        return;
      }
      this.mThreadChangedImpl.removeSuggestionFromHistory(paramSuggestion);
    }
    
    public void set(Query paramQuery)
      throws RemoteException
    {
      if (this.mDetached) {
        return;
      }
      this.mThreadChangedImpl.set(paramQuery);
    }
    
    public void setDetached(boolean paramBoolean)
    {
      this.mDetached = paramBoolean;
    }
    
    public void setHotwordDetectionEnabled(boolean paramBoolean)
    {
      if (this.mDetached) {
        return;
      }
      this.mThreadChangedImpl.setHotwordDetectionEnabled(paramBoolean);
    }
    
    public void setSearchServiceUiCallback(final ISearchServiceUiCallback paramISearchServiceUiCallback, final ClientConfig paramClientConfig)
      throws RemoteException
    {
      if (this.mDetached) {
        return;
      }
      this.mUiThread.execute(new Runnable()
      {
        public void run()
        {
          if (!SearchServiceImpl.SearchServiceBinder.this.mDetached)
          {
            SearchServiceImpl.this.setClientCallback(paramISearchServiceUiCallback);
            SearchServiceImpl.SearchServiceBinder.this.mImpl.setSearchServiceUiCallback(SearchServiceImpl.SearchServiceBinder.this.mSearchServiceListener, paramClientConfig);
          }
        }
      });
    }
    
    public void startQueryEdit()
      throws RemoteException
    {
      if (this.mDetached) {
        return;
      }
      this.mThreadChangedImpl.startQueryEdit();
    }
    
    public void stopListening()
      throws RemoteException
    {
      this.mThreadChangedImpl.stopListening();
    }
  }
  
  private class SearchServiceListener
    implements SearchServiceUiCallback
  {
    private SearchServiceListener() {}
    
    public void hideSuggestions()
    {
      try
      {
        if (SearchServiceImpl.this.mCallback != null) {
          SearchServiceImpl.this.mCallback.hideSuggestions();
        }
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("SearchServiceImpl", "Error calling hideSuggestions()", localRemoteException);
      }
    }
    
    public void launchIntent(Intent paramIntent)
    {
      try
      {
        if (SearchServiceImpl.this.mCallback != null)
        {
          SearchServiceImpl.this.mCallback.launchIntent(paramIntent);
          return;
        }
        if ((Feature.EYES_FREE.isEnabled()) && ((paramIntent.getComponent() == null) || (!SearchServiceImpl.VELVET_ACTIVITY_CLASS_NAME.equals(paramIntent.getComponent().getClassName()))))
        {
          Log.i("SearchServiceImpl", "Eyes-free: Starting activity for intent: " + paramIntent);
          paramIntent.setFlags(268435456);
          SearchServiceImpl.this.startActivity(paramIntent);
          return;
        }
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("SearchServiceImpl", "Error calling launchIntent()", localRemoteException);
      }
    }
    
    public void onRemoveSuggestionFromHistoryFailed()
    {
      try
      {
        if (SearchServiceImpl.this.mCallback != null) {
          SearchServiceImpl.this.mCallback.onRemoveSuggestionFromHistoryFailed();
        }
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("SearchServiceImpl", "Error calling onHotwordDetected()", localRemoteException);
      }
    }
    
    public boolean resolveIntent(Intent paramIntent)
    {
      return SearchServiceImpl.this.getPackageManager().resolveActivity(paramIntent, 65536) != null;
    }
    
    public void setExternalFlags(int paramInt, String paramString)
    {
      try
      {
        if (SearchServiceImpl.this.mCallback != null) {
          SearchServiceImpl.this.mCallback.setExternalFlags(paramInt, paramString);
        }
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("SearchServiceImpl", "Error calling setExternalFlags()", localRemoteException);
      }
    }
    
    public void setFinalRecognizedText(String paramString)
    {
      try
      {
        if (SearchServiceImpl.this.mCallback != null) {
          SearchServiceImpl.this.mCallback.setFinalRecognizedText(paramString);
        }
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("SearchServiceImpl", "Error calling setFinalRecognizedText()", localRemoteException);
      }
    }
    
    public void setQuery(Query paramQuery)
    {
      try
      {
        if (SearchServiceImpl.this.mCallback != null) {
          SearchServiceImpl.this.mCallback.setQuery(paramQuery);
        }
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("SearchServiceImpl", "Error calling setQuery()", localRemoteException);
      }
    }
    
    public void setSearchPlateMode(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      try
      {
        if (SearchServiceImpl.this.mCallback != null) {
          SearchServiceImpl.this.mCallback.setSearchPlateMode(paramInt1, paramInt2, paramBoolean);
        }
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("SearchServiceImpl", "Error calling setSearchPlateMode()", localRemoteException);
      }
    }
    
    public void showErrorMessage(String paramString)
    {
      try
      {
        if (SearchServiceImpl.this.mCallback != null) {
          SearchServiceImpl.this.mCallback.showErrorMessage(paramString);
        }
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.d("SearchServiceImpl", "Error calling showErrorMessage()", localRemoteException);
      }
    }
    
    public void showRecognitionState(int paramInt)
    {
      try
      {
        if (SearchServiceImpl.this.mCallback != null) {
          SearchServiceImpl.this.mCallback.showRecognitionState(paramInt);
        }
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("SearchServiceImpl", "Error calling showRecognitionState()", localRemoteException);
      }
    }
    
    public void showSuggestions(Query paramQuery, List<Suggestion> paramList, boolean paramBoolean, SuggestionLogInfo paramSuggestionLogInfo)
    {
      try
      {
        if (SearchServiceImpl.this.mCallback != null) {
          SearchServiceImpl.this.mCallback.showSuggestions(paramQuery, paramList, paramBoolean, paramSuggestionLogInfo);
        }
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("SearchServiceImpl", "Error calling showSuggestions()", localRemoteException);
      }
    }
    
    public void updateRecognizedText(String paramString1, String paramString2)
    {
      try
      {
        if (SearchServiceImpl.this.mCallback != null) {
          SearchServiceImpl.this.mCallback.updateRecognizedText(paramString1, paramString2);
        }
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("SearchServiceImpl", "Error calling updateRecognizedText()", localRemoteException);
      }
    }
    
    public void updateSpeechLevel(int paramInt)
    {
      try
      {
        ISearchServiceUiCallback localISearchServiceUiCallback = SearchServiceImpl.this.mCallback;
        if (localISearchServiceUiCallback != null) {
          localISearchServiceUiCallback.updateSpeechLevel(paramInt);
        }
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("SearchServiceImpl", "Error calling updateSpeechLevel()", localRemoteException);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.service.SearchServiceImpl
 * JD-Core Version:    0.7.0.1
 */