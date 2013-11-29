package com.google.android.search.core.google.gaia;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.UserRecoverableNotifiedException;
import com.google.android.search.core.GooglePlayServicesHelper;
import com.google.android.search.core.GooglePlayServicesHelper.Listener;
import com.google.android.shared.util.Consumer;
import java.io.IOException;
import javax.annotation.Nullable;

public class FallingBackGoogleAuthAdapter
  implements GoogleAuthAdapter
{
  private final GoogleAuthAdapter mAccountManagerAdapter;
  private Object mAdapterLock = new Object();
  @Nullable
  private GoogleAuthAdapter mAdapterToUse;
  private final GoogleAuthAdapter mGmsCoreAdapter;
  
  public FallingBackGoogleAuthAdapter(GoogleAuthAdapter paramGoogleAuthAdapter1, GoogleAuthAdapter paramGoogleAuthAdapter2, GooglePlayServicesHelper paramGooglePlayServicesHelper)
  {
    this.mGmsCoreAdapter = paramGoogleAuthAdapter1;
    this.mAccountManagerAdapter = paramGoogleAuthAdapter2;
    paramGooglePlayServicesHelper.addListener(new GooglePlayServicesHelper.Listener()
    {
      public void onAvailabilityChanged(int paramAnonymousInt)
      {
        FallingBackGoogleAuthAdapter.this.handleGmsCoreAvailability(paramAnonymousInt);
      }
    });
    paramGooglePlayServicesHelper.getGooglePlayServicesAvailabilityAsync(new Consumer()
    {
      public boolean consume(Integer paramAnonymousInteger)
      {
        FallingBackGoogleAuthAdapter.this.handleGmsCoreAvailability(paramAnonymousInteger.intValue());
        return false;
      }
    });
  }
  
  private GoogleAuthAdapter getAdapter()
  {
    synchronized (this.mAdapterLock)
    {
      GoogleAuthAdapter localGoogleAuthAdapter = this.mAdapterToUse;
      return localGoogleAuthAdapter;
    }
  }
  
  private void handleGmsCoreAvailability(int paramInt)
  {
    if (paramInt != 0)
    {
      setAdapter(this.mAccountManagerAdapter);
      return;
    }
    setAdapter(this.mGmsCoreAdapter);
  }
  
  private void setAdapter(GoogleAuthAdapter paramGoogleAuthAdapter)
  {
    synchronized (this.mAdapterLock)
    {
      this.mAdapterToUse = paramGoogleAuthAdapter;
      return;
    }
  }
  
  public String getTokenWithNotification(Context paramContext, String paramString1, String paramString2, Bundle paramBundle)
    throws UserRecoverableNotifiedException, IOException, GoogleAuthException
  {
    GoogleAuthAdapter localGoogleAuthAdapter = getAdapter();
    if (localGoogleAuthAdapter != null) {
      return localGoogleAuthAdapter.getTokenWithNotification(paramContext, paramString1, paramString2, paramBundle);
    }
    try
    {
      String str = this.mGmsCoreAdapter.getTokenWithNotification(paramContext, paramString1, paramString2, paramBundle);
      return str;
    }
    catch (UserRecoverableNotifiedException localUserRecoverableNotifiedException) {}
    return this.mAccountManagerAdapter.getTokenWithNotification(paramContext, paramString1, paramString2, paramBundle);
  }
  
  public void invalidateToken(Context paramContext, String paramString)
  {
    if (getAdapter() != null)
    {
      getAdapter().invalidateToken(paramContext, paramString);
      return;
    }
    this.mGmsCoreAdapter.invalidateToken(paramContext, paramString);
    this.mAccountManagerAdapter.invalidateToken(paramContext, paramString);
  }
  
  public void phoneCredentialsUpdated() {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.gaia.FallingBackGoogleAuthAdapter
 * JD-Core Version:    0.7.0.1
 */