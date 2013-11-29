package com.google.android.search.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.ExecutorAsyncTask;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class GooglePlayServicesHelper
{
  public static final String BAZAAR_PACKAGE = "com.google.android.apps.bazaar";
  public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
  private static final String TAG = Tag.getTag(GooglePlayServicesHelper.class);
  private final Executor mBgExecutor;
  private Integer mCachedAvailabilityResult;
  private Integer mCachedVersionCode;
  private final Context mContext;
  private final Supplier<Integer> mGooglePlayServicesAvailabilitySupplier;
  private final Supplier<Integer> mGooglePlayServicesVersionSupplier;
  private Integer mLastAnnouncedAvailabilityResult;
  private final List<Listener> mListeners;
  private GmsPackageWatcher mPackageWatcher;
  private final Executor mUiExecutor;
  
  public GooglePlayServicesHelper(Context paramContext, Executor paramExecutor1, Executor paramExecutor2)
  {
    this(paramContext, paramExecutor1, paramExecutor2, new Supplier()new Supplier
    {
      public Integer get()
      {
        return Integer.valueOf(GooglePlayServicesUtil.isGooglePlayServicesAvailable(GooglePlayServicesHelper.this));
      }
    }, new Supplier()
    {
      public Integer get()
      {
        try
        {
          Integer localInteger = Integer.valueOf(GooglePlayServicesHelper.this.getPackageManager().getPackageInfo("com.google.android.gms", 0).versionCode);
          return localInteger;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
        return Integer.valueOf(0);
      }
    });
  }
  
  GooglePlayServicesHelper(Context paramContext, Executor paramExecutor1, Executor paramExecutor2, Supplier<Integer> paramSupplier1, Supplier<Integer> paramSupplier2)
  {
    this.mContext = paramContext;
    this.mBgExecutor = paramExecutor1;
    this.mUiExecutor = paramExecutor2;
    this.mGooglePlayServicesAvailabilitySupplier = paramSupplier1;
    this.mGooglePlayServicesVersionSupplier = paramSupplier2;
    this.mListeners = new ArrayList();
  }
  
  private Intent createBazaarIntent(String paramString)
  {
    Uri localUri = Uri.parse("bazaar://search?q=pname:" + paramString);
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(localUri);
    localIntent.setFlags(524288);
    return localIntent;
  }
  
  private Intent createPlayStoreIntent(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(getInternalPlayStoreUri(paramString));
    localIntent.setPackage("com.android.vending");
    localIntent.addFlags(524288);
    return localIntent;
  }
  
  private Intent createSettingsIntent(String paramString)
  {
    Uri localUri = Uri.fromParts("package", paramString, null);
    Intent localIntent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
    localIntent.setData(localUri);
    return localIntent;
  }
  
  private Uri getInternalPlayStoreUri(String paramString)
  {
    return Uri.parse("market://details").buildUpon().appendQueryParameter("id", paramString).build();
  }
  
  private void registerPackageWatcher()
  {
    if (this.mPackageWatcher == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mPackageWatcher = new GmsPackageWatcher(this);
      this.mContext.registerReceiver(this.mPackageWatcher, this.mPackageWatcher.mIntentFilter);
      return;
    }
  }
  
  public void addListener(Listener paramListener)
  {
    for (;;)
    {
      synchronized (this.mListeners)
      {
        if (!this.mListeners.contains(paramListener))
        {
          bool = true;
          Preconditions.checkArgument(bool, "listener already added");
          this.mListeners.add(paramListener);
          return;
        }
      }
      boolean bool = false;
    }
  }
  
  @Deprecated
  public int getGooglePlayServicesAvailability()
  {
    int i;
    try
    {
      if (this.mPackageWatcher == null) {
        registerPackageWatcher();
      }
      if (this.mCachedAvailabilityResult == null)
      {
        this.mCachedAvailabilityResult = ((Integer)this.mGooglePlayServicesAvailabilitySupplier.get());
        this.mCachedVersionCode = ((Integer)this.mGooglePlayServicesVersionSupplier.get());
        EventLogger.recordClientEvent(109, new LogData(this.mCachedAvailabilityResult.intValue(), this.mCachedVersionCode.intValue()));
      }
      i = this.mCachedAvailabilityResult.intValue();
      if ((this.mLastAnnouncedAvailabilityResult != null) && (i == this.mLastAnnouncedAvailabilityResult.intValue())) {
        break label173;
      }
      synchronized (this.mListeners)
      {
        Iterator localIterator = this.mListeners.iterator();
        if (localIterator.hasNext()) {
          ((Listener)localIterator.next()).onAvailabilityChanged(i);
        }
      }
    }
    finally {}
    this.mLastAnnouncedAvailabilityResult = Integer.valueOf(i);
    label173:
    return i;
  }
  
  public void getGooglePlayServicesAvailabilityAsync(final Consumer<Integer> paramConsumer)
  {
    new ExecutorAsyncTask(this.mUiExecutor, this.mBgExecutor)
    {
      protected Integer doInBackground(Void... paramAnonymousVarArgs)
      {
        return Integer.valueOf(GooglePlayServicesHelper.this.getGooglePlayServicesAvailability());
      }
      
      protected void onPostExecute(Integer paramAnonymousInteger)
      {
        paramConsumer.consume(paramAnonymousInteger);
      }
    }.execute(new Void[0]);
  }
  
  @Nullable
  public Intent getGooglePlayServicesAvailabilityRecoveryIntent(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case 1: 
    case 2: 
      if (useBazaar())
      {
        if (isBazaarInstalled()) {
          return createBazaarIntent("com.google.android.gms");
        }
        return createPlayStoreIntent("com.google.android.apps.bazaar");
      }
      return createPlayStoreIntent("com.google.android.gms");
    }
    return createSettingsIntent("com.google.android.gms");
  }
  
  public int getGooglePlayServicesVersionCode()
  {
    try
    {
      if (this.mCachedVersionCode == null) {
        this.mCachedVersionCode = ((Integer)this.mGooglePlayServicesVersionSupplier.get());
      }
      int i = this.mCachedVersionCode.intValue();
      return i;
    }
    finally {}
  }
  
  protected boolean isBazaarInstalled()
  {
    try
    {
      this.mContext.getPackageManager().getPackageInfo("com.google.android.apps.bazaar", 64);
      return true;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
    return false;
  }
  
  public boolean isGooglePlayServicesAvailable()
  {
    return getGooglePlayServicesAvailability() == 0;
  }
  
  public void removeListener(Listener paramListener)
  {
    synchronized (this.mListeners)
    {
      Preconditions.checkState(this.mListeners.remove(paramListener), "listener not added");
      return;
    }
  }
  
  public void resetGooglePlayServicesAvailability()
  {
    try
    {
      this.mCachedAvailabilityResult = null;
      this.mCachedVersionCode = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  protected boolean useBazaar()
  {
    if (DebugFeatures.getInstance().dogfoodDebugEnabled()) {}
    return false;
  }
  
  public static class GmsPackageWatcher
    extends BroadcastReceiver
  {
    private final GooglePlayServicesHelper mHelper;
    private final IntentFilter mIntentFilter;
    
    public GmsPackageWatcher(GooglePlayServicesHelper paramGooglePlayServicesHelper)
    {
      this.mHelper = paramGooglePlayServicesHelper;
      this.mIntentFilter = new IntentFilter();
      this.mIntentFilter.addAction("android.intent.action.PACKAGE_ADDED");
      this.mIntentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
      this.mIntentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
      this.mIntentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
      this.mIntentFilter.addDataScheme("package");
    }
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      Uri localUri = paramIntent.getData();
      if (localUri == null) {}
      while ((!this.mIntentFilter.matchAction(paramIntent.getAction())) || (!"package".equals(localUri.getScheme())) || (!"com.google.android.gms".equals(localUri.getEncodedSchemeSpecificPart()))) {
        return;
      }
      this.mHelper.resetGooglePlayServicesAvailability();
    }
  }
  
  public static abstract interface Listener
  {
    public abstract void onAvailabilityChanged(int paramInt);
  }
  
  public static final class LogData
  {
    public final int googlePlayServicesAvailability;
    public final int googlePlayServicesVersion;
    
    public LogData(int paramInt1, int paramInt2)
    {
      this.googlePlayServicesAvailability = paramInt1;
      this.googlePlayServicesVersion = paramInt2;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.GooglePlayServicesHelper
 * JD-Core Version:    0.7.0.1
 */