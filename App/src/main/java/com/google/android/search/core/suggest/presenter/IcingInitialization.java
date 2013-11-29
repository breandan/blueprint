package com.google.android.search.core.suggest.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GooglePlayServicesHelper;
import com.google.android.search.core.GooglePlayServicesHelper.Listener;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.summons.icing.InternalCorpus;
import com.google.android.search.core.summons.icing.InternalIcingCorporaProvider;
import com.google.android.search.core.summons.icing.InternalIcingCorporaProvider.ApplicationLaunchReceiver;
import com.google.android.search.core.summons.icing.InternalIcingCorporaProvider.UpdateCorporaService;
import com.google.android.shared.util.Consumer;
import com.google.android.voicesearch.logger.EventLogger;
import java.util.concurrent.Executor;

public class IcingInitialization
{
  private final Context mAppContext;
  private final Executor mBgExecutor;
  private final CoreSearchServices mCoreSearchServices;
  private GooglePlayServicesHelper.Listener mGcoreHelperListener;
  
  public IcingInitialization(CoreSearchServices paramCoreSearchServices, Executor paramExecutor, Context paramContext)
  {
    this.mCoreSearchServices = paramCoreSearchServices;
    this.mBgExecutor = paramExecutor;
    this.mAppContext = paramContext;
  }
  
  public void destroy()
  {
    if (this.mGcoreHelperListener != null)
    {
      this.mCoreSearchServices.getGooglePlayServicesHelper().removeListener(this.mGcoreHelperListener);
      this.mGcoreHelperListener = null;
    }
  }
  
  public void initialize()
  {
    if ((InternalIcingCorporaProvider.CONTACTS_DELTA_API_SUPPORTED) && (InternalCorpus.isContactsCorpusEnabled(this.mCoreSearchServices.getConfig()))) {
      this.mAppContext.startService(InternalIcingCorporaProvider.UpdateCorporaService.createContactsDeltaUpdateIntent(this.mAppContext));
    }
    GooglePlayServicesHelper localGooglePlayServicesHelper = this.mCoreSearchServices.getGooglePlayServicesHelper();
    localGooglePlayServicesHelper.getGooglePlayServicesAvailabilityAsync(new Consumer()
    {
      public boolean consume(Integer paramAnonymousInteger)
      {
        IcingInitialization.this.maybeRegisterIcingCorpora(paramAnonymousInteger.intValue());
        return true;
      }
    });
    this.mGcoreHelperListener = new GooglePlayServicesHelper.Listener()
    {
      public void onAvailabilityChanged(int paramAnonymousInt)
      {
        IcingInitialization.this.maybeRegisterIcingCorpora(paramAnonymousInt);
      }
    };
    localGooglePlayServicesHelper.addListener(this.mGcoreHelperListener);
  }
  
  void maybeRegisterIcingCorpora(int paramInt)
  {
    SearchConfig localSearchConfig = this.mCoreSearchServices.getConfig();
    String[] arrayOfString;
    if ((paramInt == 0) && (localSearchConfig.isInternalIcingCorporaEnabled()))
    {
      arrayOfString = localSearchConfig.getDisabledInternalIcingCorpora();
      if (this.mAppContext.registerReceiver(null, new IntentFilter("android.intent.action.DEVICE_STORAGE_LOW")) == null) {
        break label94;
      }
    }
    label94:
    for (boolean bool = true;; bool = false)
    {
      this.mBgExecutor.execute(new IcingTask(this.mAppContext.getContentResolver(), this.mAppContext.getPackageManager(), this.mAppContext.getPackageName(), arrayOfString, bool, localSearchConfig.isIcingAppLaunchBroadcastHandlingEnabled()));
      return;
    }
  }
  
  public static class DiagnosticsLogData
  {
    public final boolean deviceStorageLow;
    public final Bundle diagnosticsBundle;
    
    public DiagnosticsLogData(Bundle paramBundle, boolean paramBoolean)
    {
      this.diagnosticsBundle = paramBundle;
      this.deviceStorageLow = paramBoolean;
    }
  }
  
  static class IcingTask
    implements Runnable
  {
    private final boolean mAppLaunchLoggingEnabled;
    private final ContentResolver mContentResolver;
    private final boolean mDeviceStorageLow;
    private final String[] mDisabledCorpora;
    private final String mMyPackageName;
    private final PackageManager mPackageManager;
    
    public IcingTask(ContentResolver paramContentResolver, PackageManager paramPackageManager, String paramString, String[] paramArrayOfString, boolean paramBoolean1, boolean paramBoolean2)
    {
      this.mContentResolver = paramContentResolver;
      this.mPackageManager = paramPackageManager;
      this.mMyPackageName = paramString;
      this.mDisabledCorpora = paramArrayOfString;
      this.mDeviceStorageLow = paramBoolean1;
      this.mAppLaunchLoggingEnabled = paramBoolean2;
    }
    
    private void registerCorpora()
    {
      this.mContentResolver.update(InternalIcingCorporaProvider.REGISTER_CORPORA_URI, null, null, this.mDisabledCorpora);
    }
    
    IcingInitialization.DiagnosticsLogData performDiagnostics()
    {
      Cursor localCursor = this.mContentResolver.query(InternalIcingCorporaProvider.DIAGNOSE_QUERY_URI, null, null, this.mDisabledCorpora, null);
      IcingInitialization.DiagnosticsLogData localDiagnosticsLogData = null;
      Bundle localBundle;
      if (localCursor != null)
      {
        localBundle = new Bundle();
        try
        {
          while (localCursor.moveToNext()) {
            localBundle.putInt(localCursor.getString(0), localCursor.getInt(1));
          }
          localDiagnosticsLogData = new IcingInitialization.DiagnosticsLogData(localBundle, this.mDeviceStorageLow);
        }
        finally
        {
          localCursor.close();
        }
      }
      return localDiagnosticsLogData;
    }
    
    public void run()
    {
      InternalIcingCorporaProvider.ApplicationLaunchReceiver.setEnabledSetting(this.mPackageManager, this.mMyPackageName, this.mAppLaunchLoggingEnabled);
      IcingInitialization.DiagnosticsLogData localDiagnosticsLogData = performDiagnostics();
      if (localDiagnosticsLogData != null) {
        EventLogger.recordClientEvent(121, localDiagnosticsLogData);
      }
      registerCorpora();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.presenter.IcingInitialization
 * JD-Core Version:    0.7.0.1
 */