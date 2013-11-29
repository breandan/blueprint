package com.google.android.search.core.summons.icing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.util.Log;
import com.google.android.gms.appdatasearch.AppDataSearchClient;
import com.google.android.gms.appdatasearch.GlobalSearchApplicationInfo;
import com.google.android.search.core.GooglePlayServicesHelper;
import com.google.android.search.core.GooglePlayServicesHelper.Listener;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.summons.SourceNameHelper;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.Consumers;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.Executor;

public class IcingFactory
{
  private final SearchConfig mConfig;
  private final IcingConnection mConnection;
  private final Context mContext;
  private final GooglePlayServicesHelper mGooglePlayServicesHelper;
  private final Executor mUiExecutor;
  
  public IcingFactory(Context paramContext, Executor paramExecutor, SearchConfig paramSearchConfig, GooglePlayServicesHelper paramGooglePlayServicesHelper)
  {
    this.mContext = paramContext;
    this.mUiExecutor = paramExecutor;
    this.mConfig = paramSearchConfig;
    this.mGooglePlayServicesHelper = paramGooglePlayServicesHelper;
    if (new Random().nextInt(1000000) < paramSearchConfig.shouldLogIcingQueryPpm()) {}
    for (boolean bool = true;; bool = false)
    {
      this.mConnection = new IcingConnection(new AppDataSearchClient(this.mContext), paramSearchConfig, false, true, bool);
      if (this.mConfig.isIcingSourcesEnabled())
      {
        paramGooglePlayServicesHelper.getGooglePlayServicesAvailabilityAsync(new Consumer()
        {
          public boolean consume(Integer paramAnonymousInteger)
          {
            IcingFactory.this.mConnection.setServiceAvailable(IcingFactory.this.isServiceAvailable(paramAnonymousInteger.intValue()));
            return true;
          }
        });
        paramGooglePlayServicesHelper.addListener(new GooglePlayServicesHelper.Listener()
        {
          public void onAvailabilityChanged(int paramAnonymousInt)
          {
            IcingFactory.this.mConnection.setServiceAvailable(IcingFactory.this.isServiceAvailable(paramAnonymousInt));
          }
        });
      }
      return;
    }
  }
  
  private boolean isServiceAvailable(int paramInt)
  {
    return (paramInt == 0) && (this.mConfig.isIcingSourcesEnabled());
  }
  
  private void onAvailabilityDetermined(IcingSources paramIcingSources, Consumer<GlobalSearchApplicationInfo[]> paramConsumer, int paramInt)
  {
    boolean bool = isServiceAvailable(paramInt);
    if (bool)
    {
      this.mConnection.setServiceAvailable(bool);
      this.mConnection.getGlobalSearchRegisteredApplications(paramConsumer);
      return;
    }
    paramIcingSources.clear();
  }
  
  public IcingSources createIcingSources(SearchSettings paramSearchSettings, SourceNameHelper paramSourceNameHelper)
  {
    final IcingSourcesFactory localIcingSourcesFactory = new IcingSourcesFactory(this.mContext, this.mConfig, paramSourceNameHelper);
    final IcingSourcesImpl localIcingSourcesImpl = new IcingSourcesImpl(new IcingSuggestionsFactory(this.mConnection, paramSearchSettings, paramSourceNameHelper));
    if (!this.mConfig.isIcingSourcesEnabled())
    {
      Log.i("Search.IcingFactory", "All icing sources disabled");
      return localIcingSourcesImpl;
    }
    final Consumer localConsumer = Consumers.createAsyncConsumer(this.mUiExecutor, new Consumer()
    {
      public boolean consume(GlobalSearchApplicationInfo[] paramAnonymousArrayOfGlobalSearchApplicationInfo)
      {
        localIcingSourcesImpl.addSources(localIcingSourcesFactory.createSources(paramAnonymousArrayOfGlobalSearchApplicationInfo));
        return true;
      }
    });
    this.mGooglePlayServicesHelper.addListener(new GooglePlayServicesHelper.Listener()
    {
      public void onAvailabilityChanged(int paramAnonymousInt)
      {
        IcingFactory.this.onAvailabilityDetermined(localIcingSourcesImpl, localConsumer, paramAnonymousInt);
      }
    });
    this.mGooglePlayServicesHelper.getGooglePlayServicesAvailabilityAsync(new Consumer()
    {
      public boolean consume(Integer paramAnonymousInteger)
      {
        IcingFactory.this.onAvailabilityDetermined(localIcingSourcesImpl, localConsumer, paramAnonymousInteger.intValue());
        return true;
      }
    });
    IcingSourceUpdateReceiver localIcingSourceUpdateReceiver = new IcingSourceUpdateReceiver(localIcingSourcesImpl, localIcingSourcesFactory);
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.google.android.gms.icing.GlobalSearchAppRegistered");
    localIntentFilter.addAction("com.google.android.gms.icing.GlobalSearchableAppUnRegistered");
    this.mContext.getApplicationContext().registerReceiver(localIcingSourceUpdateReceiver, localIntentFilter);
    return localIcingSourcesImpl;
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    this.mConnection.dump(paramString, paramPrintWriter);
  }
  
  public ConnectionToIcing getConnectionToIcing()
  {
    return this.mConnection;
  }
  
  private static class IcingSourceUpdateReceiver
    extends BroadcastReceiver
  {
    private final IcingSourcesFactory mIcingSourceFactory;
    private final IcingSources mIcingSources;
    
    public IcingSourceUpdateReceiver(IcingSources paramIcingSources, IcingSourcesFactory paramIcingSourcesFactory)
    {
      this.mIcingSources = paramIcingSources;
      this.mIcingSourceFactory = paramIcingSourcesFactory;
    }
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      String str1 = paramIntent.getAction();
      if ("com.google.android.gms.icing.GlobalSearchAppRegistered".equals(str1))
      {
        localParcelable = paramIntent.getParcelableExtra("AppInfo");
        if ((localParcelable != null) && ((localParcelable instanceof GlobalSearchApplicationInfo)))
        {
          localGlobalSearchApplicationInfo = (GlobalSearchApplicationInfo)localParcelable;
          this.mIcingSources.addSources(this.mIcingSourceFactory.createSources(new GlobalSearchApplicationInfo[] { localGlobalSearchApplicationInfo }));
        }
      }
      while (!"com.google.android.gms.icing.GlobalSearchableAppUnRegistered".equals(str1))
      {
        Parcelable localParcelable;
        GlobalSearchApplicationInfo localGlobalSearchApplicationInfo;
        return;
      }
      String str2 = paramIntent.getStringExtra("AppPackageName");
      this.mIcingSources.removeSources(str2);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.IcingFactory
 * JD-Core Version:    0.7.0.1
 */