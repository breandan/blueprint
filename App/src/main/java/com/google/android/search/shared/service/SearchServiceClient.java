package com.google.android.search.shared.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.SearchBoxStats;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.api.SuggestionLogInfo;
import com.google.android.shared.util.HandlerScheduledExecutor;
import com.google.android.shared.util.ThreadChanger;
import java.util.List;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class SearchServiceClient
{
  private final ServiceConnection mConnection;
  private final Context mContext;
  private boolean mIsConnecting;
  private ISearchService mService;
  
  public SearchServiceClient(Context paramContext, ConnectionListener paramConnectionListener, @Nullable SearchServiceUiCallback paramSearchServiceUiCallback, ClientConfig paramClientConfig)
  {
    this.mContext = paramContext;
    if (paramSearchServiceUiCallback != null) {}
    for (UiCallbackStub localUiCallbackStub = new UiCallbackStub(paramSearchServiceUiCallback);; localUiCallbackStub = null)
    {
      this.mConnection = new SearchServiceConnection(paramConnectionListener, localUiCallbackStub, paramClientConfig);
      return;
    }
  }
  
  public void cancel()
  {
    if (isConnected()) {}
    try
    {
      this.mService.cancel();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SearchServiceClient", "cancel() failed", localRemoteException);
    }
  }
  
  public void commit(Query paramQuery)
  {
    if (isConnected()) {}
    try
    {
      this.mService.commit(paramQuery);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SearchServiceClient", "commit() failed", localRemoteException);
    }
  }
  
  public void connect()
  {
    if (this.mIsConnecting) {}
    Intent localIntent;
    do
    {
      return;
      this.mIsConnecting = true;
      localIntent = new Intent();
      localIntent.setClassName(this.mContext, "com.google.android.search.core.service.SearchServiceImpl");
    } while (this.mContext.bindService(localIntent, this.mConnection, 1));
    Log.w("SearchServiceClient", "Unable to bind to the search service");
    this.mIsConnecting = false;
  }
  
  public void disconnect()
  {
    if (isConnected())
    {
      this.mContext.unbindService(this.mConnection);
      this.mService = null;
    }
    this.mIsConnecting = false;
  }
  
  public boolean isConnected()
  {
    return this.mService != null;
  }
  
  public void onQuickContactClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats)
  {
    if (isConnected()) {}
    try
    {
      this.mService.onQuickContactClicked(paramSuggestion, paramSearchBoxStats);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SearchServiceClient", "onQuickContactClicked() failed", localRemoteException);
    }
  }
  
  public void onSuggestionClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats)
  {
    if (isConnected()) {}
    try
    {
      this.mService.onSuggestionClicked(paramSuggestion, paramSearchBoxStats);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SearchServiceClient", "onSuggestionClicked() failed", localRemoteException);
    }
  }
  
  public void removeSuggestionFromHistory(Suggestion paramSuggestion)
  {
    if (isConnected()) {}
    try
    {
      this.mService.removeSuggestionFromHistory(paramSuggestion);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SearchServiceClient", "removeSuggestionFromHistory() failed", localRemoteException);
    }
  }
  
  public void set(Query paramQuery)
  {
    if (isConnected()) {}
    try
    {
      this.mService.set(paramQuery);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SearchServiceClient", "set() failed", localRemoteException);
    }
  }
  
  public void setHotwordDetectionEnabled(boolean paramBoolean)
  {
    if (isConnected()) {}
    try
    {
      this.mService.setHotwordDetectionEnabled(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SearchServiceClient", "setHotwordDetectionEnabled() failed", localRemoteException);
    }
  }
  
  public void startQueryEdit()
  {
    if (isConnected()) {}
    try
    {
      this.mService.startQueryEdit();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SearchServiceClient", "startQueryEdit() failed", localRemoteException);
    }
  }
  
  public void stopListening()
  {
    if (isConnected()) {}
    try
    {
      this.mService.stopListening();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SearchServiceClient", "stopListening() failed", localRemoteException);
    }
  }
  
  public static abstract interface ConnectionListener
  {
    public abstract void onServiceConnected();
    
    public abstract void onServiceDisconnected();
  }
  
  private class SearchServiceConnection
    implements ServiceConnection
  {
    private final ClientConfig mConfig;
    private final SearchServiceClient.ConnectionListener mConnectionListener;
    @Nullable
    private final SearchServiceClient.UiCallbackStub mUiCallbackStub;
    
    public SearchServiceConnection(SearchServiceClient.ConnectionListener paramConnectionListener, @Nullable SearchServiceClient.UiCallbackStub paramUiCallbackStub, ClientConfig paramClientConfig)
    {
      this.mConnectionListener = paramConnectionListener;
      this.mUiCallbackStub = paramUiCallbackStub;
      this.mConfig = paramClientConfig;
    }
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      SearchServiceClient.access$002(SearchServiceClient.this, ISearchService.Stub.asInterface(paramIBinder));
      if (!SearchServiceClient.this.mIsConnecting)
      {
        SearchServiceClient.this.disconnect();
        return;
      }
      SearchServiceClient.access$102(SearchServiceClient.this, false);
      try
      {
        SearchServiceClient.this.mService.setSearchServiceUiCallback(this.mUiCallbackStub, this.mConfig);
        this.mConnectionListener.onServiceConnected();
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("SearchServiceClient", "setSearchServiceUiCallback() failed", localRemoteException);
      }
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      SearchServiceClient.access$002(SearchServiceClient.this, null);
      SearchServiceClient.access$102(SearchServiceClient.this, false);
      this.mConnectionListener.onServiceDisconnected();
    }
  }
  
  private class UiCallbackStub
    extends ISearchServiceUiCallback.Stub
  {
    private final SearchServiceUiCallback mDirectSearchServiceUi;
    private final SearchServiceUiCallback mSearchServiceUi;
    
    UiCallbackStub(SearchServiceUiCallback paramSearchServiceUiCallback)
    {
      this.mSearchServiceUi = ((SearchServiceUiCallback)ThreadChanger.createNonBlockingThreadChangeProxy(getUiThreadExecutor(), SearchServiceUiCallback.class, paramSearchServiceUiCallback));
      this.mDirectSearchServiceUi = paramSearchServiceUiCallback;
    }
    
    private Executor getUiThreadExecutor()
    {
      return new HandlerScheduledExecutor(new Handler(Looper.getMainLooper()), Looper.myQueue());
    }
    
    public void hideSuggestions()
      throws RemoteException
    {
      this.mSearchServiceUi.hideSuggestions();
    }
    
    public void launchIntent(Intent paramIntent)
      throws RemoteException
    {
      this.mSearchServiceUi.launchIntent(paramIntent);
    }
    
    public void onRemoveSuggestionFromHistoryFailed()
      throws RemoteException
    {
      this.mSearchServiceUi.onRemoveSuggestionFromHistoryFailed();
    }
    
    public void setExternalFlags(int paramInt, String paramString)
      throws RemoteException
    {
      this.mSearchServiceUi.setExternalFlags(paramInt, paramString);
    }
    
    public void setFinalRecognizedText(String paramString)
      throws RemoteException
    {
      this.mSearchServiceUi.setFinalRecognizedText(paramString);
    }
    
    public void setQuery(Query paramQuery)
      throws RemoteException
    {
      if (SearchServiceClient.this.isConnected()) {
        this.mSearchServiceUi.setQuery(paramQuery);
      }
    }
    
    public void setSearchPlateMode(int paramInt1, int paramInt2, boolean paramBoolean)
      throws RemoteException
    {
      this.mSearchServiceUi.setSearchPlateMode(paramInt1, paramInt2, paramBoolean);
    }
    
    public void showErrorMessage(String paramString)
      throws RemoteException
    {
      this.mSearchServiceUi.showErrorMessage(paramString);
    }
    
    public void showRecognitionState(int paramInt)
      throws RemoteException
    {
      this.mSearchServiceUi.showRecognitionState(paramInt);
    }
    
    public void showSuggestions(Query paramQuery, List<Suggestion> paramList, boolean paramBoolean, SuggestionLogInfo paramSuggestionLogInfo)
      throws RemoteException
    {
      this.mSearchServiceUi.showSuggestions(paramQuery, paramList, paramBoolean, paramSuggestionLogInfo);
    }
    
    public void updateRecognizedText(String paramString1, String paramString2)
      throws RemoteException
    {
      this.mSearchServiceUi.updateRecognizedText(paramString1, paramString2);
    }
    
    public void updateSpeechLevel(int paramInt)
      throws RemoteException
    {
      this.mDirectSearchServiceUi.updateSpeechLevel(paramInt);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.service.SearchServiceClient
 * JD-Core Version:    0.7.0.1
 */