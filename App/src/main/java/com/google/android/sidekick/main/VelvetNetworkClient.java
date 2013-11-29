package com.google.android.sidekick.main;

import android.accounts.Account;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.util.Log;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GooglePlayServicesHelper;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.search.core.preferences.SharedPreferencesExt.Editor;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.HttpException;
import com.google.android.search.core.util.HttpHelper.PostRequest;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.main.inject.ExecutedUserActionStore;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.NetworkClient.ResponseAndEventId;
import com.google.android.sidekick.main.inject.SessionManager;
import com.google.android.sidekick.main.inject.SessionManager.SessionKey;
import com.google.android.velvet.VelvetBackgroundTasks;
import com.google.android.velvet.util.IntentUtils;
import com.google.common.collect.ImmutableSet;
import com.google.geo.sidekick.Sidekick.ActionsQuery;
import com.google.geo.sidekick.Sidekick.AndroidClientDescription;
import com.google.geo.sidekick.Sidekick.ClientDescription;
import com.google.geo.sidekick.Sidekick.ClientLatency;
import com.google.geo.sidekick.Sidekick.EntryQuery;
import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import com.google.geo.sidekick.Sidekick.ExperimentOverrides;
import com.google.geo.sidekick.Sidekick.Interest;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.geo.sidekick.Sidekick.SensorSignals;
import com.google.geo.sidekick.Sidekick.SidekickHttpRequest;
import com.google.geo.sidekick.Sidekick.SidekickHttpResponse;
import com.google.protobuf.micro.ByteStringMicro;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

public class VelvetNetworkClient
  implements NetworkClient
{
  public static final Sidekick.ResponsePayload AUTHENTICATION_FAILURE_RESPONSE = new Sidekick.ResponsePayload();
  static int LATENCY_SAMPLING_RATE = 10;
  private final String mAppVersionName;
  private final ConnectivityManager mConnectivityManager;
  private final Context mContext;
  private final CoreSearchServices mCoreSearchServices;
  private boolean mDebugBadConnection = false;
  private final DebugFeatures mDebugFeatures;
  private Sidekick.ResponsePayload mDebugResponse = null;
  private final ExecutedUserActionStore mExecutedUserActionStore;
  private final GooglePlayServicesHelper mGooglePlayServicesHelper;
  private final GsaPreferenceController mGsaPreferenceController;
  private final HttpHelper mHttpHelper;
  private boolean mLoadPushTestCards = false;
  private final LoginHelper mLoginHelper;
  private final SensorSignalsOracle mSensorSignalsOracle;
  private final SessionManager mSessionManager;
  private final UserClientIdManager mUserClientIdManager;
  
  public VelvetNetworkClient(Context paramContext, CoreSearchServices paramCoreSearchServices, DebugFeatures paramDebugFeatures, String paramString, HttpHelper paramHttpHelper, LoginHelper paramLoginHelper, SensorSignalsOracle paramSensorSignalsOracle, ConnectivityManager paramConnectivityManager, ExecutedUserActionStore paramExecutedUserActionStore, SessionManager paramSessionManager, GooglePlayServicesHelper paramGooglePlayServicesHelper, UserClientIdManager paramUserClientIdManager, GsaPreferenceController paramGsaPreferenceController)
  {
    this.mContext = paramContext;
    this.mCoreSearchServices = paramCoreSearchServices;
    this.mDebugFeatures = paramDebugFeatures;
    this.mAppVersionName = paramString;
    this.mHttpHelper = paramHttpHelper;
    this.mLoginHelper = paramLoginHelper;
    this.mExecutedUserActionStore = paramExecutedUserActionStore;
    this.mSensorSignalsOracle = paramSensorSignalsOracle;
    this.mConnectivityManager = paramConnectivityManager;
    this.mSessionManager = paramSessionManager;
    this.mGooglePlayServicesHelper = paramGooglePlayServicesHelper;
    this.mUserClientIdManager = paramUserClientIdManager;
    this.mGsaPreferenceController = paramGsaPreferenceController;
  }
  
  private Sidekick.RequestPayload addSensorSignalsToPayload(Sidekick.RequestPayload paramRequestPayload, boolean paramBoolean)
  {
    if (paramRequestPayload.hasSensorSignals()) {}
    for (Sidekick.SensorSignals localSensorSignals = paramRequestPayload.getSensorSignals();; localSensorSignals = null)
    {
      paramRequestPayload.setSensorSignals(this.mSensorSignalsOracle.buildCurrentSensorSignals(localSensorSignals, paramBoolean));
      return paramRequestPayload;
    }
  }
  
  private Sidekick.RequestPayload addStoredExecutedUserActionsToPayload(Sidekick.RequestPayload paramRequestPayload)
  {
    List localList = this.mExecutedUserActionStore.flush();
    if (!localList.isEmpty())
    {
      if (paramRequestPayload.hasActionsQuery()) {}
      for (Sidekick.ActionsQuery localActionsQuery = paramRequestPayload.getActionsQuery();; localActionsQuery = new Sidekick.ActionsQuery())
      {
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext()) {
          localActionsQuery.addExecutedUserAction((Sidekick.ExecutedUserAction)localIterator.next());
        }
      }
      paramRequestPayload.setActionsQuery(localActionsQuery);
    }
    return paramRequestPayload;
  }
  
  private Sidekick.RequestPayload addStoredLatenciesToPayload(Sidekick.RequestPayload paramRequestPayload)
  {
    SharedPreferencesExt localSharedPreferencesExt = this.mGsaPreferenceController.getMainPreferences();
    if ((localSharedPreferencesExt.contains("latency_event_id")) && ((int)(Math.random() * LATENCY_SAMPLING_RATE) % LATENCY_SAMPLING_RATE == 0))
    {
      ByteStringMicro localByteStringMicro = ByteStringMicro.copyFrom(localSharedPreferencesExt.getBytes("latency_event_id", null));
      paramRequestPayload.addPreviousClientLatency(new Sidekick.ClientLatency().setEncodedEventId(localByteStringMicro).setTotalNetworkLatencyMs(localSharedPreferencesExt.getInt("total_network_latency", 0)).setDeserializationLatencyMs(localSharedPreferencesExt.getInt("deserialization_latency", 0)));
    }
    return paramRequestPayload;
  }
  
  private void restoreExecutedUserActions(Sidekick.RequestPayload paramRequestPayload)
  {
    if ((paramRequestPayload.hasActionsQuery()) && (paramRequestPayload.getActionsQuery().getExecutedUserActionCount() > 0)) {
      this.mExecutedUserActionStore.saveExecutedUserActions(paramRequestPayload.getActionsQuery().getExecutedUserActionList());
    }
  }
  
  @Nullable
  private NetworkClient.ResponseAndEventId sendRequestHelper(Sidekick.RequestPayload paramRequestPayload, boolean paramBoolean, Account paramAccount)
  {
    
    if (this.mDebugBadConnection) {}
    try
    {
      Thread.sleep(2000L);
      label16:
      NetworkClient.ResponseAndEventId localResponseAndEventId = new NetworkClient.ResponseAndEventId(null, null);
      for (;;)
      {
        return localResponseAndEventId;
        if ((this.mDebugResponse != null) && (!paramRequestPayload.hasStaticMapQuery())) {
          return new NetworkClient.ResponseAndEventId(this.mDebugResponse, null);
        }
        if ((this.mLoadPushTestCards) && (paramRequestPayload.hasEntryQuery()) && (paramRequestPayload.getEntryQuery().getInterestCount() == 1))
        {
          Sidekick.Interest localInterest = paramRequestPayload.getEntryQuery().getInterest(0);
          if (localInterest.getEntryTypeRestrictCount() == 0) {
            localInterest.setTestData(true);
          }
        }
        if (!isNetworkAvailable())
        {
          Log.i("Velvet.VelvetNetworkClient", "Network connection not availble");
          return null;
        }
        if (paramAccount == null) {
          paramAccount = this.mLoginHelper.getAccount();
        }
        if (paramAccount == null)
        {
          Log.w("Velvet.VelvetNetworkClient", "Cannot connect to server without account");
          return null;
        }
        long l1 = System.currentTimeMillis();
        Sidekick.RequestPayload localRequestPayload = addStoredLatenciesToPayload(addForcedExperimentsToPayload(addStoredExecutedUserActionsToPayload(addSensorSignalsToPayload(paramRequestPayload, paramBoolean))));
        HttpHelper.PostRequest localPostRequest = new HttpHelper.PostRequest("https://android.googleapis.com/tg/fe/request");
        localPostRequest.setContent(buildRequest(localRequestPayload, l1).toByteArray());
        localPostRequest.setHeader("Content-Type", "application/octet-stream");
        int i = 0;
        int j = 0;
        label224:
        if (j < 2) {}
        try
        {
          String str1 = this.mLoginHelper.blockingGetTokenForAccount(paramAccount, "oauth2:https://www.googleapis.com/auth/googlenow", 5000L);
          if (str1 == null)
          {
            Log.e("Velvet.VelvetNetworkClient", "Failed to get auth token");
            localResponseAndEventId = new NetworkClient.ResponseAndEventId(AUTHENTICATION_FAILURE_RESPONSE, null);
            return localResponseAndEventId;
          }
          localPostRequest.setHeader("Authorization", "OAuth " + str1);
          long l2 = System.currentTimeMillis();
          try
          {
            byte[] arrayOfByte = this.mHttpHelper.rawPost(localPostRequest, 8);
            if (arrayOfByte == null)
            {
              this.mLoadPushTestCards = false;
              localResponseAndEventId = null;
              if (i != 0) {
                continue;
              }
              restoreExecutedUserActions(localRequestPayload);
              return null;
            }
            long l3 = System.currentTimeMillis() - l2;
            long l4 = System.currentTimeMillis();
            Sidekick.SidekickHttpResponse localSidekickHttpResponse = Sidekick.SidekickHttpResponse.parseFrom(arrayOfByte);
            long l5 = System.currentTimeMillis() - l4;
            if (localSidekickHttpResponse.getStatus() == 2)
            {
              String str2 = "";
              if (localSidekickHttpResponse.hasErrorCode()) {
                str2 = ": " + localSidekickHttpResponse.getErrorCode();
              }
              Log.e("Velvet.VelvetNetworkClient", "Received ERROR from server" + str2);
              this.mLoadPushTestCards = false;
              localResponseAndEventId = null;
              if (i != 0) {
                continue;
              }
              restoreExecutedUserActions(localRequestPayload);
              return null;
            }
            this.mCoreSearchServices.getBackgroundTasks().maybeStartTasks();
            storeLatencies(localSidekickHttpResponse.getEncodedEventId(), (int)l3, (int)l5);
            i = 1;
            localResponseAndEventId = new NetworkClient.ResponseAndEventId(localSidekickHttpResponse.getPayload(), localSidekickHttpResponse.getEncodedEventId());
            return localResponseAndEventId;
          }
          catch (HttpHelper.HttpException localHttpException)
          {
            if (localHttpException.getStatusCode() == 401)
            {
              Log.w("Velvet.VelvetNetworkClient", "Authorization exception: " + localHttpException);
              this.mLoginHelper.invalidateToken(str1);
              j++;
              break label224;
            }
            throw localHttpException;
          }
        }
        catch (IOException localIOException)
        {
          Log.w("Velvet.VelvetNetworkClient", "Network error: " + localIOException);
          this.mLoadPushTestCards = false;
          localResponseAndEventId = null;
          if (i != 0) {
            continue;
          }
          restoreExecutedUserActions(localRequestPayload);
          return null;
          Log.w("Velvet.VelvetNetworkClient", "Request retries failed: " + localPostRequest.getUrl());
          this.mLoadPushTestCards = false;
          localResponseAndEventId = null;
          if (i != 0) {
            continue;
          }
          restoreExecutedUserActions(localRequestPayload);
          return null;
        }
        finally
        {
          this.mLoadPushTestCards = false;
          if (i == 0) {
            restoreExecutedUserActions(localRequestPayload);
          }
        }
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      break label16;
    }
  }
  
  private void storeLatencies(ByteStringMicro paramByteStringMicro, int paramInt1, int paramInt2)
  {
    SharedPreferencesExt.Editor localEditor = this.mGsaPreferenceController.getMainPreferences().edit();
    localEditor.putBytes("latency_event_id", paramByteStringMicro.toByteArray());
    localEditor.putInt("total_network_latency", paramInt1 + paramInt2);
    localEditor.putInt("deserialization_latency", paramInt2);
    localEditor.apply();
  }
  
  Sidekick.RequestPayload addForcedExperimentsToPayload(Sidekick.RequestPayload paramRequestPayload)
  {
    Sidekick.ExperimentOverrides localExperimentOverrides = new Sidekick.ExperimentOverrides();
    Set localSet = this.mGsaPreferenceController.getStartupPreferences().getStringSet("now_opted_in_experiments", ImmutableSet.of());
    if (!localSet.isEmpty())
    {
      Iterator localIterator = localSet.iterator();
      while (localIterator.hasNext()) {
        localExperimentOverrides.addForceExperimentId(Integer.parseInt((String)localIterator.next()));
      }
      paramRequestPayload.setExperimentOverrides(localExperimentOverrides);
    }
    return paramRequestPayload;
  }
  
  Sidekick.SidekickHttpRequest buildRequest(Sidekick.RequestPayload paramRequestPayload, long paramLong)
  {
    SessionManager.SessionKey localSessionKey = this.mSessionManager.getSessionKey();
    Sidekick.ClientDescription localClientDescription = new Sidekick.ClientDescription().setSessionId(localSessionKey.key.toString()).setSessionIdExpirationSeconds(localSessionKey.expirationSeconds).setOsType(1).setOsVersion(Build.VERSION.RELEASE).setNetworkConnectionType(getNetworkConnectionType());
    Long localLong = this.mUserClientIdManager.getUserClientId();
    if (localLong != null) {
      localClientDescription.setUserClientId(localLong.longValue());
    }
    localClientDescription.setSidekickAppVersion(this.mAppVersionName);
    localClientDescription.setAndroidClientDescription(new Sidekick.AndroidClientDescription().setPlayServicesAvailability(this.mGooglePlayServicesHelper.getGooglePlayServicesAvailability()).setPlayServicesVersionCode(this.mGooglePlayServicesHelper.getGooglePlayServicesVersionCode()).setIsAndroidLauncher(IntentUtils.isGelDefaultLauncher(this.mContext)));
    return new Sidekick.SidekickHttpRequest().setClient(localClientDescription).setPayload(paramRequestPayload).setTimestampSeconds(paramLong / 1000L);
  }
  
  int getNetworkConnectionType()
  {
    NetworkInfo localNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
    if ((localNetworkInfo == null) || (!localNetworkInfo.isConnectedOrConnecting())) {
      return 0;
    }
    switch (localNetworkInfo.getType())
    {
    case 8: 
    default: 
      return 0;
    case 0: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
      switch (localNetworkInfo.getSubtype())
      {
      default: 
        return 0;
      case 1: 
        return 12;
      }
    case 7: 
      return 4;
    case 9: 
      return 3;
    case 1: 
      return 1;
    }
    return 2;
    return 5;
    return 6;
    return 7;
    return 9;
    return 8;
    return 10;
    return 11;
    return 13;
    return 14;
    return 15;
    return 16;
    return 17;
    return 18;
    return 19;
  }
  
  public boolean isNetworkAvailable()
  {
    NetworkInfo localNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
    if (localNetworkInfo == null) {
      return false;
    }
    return localNetworkInfo.isConnectedOrConnecting();
  }
  
  public Sidekick.ResponsePayload sendRequestWithLocation(Sidekick.RequestPayload paramRequestPayload)
  {
    NetworkClient.ResponseAndEventId localResponseAndEventId = sendRequestWithLocationCaptureEventId(paramRequestPayload);
    if (localResponseAndEventId != null) {
      return localResponseAndEventId.mPayload;
    }
    return null;
  }
  
  @Nullable
  public NetworkClient.ResponseAndEventId sendRequestWithLocationCaptureEventId(Sidekick.RequestPayload paramRequestPayload)
  {
    return sendRequestHelper(paramRequestPayload, true, null);
  }
  
  public Sidekick.ResponsePayload sendRequestWithoutLocation(Sidekick.RequestPayload paramRequestPayload)
  {
    NetworkClient.ResponseAndEventId localResponseAndEventId = sendRequestHelper(paramRequestPayload, false, null);
    Sidekick.ResponsePayload localResponsePayload = null;
    if (localResponseAndEventId != null) {
      localResponsePayload = localResponseAndEventId.mPayload;
    }
    return localResponsePayload;
  }
  
  public Sidekick.ResponsePayload sendRequestWithoutLocationWithAccount(Sidekick.RequestPayload paramRequestPayload, Account paramAccount)
  {
    NetworkClient.ResponseAndEventId localResponseAndEventId = sendRequestHelper(paramRequestPayload, false, paramAccount);
    if (localResponseAndEventId != null) {
      return localResponseAndEventId.mPayload;
    }
    return null;
  }
  
  public void setDebugBadConnection(boolean paramBoolean)
  {
    if (this.mDebugFeatures.teamDebugEnabled())
    {
      this.mDebugBadConnection = paramBoolean;
      Log.d("Velvet.VelvetNetworkClient", "Simulating a bad network connection");
      return;
    }
    Log.d("Velvet.VelvetNetworkClient", "Can't set debug state if debug features aren't enabled");
  }
  
  public void setDebugResponse(Sidekick.ResponsePayload paramResponsePayload)
  {
    if (this.mDebugFeatures.teamDebugEnabled())
    {
      this.mDebugResponse = paramResponsePayload;
      Log.d("Velvet.VelvetNetworkClient", "Setting debug network response");
      return;
    }
    Log.d("Velvet.VelvetNetworkClient", "Can't set debug response if debug features aren't enabled");
  }
  
  public void setLoadPushTestCards()
  {
    if (this.mDebugFeatures.teamDebugEnabled()) {
      this.mLoadPushTestCards = true;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.VelvetNetworkClient
 * JD-Core Version:    0.7.0.1
 */