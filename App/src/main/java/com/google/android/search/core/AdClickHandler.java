package com.google.android.search.core;

import android.net.Uri;
import android.util.Log;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.search.core.util.HttpHelper.HttpRedirectException;
import com.google.android.search.core.util.UriRequest;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.IOException;
import java.util.concurrent.Executor;

public class AdClickHandler
{
  private final Executor mBgExecutor;
  private Supplier<UriRequest> mClickedAd;
  private final Client mClient;
  private final HttpHelper mHttpHelper;
  private final ScheduledSingleThreadedExecutor mUiThread;
  
  public AdClickHandler(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, Executor paramExecutor, HttpHelper paramHttpHelper, Client paramClient)
  {
    this.mUiThread = paramScheduledSingleThreadedExecutor;
    this.mBgExecutor = paramExecutor;
    this.mHttpHelper = paramHttpHelper;
    this.mClient = paramClient;
  }
  
  private void handleRedirect(Supplier<UriRequest> paramSupplier, Uri paramUri)
  {
    Preconditions.checkState(this.mUiThread.isThisThread());
    if (paramSupplier != this.mClickedAd) {
      return;
    }
    this.mClickedAd = null;
    if (paramUri != null)
    {
      this.mClient.onReceivedAdClickRedirect(paramUri);
      return;
    }
    this.mClient.onAdClickRedirectError();
  }
  
  private void logAdClickAndGetRedirectDestination(final Supplier<UriRequest> paramSupplier)
  {
    HttpHelper.GetRequest localGetRequest = ((UriRequest)paramSupplier.get()).asGetRequest();
    localGetRequest.setFollowRedirects(false);
    Uri localUri1 = null;
    try
    {
      this.mHttpHelper.get(localGetRequest, 5);
      Log.w("Velvet.AdClickHandler", "Did not receive a redirect from an ad click!");
      final Uri localUri2 = localUri1;
      this.mUiThread.execute(new Runnable()
      {
        public void run()
        {
          AdClickHandler.this.handleRedirect(paramSupplier, localUri2);
        }
      });
      return;
    }
    catch (HttpHelper.HttpRedirectException localHttpRedirectException)
    {
      for (;;)
      {
        localUri1 = Uri.parse(localHttpRedirectException.getRedirectLocation());
      }
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        Log.w("Velvet.AdClickHandler", "Ad click failed: " + localIOException);
        localUri1 = null;
      }
    }
  }
  
  public void cancel()
  {
    Preconditions.checkState(this.mUiThread.isThisThread());
    this.mClickedAd = null;
  }
  
  public void onAdClicked(final Supplier<UriRequest> paramSupplier)
  {
    Preconditions.checkState(this.mUiThread.isThisThread());
    this.mClickedAd = paramSupplier;
    this.mBgExecutor.execute(new Runnable()
    {
      public void run()
      {
        AdClickHandler.this.logAdClickAndGetRedirectDestination(paramSupplier);
      }
    });
  }
  
  public static abstract interface Client
  {
    public abstract void onAdClickRedirectError();
    
    public abstract void onReceivedAdClickRedirect(Uri paramUri);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.AdClickHandler
 * JD-Core Version:    0.7.0.1
 */