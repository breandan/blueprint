package com.google.android.sidekick.main;

import android.accounts.Account;
import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.reporting.ReportingClient;
import com.google.android.gms.location.reporting.ReportingState;
import com.google.android.gms.location.reporting.UploadRequest;
import com.google.android.gms.location.reporting.UploadRequest.Builder;
import com.google.android.gms.location.reporting.UploadRequestResult;
import com.google.android.search.core.GmsClientWrapper;
import com.google.android.search.core.GmsClientWrapper.GmsFuture;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.geo.sidekick.Sidekick.LocationReportingConfiguration;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class GmsLocationReportingHelper
  extends GmsClientWrapper<ReportingClient>
{
  private static final String TAG = Tag.getTag(GmsLocationReportingHelper.class);
  private final Map<String, ActiveBurstRequest> mActiveBurstRequests = Maps.newHashMap();
  private final Clock mClock;
  private final LoginHelper mLoginHelper;
  
  public GmsLocationReportingHelper(Context paramContext, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, Executor paramExecutor, LoginHelper paramLoginHelper, Clock paramClock)
  {
    super(TAG, paramContext, paramScheduledSingleThreadedExecutor, paramExecutor, 30000L);
    this.mLoginHelper = paramLoginHelper;
    this.mClock = paramClock;
  }
  
  private int cancelActiveBurstRequest(String paramString)
    throws IOException
  {
    synchronized (this.mActiveBurstRequests)
    {
      ActiveBurstRequest localActiveBurstRequest = (ActiveBurstRequest)this.mActiveBurstRequests.remove(paramString);
      if (localActiveBurstRequest != null)
      {
        long l = localActiveBurstRequest.mRequestId;
        int i = ((ReportingClient)getClient()).cancelUpload(l);
        if (i == 100) {
          i = 0;
        }
        return i;
      }
    }
    return 0;
  }
  
  public GmsClientWrapper.GmsFuture<Integer> cancelBurstMode(final String paramString)
  {
    synchronized (this.mActiveBurstRequests)
    {
      if (!this.mActiveBurstRequests.containsKey(paramString))
      {
        GmsClientWrapper.GmsFuture localGmsFuture = GmsClientWrapper.GmsFuture.immediateFuture(Integer.valueOf(0));
        return localGmsFuture;
      }
      invoke(new Callable()
      {
        public Integer call()
          throws Exception
        {
          return Integer.valueOf(GmsLocationReportingHelper.this.cancelActiveBurstRequest(paramString));
        }
      });
    }
  }
  
  protected ReportingClient createClient(Context paramContext, GooglePlayServicesClient.ConnectionCallbacks paramConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener paramOnConnectionFailedListener)
  {
    return new ReportingClient(paramContext, paramConnectionCallbacks, paramOnConnectionFailedListener);
  }
  
  public GmsClientWrapper.GmsFuture<ReportingState> getCurrentAccountReportingState()
  {
    Account localAccount = this.mLoginHelper.getAccount();
    if (localAccount == null) {
      return GmsClientWrapper.GmsFuture.immediateFuture(null);
    }
    return getReportingState(localAccount);
  }
  
  public GmsClientWrapper.GmsFuture<ReportingState> getReportingState(final Account paramAccount)
  {
    invoke(new Callable()
    {
      public ReportingState call()
        throws Exception
      {
        return ((ReportingClient)GmsLocationReportingHelper.this.getClient()).getReportingState(paramAccount);
      }
    });
  }
  
  public GmsClientWrapper.GmsFuture<Integer> handleLocationReportingConfiguration(Sidekick.LocationReportingConfiguration paramLocationReportingConfiguration)
  {
    Account localAccount = this.mLoginHelper.getAccount();
    if (localAccount == null) {
      return GmsClientWrapper.GmsFuture.immediateFuture(Integer.valueOf(3));
    }
    String str = paramLocationReportingConfiguration.getAppSpecificKey();
    if (Strings.isNullOrEmpty(str)) {
      return GmsClientWrapper.GmsFuture.immediateFailedFuture(new IllegalArgumentException("Invalid app-specific key"));
    }
    if (paramLocationReportingConfiguration.hasCancelBurstReporting()) {
      return cancelBurstMode(str);
    }
    long l1 = TimeUnit.SECONDS.toMillis(paramLocationReportingConfiguration.getEndBurstReportingTimestampSeconds()) - this.mClock.currentTimeMillis();
    if (l1 < 0L) {
      return GmsClientWrapper.GmsFuture.immediateFuture(Integer.valueOf(2));
    }
    long l2 = l1 + this.mClock.elapsedRealtime();
    synchronized (this.mActiveBurstRequests)
    {
      ActiveBurstRequest localActiveBurstRequest = (ActiveBurstRequest)this.mActiveBurstRequests.get(str);
      if ((localActiveBurstRequest != null) && (l2 <= localActiveBurstRequest.mEndRealtime)) {
        return GmsClientWrapper.GmsFuture.immediateFuture(Integer.valueOf(0));
      }
    }
    return requestBurstMode(UploadRequest.builder(localAccount, paramLocationReportingConfiguration.getReason(), l1).appSpecificKey(str).movingLatencyMillis(paramLocationReportingConfiguration.getMovingFrequencyMillis()).stationaryLatencyMillis(paramLocationReportingConfiguration.getStationaryFrequencyMillis()).build());
  }
  
  public GmsClientWrapper.GmsFuture<Integer> requestBurstMode(final UploadRequest paramUploadRequest)
  {
    invoke(new Callable()
    {
      public Integer call()
        throws Exception
      {
        synchronized (GmsLocationReportingHelper.this.mActiveBurstRequests)
        {
          GmsLocationReportingHelper.this.cancelActiveBurstRequest(paramUploadRequest.getAppSpecificKey());
          UploadRequestResult localUploadRequestResult = ((ReportingClient)GmsLocationReportingHelper.this.getClient()).requestUpload(paramUploadRequest);
          if (localUploadRequestResult.getResultCode() == 0)
          {
            GmsLocationReportingHelper.ActiveBurstRequest localActiveBurstRequest = new GmsLocationReportingHelper.ActiveBurstRequest(GmsLocationReportingHelper.this.mClock.elapsedRealtime() + paramUploadRequest.getDurationMillis(), localUploadRequestResult.getRequestId());
            GmsLocationReportingHelper.this.mActiveBurstRequests.put(paramUploadRequest.getAppSpecificKey(), localActiveBurstRequest);
          }
          Integer localInteger = Integer.valueOf(localUploadRequestResult.getResultCode());
          return localInteger;
        }
      }
    });
  }
  
  public GmsClientWrapper.GmsFuture<Integer> tryOptIn(final Account paramAccount)
  {
    invoke(new Callable()
    {
      public Integer call()
        throws Exception
      {
        return Integer.valueOf(((ReportingClient)GmsLocationReportingHelper.this.getClient()).tryOptIn(paramAccount));
      }
    });
  }
  
  private static class ActiveBurstRequest
  {
    private final long mEndRealtime;
    private long mRequestId;
    
    ActiveBurstRequest(long paramLong1, long paramLong2)
    {
      this.mEndRealtime = paramLong1;
      this.mRequestId = paramLong2;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.GmsLocationReportingHelper
 * JD-Core Version:    0.7.0.1
 */