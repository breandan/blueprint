package com.google.android.sidekick.main.gcm;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetApplication;
import com.google.common.base.Supplier;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

public class GcmManager
{
  private static final long REGISTRATION_EXPIRATION_MILLS = TimeUnit.DAYS.toMillis(7L);
  private static final String TAG = Tag.getTag(GcmManager.class);
  private final Context mAppContext;
  private final String mApplicationVersion;
  private final Clock mClock;
  private final GoogleCloudMessaging mGoogleCloudMessaging;
  private final AtomicBoolean mKnownRegistered;
  private final Supplier<SharedPreferences> mMainPreferencesSupplier;
  
  public GcmManager(Context paramContext, GoogleCloudMessaging paramGoogleCloudMessaging, Clock paramClock, Supplier<SharedPreferences> paramSupplier)
  {
    this.mAppContext = paramContext.getApplicationContext();
    this.mClock = paramClock;
    this.mMainPreferencesSupplier = paramSupplier;
    this.mApplicationVersion = VelvetApplication.getVersionName();
    this.mGoogleCloudMessaging = paramGoogleCloudMessaging;
    this.mKnownRegistered = new AtomicBoolean(false);
  }
  
  @Nullable
  private RegistrationState getAccountState(Account paramAccount, long paramLong)
  {
    SharedPreferences localSharedPreferences = (SharedPreferences)this.mMainPreferencesSupplier.get();
    String str1 = this.mAppContext.getString(2131362049) + "state:" + paramAccount.name;
    String str2 = this.mAppContext.getString(2131362049) + "expires:" + paramAccount.name;
    String str3 = localSharedPreferences.getString(str1, null);
    long l = localSharedPreferences.getLong(str2, 0L);
    if ((str3 == null) || (l < paramLong)) {
      return null;
    }
    try
    {
      RegistrationState localRegistrationState = RegistrationState.decode(str3);
      return localRegistrationState;
    }
    catch (Exception localException)
    {
      Log.w(TAG, "Bad GCM registration state", localException);
    }
    return null;
  }
  
  @Nullable
  private RegistrationState getDeviceState()
  {
    String str = ((SharedPreferences)this.mMainPreferencesSupplier.get()).getString(this.mAppContext.getString(2131362048), null);
    if (str == null) {
      return null;
    }
    try
    {
      RegistrationState localRegistrationState = RegistrationState.decode(str);
      return localRegistrationState;
    }
    catch (Exception localException)
    {
      Log.w(TAG, "Bad GCM registration state", localException);
    }
    return null;
  }
  
  private void registerIfNeeded()
  {
    if (!this.mKnownRegistered.getAndSet(true))
    {
      RegistrationState localRegistrationState = getDeviceState();
      if ((localRegistrationState != null) && (this.mApplicationVersion.equals(localRegistrationState.mApplicationVersion))) {}
    }
    try
    {
      register();
      return;
    }
    catch (IOException localIOException)
    {
      Log.w(TAG, "Failure to register with GCM", localIOException);
      this.mKnownRegistered.set(false);
    }
  }
  
  public void ackRegistrationChangeFor(Account paramAccount, RegistrationState paramRegistrationState)
  {
    setAccountState(paramAccount, paramRegistrationState, this.mClock.currentTimeMillis() + REGISTRATION_EXPIRATION_MILLS);
  }
  
  @Nullable
  public RegistrationState getRegistrationChangeFor(Account paramAccount)
  {
    registerIfNeeded();
    RegistrationState localRegistrationState1 = getDeviceState();
    RegistrationState localRegistrationState2 = getAccountState(paramAccount, this.mClock.currentTimeMillis());
    if ((localRegistrationState1 == null) || (localRegistrationState1.equals(localRegistrationState2))) {
      localRegistrationState1 = null;
    }
    return localRegistrationState1;
  }
  
  void notifyOfRegistration(String paramString)
  {
    setDeviceState(new RegistrationState(this.mApplicationVersion, paramString));
  }
  
  public void register()
    throws IOException
  {
    ExtraPreconditions.checkNotMainThread();
    notifyOfRegistration(this.mGoogleCloudMessaging.register(new String[] { "638181764685" }));
  }
  
  void setAccountState(Account paramAccount, RegistrationState paramRegistrationState, long paramLong)
  {
    SharedPreferences localSharedPreferences = (SharedPreferences)this.mMainPreferencesSupplier.get();
    String str1 = this.mAppContext.getString(2131362049) + "state:" + paramAccount.name;
    String str2 = this.mAppContext.getString(2131362049) + "expires:" + paramAccount.name;
    String str3 = paramRegistrationState.encode();
    localSharedPreferences.edit().putString(str1, str3).putLong(str2, paramLong).apply();
  }
  
  void setDeviceState(RegistrationState paramRegistrationState)
  {
    SharedPreferences localSharedPreferences = (SharedPreferences)this.mMainPreferencesSupplier.get();
    String str1 = this.mAppContext.getString(2131362048);
    String str2 = paramRegistrationState.encode();
    localSharedPreferences.edit().putString(str1, str2).apply();
  }
  
  public static class RegistrationState
  {
    public final String mApplicationVersion;
    public final String mRegistrationId;
    
    RegistrationState(String paramString1, String paramString2)
    {
      this.mRegistrationId = paramString2;
      this.mApplicationVersion = paramString1;
    }
    
    static RegistrationState decode(String paramString)
    {
      String str1 = paramString.substring(1);
      int i = str1.lastIndexOf(':');
      String str2;
      if (i > 0) {
        str2 = str1.substring(0, i);
      }
      for (String str3 = str1.substring(i + 1);; str3 = "")
      {
        return new RegistrationState(str3, str2);
        str2 = str1;
      }
    }
    
    String encode()
    {
      return '+' + this.mRegistrationId + ':' + this.mApplicationVersion;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {}
      RegistrationState localRegistrationState;
      do
      {
        return true;
        if (paramObject == null) {
          return false;
        }
        if (getClass() != paramObject.getClass()) {
          return false;
        }
        localRegistrationState = (RegistrationState)paramObject;
        if (!this.mApplicationVersion.equals(localRegistrationState.mApplicationVersion)) {
          return false;
        }
      } while (this.mRegistrationId.equals(localRegistrationState.mRegistrationId));
      return false;
    }
    
    public int hashCode()
    {
      return 31 * (31 + this.mRegistrationId.hashCode()) + this.mApplicationVersion.hashCode();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.gcm.GcmManager
 * JD-Core Version:    0.7.0.1
 */