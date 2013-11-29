package com.google.android.search.gel;

import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.shared.util.GelStartupPrefs;
import com.google.android.sidekick.shared.client.NowRemoteClient;
import com.google.android.sidekick.shared.client.NowRemoteClient.NowRemoteClientLock;
import com.google.android.sidekick.shared.remoteapi.LoggingRequest;

public class GoogleNowPromoController
{
  private final GelStartupPrefs mGelStartupPrefs;
  private final NowRemoteClient mNowRemoteClient;
  private NowRemoteClient.NowRemoteClientLock mNowRemoteClientLock;
  private final Runnable mRecordPromoClickedWorker = new Runnable()
  {
    public void run()
    {
      new GoogleNowPromoController.RecordPromoClickedTask(GoogleNowPromoController.this, null).execute(new Void[0]);
    }
  };
  private final Runnable mRecordPromoDismissedWorker = new Runnable()
  {
    public void run()
    {
      new GoogleNowPromoController.RecordPromoDismissedTask(GoogleNowPromoController.this, null).execute(new Void[0]);
    }
  };
  
  public GoogleNowPromoController(NowRemoteClient paramNowRemoteClient, GelStartupPrefs paramGelStartupPrefs)
  {
    this.mNowRemoteClient = paramNowRemoteClient;
    this.mGelStartupPrefs = paramGelStartupPrefs;
  }
  
  private void disconnectRemoteService()
  {
    this.mNowRemoteClient.unregisterConnectionListener(this.mRecordPromoClickedWorker);
    this.mNowRemoteClient.unregisterConnectionListener(this.mRecordPromoDismissedWorker);
    if (this.mNowRemoteClientLock != null)
    {
      this.mNowRemoteClientLock.release();
      this.mNowRemoteClientLock = null;
    }
  }
  
  private void runRunnable(Runnable paramRunnable)
  {
    if (this.mNowRemoteClientLock == null)
    {
      this.mNowRemoteClientLock = this.mNowRemoteClient.newConnectionLock("GoogleNowPromoController");
      this.mNowRemoteClientLock.acquire();
    }
    if (this.mNowRemoteClient.isConnected())
    {
      paramRunnable.run();
      return;
    }
    this.mNowRemoteClient.registerConnectionListener(paramRunnable);
  }
  
  public void recordPromoClicked()
  {
    runRunnable(this.mRecordPromoClickedWorker);
  }
  
  public void recordPromoDismissed()
  {
    runRunnable(this.mRecordPromoDismissedWorker);
  }
  
  public boolean shouldShowPromo()
  {
    boolean bool1 = this.mGelStartupPrefs.getBoolean("GSAPrefs.now_promo_dismissed", false);
    boolean bool2 = false;
    if (!bool1)
    {
      boolean bool3 = this.mGelStartupPrefs.getBoolean("GEL.GSAPrefs.now_enabled", false);
      bool2 = false;
      if (!bool3)
      {
        boolean bool4 = this.mGelStartupPrefs.getBoolean("GEL.GSAPrefs.can_optin_to_now", false);
        bool2 = false;
        if (bool4) {
          bool2 = true;
        }
      }
    }
    return bool2;
  }
  
  private class RecordPromoClickedTask
    extends AsyncTask<Void, Void, Void>
  {
    private RecordPromoClickedTask() {}
    
    protected Void doInBackground(Void... paramVarArgs)
    {
      if (GoogleNowPromoController.this.mNowRemoteClient.isConnected())
      {
        LoggingRequest localLoggingRequest = LoggingRequest.forAnalyticsAction("BUTTON_PRESS", "GET_GOOGLE_NOW_PROMO_ACCEPT");
        GoogleNowPromoController.this.mNowRemoteClient.logAction(localLoggingRequest);
      }
      for (;;)
      {
        GoogleNowPromoController.this.disconnectRemoteService();
        return null;
        Log.e("GoogleNowPromoController", "Service disconnected before we could log promo click");
      }
    }
  }
  
  private class RecordPromoDismissedTask
    extends AsyncTask<Void, Void, Void>
  {
    private RecordPromoDismissedTask() {}
    
    protected Void doInBackground(Void... paramVarArgs)
    {
      if (GoogleNowPromoController.this.mNowRemoteClient.isConnected())
      {
        LoggingRequest localLoggingRequest = LoggingRequest.forAnalyticsAction("BUTTON_PRESS", "GET_GOOGLE_NOW_PROMO_DISMISS");
        GoogleNowPromoController.this.mNowRemoteClient.logAction(localLoggingRequest);
      }
      for (;;)
      {
        try
        {
          GoogleNowPromoController.this.mNowRemoteClient.recordGoogleNowPromoDismissed();
          GoogleNowPromoController.this.mGelStartupPrefs.startReloadIfChanged();
          GoogleNowPromoController.this.disconnectRemoteService();
          return null;
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("GoogleNowPromoController", "Failed to record dismiss of Now promo", localRemoteException);
          continue;
        }
        Log.e("GoogleNowPromoController", "Service disconnected before we could log promo dismiss");
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.gel.GoogleNowPromoController
 * JD-Core Version:    0.7.0.1
 */