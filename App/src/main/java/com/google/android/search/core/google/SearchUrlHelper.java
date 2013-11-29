package com.google.android.search.core.google;

import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.Feature;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.discoursecontext.DiscourseContextProtoHelper;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.google.gaia.LoginHelper.AuthToken;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.search.core.util.UriDiff;
import com.google.android.search.core.util.UriRequest;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ProtoUtils;
import com.google.android.shared.util.Util;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.velvet.Cookies;
import com.google.android.velvet.Corpora;
import com.google.android.velvet.Corpus;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.velvet.WebCorpus;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.knowledge.context.ClientDiscourseContextProto.ClientDiscourseContext;
import com.google.webserver.shared.gws.experiments.ClientDataHeaderProto.ClientDataHeader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SearchUrlHelper
{
  private static final ImmutableSet<String> AUTH_QUERY_PARAMETERS = ImmutableSet.of("auth", "uberauth");
  private static final ImmutableSet<String> NON_REDACTED_HEADERS = ImmutableSet.of("X-Additional-Discourse-Context", "X-Geo", "Geo-Position");
  static final String PARAM_INPUT_METHOD = "inm";
  static final String PARAM_SPOKEN_LANGUAGE = "spknlang";
  static final String PARAM_VALUE_VOICE_SEARCH = "vs";
  public static String SAFE_SEARCH_TRIMODAL_IMAGES = "images";
  public static String SAFE_SEARCH_TRIMODAL_OFF = "off";
  public static String SAFE_SEARCH_TRIMODAL_STRICT = "active";
  public static String TTS_MODE_DEFAULT = "default";
  public static String TTS_MODE_EYES_FREE = "eyesfree";
  private volatile Point mBrowserDimensions;
  private final Clock mClock;
  private final SearchConfig mConfig;
  private final Cookies mCookies;
  private final Corpora mCorpora;
  private final Supplier<Locale> mDefaultLocale;
  private final DiscourseContextProtoHelper mDiscourseContextHelper;
  private final GsaConfigFlags mGsaConfig;
  private final LoginHelper mLoginHelper;
  private final PartnerInfo mPartner;
  private final PredictiveCardsPreferences mPreferences;
  private final RlzHelper mRlzHelper;
  private final SearchBoxLogging mSearchBoxLogging;
  private final SearchSettings mSearchSettings;
  private final SimpleDateFormat mSimpleDateFormat;
  private final VelvetServices mVelvetServices;
  private final String mVersionName;
  private final Settings mVoiceSettings;
  
  public SearchUrlHelper(SearchSettings paramSearchSettings, SearchConfig paramSearchConfig, GsaConfigFlags paramGsaConfigFlags, PredictiveCardsPreferences paramPredictiveCardsPreferences, Clock paramClock, Corpora paramCorpora, VelvetServices paramVelvetServices, SearchBoxLogging paramSearchBoxLogging, RlzHelper paramRlzHelper, Supplier<Locale> paramSupplier, PartnerInfo paramPartnerInfo, Cookies paramCookies, Settings paramSettings, DiscourseContextProtoHelper paramDiscourseContextProtoHelper, LoginHelper paramLoginHelper, String paramString)
  {
    this.mSearchSettings = paramSearchSettings;
    this.mConfig = ((SearchConfig)Preconditions.checkNotNull(paramSearchConfig));
    this.mGsaConfig = ((GsaConfigFlags)Preconditions.checkNotNull(paramGsaConfigFlags));
    this.mPreferences = ((PredictiveCardsPreferences)Preconditions.checkNotNull(paramPredictiveCardsPreferences));
    this.mVelvetServices = paramVelvetServices;
    this.mClock = ((Clock)Preconditions.checkNotNull(paramClock));
    this.mCorpora = ((Corpora)Preconditions.checkNotNull(paramCorpora));
    this.mRlzHelper = ((RlzHelper)Preconditions.checkNotNull(paramRlzHelper));
    this.mSearchBoxLogging = ((SearchBoxLogging)Preconditions.checkNotNull(paramSearchBoxLogging));
    this.mDefaultLocale = paramSupplier;
    this.mPartner = paramPartnerInfo;
    this.mCookies = paramCookies;
    this.mVoiceSettings = paramSettings;
    this.mSimpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss' GMT'", Locale.US);
    this.mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    this.mDiscourseContextHelper = paramDiscourseContextProtoHelper;
    this.mLoginHelper = paramLoginHelper;
    this.mVersionName = paramString;
  }
  
  private void addExperimentIdsAndHeader(Builder paramBuilder)
  {
    if ((!TextUtils.isEmpty(this.mConfig.getGservicesExperimentIds())) && (!TextUtils.isEmpty(this.mConfig.getClientExperimentsParam()))) {
      paramBuilder.putParam(this.mConfig.getClientExperimentsParam(), this.mConfig.getGservicesExperimentIds());
    }
    fillClientExperimentsHeader(paramBuilder);
  }
  
  private static String appendWwwPrefix(String paramString)
  {
    if (!TextUtils.isEmpty(paramString)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      if (paramString.startsWith(".")) {
        paramString = "www" + paramString;
      }
      return paramString;
    }
  }
  
  private boolean canSendAuthHeader(Uri paramUri)
  {
    return ("https".equals(paramUri.getScheme())) && (isSearchAuthority(paramUri));
  }
  
  public static String convertObsoleteGeolocationCodeToNew(String paramString)
  {
    String str;
    if (paramString == null) {
      str = null;
    }
    do
    {
      return str;
      str = paramString.toLowerCase(Locale.US);
    } while (!"gb".equals(str));
    return "uk";
  }
  
  public static String convertObsoleteLanguageCodeToNew(String paramString)
  {
    if (paramString == null) {
      paramString = null;
    }
    do
    {
      return paramString;
      if ("iw".equals(paramString)) {
        return "he";
      }
      if ("in".equals(paramString)) {
        return "id";
      }
    } while (!"ji".equals(paramString));
    return "yi";
  }
  
  private static boolean endsWithIfPresent(String paramString1, @Nullable String paramString2)
  {
    if (TextUtils.isEmpty(paramString2)) {
      return false;
    }
    return paramString1.endsWith(paramString2);
  }
  
  private void fillClientExperimentsHeader(Builder paramBuilder)
  {
    if ("X-Client-Data".equals(this.mConfig.getClientExperimentsHeader()))
    {
      localClientDataHeader = new ClientDataHeaderProto.ClientDataHeader();
      if (!TextUtils.isEmpty(this.mConfig.getGservicesExperimentIds()))
      {
        arrayOfString = this.mConfig.getGservicesExperimentIds().split(",");
        k = 0;
        for (;;)
        {
          if (k < arrayOfString.length) {
            try
            {
              localClientDataHeader.addExperimentId(Integer.parseInt(arrayOfString[k]));
              k++;
            }
            catch (NumberFormatException localNumberFormatException)
            {
              for (;;)
              {
                Log.w("Search.SearchUrlHelper", "Invalid integer value \"" + arrayOfString[k] + "\" in experiments IDs.");
              }
            }
          }
        }
      }
      arrayOfInteger = this.mVelvetServices.getGsaConfigFlags().getExperimentIds();
      i = arrayOfInteger.length;
      for (j = 0; j < i; j++) {
        localClientDataHeader.addExperimentId(arrayOfInteger[j].intValue());
      }
      l = this.mVelvetServices.getGsaConfigFlags().getTimestampUsec();
      if (l != -1L)
      {
        localClientDataHeader.setConfigTimeUsec(l);
        paramBuilder.putHeader(this.mConfig.getClientExperimentsHeader(), ProtoUtils.messageToUrlSafeBase64(localClientDataHeader));
      }
    }
    while ((TextUtils.isEmpty(this.mConfig.getClientExperimentsHeader())) || (TextUtils.isEmpty(this.mConfig.getGservicesExperimentIds()))) {
      for (;;)
      {
        ClientDataHeaderProto.ClientDataHeader localClientDataHeader;
        String[] arrayOfString;
        int k;
        Integer[] arrayOfInteger;
        int i;
        int j;
        long l;
        return;
        Log.i("Search.SearchUrlHelper", "No config timestamp found.");
      }
    }
    paramBuilder.putHeader(this.mConfig.getClientExperimentsHeader(), this.mConfig.getGservicesExperimentIds());
  }
  
  public static Map<String, String> getAllQueryParameters(Uri paramUri)
  {
    LinkedHashMap localLinkedHashMap = Maps.newLinkedHashMap();
    Iterator localIterator1 = paramUri.getQueryParameterNames().iterator();
    while (localIterator1.hasNext())
    {
      String str4 = (String)localIterator1.next();
      if (!TextUtils.isEmpty(str4))
      {
        String str5 = paramUri.getQueryParameter(str4);
        if (str5 != null) {
          localLinkedHashMap.put(str4, str5);
        } else {
          Log.w("Search.SearchUrlHelper", "Error parsing URL: " + paramUri);
        }
      }
    }
    String str1 = paramUri.getFragment();
    if (str1 != null)
    {
      Uri localUri = paramUri.buildUpon().encodedQuery(str1).fragment(null).build();
      Iterator localIterator2 = localUri.getQueryParameterNames().iterator();
      while (localIterator2.hasNext())
      {
        String str2 = (String)localIterator2.next();
        String str3 = localUri.getQueryParameter(str2);
        if (str3 != null) {
          localLinkedHashMap.put(str2, str3);
        } else {
          Log.w("Search.SearchUrlHelper", "Error parsing URL [fragment]: " + paramUri);
        }
      }
    }
    return localLinkedHashMap;
  }
  
  private String getClientParam()
  {
    String str = this.mPartner.getSearchClientId();
    if (str != null) {
      return str;
    }
    return "ms-" + this.mPartner.getClientId();
  }
  
  private static String getHlParameter(Locale paramLocale, String paramString)
  {
    String str = stringFromLocale(paramLocale);
    if (!TextUtils.isEmpty(paramString))
    {
      Locale localLocale = localeFromString(paramString);
      if ((localLocale != null) && (localLocale.getLanguage().equals(paramLocale.getLanguage())) && (!Arrays.asList(Locale.getAvailableLocales()).contains(localLocale))) {
        return paramString;
      }
    }
    return str;
  }
  
  private String getHlParameterForSearch()
  {
    return getHlParameter((Locale)this.mDefaultLocale.get(), this.mSearchSettings.getSearchDomainLanguage());
  }
  
  private int getIntQueryParameter(Uri paramUri, String paramString, int paramInt)
  {
    String str = getQueryParameter(paramUri, paramString);
    if (!TextUtils.isEmpty(str)) {}
    try
    {
      int i = Integer.parseInt(str);
      paramInt = i;
      return paramInt;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      Log.w("Search.SearchUrlHelper", "Invalid integer value \"" + paramString + "\" in search URL " + str);
    }
    return paramInt;
  }
  
  private String getPathQueryAndFragment(Uri paramUri)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (!TextUtils.isEmpty(paramUri.getPath())) {
      localStringBuilder.append(paramUri.getPath());
    }
    if (!TextUtils.isEmpty(paramUri.getQuery()))
    {
      localStringBuilder.append("?");
      localStringBuilder.append(paramUri.getEncodedQuery());
    }
    if (!TextUtils.isEmpty(paramUri.getFragment()))
    {
      localStringBuilder.append("#");
      localStringBuilder.append(paramUri.getEncodedFragment());
    }
    return localStringBuilder.toString();
  }
  
  private String getPersonalizedSearchValue()
  {
    if (this.mConfig.isPersonalizedSearchEnabled())
    {
      if (this.mSearchSettings.getPersonalizedSearchEnabled()) {
        return "1";
      }
      return "0";
    }
    return null;
  }
  
  private String getQueryParameter(Uri paramUri, String paramString)
  {
    String str1 = paramUri.getFragment();
    String str2 = paramUri.getQueryParameter(paramString);
    if (str1 == null) {}
    String str3;
    do
    {
      return str2;
      str3 = paramUri.buildUpon().encodedQuery(str1).fragment(null).build().getQueryParameter(paramString);
    } while (str3 == null);
    return str3;
  }
  
  @Nullable
  public static Uri getResultTargetUri(@Nullable String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (paramString1 == null) {}
    do
    {
      return null;
      if (paramString1.startsWith(paramString2)) {
        return Uri.parse(paramString3 + "://" + paramString4 + paramString1);
      }
    } while (!paramString1.startsWith("http"));
    return Uri.parse(paramString1);
  }
  
  private String getSearchDomain(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (!paramBoolean1)
    {
      String str2 = getSearchDomainDebugOrDogfoodOverride();
      if (!TextUtils.isEmpty(str2)) {
        return str2;
      }
      String str3 = this.mConfig.getFixedSearchDomain();
      if (!TextUtils.isEmpty(str3)) {
        return appendWwwPrefix(str3);
      }
    }
    if ((!paramBoolean2) && (shouldUseGoogleCom())) {
      return getDefaultSearchDomain();
    }
    String str1 = this.mSearchSettings.getSearchDomainPreference();
    if (TextUtils.isEmpty(str1)) {
      return getDefaultSearchDomain();
    }
    return appendWwwPrefix(str1);
  }
  
  @Nullable
  private String getSearchDomainDebugOrDogfoodOverride()
  {
    String str1 = this.mSearchSettings.getDebugSearchDomainOverride();
    if (!TextUtils.isEmpty(str1))
    {
      Log.i("Search.SearchUrlHelper", "Using manual override for search domain: " + str1);
      return appendWwwPrefix(str1);
    }
    String str2 = this.mConfig.getDogfoodDomainOverride();
    if (!TextUtils.isEmpty(str2))
    {
      Log.i("Search.SearchUrlHelper", "Using overriden search domain: " + str2);
      return appendWwwPrefix(str2);
    }
    return null;
  }
  
  private Builder getSearchUrlBuilder(Query paramQuery, @Nullable String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    return getSearchUrlBuilder(getWebCorpusForQuery(paramQuery).getWebSearchPattern(), paramQuery, paramString, paramBoolean1, shouldRequestPelletResponse(paramQuery), paramBoolean2);
  }
  
  private Builder getSearchUrlBuilder(String paramString1, Query paramQuery, @Nullable String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    String str = null;
    if (paramQuery != null)
    {
      Preconditions.checkArgument(paramQuery.isValidSearch());
      str = paramQuery.getQueryStringForSearch();
    }
    Builder localBuilder = getUrlBuilder(paramString1, paramQuery, str, getClientParam(), paramString2, paramBoolean1, paramBoolean2, paramBoolean3, true);
    if (Feature.DISCOURSE_CONTEXT.isEnabled()) {
      localBuilder.setIncludeDiscourseContext();
    }
    return localBuilder;
  }
  
  private Builder getUrlBuilder(String paramString1, Query paramQuery, String paramString2, String paramString3, @Nullable String paramString4, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    Builder localBuilder = formatUrlBase(paramString1, getSearchDomain());
    if (paramQuery != null) {
      buildUpFromQuery(localBuilder, paramString2, paramQuery, paramString3, paramString4, paramBoolean2, paramBoolean3, paramBoolean4);
    }
    setSearchParams(localBuilder, paramBoolean1, paramQuery);
    return localBuilder;
  }
  
  @Nullable
  private String getUrlStringFromRedirect(Uri paramUri, boolean paramBoolean)
  {
    String str1 = this.mConfig.getClickedResultUrlPath();
    if ((paramUri != null) && (isSearchAuthority(paramUri, false, paramBoolean)) && (TextUtils.equals(paramUri.getPath(), str1)))
    {
      String[] arrayOfString = this.mConfig.getClickedResultDestinationParams();
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++)
      {
        String str2 = paramUri.getQueryParameter(arrayOfString[j]);
        if (!TextUtils.isEmpty(str2)) {
          return str2;
        }
      }
    }
    return null;
  }
  
  private WebCorpus getWebCorpusForQuery(Query paramQuery)
  {
    Preconditions.checkState(this.mCorpora.areWebCorporaLoaded());
    WebCorpus localWebCorpus = (WebCorpus)this.mCorpora.getCorpus(paramQuery.getCorpusId());
    if (localWebCorpus == null) {
      localWebCorpus = this.mCorpora.getWebCorpus();
    }
    return localWebCorpus;
  }
  
  private boolean isSearchAuthority(Uri paramUri, boolean paramBoolean1, boolean paramBoolean2)
  {
    String str1 = paramUri.getAuthority();
    String str2 = paramUri.getScheme();
    if ((paramBoolean2) && ((TextUtils.isEmpty(str1)) || (TextUtils.isEmpty(str2)))) {}
    do
    {
      return false;
      if ((TextUtils.isEmpty(str1)) && (TextUtils.isEmpty(str2))) {
        return true;
      }
    } while ((!paramBoolean1) && (!TextUtils.isEmpty(str2)) && (!str2.equals(getSearchDomainScheme())) && (!str2.equals("https")));
    return isSearchAuthority(str1);
  }
  
  private boolean isSearchPath(Uri paramUri)
  {
    return this.mConfig.isGoogleSearchUrlPath(paramUri.getPath());
  }
  
  private static Locale localeFromString(String paramString)
  {
    String[] arrayOfString = paramString.split("-");
    if (arrayOfString.length == 1) {
      return new Locale(arrayOfString[0]);
    }
    if (arrayOfString.length == 2)
    {
      if ((arrayOfString[1].equals("Hant")) || (arrayOfString[1].equals("Hans"))) {
        return new Locale(arrayOfString[0], "", arrayOfString[1]);
      }
      return new Locale(arrayOfString[0], arrayOfString[1]);
    }
    if (arrayOfString.length == 3) {
      return new Locale(arrayOfString[0], arrayOfString[2], arrayOfString[1]);
    }
    VelvetStrictMode.logW("Search.SearchUrlHelper", "Unsupported locale format: " + paramString);
    return null;
  }
  
  public static Uri makeAbsoluteUri(Uri paramUri1, Uri paramUri2, @Nullable Set<String> paramSet, @Nullable Map<String, String> paramMap)
  {
    return makeAbsoluteUri(paramUri1.getScheme(), paramUri1.getAuthority(), paramUri2, paramSet, paramMap);
  }
  
  public static Uri makeAbsoluteUri(String paramString1, String paramString2, Uri paramUri, @Nullable Set<String> paramSet, @Nullable Map<String, String> paramMap)
  {
    Uri.Builder localBuilder;
    Iterator localIterator2;
    if ((paramSet != null) && (paramSet.size() > 0))
    {
      localBuilder = new Uri.Builder().fragment(paramUri.getFragment()).path(paramUri.getPath());
      localIterator2 = paramUri.getQueryParameterNames().iterator();
    }
    while (localIterator2.hasNext())
    {
      String str = (String)localIterator2.next();
      if (!paramSet.contains(str))
      {
        Iterator localIterator3 = paramUri.getQueryParameters(str).iterator();
        while (localIterator3.hasNext()) {
          localBuilder.appendQueryParameter(str, (String)localIterator3.next());
        }
        continue;
        localBuilder = paramUri.buildUpon();
      }
    }
    localBuilder.scheme(paramString1).authority(paramString2);
    if (paramMap != null)
    {
      Iterator localIterator1 = paramMap.entrySet().iterator();
      while (localIterator1.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator1.next();
        localBuilder.appendQueryParameter((String)localEntry.getKey(), (String)localEntry.getValue());
      }
    }
    return localBuilder.build();
  }
  
  private void maybeAddAuthHeader(Uri paramUri, Map<String, String> paramMap, boolean paramBoolean)
  {
    maybeAddTokenHeader(paramUri, this.mConfig.getTextSearchTokenType(), paramMap, paramBoolean);
  }
  
  private void maybeAddCookieHeader(Uri paramUri, Map<String, String> paramMap)
  {
    String str = this.mCookies.getCookie(paramUri.toString());
    if (!TextUtils.isEmpty(str))
    {
      paramMap.put("Cookie", str);
      return;
    }
    Log.w("Search.SearchUrlHelper", "Auth token not ready, no auth header set.");
  }
  
  private void maybeAddDiscourseContextHeader(Map<String, String> paramMap)
  {
    Preconditions.checkState(Feature.DISCOURSE_CONTEXT.isEnabled());
    ClientDiscourseContextProto.ClientDiscourseContext localClientDiscourseContext = this.mDiscourseContextHelper.takeRecentDiscourseContext();
    if (localClientDiscourseContext != null) {
      paramMap.put("X-Additional-Discourse-Context", ProtoUtils.messageToUrlSafeBase64(localClientDiscourseContext));
    }
  }
  
  private void maybeSetDebugHostParam(Builder paramBuilder)
  {
    String str1 = this.mSearchSettings.getDebugSearchHostParam();
    String str2;
    if (!TextUtils.isEmpty(str1))
    {
      str2 = str1;
      Log.i("Search.SearchUrlHelper", "Using local host parameter from preferences: " + str2);
    }
    for (;;)
    {
      String str3 = getSearchDomain();
      if ((!TextUtils.isEmpty(str3)) && (str3.endsWith(".sandbox.google.com")) && (!str3.equals(str2))) {
        paramBuilder.setDebugHostParam(str2);
      }
      return;
      str2 = getSearchDomain(true, false);
    }
  }
  
  public static String safeLogHeader(String paramString1, String paramString2)
  {
    if ((!TextUtils.isEmpty(paramString2)) && (!NON_REDACTED_HEADERS.contains(paramString1))) {
      paramString2 = "REDACTED";
    }
    return paramString2;
  }
  
  public static String safeLogUrl(Uri paramUri)
  {
    if (paramUri == null) {
      return "";
    }
    if ((paramUri.isHierarchical()) && (!Collections.disjoint(paramUri.getQueryParameterNames(), AUTH_QUERY_PARAMETERS))) {
      return paramUri.buildUpon().query("REDACTED").build().toString();
    }
    return paramUri.toString();
  }
  
  public static String safeLogUrl(String paramString)
  {
    if (paramString == null) {
      return "";
    }
    return safeLogUrl(Uri.parse(paramString));
  }
  
  private Uri sanitizeAndParseMaybeRelative(String paramString)
  {
    if (paramString.startsWith("/")) {}
    for (String str = paramString.replace(":", "%3A");; str = paramString) {
      return Uri.parse(str);
    }
  }
  
  private void setSearchParams(Builder paramBuilder, boolean paramBoolean, Query paramQuery)
  {
    if ((paramBoolean) && ((paramQuery == null) || (!paramQuery.shouldExcludeAuthTokens())))
    {
      paramBuilder.setIncludeAuthorization();
      if ((paramQuery != null) && (paramQuery.shouldRefreshAuthTokens())) {
        paramBuilder.setRefreshAuthorization();
      }
    }
    if (this.mConfig.shouldCombineSuggestAndPrefetch()) {
      paramBuilder.setNativeIg();
    }
    if ((paramQuery != null) && (paramQuery.isFromBackStack())) {
      paramBuilder.setRefetch();
    }
  }
  
  private void setSuggestParams(Builder paramBuilder, Query paramQuery, String paramString1, @Nullable LoginHelper.AuthToken paramAuthToken, String paramString2)
  {
    if (TextUtils.isEmpty(paramString1))
    {
      if (paramQuery.isHistoryRefreshQuery()) {
        paramBuilder.setHistoryRefresh();
      }
      paramBuilder.setQueryString("");
    }
    if (paramAuthToken != null) {
      paramBuilder.setSuggestionAuthorization(paramAuthToken);
    }
    for (;;)
    {
      if (paramString2 != null) {
        paramBuilder.setSuggestClient(paramString2);
      }
      if (this.mConfig.isSuggestLookAheadEnabled()) {
        paramBuilder.setSuggestLookahead();
      }
      if (this.mConfig.isWordByWordEnabled()) {
        paramBuilder.setWordByWordEnabled();
      }
      int i = paramQuery.getSelectionStart();
      if (i >= 0) {
        paramBuilder.setCaretPosition(i);
      }
      paramBuilder.setPsychicSession(this.mSearchBoxLogging.getPsychicSessionId());
      paramBuilder.setPsychicRequest(this.mSearchBoxLogging.takePsychicRequestCounter());
      String str = getGeoLocation();
      if (str != null) {
        paramBuilder.setGeoLocation(str);
      }
      String[] arrayOfString = this.mConfig.getCompleteServerExtraParams();
      if (arrayOfString != null) {
        paramBuilder.setExtraParams(arrayOfString);
      }
      return;
      paramBuilder.setIncludeAuthorization();
    }
  }
  
  private boolean shouldUseGoogleCom()
  {
    return this.mSearchSettings.shouldUseGoogleCom();
  }
  
  private static String stringFromLocale(Locale paramLocale)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramLocale.getLanguage());
    String str1 = paramLocale.getVariant();
    if (!TextUtils.isEmpty(str1)) {
      localStringBuilder.append("-").append(str1);
    }
    String str2 = paramLocale.getCountry();
    if (!TextUtils.isEmpty(str2)) {
      localStringBuilder.append("-").append(str2);
    }
    return localStringBuilder.toString();
  }
  
  private static String stripWwwPrefix(String paramString)
  {
    if (!TextUtils.isEmpty(paramString)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      if (paramString.startsWith("www.")) {
        paramString = paramString.substring(3);
      }
      return paramString;
    }
  }
  
  void buildUpFromQuery(Builder paramBuilder, String paramString1, Query paramQuery, String paramString2, @Nullable String paramString3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    paramBuilder.setStaticParams(paramString2).setSubmissionTime(this.mClock.currentTimeMillis()).setRlz(this.mRlzHelper.getRlzForSearch()).setSpeechCookie().setNoJesr().setCountryCode(this.mConfig.getDeviceCountry()).setTimezone().setDateHeader().setVersion(this.mVersionName);
    if (!TextUtils.isEmpty(paramString1)) {
      paramBuilder.setQueryString(paramString1);
    }
    maybeSetDebugHostParam(paramBuilder);
    if (paramBoolean3)
    {
      WebCorpus localWebCorpus = getWebCorpusForQuery(paramQuery);
      if ((localWebCorpus != null) && (localWebCorpus.getQueryParams() != null)) {
        paramBuilder.putParams(localWebCorpus.getQueryParams());
      }
    }
    if ((paramQuery.isVoiceSearch()) || (paramQuery.isQueryTextFromVoice()))
    {
      paramBuilder.setInputMethod("vs");
      String str1 = this.mVoiceSettings.getSpokenLocaleBcp47();
      if (!TextUtils.isEmpty(str1)) {
        paramBuilder.setSpokenLanguage(str1);
      }
    }
    if (paramQuery.getResultIndex() > 0) {
      paramBuilder.setResultStartIndex(Integer.toString(paramQuery.getResultIndex()));
    }
    String str2 = getPersonalizedSearchValue();
    if (str2 != null) {
      paramBuilder.setPersonalizedSearch(str2);
    }
    boolean bool1 = this.mVelvetServices.getCoreServices().getLocationSettings().canUseLocationForSearch();
    Location localLocation;
    label267:
    String str3;
    if (bool1)
    {
      localLocation = this.mVelvetServices.getLocationOracle().getBestLocation();
      paramBuilder.setOrDisableLocation(bool1, localLocation, paramQuery.getLocationOverride());
      if (paramBoolean2) {
        this.mSearchBoxLogging.setLoggingParams(paramQuery, paramBuilder);
      }
      if (!paramBoolean1) {
        break label364;
      }
      paramBuilder.setPelletizedResponse();
      if (paramQuery.isVoiceSearch())
      {
        if (!paramQuery.isEyesFree()) {
          break label372;
        }
        str3 = TTS_MODE_EYES_FREE;
        label286:
        paramBuilder.setTtsMode(str3);
      }
      if (this.mPreferences.getUnits() != 1) {
        break label380;
      }
    }
    label364:
    label372:
    label380:
    for (boolean bool2 = true;; bool2 = false)
    {
      paramBuilder.setTemperatureFahrenheit(bool2);
      ImmutableMap localImmutableMap = paramQuery.getPersistCgiParameters();
      if (localImmutableMap != null) {
        paramBuilder.setPersistCgiParameters(localImmutableMap);
      }
      paramBuilder.setBrowserDimensionsIfAvailable();
      if (!TextUtils.isEmpty(paramString3)) {
        paramBuilder.setSpeechRequestId(paramString3);
      }
      addExperimentIdsAndHeader(paramBuilder);
      return;
      localLocation = null;
      break;
      paramBuilder.setDoubleRequestArchitecture();
      break label267;
      str3 = TTS_MODE_DEFAULT;
      break label286;
    }
  }
  
  public boolean cgiParamsEqualForSearch(Query paramQuery1, Query paramQuery2)
  {
    ImmutableMap localImmutableMap1 = paramQuery1.getPersistCgiParameters();
    ImmutableMap localImmutableMap2 = paramQuery2.getPersistCgiParameters();
    Set localSet = this.mConfig.getGwsParamsAffectingQueryEquivalenceForSearch();
    if (localImmutableMap1 != localImmutableMap2)
    {
      Iterator localIterator = localSet.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if (!TextUtils.equals((CharSequence)localImmutableMap1.get(str), (CharSequence)localImmutableMap2.get(str))) {
          return false;
        }
      }
    }
    return true;
  }
  
  public boolean equivalentForContext(Query paramQuery1, Query paramQuery2)
  {
    return (Query.equivalentForSearchDisregardingParamsCorpusAndIndex(paramQuery1, paramQuery2)) && (cgiParamsEqualForSearch(paramQuery1, paramQuery2));
  }
  
  public boolean equivalentForSearch(Query paramQuery1, Query paramQuery2)
  {
    return (Query.equivalentForSearchDisregardingParams(paramQuery1, paramQuery2)) && (cgiParamsEqualForSearch(paramQuery1, paramQuery2));
  }
  
  Builder formatUrlBase(String paramString1, String paramString2)
  {
    if (paramString1 == null) {
      return null;
    }
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = getSearchDomainScheme();
    arrayOfObject[1] = paramString2;
    return new Builder(String.format(localLocale, paramString1, arrayOfObject), null);
  }
  
  public Uri formatUrlForSearchDomain(String paramString)
  {
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = getSearchDomainScheme();
    arrayOfObject[1] = getSearchDomain();
    return Uri.parse(String.format(localLocale, paramString, arrayOfObject));
  }
  
  public Builder getAdUrlBuilderForRedirectHandling(Uri paramUri)
  {
    if ((paramUri != null) && (isSearchAuthority(paramUri)) && (this.mConfig.getClickedAdUrlPath().equals(paramUri.getPath())))
    {
      if (this.mConfig.isAdClickUrlException(paramUri.toString())) {
        Log.w("Search.SearchUrlHelper", "Not handling JS-redirected ad click link");
      }
    }
    else {
      return null;
    }
    List localList = this.mConfig.getAdClickUrlSubstitutions();
    String str = Util.asString(paramUri);
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      Pair localPair = (Pair)localIterator.next();
      str = str.replaceAll((String)localPair.first, (String)localPair.second);
    }
    return new Builder(str, null).setIncludeAuthorization();
  }
  
  public Builder getCardAboveSrpLogUri(@Nonnull String paramString)
  {
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = getSearchDomainScheme();
    arrayOfObject[1] = getSearchDomain();
    Builder localBuilder = new Builder(String.format(localLocale, "%1$s://%2$s/velog/action", arrayOfObject), null);
    localBuilder.setEventId(paramString);
    localBuilder.setIncludeAuthorization();
    return localBuilder;
  }
  
  public String getDefaultSearchDomain()
  {
    return "www.google.com";
  }
  
  public Intent getExternalIntentForUri(Uri paramUri)
    throws URISyntaxException
  {
    return Intent.parseUri(paramUri.toString(), 1).addCategory("android.intent.category.BROWSABLE").setComponent(null);
  }
  
  public UriRequest getExternalSuggestionRequest(Query paramQuery)
  {
    String str = paramQuery.getQueryStringForSuggest();
    if (!TextUtils.isEmpty(str)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      Builder localBuilder = formatUrlBase(this.mConfig.getSuggestionsUrlFormat(), getSearchDomain());
      localBuilder.setStaticParams(this.mConfig.getCompleteServerClientExternalId()).setCountryCode(this.mConfig.getDeviceCountry()).setTimezone().setDateHeader().setVersion(this.mVersionName);
      if (!TextUtils.isEmpty(str)) {
        localBuilder.setQueryString(str);
      }
      maybeSetDebugHostParam(localBuilder);
      addExperimentIdsAndHeader(localBuilder);
      setSuggestParams(localBuilder, paramQuery, str, null, null);
      localBuilder.setExcludeCookies();
      return localBuilder.build();
    }
  }
  
  public String getGeoLocation()
  {
    String str = this.mSearchSettings.getSearchDomainCountryCode();
    if (TextUtils.isEmpty(str)) {
      str = Locale.getDefault().getCountry();
    }
    return convertObsoleteGeolocationCodeToNew(str);
  }
  
  public UriRequest getGsaExperimentConfigRequest()
  {
    UriRequest localUriRequest = getSearchBaseUri(true, true).build();
    Uri localUri = makeAbsoluteUri(localUriRequest.getUri(), Uri.parse("/ajax/searchapp"), null, null);
    HashMap localHashMap = new HashMap();
    localHashMap.putAll(localUriRequest.getHeaders());
    String str = this.mSearchSettings.getGsaConfigChecksum();
    if (str != null) {
      localHashMap.put("ETag", str);
    }
    return new UriRequest(localUri, null, localHashMap);
  }
  
  public UriRequest getGsaSearchParametersRequest()
  {
    if ("https".equals(getSearchDomainScheme())) {}
    for (String str = getSearchDomain();; str = getDefaultSearchDomain())
    {
      Uri localUri = Uri.parse(String.format(Locale.US, this.mConfig.getSearchDomainCheckPattern(), new Object[] { "https", str }));
      HashMap localHashMap = Maps.newHashMap();
      maybeAddAuthHeader(localUri, localHashMap, false);
      maybeAddCookieHeader(localUri, localHashMap);
      return new UriRequest(localUri, localHashMap);
    }
  }
  
  public String getHistoryApiChangeUrl()
  {
    Locale localLocale = Locale.US;
    String str = this.mConfig.getHistoryApiChangeUrlPattern();
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "https";
    arrayOfObject[1] = getSearchDomain();
    return String.format(localLocale, str, arrayOfObject);
  }
  
  public Uri getHistoryApiLookupUrl(long paramLong1, long paramLong2, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    Locale localLocale = Locale.US;
    String str = this.mConfig.getHistoryApiLookupUrlPattern();
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "https";
    arrayOfObject[1] = getSearchDomain();
    Uri.Builder localBuilder = Uri.parse(String.format(localLocale, str, arrayOfObject)).buildUpon();
    localBuilder.appendQueryParameter("client", this.mConfig.getHistoryApiClientId());
    if (paramLong1 != 0L) {
      localBuilder.appendQueryParameter("min", Long.toString(paramLong1));
    }
    if (paramLong2 != 0L) {
      localBuilder.appendQueryParameter("max", Long.toString(paramLong2));
    }
    if (paramInt != -1) {
      localBuilder.appendQueryParameter("num", Integer.toString(paramInt));
    }
    if (paramBoolean1) {
      localBuilder.appendQueryParameter("titles", "1");
    }
    if (paramBoolean2) {
      localBuilder.appendQueryParameter("thumbnails", "1");
    }
    return localBuilder.build();
  }
  
  int getImageMetadataChunkParam(int paramInt)
  {
    if (paramInt < 20) {
      return 0;
    }
    if (paramInt < 100) {
      return 1;
    }
    return 1 + paramInt / 100;
  }
  
  int getImageMetadataStartParam(int paramInt)
  {
    int i = 20;
    if (paramInt < i) {
      i = 0;
    }
    while (paramInt < 100) {
      return i;
    }
    return 100 * (paramInt / 100);
  }
  
  public Uri getImageMetadataUrl(Query paramQuery, int paramInt)
  {
    int i = getImageMetadataChunkParam(paramInt);
    int j = getImageMetadataStartParam(paramInt);
    Locale localLocale = Locale.US;
    String str = this.mGsaConfig.getImageMetatDataUrlPattern();
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = getSearchDomainScheme();
    arrayOfObject[1] = getSearchDomain();
    Uri.Builder localBuilder = Uri.parse(String.format(localLocale, str, arrayOfObject)).buildUpon();
    localBuilder.appendQueryParameter(this.mConfig.getWebCorpusQueryParam(), "isch").appendQueryParameter("biw", "100").appendQueryParameter("bih", "200").appendQueryParameter("ifm", "1").appendQueryParameter("ijn", Integer.toString(i)).appendQueryParameter("start", Integer.toString(j)).appendQueryParameter("dbla", "1").appendQueryParameter("q", paramQuery.getQueryStringForSearch());
    return localBuilder.build();
  }
  
  public String getLocalSearchDomain()
  {
    return getSearchDomain(false, true);
  }
  
  public Uri getLoginDomainUrl()
  {
    return new Uri.Builder().scheme(getSearchDomainScheme()).authority(getSearchDomain()).build();
  }
  
  public Builder getPrefetchGen204Uri(Query paramQuery, String paramString)
  {
    Preconditions.checkNotNull(paramQuery);
    String str = this.mConfig.getGoogleGen204Pattern();
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = getSearchDomainScheme();
    arrayOfObject[1] = getSearchDomain();
    Builder localBuilder = new Builder(String.format(localLocale, str, arrayOfObject), null);
    this.mSearchBoxLogging.setLoggingParams(paramQuery, localBuilder);
    localBuilder.setQueryString(paramQuery.getQueryStringForSearch());
    localBuilder.setEventId(paramString);
    localBuilder.setIncludeAuthorization();
    return localBuilder;
  }
  
  @Nullable
  public Query getQueryFromUrl(Query paramQuery, Uri paramUri)
  {
    boolean bool1 = isSearchAuthority(paramUri);
    String str1;
    if (bool1) {
      if (isSearchPath(paramUri))
      {
        boolean bool4 = paramUri.isHierarchical();
        String str4 = null;
        if (bool4) {
          str4 = getQueryParameter(paramUri, this.mConfig.getWebCorpusQueryParam());
        }
        if (str4 != null) {
          str1 = this.mCorpora.resolveWebCorpusId(str4);
        }
      }
    }
    while (str1 != null)
    {
      String str2 = getQueryParameter(paramUri, "output");
      if ((TextUtils.equals("rss", str2)) || (TextUtils.equals("atom", str2)))
      {
        return null;
        str1 = "web";
        continue;
        Uri localUri = getUriFromRedirect(paramUri);
        str1 = null;
        if (localUri != null)
        {
          Query localQuery = getQueryFromUrl(paramQuery, localUri);
          str1 = null;
          if (localQuery != null)
          {
            return localQuery;
            boolean bool2 = this.mCorpora.areWebCorporaLoaded();
            str1 = null;
            if (bool2)
            {
              Iterator localIterator = this.mCorpora.getSubCorpora(this.mCorpora.getWebCorpus()).iterator();
              WebCorpus localWebCorpus;
              do
              {
                boolean bool3 = localIterator.hasNext();
                str1 = null;
                if (!bool3) {
                  break;
                }
                localWebCorpus = (WebCorpus)localIterator.next();
              } while (!localWebCorpus.matchesUrl(paramUri, bool1));
              str1 = localWebCorpus.getIdentifier();
            }
          }
        }
      }
      else
      {
        String str3 = getQueryParameter(paramUri, "q");
        if (str3 != null) {
          return paramQuery.withQueryStringCorpusIndexAndPersistCgiParameters(str3, str1, getIntQueryParameter(paramUri, "start", 0), getAllQueryParameters(paramUri));
        }
      }
    }
    return null;
  }
  
  @Nonnull
  public Pair<UriRequest, Builder> getResultTargetAndLogUrl(Uri paramUri1, @Nullable Uri paramUri2)
  {
    HashMap localHashMap = Maps.newHashMap();
    String str = getUrlStringFromRedirect(paramUri1, false);
    if (!TextUtils.isEmpty(str))
    {
      Uri localUri1 = maybeMakeAbsoluteUri(paramUri1, str);
      Builder localBuilder = new Builder(paramUri1, null);
      localBuilder.setIncludeAuthorization();
      localBuilder.setSpeechCookie();
      if ((paramUri2 != null) && (getSearchDomainDebugOrDogfoodOverride() == null))
      {
        Uri localUri2 = paramUri2;
        if (paramUri2.getScheme().equalsIgnoreCase("https")) {
          localUri2 = Uri.EMPTY.buildUpon().scheme("https").authority(paramUri2.getAuthority()).build();
        }
        localHashMap.put("Referer", Util.asString(localUri2));
      }
      return Pair.create(new UriRequest(localUri1, localHashMap), localBuilder);
    }
    return Pair.create(new UriRequest(paramUri1, localHashMap), null);
  }
  
  @Nullable
  public Uri getResultTargetUri(@Nullable String paramString)
  {
    return getResultTargetUri(paramString, this.mConfig.getClickedResultUrlPath(), getSearchDomainScheme(), getSearchDomain());
  }
  
  public Builder getSearchBaseUri(boolean paramBoolean1, boolean paramBoolean2)
  {
    Uri.Builder localBuilder = new Uri.Builder();
    if (paramBoolean1)
    {
      localBuilder.scheme("https");
      if (isSearchDomainSchemeHttps()) {
        localBuilder.authority(getSearchDomain());
      }
    }
    for (;;)
    {
      Builder localBuilder1 = new Builder(localBuilder.build(), null);
      if (paramBoolean2) {
        localBuilder1.setIncludeAuthorization();
      }
      return localBuilder1;
      localBuilder.authority(getDefaultSearchDomain());
      continue;
      localBuilder.scheme(getSearchDomainScheme());
      localBuilder.authority(getSearchDomain());
    }
  }
  
  public Builder getSearchBaseUrl()
  {
    return formatUrlBase(this.mGsaConfig.getSearchUrlFormat(), getSearchDomain());
  }
  
  public String getSearchDomain()
  {
    return getSearchDomain(false, false);
  }
  
  @Nonnull
  public String getSearchDomainScheme()
  {
    if (this.mConfig.shouldAllowSslSearch())
    {
      String str = this.mSearchSettings.getDebugSearchSchemeOverride();
      if (!TextUtils.isEmpty(str)) {
        return str;
      }
      return this.mSearchSettings.getSearchDomainSchemePreference();
    }
    return "http";
  }
  
  public UriRequest getSearchRequest(Query paramQuery, String paramString)
  {
    return getSearchUrlBuilder(paramQuery, paramString, true, true).build();
  }
  
  public UriRequest getSearchRequestNoAuthOrCookies(Query paramQuery, String paramString)
  {
    Builder localBuilder = getSearchUrlBuilder(paramQuery, paramString, false, false);
    localBuilder.setExcludeCookies();
    return localBuilder.build();
  }
  
  Builder getSearchUrlBuilder(Uri paramUri, Query paramQuery, @Nullable String paramString, boolean paramBoolean)
  {
    Builder localBuilder = new Builder(paramUri, null);
    if (paramQuery != null)
    {
      Preconditions.checkArgument(paramQuery.isValidSearch());
      buildUpFromQuery(localBuilder, paramQuery.getQueryStringForSearch(), paramQuery, getClientParam(), paramString, shouldRequestPelletResponse(paramQuery), true, true);
    }
    setSearchParams(localBuilder, true, paramQuery);
    return localBuilder;
  }
  
  Builder getSearchUrlBuilder(String paramString)
  {
    return getSearchUrlBuilder(paramString, null, null, true, true, true);
  }
  
  public UriRequest getSuggestionAndPrefetchRequest(Query paramQuery, @Nullable LoginHelper.AuthToken paramAuthToken)
  {
    String str1 = getWebCorpusForQuery(paramQuery).getPrefetchPattern();
    String str2 = paramQuery.getQueryStringForSuggest();
    String str3 = getClientParam();
    String str4 = this.mConfig.getCompleteServerClientId();
    if (paramAuthToken == null) {}
    for (boolean bool = true;; bool = false)
    {
      Builder localBuilder = getUrlBuilder(str1, paramQuery, str2, str3, null, bool, true, false, true);
      localBuilder.setPrefetch();
      setSuggestParams(localBuilder, paramQuery, str2, paramAuthToken, str4);
      return localBuilder.build();
    }
  }
  
  public UriRequest getSuggestionRequest(Query paramQuery, @Nullable LoginHelper.AuthToken paramAuthToken)
  {
    String str1 = this.mConfig.getSuggestionsUrlFormat();
    String str2 = paramQuery.getQueryStringForSuggest();
    String str3 = this.mConfig.getCompleteServerClientId();
    if (paramAuthToken == null) {}
    for (boolean bool = true;; bool = false)
    {
      Builder localBuilder = getUrlBuilder(str1, paramQuery, str2, str3, null, bool, false, false, false);
      setSuggestParams(localBuilder, paramQuery, str2, paramAuthToken, null);
      return localBuilder.build();
    }
  }
  
  @Nullable
  public Uri getUriFromRedirect(Uri paramUri)
  {
    String str = getUrlStringFromRedirect(paramUri, true);
    if (str == null) {
      return null;
    }
    return Uri.parse(str);
  }
  
  public Builder getUrlQueryLogUri(@Nonnull String paramString1, @Nonnull String paramString2)
  {
    Preconditions.checkNotNull(paramString1);
    Preconditions.checkNotNull(paramString2);
    str1 = this.mVelvetServices.getGsaConfigFlags().getLaunchUrlQueryGen204Pattern();
    try
    {
      Locale localLocale2 = Locale.US;
      Object[] arrayOfObject2 = new Object[3];
      arrayOfObject2[0] = getSearchDomain();
      arrayOfObject2[1] = URLEncoder.encode(paramString1, Charset.defaultCharset().displayName());
      arrayOfObject2[2] = URLEncoder.encode(paramString2, Charset.defaultCharset().displayName());
      String str3 = String.format(localLocale2, str1, arrayOfObject2);
      str2 = str3;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      for (;;)
      {
        Builder localBuilder;
        Log.w("Search.SearchUrlHelper", "Failed to URL encode query and url: " + paramString1 + ", " + paramString2);
        Locale localLocale1 = Locale.US;
        Object[] arrayOfObject1 = new Object[3];
        arrayOfObject1[0] = getSearchDomain();
        arrayOfObject1[1] = paramString1.replaceAll("@|&", "");
        arrayOfObject1[2] = paramString2.replaceAll("@|&", "");
        String str2 = String.format(localLocale1, str1, arrayOfObject1);
      }
    }
    localBuilder = new Builder(str2, null);
    localBuilder.setIncludeAuthorization();
    return localBuilder;
  }
  
  public Builder getVoiceSearchUrlBuilder(Query paramQuery, Uri paramUri, String paramString)
  {
    return getSearchUrlBuilder(paramUri, paramQuery, paramString, shouldRequestPelletResponse(paramQuery));
  }
  
  public String getWebSearchBaseUrl()
  {
    Locale localLocale = Locale.US;
    String str = this.mCorpora.getWebCorpus().getWebSearchPattern();
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = getSearchDomainScheme();
    arrayOfObject[1] = getSearchDomain();
    return String.format(localLocale, str, arrayOfObject);
  }
  
  public boolean isDotComAnyway()
  {
    return TextUtils.equals(getDefaultSearchDomain(), getLocalSearchDomain());
  }
  
  public boolean isSearchAuthority(Uri paramUri)
  {
    return isSearchAuthority(paramUri, false, false);
  }
  
  public boolean isSearchAuthority(String paramString)
  {
    if (paramString == null) {}
    String str;
    do
    {
      return false;
      str = paramString.toLowerCase(Locale.US);
    } while ((!str.endsWith(getDefaultSearchDomain().toLowerCase(Locale.US))) && (!str.endsWith(getLocalSearchDomain().toLowerCase(Locale.US))));
    return true;
  }
  
  public boolean isSearchDomainSchemeHttps()
  {
    return getSearchDomainScheme().equals("https");
  }
  
  public boolean isSearchDomainSchemeSecure()
  {
    String str = getSearchDomainScheme();
    return (str.equals("https")) || (str.equals(this.mSearchSettings.getDebugSearchSchemeOverride()));
  }
  
  public boolean isSecureGoogleUri(Uri paramUri)
  {
    String str1 = paramUri.getScheme();
    String str2 = paramUri.getHost();
    if ((!str1.equalsIgnoreCase("https")) && (!str1.equalsIgnoreCase(this.mSearchSettings.getDebugSearchSchemeOverride()))) {}
    while ((!endsWithIfPresent(str2, stripWwwPrefix(getDefaultSearchDomain()))) && (!endsWithIfPresent(str2, stripWwwPrefix(getLocalSearchDomain())))) {
      return false;
    }
    return true;
  }
  
  protected void maybeAddTokenHeader(Uri paramUri, String paramString, Map<String, String> paramMap, boolean paramBoolean)
  {
    if ((!TextUtils.isEmpty(paramString)) && (canSendAuthHeader(paramUri)))
    {
      String str = this.mLoginHelper.blockingGetToken(paramString, 1000L);
      if (str != null)
      {
        if (paramBoolean)
        {
          this.mLoginHelper.invalidateToken(str);
          str = this.mLoginHelper.blockingGetToken(paramString, 1000L);
        }
        paramMap.put("Authorization", "Bearer " + str);
      }
    }
  }
  
  public Uri maybeMakeAbsoluteUri(@Nullable Uri paramUri, String paramString)
  {
    Uri localUri = sanitizeAndParseMaybeRelative(paramString);
    String str1;
    if (localUri.isRelative())
    {
      if (paramUri == null) {
        break label42;
      }
      str1 = paramUri.getScheme();
    }
    for (String str2 = paramUri.getAuthority();; str2 = getSearchDomain())
    {
      localUri = makeAbsoluteUri(str1, str2, localUri, null, null);
      return localUri;
      label42:
      str1 = getSearchDomainScheme();
    }
  }
  
  public Uri maybeMakeAbsoluteUri(String paramString)
  {
    return maybeMakeAbsoluteUri(null, paramString);
  }
  
  public void setBrowserDimensions(Point paramPoint)
  {
    this.mBrowserDimensions = paramPoint;
  }
  
  public boolean shouldAllowBackBetween(Uri paramUri1, Uri paramUri2)
  {
    int j;
    boolean bool1;
    if (isSearchAuthority(paramUri2))
    {
      String str = getPathQueryAndFragment(paramUri2);
      String[] arrayOfString = this.mConfig.getAllowBackFromUrlWhitelist();
      int i = arrayOfString.length;
      j = 0;
      if (j < i) {
        if (str.matches(arrayOfString[j])) {
          bool1 = true;
        }
      }
    }
    Sets.SetView localSetView;
    boolean bool5;
    do
    {
      UriDiff localUriDiff1;
      boolean bool4;
      do
      {
        boolean bool3;
        do
        {
          boolean bool2;
          do
          {
            UriDiff localUriDiff2;
            do
            {
              return bool1;
              j++;
              break;
              localUriDiff1 = UriDiff.diff(paramUri1, paramUri2);
              localUriDiff2 = UriDiff.SAME;
              bool1 = false;
            } while (localUriDiff1 == localUriDiff2);
            bool2 = localUriDiff1.authorityDifferent();
            bool1 = false;
          } while (bool2);
          bool3 = localUriDiff1.schemeDifferent();
          bool1 = false;
        } while (bool3);
        bool4 = localUriDiff1.pathDifferent();
        bool1 = false;
      } while (bool4);
      localSetView = Sets.union(localUriDiff1.queryDiffs(), localUriDiff1.fragmentDiffs());
      bool5 = localSetView.isEmpty();
      bool1 = false;
    } while (bool5);
    return Sets.difference(localSetView, this.mConfig.getChangingParamsThatAllowBackNavigation()).isEmpty();
  }
  
  public boolean shouldRequestPelletResponse(@Nonnull Query paramQuery)
  {
    return paramQuery.shouldShowCards();
  }
  
  public class Builder
    implements Supplier<UriRequest>
  {
    private final Map<String, String> mHeaders = Maps.newHashMap();
    private boolean mIncludeAuth = false;
    private boolean mIncludeCookies = true;
    private boolean mIncludeDiscourseContextHeader = false;
    private final Map<String, String> mParams;
    private Map<String, String> mPersistCgiParameters;
    private boolean mRefreshAuth = false;
    private LoginHelper.AuthToken mSuggestionsAuthToken;
    private final Uri mUri;
    
    private Builder(Uri paramUri)
    {
      this.mUri = paramUri;
      this.mParams = SearchUrlHelper.getAllQueryParameters(this.mUri);
    }
    
    private Builder(String paramString)
    {
      this(Uri.parse(paramString));
    }
    
    private void overrideParam(String paramString1, String paramString2)
    {
      this.mParams.put(paramString1, paramString2);
    }
    
    private void putAndCheck(Map<String, String> paramMap, String paramString1, String paramString2)
    {
      Preconditions.checkNotNull(paramString2);
      if ((String)paramMap.put(paramString1, paramString2) != null) {
        Log.w("Search.SearchUrlHelper", "URL param or header written twice. Key: " + paramString1 + ", old value: \"" + (String)paramMap.get(paramString1) + "\" new value: \"" + paramString2 + "\"", new Throwable());
      }
    }
    
    private void putHeader(String paramString1, String paramString2)
    {
      putAndCheck(this.mHeaders, paramString1, paramString2);
    }
    
    private void putParam(String paramString1, String paramString2)
    {
      putAndCheck(this.mParams, paramString1, paramString2);
    }
    
    private Builder setDateHeader()
    {
      synchronized (SearchUrlHelper.this.mSimpleDateFormat)
      {
        String str = SearchUrlHelper.this.mSimpleDateFormat.format(new Date());
        putHeader("Date", str);
        return this;
      }
    }
    
    public UriRequest build()
    {
      return buildAndRewrite(null);
    }
    
    public UriRequest buildAndRewrite(UriRewriter paramUriRewriter)
    {
      Uri.Builder localBuilder = this.mUri.buildUpon().clearQuery();
      Object localObject;
      if (this.mPersistCgiParameters != null)
      {
        localObject = Maps.newHashMap();
        ((Map)localObject).putAll(this.mPersistCgiParameters);
        ((Map)localObject).putAll(this.mParams);
      }
      for (;;)
      {
        Iterator localIterator = ((Map)localObject).entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          localBuilder.appendQueryParameter((String)localEntry.getKey(), (String)localEntry.getValue());
        }
        localObject = this.mParams;
      }
      Uri localUri = localBuilder.build();
      if (this.mSuggestionsAuthToken != null) {
        this.mHeaders.put("Authorization", SearchUrlHelper.this.mConfig.getSuggestionAuthHeaderPrefix() + this.mSuggestionsAuthToken.getToken());
      }
      if (this.mIncludeAuth) {
        SearchUrlHelper.this.maybeAddAuthHeader(localUri, this.mHeaders, this.mRefreshAuth);
      }
      if (this.mIncludeCookies) {
        SearchUrlHelper.this.maybeAddCookieHeader(localUri, this.mHeaders);
      }
      if (this.mIncludeDiscourseContextHeader) {
        SearchUrlHelper.this.maybeAddDiscourseContextHeader(this.mHeaders);
      }
      if (paramUriRewriter != null) {
        localUri = Uri.parse(paramUriRewriter.rewrite(localUri.toString()));
      }
      return new UriRequest(localUri, (Map)localObject, this.mHeaders);
    }
    
    public UriRequest get()
    {
      return build();
    }
    
    public Builder putParams(Map<String, String> paramMap)
    {
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        putParam((String)localEntry.getKey(), (String)localEntry.getValue());
      }
      return this;
    }
    
    public Builder setAssistedQueryStats(String paramString)
    {
      putParam("aqs", paramString);
      return this;
    }
    
    public Builder setAutoExecMs(long paramLong)
    {
      if (paramLong >= 0L) {
        putParam("auto_exec_ms", Long.toString(paramLong));
      }
      return this;
    }
    
    public Builder setBrowserDimensionsIfAvailable()
    {
      Point localPoint = SearchUrlHelper.this.mBrowserDimensions;
      if (localPoint != null)
      {
        putParam("biw", Integer.toString(localPoint.x));
        putParam("bih", Integer.toString(localPoint.y));
      }
      return this;
    }
    
    public Builder setCaretPosition(int paramInt)
    {
      putParam("cp", Integer.toString(paramInt));
      return this;
    }
    
    public Builder setCountryCode(String paramString)
    {
      putParam("gcc", paramString);
      return this;
    }
    
    public Builder setDebugHostParam(String paramString)
    {
      putParam("host", paramString);
      return this;
    }
    
    public Builder setDoubleRequestArchitecture()
    {
      putParam("dbla", "1");
      return this;
    }
    
    public Builder setEntryPoint(String paramString)
    {
      if (!TextUtils.isEmpty(paramString))
      {
        putParam("entrypoint", "android-" + paramString);
        return this;
      }
      VelvetStrictMode.logW("Search.SearchUrlHelper", "Not setting entrypoint on url. This shouldn't happen.");
      return this;
    }
    
    public Builder setEventId(String paramString)
    {
      putParam("ei", paramString);
      return this;
    }
    
    public Builder setExcludeCookies()
    {
      this.mIncludeCookies = false;
      return this;
    }
    
    public Builder setExtraParams(String[] paramArrayOfString)
    {
      for (int i = 0; i < -1 + paramArrayOfString.length; i += 2) {
        overrideParam(paramArrayOfString[i], paramArrayOfString[(i + 1)]);
      }
      return this;
    }
    
    public Builder setGeoLocation(String paramString)
    {
      putParam("gl", paramString);
      return this;
    }
    
    public Builder setHistoryRefresh()
    {
      String str = SearchUrlHelper.this.mConfig.getCompleteServerHistoryRefreshParamName();
      if (!TextUtils.isEmpty(str)) {
        putParam(str, SearchUrlHelper.this.mConfig.getCompleteServerHistoryRefreshParamValue());
      }
      return this;
    }
    
    public Builder setHostHeader(String paramString)
    {
      putHeader("Host", paramString);
      return this;
    }
    
    public Builder setIncludeAuthorization()
    {
      if (this.mSuggestionsAuthToken == null) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkState(bool);
        this.mIncludeAuth = true;
        return this;
      }
    }
    
    public Builder setIncludeDiscourseContext()
    {
      this.mIncludeDiscourseContextHeader = true;
      String str = SearchUrlHelper.this.mDiscourseContextHelper.getCurrentEventId();
      if (str != null) {
        setEventId(str);
      }
      return this;
    }
    
    public Builder setInputMethod(String paramString)
    {
      putParam("inm", paramString);
      return this;
    }
    
    public Builder setNativeIg()
    {
      putParam("pbx", "1");
      return this;
    }
    
    public Builder setNoJesr()
    {
      putParam("noj", "1");
      return this;
    }
    
    public Builder setOrDisableLocation(boolean paramBoolean, @Nullable Location paramLocation1, @Nullable Location paramLocation2)
    {
      if (paramBoolean)
      {
        if ((paramLocation1 != null) || (paramLocation2 != null))
        {
          putParam("action", "devloc");
          putHeader("X-Geo", XGeoEncoder.createHeader(SearchUrlHelper.this.mConfig.shouldSendQueryLocation(), paramLocation1, paramLocation2));
          if (paramLocation1 != null) {
            putHeader("Geo-Position", GeoPositionEncoder.encodeLocation(paramLocation1));
          }
        }
        return this;
      }
      putParam("devloc", "0");
      return this;
    }
    
    public Builder setOriginalQueryString(String paramString)
    {
      putParam("oq", paramString);
      return this;
    }
    
    public void setPelletizedResponse()
    {
      putParam("tch", "6");
    }
    
    public Builder setPersistCgiParameters(Map<String, String> paramMap)
    {
      this.mPersistCgiParameters = paramMap;
      return this;
    }
    
    public Builder setPersonalizedSearch(String paramString)
    {
      putParam("pws", paramString);
      return this;
    }
    
    public Builder setPrefetch()
    {
      putParam("pf", "i");
      return this;
    }
    
    public Builder setPsychicRequest(int paramInt)
    {
      putParam("ech", Integer.toString(paramInt));
      return this;
    }
    
    public Builder setPsychicSession(String paramString)
    {
      putParam("psi", paramString);
      return this;
    }
    
    public Builder setQueryString(String paramString)
    {
      putParam("q", paramString);
      return this;
    }
    
    public Builder setRefetch()
    {
      putParam("rf", "1");
      return this;
    }
    
    public Builder setRefreshAuthorization()
    {
      Preconditions.checkState(this.mIncludeAuth);
      this.mRefreshAuth = true;
      return this;
    }
    
    public Builder setResultStartIndex(String paramString)
    {
      putParam("start", paramString);
      return this;
    }
    
    public Builder setRlz(String paramString)
    {
      if (!TextUtils.isEmpty(paramString)) {
        putParam("rlz", paramString);
      }
      return this;
    }
    
    public Builder setSource(String paramString)
    {
      if (!TextUtils.isEmpty(paramString))
      {
        putParam("source", paramString);
        return this;
      }
      VelvetStrictMode.logW("Search.SearchUrlHelper", "Not setting source on url. This shouldn't happen.");
      return this;
    }
    
    public Builder setSpeechCookie()
    {
      String str = SearchUrlHelper.this.mSearchSettings.getVoiceSearchInstallId();
      if (!TextUtils.isEmpty(str)) {
        putHeader("X-Speech-Cookie", str);
      }
      return this;
    }
    
    public Builder setSpeechRequestId(@Nullable String paramString)
    {
      if (!TextUtils.isEmpty(paramString)) {
        putHeader("X-Speech-RequestId", paramString);
      }
      return this;
    }
    
    public Builder setSpokenLanguage(String paramString)
    {
      putParam("spknlang", paramString);
      return this;
    }
    
    public Builder setStaticParams()
    {
      return setStaticParams(SearchUrlHelper.this.getClientParam());
    }
    
    public Builder setStaticParams(String paramString)
    {
      putParam("redir_esc", "");
      putParam("client", paramString);
      putParam("hl", SearchUrlHelper.this.getHlParameterForSearch());
      putParam("safe", SearchUrlHelper.this.mSearchSettings.getSafeSearch());
      putParam("oe", "utf-8");
      return this;
    }
    
    public Builder setSubmissionTime(long paramLong)
    {
      putParam("qsubts", Long.toString(paramLong));
      return this;
    }
    
    public Builder setSuggestClient(String paramString)
    {
      putParam("sclient", paramString);
      return this;
    }
    
    public Builder setSuggestLookahead()
    {
      putParam("sla", "1");
      return this;
    }
    
    public Builder setSuggestionAuthorization(LoginHelper.AuthToken paramAuthToken)
    {
      if (!this.mIncludeAuth) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkState(bool);
        this.mSuggestionsAuthToken = paramAuthToken;
        return this;
      }
    }
    
    public Builder setTemperatureFahrenheit(boolean paramBoolean)
    {
      if (paramBoolean) {}
      for (String str = "1";; str = "0")
      {
        putParam("fheit", str);
        return this;
      }
    }
    
    public Builder setTimezone()
    {
      putParam("ctzn", TimeZone.getDefault().getID());
      return this;
    }
    
    public Builder setTtsMode(String paramString)
    {
      putParam("ttsm", paramString);
      return this;
    }
    
    public Builder setUserAgent(String paramString)
    {
      putHeader("User-Agent", paramString);
      return this;
    }
    
    public Builder setVersion(String paramString)
    {
      putParam("v", paramString);
      return this;
    }
    
    public Builder setWordByWordEnabled()
    {
      putParam("sugexp", "wbw");
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.SearchUrlHelper
 * JD-Core Version:    0.7.0.1
 */