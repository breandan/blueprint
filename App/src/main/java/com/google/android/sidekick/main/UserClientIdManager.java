package com.google.android.sidekick.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Supplier;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

public class UserClientIdManager
{
  private static final String TAG = Tag.getTag(UserClientIdManager.class);
  private final Context mAppContext;
  private Object mIdAndPreferenceLock = new Object();
  private final AtomicBoolean mInitialized = new AtomicBoolean(false);
  private final CountDownLatch mInitializedLatch = new CountDownLatch(1);
  private final String mKey;
  private volatile boolean mNeedToRegenerate = false;
  private final Supplier<SharedPreferences> mPreferencesSupplier;
  private volatile long mUserClientId;
  
  public UserClientIdManager(Context paramContext, Supplier<SharedPreferences> paramSupplier)
  {
    this.mAppContext = paramContext.getApplicationContext();
    this.mPreferencesSupplier = paramSupplier;
    this.mKey = this.mAppContext.getString(2131362047);
  }
  
  private boolean awaitInitializationComplete()
  {
    try
    {
      this.mInitializedLatch.await();
      return true;
    }
    catch (InterruptedException localInterruptedException)
    {
      Log.w(TAG, "Initialization latch wait interrupted");
      Thread.currentThread().interrupt();
    }
    return false;
  }
  
  private void initializeIfNeeded()
  {
    if (!this.mInitialized.getAndSet(true)) {
      synchronized (this.mIdAndPreferenceLock)
      {
        SharedPreferences localSharedPreferences = (SharedPreferences)this.mPreferencesSupplier.get();
        if (!localSharedPreferences.contains(this.mKey))
        {
          synchronousRegenerateAndStoreRandomClientId();
          this.mInitializedLatch.countDown();
          return;
        }
        this.mUserClientId = localSharedPreferences.getLong(this.mKey, 0L);
      }
    }
  }
  
  @Nullable
  public Long getUserClientId()
  {
    ExtraPreconditions.checkNotMainThread();
    initializeIfNeeded();
    if (!awaitInitializationComplete()) {
      return null;
    }
    if (this.mNeedToRegenerate) {}
    synchronized (this.mIdAndPreferenceLock)
    {
      if (this.mNeedToRegenerate)
      {
        synchronousRegenerateAndStoreRandomClientId();
        this.mNeedToRegenerate = false;
      }
      return Long.valueOf(this.mUserClientId);
    }
  }
  
  public void setNeedToRegenerateAndStoreRandomClientId()
  {
    this.mNeedToRegenerate = true;
  }
  
  void synchronousRegenerateAndStoreRandomClientId()
  {
    ExtraPreconditions.checkHoldsLock(this.mIdAndPreferenceLock);
    ExtraPreconditions.checkNotMainThread();
    long l = new Random().nextLong();
    ((SharedPreferences)this.mPreferencesSupplier.get()).edit().putLong(this.mKey, l).apply();
    this.mUserClientId = l;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.UserClientIdManager
 * JD-Core Version:    0.7.0.1
 */