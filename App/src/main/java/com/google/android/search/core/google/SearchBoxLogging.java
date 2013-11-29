package com.google.android.search.core.google;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.core.suggest.SuggestionsUi;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.search.core.util.HttpHelper.HttpException;
import com.google.android.search.core.util.HttpHelper.HttpRedirectException;
import com.google.android.search.core.util.UriRequest;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.SearchBoxStats;
import com.google.android.search.shared.api.SearchBoxStats.Builder;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.api.SuggestionLogInfo;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.wireless.voicesearch.proto.CardMetdataProtos.LoggingUrls;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SearchBoxLogging
{
  private static String sAppSessionId;
  private final Executor mBgExecutor;
  private String mClientId;
  private final Clock mClock;
  private final SearchConfig mConfig;
  private final HttpHelper mHttpHelper;
  private final LocationSettings mLocationSettings;
  private final LoginHelper mLoginHelper;
  private int mPsychicRequestCounter;
  private String mPsychicSessionId;
  private int mPsychicSessionRequested;
  private int mPsychicSessionStarted;
  private final SearchSettings mSearchSettings;
  private String mSource;
  private final Object mStatsLock = new Object();
  private SuggestUplStats mSuggestUplStats;
  
  public SearchBoxLogging(SearchConfig paramSearchConfig, HttpHelper paramHttpHelper, Executor paramExecutor, Clock paramClock, SearchSettings paramSearchSettings, LocationSettings paramLocationSettings, LoginHelper paramLoginHelper)
  {
    this.mConfig = paramSearchConfig;
    this.mHttpHelper = paramHttpHelper;
    this.mBgExecutor = paramExecutor;
    this.mClock = paramClock;
    this.mSearchSettings = paramSearchSettings;
    this.mLocationSettings = paramLocationSettings;
    this.mLoginHelper = paramLoginHelper;
    this.mSuggestUplStats = new SuggestUplStats();
  }
  
  public static SuggestionLogInfo createSuggestionsLogInfo(List<Suggestion> paramList)
  {
    StatsBuilder localStatsBuilder1 = new StatsBuilder("j");
    StatsBuilder localStatsBuilder2 = new StatsBuilder(",");
    int i = -1;
    int j = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Suggestion localSuggestion = (Suggestion)localIterator.next();
      int k;
      if ((localSuggestion.isWebSearchSuggestion()) || (localSuggestion.isNavSuggestion()))
      {
        k = -1;
        try
        {
          int m = Integer.parseInt(localSuggestion.getSuggestionLogType());
          k = m;
        }
        catch (NumberFormatException localNumberFormatException)
        {
          for (;;)
          {
            Log.w("Velvet.SearchBoxLogging", "NumberFormatException logging suggestions: " + localNumberFormatException);
          }
          if (j <= 0) {
            break label184;
          }
          StatsBuilder localStatsBuilder4 = new StatsBuilder("l");
          localStatsBuilder4.addStat(String.valueOf(i));
          if (j <= 1) {
            break label174;
          }
          localStatsBuilder4.addStat(String.valueOf(j));
          localStatsBuilder1.addStat(localStatsBuilder4.build());
          i = k;
          j = 1;
        }
        if (k == i) {
          j++;
        }
      }
      else
      {
        label174:
        label184:
        if (!localSuggestion.isCorrectionSuggestion()) {
          localStatsBuilder2.addStat(Integer.valueOf(getCanonicalSourceEnum(localSuggestion.getSourcePackageName(), localSuggestion.getSourceCanonicalName())));
        }
      }
    }
    if ((i != -1) && (j > 0))
    {
      StatsBuilder localStatsBuilder3 = new StatsBuilder("l");
      localStatsBuilder3.addStat(String.valueOf(i));
      if (j > 1) {
        localStatsBuilder3.addStat(String.valueOf(j));
      }
      localStatsBuilder1.addStat(localStatsBuilder3.build());
    }
    return new SuggestionLogInfo(localStatsBuilder1.build(), localStatsBuilder2.build());
  }
  
  private static String getAppSessionId()
  {
    try
    {
      ExtraPreconditions.checkNotMainThread();
      if (sAppSessionId == null)
      {
        SecureRandom localSecureRandom = new SecureRandom();
        byte[] arrayOfByte = new byte[8];
        localSecureRandom.nextBytes(arrayOfByte);
        sAppSessionId = Base64.encodeToString(arrayOfByte, 11);
      }
      String str = sAppSessionId;
      return str;
    }
    finally {}
  }
  
  private String getAssistedQueryStatsParam(@Nullable SearchBoxStats paramSearchBoxStats)
  {
    StatsBuilder localStatsBuilder = new StatsBuilder(".");
    localStatsBuilder.addStat(getDefaultClientName());
    String str1 = "";
    String str2 = "";
    if (paramSearchBoxStats != null)
    {
      if (paramSearchBoxStats.hasSuggestClick()) {
        str1 = String.valueOf(paramSearchBoxStats.getSuggestIndex());
      }
      str2 = paramSearchBoxStats.getLastSuggestionsStats().getSuggestionsEncoding();
    }
    String str3 = getExperimentStats(paramSearchBoxStats);
    localStatsBuilder.addStat(str1).addStat(str2).addStat(str3);
    return localStatsBuilder.build();
  }
  
  public static int getCanonicalSourceEnum(String paramString1, String paramString2)
  {
    if ("contacts".equals(paramString2)) {
      return 1;
    }
    if ("applications".equals(paramString2)) {
      return 2;
    }
    if ("com.android.chrome".equals(paramString1)) {
      return 3;
    }
    if ("com.android.browser".equals(paramString1)) {
      return 4;
    }
    if (!"navsuggestion".equals(paramString1)) {
      return 5;
    }
    return -1;
  }
  
  private String getDefaultClientName()
  {
    return this.mConfig.getCompleteServerClientId();
  }
  
  private String getExperimentStats(@Nullable SearchBoxStats paramSearchBoxStats)
  {
    EmptyZeroesStatsBuilder localEmptyZeroesStatsBuilder = new EmptyZeroesStatsBuilder("j");
    Object localObject1;
    Object localObject2;
    label31:
    Object localObject3;
    label46:
    Object localObject4;
    label61:
    Object localObject5;
    label76:
    Object localObject6;
    label91:
    Object localObject7;
    label106:
    Object localObject8;
    label121:
    Object localObject9;
    label136:
    Object localObject10;
    label151:
    String str1;
    label174:
    String str2;
    label198:
    String str3;
    label222:
    Object localObject11;
    label237:
    Object localObject12;
    label252:
    Object localObject13;
    label267:
    Object localObject14;
    label282:
    Object localObject15;
    label297:
    String str4;
    if (paramSearchBoxStats == null)
    {
      localObject1 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject1);
      if (paramSearchBoxStats != null) {
        break label373;
      }
      localObject2 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject2);
      if (paramSearchBoxStats != null) {
        break label385;
      }
      localObject3 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject3);
      if (paramSearchBoxStats != null) {
        break label397;
      }
      localObject4 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject4);
      if (paramSearchBoxStats != null) {
        break label409;
      }
      localObject5 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject5);
      if (paramSearchBoxStats != null) {
        break label421;
      }
      localObject6 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject6);
      if (paramSearchBoxStats != null) {
        break label438;
      }
      localObject7 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject7);
      if (paramSearchBoxStats != null) {
        break label450;
      }
      localObject8 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject8);
      if (paramSearchBoxStats != null) {
        break label462;
      }
      localObject9 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject9);
      if (paramSearchBoxStats != null) {
        break label474;
      }
      localObject10 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject10);
      if (!this.mSearchSettings.getSignedOut()) {
        break label500;
      }
      str1 = "1";
      localEmptyZeroesStatsBuilder.addStat(str1);
      if (!this.mLocationSettings.isSystemLocationEnabled()) {
        break label508;
      }
      str2 = "0";
      localEmptyZeroesStatsBuilder.addStat(str2);
      if (!this.mLocationSettings.isGoogleLocationEnabled()) {
        break label515;
      }
      str3 = "0";
      localEmptyZeroesStatsBuilder.addStat(str3);
      if (paramSearchBoxStats != null) {
        break label522;
      }
      localObject11 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject11);
      if (paramSearchBoxStats != null) {
        break label534;
      }
      localObject12 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject12);
      if (paramSearchBoxStats != null) {
        break label546;
      }
      localObject13 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject13);
      if (paramSearchBoxStats != null) {
        break label558;
      }
      localObject14 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject14);
      if (paramSearchBoxStats != null) {
        break label570;
      }
      localObject15 = "";
      localEmptyZeroesStatsBuilder.addStat(localObject15);
      if (paramSearchBoxStats != null) {
        break label582;
      }
      str4 = "";
      label312:
      localEmptyZeroesStatsBuilder.addStat(str4);
      localEmptyZeroesStatsBuilder.addStat(Integer.valueOf(this.mLocationSettings.getKlpLocationMode()));
      if (!this.mLoginHelper.isUserOptedIntoGoogleNow()) {
        break label604;
      }
    }
    label385:
    label397:
    label409:
    label421:
    label438:
    label450:
    label462:
    label474:
    label604:
    for (String str5 = "1";; str5 = "0")
    {
      localEmptyZeroesStatsBuilder.addStat(str5);
      return localEmptyZeroesStatsBuilder.build();
      localObject1 = Long.valueOf(paramSearchBoxStats.getCommitMs());
      break;
      label373:
      localObject2 = Long.valueOf(paramSearchBoxStats.getFirstEditMs());
      break label31;
      localObject3 = Long.valueOf(paramSearchBoxStats.getLastEditMs());
      break label46;
      localObject4 = Integer.valueOf(paramSearchBoxStats.getSuggestRequestCount());
      break label61;
      localObject5 = Integer.valueOf(paramSearchBoxStats.getSuggestCacheHitCount());
      break label76;
      localObject6 = Integer.valueOf(paramSearchBoxStats.getSnappySuggestCount() - paramSearchBoxStats.getSuggestCacheHitCount());
      break label91;
      localObject7 = Integer.valueOf(paramSearchBoxStats.getSuggestServerResponseCount());
      break label106;
      localObject8 = Long.valueOf(paramSearchBoxStats.getServiceSuggestLatency());
      break label121;
      localObject9 = Integer.valueOf(paramSearchBoxStats.getNumZeroPrefixSuggestionsShown());
      break label136;
      if (paramSearchBoxStats.hasSuggestRefinement())
      {
        localObject10 = Long.valueOf(paramSearchBoxStats.getLastSuggestionInteractionMs());
        break label151;
      }
      localObject10 = "";
      break label151;
      label500:
      str1 = "0";
      break label174;
      label508:
      str2 = "1";
      break label198;
      str3 = "1";
      break label222;
      localObject11 = Long.valueOf(paramSearchBoxStats.getSearchBoxReadyMs());
      break label237;
      localObject12 = Long.valueOf(paramSearchBoxStats.getSearchServiceConnectedMs());
      break label252;
      localObject13 = Long.valueOf(paramSearchBoxStats.getZeroPrefixSuggestionsShownMs());
      break label267;
      localObject14 = Integer.valueOf(paramSearchBoxStats.getUserVisibleSuggestRequests());
      break label282;
      localObject15 = Long.valueOf(paramSearchBoxStats.getUserVisibleSuggestLatency());
      break label297;
      if (paramSearchBoxStats.getIsFromPredictive())
      {
        str4 = "1";
        break label312;
      }
      str4 = "0";
      break label312;
    }
  }
  
  private void logClickInternal(Uri paramUri, Map<String, String> paramMap)
  {
    try
    {
      HttpHelper.GetRequest localGetRequest = new HttpHelper.GetRequest(paramUri.toString(), paramMap);
      localGetRequest.setFollowRedirects(false);
      this.mHttpHelper.get(localGetRequest, 5);
      return;
    }
    catch (HttpHelper.HttpRedirectException localHttpRedirectException)
    {
      Log.w("Velvet.SearchBoxLogging", "Got redirect from click log request: " + localHttpRedirectException.getRedirectLocation());
      return;
    }
    catch (IOException localIOException) {}catch (HttpHelper.HttpException localHttpException) {}
  }
  
  private void logResultClickInternal(UriRequest paramUriRequest)
  {
    Uri localUri = paramUriRequest.getUri();
    String str1 = localUri.getQueryParameter("sa");
    if (str1 == null) {
      localUri = localUri.buildUpon().appendQueryParameter("sa", "T").build();
    }
    for (;;)
    {
      logClickInternal(localUri, paramUriRequest.getHeaders());
      return;
      if (!str1.equals("T"))
      {
        Uri.Builder localBuilder = localUri.buildUpon().clearQuery();
        Iterator localIterator1 = localUri.getQueryParameterNames().iterator();
        while (localIterator1.hasNext())
        {
          String str2 = (String)localIterator1.next();
          if (!str2.equals("sa"))
          {
            Iterator localIterator2 = localUri.getQueryParameters(str2).iterator();
            while (localIterator2.hasNext()) {
              localBuilder.appendQueryParameter(str2, (String)localIterator2.next());
            }
          }
        }
        localBuilder.appendQueryParameter("sa", "T");
        localUri = localBuilder.build();
      }
    }
  }
  
  public SuggestionsUi captureShownWebSuggestions(final SuggestionsUi paramSuggestionsUi)
  {
    new SuggestionsUi()
    {
      public void showSuggestions(SuggestionList paramAnonymousSuggestionList, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        Object localObject1 = SearchBoxLogging.this.mStatsLock;
        if (paramAnonymousBoolean) {}
        try
        {
          SearchBoxLogging.this.mSuggestUplStats.registerSuggestResponse(SearchBoxLogging.this.mClock.elapsedRealtime(), paramAnonymousSuggestionList.isFromCache(), paramAnonymousSuggestionList.getUserQuery());
          paramSuggestionsUi.showSuggestions(paramAnonymousSuggestionList, paramAnonymousInt, paramAnonymousBoolean);
          return;
        }
        finally {}
      }
    };
  }
  
  @Nullable
  public String getClientId()
  {
    return this.mClientId;
  }
  
  public String getPsychicSessionId()
  {
    synchronized (this.mStatsLock)
    {
      if ((this.mPsychicSessionId == null) || (this.mPsychicSessionRequested != this.mPsychicSessionStarted))
      {
        this.mPsychicSessionRequested = this.mPsychicSessionStarted;
        long l = System.currentTimeMillis();
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(getAppSessionId()).append('.').append(l).append('.');
        localStringBuilder.append(this.mPsychicSessionRequested);
        this.mPsychicSessionId = localStringBuilder.toString();
      }
      String str = this.mPsychicSessionId;
      return str;
    }
  }
  
  @Nullable
  public String getSource()
  {
    return this.mSource;
  }
  
  void logCardAboveSrp(final String paramString1, final String paramString2, final SearchUrlHelper paramSearchUrlHelper, final long paramLong)
  {
    this.mBgExecutor.execute(new Runnable()
    {
      public void run()
      {
        UriRequest localUriRequest = paramSearchUrlHelper.getCardAboveSrpLogUri(paramString1).setSpeechCookie().setSpeechRequestId(paramString2).setAutoExecMs(paramLong).build();
        HttpHelper.GetRequest localGetRequest = new HttpHelper.GetRequest(localUriRequest.getUri().toString(), localUriRequest.getHeaders());
        try
        {
          SearchBoxLogging.this.mHttpHelper.get(localGetRequest, 5);
          return;
        }
        catch (IOException localIOException)
        {
          Log.w("Velvet.SearchBoxLogging", "Could not log card above SRP" + paramString1, localIOException);
        }
      }
    });
  }
  
  void logEventToGws(final String paramString1, @Nullable final String paramString2, final SearchUrlHelper paramSearchUrlHelper)
  {
    if (TextUtils.isEmpty(paramString1))
    {
      Log.e("Velvet.SearchBoxLogging", "Cannot log event to GWS because URL is null/empty");
      return;
    }
    this.mBgExecutor.execute(new Runnable()
    {
      public void run()
      {
        HttpHelper.GetRequest localGetRequest = SearchBoxLogging.this.makeGwsLoggingRequestFromRelativeUrl(paramString1, paramString2, paramSearchUrlHelper);
        try
        {
          SearchBoxLogging.this.mHttpHelper.get(localGetRequest, 5);
          return;
        }
        catch (IOException localIOException)
        {
          Log.w("Velvet.SearchBoxLogging", "Could not do GWS gen_204", localIOException);
        }
      }
    });
  }
  
  public void logEventsToGws(int paramInt, @Nullable CardMetdataProtos.LoggingUrls paramLoggingUrls, @Nullable String paramString1, @Nullable String paramString2, @Nonnull SearchUrlHelper paramSearchUrlHelper, long paramLong)
  {
    if (paramSearchUrlHelper == null) {
      Log.e("Velvet.SearchBoxLogging", "Cannot log, urlHelper==null");
    }
    label213:
    label223:
    for (;;)
    {
      return;
      if ((0x10000 & paramInt) != 0)
      {
        if (paramString1 == null) {
          break label213;
        }
        logCardAboveSrp(paramString1, paramString2, paramSearchUrlHelper, paramLong);
        paramInt &= 0xFFFEFFFF;
      }
      for (;;)
      {
        if (paramInt == 0) {
          break label223;
        }
        if (paramLoggingUrls == null) {
          break label225;
        }
        if ((paramInt & 0x100) != 0) {
          logEventToGws(paramLoggingUrls.getAcceptUrl(), paramString2, paramSearchUrlHelper);
        }
        if ((paramInt & 0x200) != 0) {
          logEventToGws(paramLoggingUrls.getAcceptFromTimerUrl(), paramString2, paramSearchUrlHelper);
        }
        if ((paramInt & 0x400) != 0) {
          logEventToGws(paramLoggingUrls.getBailOutUrl(), paramString2, paramSearchUrlHelper);
        }
        if ((paramInt & 0x800) != 0) {
          logEventToGws(paramLoggingUrls.getRejectBySwipingCardUrl(), paramString2, paramSearchUrlHelper);
        }
        if ((paramInt & 0x1000) != 0) {
          logEventToGws(paramLoggingUrls.getRejectTimerUrl(), paramString2, paramSearchUrlHelper);
        }
        if ((paramInt & 0x2000) != 0) {
          logEventToGws(paramLoggingUrls.getRejectByHittingBackUrl(), paramString2, paramSearchUrlHelper);
        }
        if ((paramInt & 0x4000) != 0) {
          logEventToGws(paramLoggingUrls.getRejectByScrollingDownUrl(), paramString2, paramSearchUrlHelper);
        }
        if ((0x8000 & paramInt) == 0) {
          break;
        }
        logEventToGws(paramLoggingUrls.getShowCardUrl(), paramString2, paramSearchUrlHelper);
        return;
        Log.e("Velvet.SearchBoxLogging", "Cannot log card above SRP because event id is null");
      }
    }
    label225:
    Log.w("Velvet.SearchBoxLogging", "Cannot log to GWS: all URLs missing.");
  }
  
  public void logResultClick(final Supplier<UriRequest> paramSupplier)
  {
    this.mBgExecutor.execute(new Runnable()
    {
      public void run()
      {
        SearchBoxLogging.this.logResultClickInternal((UriRequest)paramSupplier.get());
      }
    });
  }
  
  public void logSnappyRequest(Query paramQuery)
  {
    synchronized (this.mStatsLock)
    {
      this.mSuggestUplStats.registerSnappyRequest(paramQuery);
      return;
    }
  }
  
  public void logSuggestRequest()
  {
    synchronized (this.mStatsLock)
    {
      this.mSuggestUplStats.registerSuggestRequest(this.mClock.elapsedRealtime());
      return;
    }
  }
  
  public void logSuggestSessionStart()
  {
    synchronized (this.mStatsLock)
    {
      this.mSuggestUplStats = new SuggestUplStats();
      this.mPsychicSessionStarted = (1 + this.mPsychicSessionStarted);
      this.mPsychicRequestCounter = 1;
      return;
    }
  }
  
  public void logUrlQueryWithGen_204(final SearchUrlHelper paramSearchUrlHelper, final String paramString1, final String paramString2)
  {
    this.mBgExecutor.execute(new Runnable()
    {
      public void run()
      {
        UriRequest localUriRequest = paramSearchUrlHelper.getUrlQueryLogUri(paramString1, paramString2).setSpeechCookie().build();
        HttpHelper.GetRequest localGetRequest = new HttpHelper.GetRequest(localUriRequest.getUri().toString(), localUriRequest.getHeaders());
        try
        {
          SearchBoxLogging.this.mHttpHelper.get(localGetRequest, 5);
          return;
        }
        catch (IOException localIOException)
        {
          Log.w("Velvet.SearchBoxLogging", "Could not do gen_204 for launching url", localIOException);
        }
      }
    });
  }
  
  HttpHelper.GetRequest makeGwsLoggingRequestFromRelativeUrl(String paramString1, @Nullable String paramString2, SearchUrlHelper paramSearchUrlHelper)
  {
    UriRequest localUriRequest = paramSearchUrlHelper.getSearchBaseUri(false, true).setSpeechCookie().setSpeechRequestId(paramString2).build();
    return new HttpHelper.GetRequest(SearchUrlHelper.makeAbsoluteUri(localUriRequest.getUri(), Uri.parse(paramString1), null, null).toString(), localUriRequest.getHeaders());
  }
  
  public void sendGen204(final Query paramQuery, final String paramString, final SearchUrlHelper paramSearchUrlHelper)
  {
    this.mBgExecutor.execute(new Runnable()
    {
      public void run()
      {
        UriRequest localUriRequest = paramSearchUrlHelper.getPrefetchGen204Uri(paramQuery, paramString).build();
        HttpHelper.GetRequest localGetRequest = new HttpHelper.GetRequest(localUriRequest.getUri().toString(), localUriRequest.getHeaders());
        try
        {
          SearchBoxLogging.this.mHttpHelper.get(localGetRequest, 5);
          return;
        }
        catch (IOException localIOException)
        {
          Log.w("Velvet.SearchBoxLogging", "Could not log prefetch " + paramString, localIOException);
        }
      }
    });
  }
  
  public void setFallbackClientIdAndSource(String paramString1, String paramString2)
  {
    this.mClientId = paramString1;
    this.mSource = paramString2;
  }
  
  public void setLoggingParams(Query paramQuery, SearchUrlHelper.Builder paramBuilder)
  {
    SearchBoxStats localSearchBoxStats = paramQuery.getSearchBoxStats();
    if (paramQuery.isTextSearch())
    {
      if (localSearchBoxStats != null) {
        paramBuilder.setOriginalQueryString(localSearchBoxStats.getOriginalQuery());
      }
      paramBuilder.setAssistedQueryStats(getAssistedQueryStatsParam(localSearchBoxStats));
    }
    paramBuilder.setSource(this.mSource);
    paramBuilder.setEntryPoint(this.mClientId);
  }
  
  public Query snapshotServiceSideQueryStats(Query paramQuery)
  {
    SearchBoxStats localSearchBoxStats1 = paramQuery.getSearchBoxStats();
    if (localSearchBoxStats1 != null)
    {
      SearchBoxStats localSearchBoxStats2 = localSearchBoxStats1.buildUpon().setServiceSuggestStats(this.mSuggestUplStats.getTotalRequestCount(), this.mSuggestUplStats.getSnappyRequestCount(), this.mSuggestUplStats.getCacheHitCount(), this.mSuggestUplStats.getServerResponseCount(), this.mSuggestUplStats.getTotalSuggestLatency()).build();
      paramQuery = paramQuery.withSearchBoxStats(localSearchBoxStats2);
      this.mClientId = localSearchBoxStats2.getClientId();
      this.mSource = localSearchBoxStats2.getSource();
    }
    return paramQuery;
  }
  
  public int takePsychicRequestCounter()
  {
    synchronized (this.mStatsLock)
    {
      int i = this.mPsychicRequestCounter;
      this.mPsychicRequestCounter = (i + 1);
      return i;
    }
  }
  
  protected static class EmptyZeroesStatsBuilder
    extends SearchBoxLogging.StatsBuilder
  {
    EmptyZeroesStatsBuilder(String paramString)
    {
      super();
    }
    
    protected boolean isEmpty(String paramString)
    {
      return (super.isEmpty(paramString)) || (paramString.equals("0"));
    }
  }
  
  protected static class StatsBuilder
  {
    private final String mDenominator;
    private int mLastNonEmptyEntryAt;
    private final List<String> mStatsList;
    
    StatsBuilder(String paramString)
    {
      this.mDenominator = paramString;
      this.mStatsList = Lists.newArrayList();
      this.mLastNonEmptyEntryAt = 0;
    }
    
    StatsBuilder addStat(Object paramObject)
    {
      if (!isEmpty(paramObject.toString()))
      {
        this.mStatsList.add(paramObject.toString());
        this.mLastNonEmptyEntryAt = this.mStatsList.size();
        return this;
      }
      this.mStatsList.add("");
      return this;
    }
    
    String build()
    {
      if (this.mLastNonEmptyEntryAt == 0) {
        return "";
      }
      StringBuffer localStringBuffer = new StringBuffer((String)this.mStatsList.get(0));
      for (int i = 1; i < this.mLastNonEmptyEntryAt; i++) {
        localStringBuffer.append(this.mDenominator).append((String)this.mStatsList.get(i));
      }
      return localStringBuffer.toString();
    }
    
    protected boolean isEmpty(String paramString)
    {
      return TextUtils.isEmpty(paramString);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.SearchBoxLogging
 * JD-Core Version:    0.7.0.1
 */