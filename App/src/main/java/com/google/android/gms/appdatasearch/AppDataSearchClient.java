package com.google.android.gms.appdatasearch;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.cw;
import com.google.android.gms.internal.cx;
import com.google.android.gms.internal.ds;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AppDataSearchClient
  implements GooglePlayServicesClient
{
  private final ConditionVariable io = new ConditionVariable();
  private ConnectionResult ip;
  private final cw iq;
  private final Context mContext;
  
  public AppDataSearchClient(Context paramContext)
  {
    this.mContext = paramContext;
    this.iq = new cw(paramContext, new a(null), new b(null));
  }
  
  private void d(Uri paramUri)
  {
    this.mContext.grantUriPermission("com.google.android.gms", paramUri, 1);
  }
  
  public static void verifyContentProviderClient(Context paramContext)
    throws SecurityException
  {
    int i = Binder.getCallingUid();
    if (i == Process.myUid()) {
      Log.i("AppDataSearchClient", "verifyContentProviderClient: caller is current process");
    }
    int j;
    do
    {
      return;
      try
      {
        if (paramContext.getPackageManager().getApplicationInfo("com.google.android.gms", 0).uid != i) {
          throw new SecurityException("Calling UID " + i + " is not Google Play Services.");
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        throw new SecurityException("Google Play Services not installed", localNameNotFoundException);
      }
      j = GooglePlayServicesUtil.isGooglePlayServicesAvailable(paramContext);
    } while (j == 0);
    throw new SecurityException("Calling package problem: " + GooglePlayServicesUtil.getErrorString(j));
  }
  
  public void connect()
  {
    this.ip = null;
    this.io.close();
    this.iq.connect();
  }
  
  public ConnectionResult connectWithTimeout(long paramLong)
  {
    connect();
    if (!this.io.block(paramLong))
    {
      disconnect();
      return new ConnectionResult(8, null);
    }
    if (this.ip != null) {
      return this.ip;
    }
    ds.k(isConnected());
    return ConnectionResult.mv;
  }
  
  public void disconnect()
  {
    this.iq.disconnect();
  }
  
  public CorpusStatus getCorpusStatus(String paramString)
  {
    try
    {
      CorpusStatus localCorpusStatus = this.iq.getSearchService().d(this.mContext.getPackageName(), paramString);
      return localCorpusStatus;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AppDataSearchClient", "Get corpus status failed.", localRemoteException);
    }
    return null;
  }
  
  public GlobalSearchApplicationInfo[] getGlobalSearchRegisteredApplications()
  {
    try
    {
      GlobalSearchApplicationInfo[] arrayOfGlobalSearchApplicationInfo = this.iq.getSearchService().aG();
      return arrayOfGlobalSearchApplicationInfo;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AppDataSearchClient", "Get UniversalSearchableApps failed.", localRemoteException);
    }
    return null;
  }
  
  public PhraseAffinityResponse getPhraseAffinity(String[] paramArrayOfString, PhraseAffinitySpecification paramPhraseAffinitySpecification)
  {
    try
    {
      PhraseAffinityResponse localPhraseAffinityResponse = this.iq.getSearchService().getPhraseAffinity(paramArrayOfString, paramPhraseAffinitySpecification);
      return localPhraseAffinityResponse;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AppDataSearchClient", "Getting phrase affinity failed.", localRemoteException);
    }
    return null;
  }
  
  public boolean isConnected()
  {
    return this.iq.isConnected();
  }
  
  public SearchResults query(String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2, QuerySpecification paramQuerySpecification)
  {
    try
    {
      SearchResults localSearchResults = this.iq.getSearchService().a(paramString, this.mContext.getPackageName(), paramArrayOfString, paramInt1, paramInt2, paramQuerySpecification);
      return localSearchResults;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AppDataSearchClient", "Query failed.", localRemoteException);
    }
    return null;
  }
  
  public SearchResults queryGlobalSearch(String paramString, int paramInt1, int paramInt2, GlobalSearchQuerySpecification paramGlobalSearchQuerySpecification)
  {
    try
    {
      SearchResults localSearchResults = this.iq.getSearchService().a(paramString, paramInt1, paramInt2, paramGlobalSearchQuerySpecification);
      return localSearchResults;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AppDataSearchClient", "Query failed.", localRemoteException);
    }
    return null;
  }
  
  public boolean registerCorpus(RegisterCorpusInfo paramRegisterCorpusInfo)
  {
    d(paramRegisterCorpusInfo.contentProviderUri);
    try
    {
      boolean bool = this.iq.getSearchService().b(this.mContext.getPackageName(), paramRegisterCorpusInfo);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AppDataSearchClient", "Register corpus failed.", localRemoteException);
    }
    return false;
  }
  
  public boolean registerGlobalSearchApplication(GlobalSearchApplicationInfo paramGlobalSearchApplicationInfo)
  {
    GlobalSearchApplicationInfo localGlobalSearchApplicationInfo = paramGlobalSearchApplicationInfo.r(this.mContext.getPackageName());
    try
    {
      this.iq.getSearchService().a(localGlobalSearchApplicationInfo);
      return true;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AppDataSearchClient", "Register UniversalSearchableAppInfo failed.", localRemoteException);
    }
    return false;
  }
  
  public boolean requestIndexing(String paramString, long paramLong)
  {
    try
    {
      boolean bool = this.iq.getSearchService().a(this.mContext.getPackageName(), paramString, paramLong, null);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AppDataSearchClient", "Request indexing failed.", localRemoteException);
    }
    return false;
  }
  
  public boolean setRegisteredCorpora(Collection<RegisterCorpusInfo> paramCollection)
  {
    String[] arrayOfString;
    HashSet localHashSet;
    try
    {
      arrayOfString = this.iq.getSearchService().s(this.mContext.getPackageName());
      localHashSet = new HashSet(paramCollection.size());
      Iterator localIterator1 = paramCollection.iterator();
      while (localIterator1.hasNext())
      {
        localHashSet.add(((RegisterCorpusInfo)localIterator1.next()).name);
        continue;
        return bool1;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AppDataSearchClient", "Getting corpora failed.", localRemoteException);
      bool1 = false;
    }
    boolean bool2 = true;
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = arrayOfString[j];
      if ((!localHashSet.contains(str)) && (!unregisterCorpus(str))) {
        bool2 = false;
      }
    }
    Iterator localIterator2 = paramCollection.iterator();
    boolean bool1 = bool2;
    label162:
    if (localIterator2.hasNext()) {
      if (registerCorpus((RegisterCorpusInfo)localIterator2.next())) {
        break label199;
      }
    }
    label199:
    for (boolean bool3 = false;; bool3 = bool1)
    {
      bool1 = bool3;
      break label162;
      break;
    }
  }
  
  public boolean unregisterCorpus(String paramString)
  {
    try
    {
      Bundle localBundle = this.iq.getSearchService().e(this.mContext.getPackageName(), paramString);
      for (String str : localBundle.getStringArray("content_provider_uris")) {
        this.mContext.revokeUriPermission(Uri.parse(str), 1);
      }
      boolean[] arrayOfBoolean = localBundle.getBooleanArray("success");
      for (int k = 0; k < arrayOfBoolean.length; k++)
      {
        int m = arrayOfBoolean[k];
        if (m == 0) {
          return false;
        }
      }
      return true;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AppDataSearchClient", "Unregister corpus failed.", localRemoteException);
      return false;
    }
  }
  
  private final class a
    implements GooglePlayServicesClient.ConnectionCallbacks
  {
    private a() {}
    
    public void onConnected(Bundle paramBundle)
    {
      AppDataSearchClient.a(AppDataSearchClient.this).open();
    }
    
    public void onDisconnected() {}
  }
  
  private final class b
    implements GooglePlayServicesClient.OnConnectionFailedListener
  {
    private b() {}
    
    public void onConnectionFailed(ConnectionResult paramConnectionResult)
    {
      AppDataSearchClient.a(AppDataSearchClient.this, paramConnectionResult);
      AppDataSearchClient.a(AppDataSearchClient.this).open();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.AppDataSearchClient
 * JD-Core Version:    0.7.0.1
 */