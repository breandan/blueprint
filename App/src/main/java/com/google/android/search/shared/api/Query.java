package com.google.android.search.shared.api;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.SuggestionSpan;
import com.google.android.shared.util.SpannedCharSequences;
import com.google.android.shared.util.Util;
import com.google.android.shared.util.Whitespace;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Query
  implements Parcelable
{
  public static final Parcelable.Creator<Query> CREATOR = new Parcelable.Creator()
  {
    public Query createFromParcel(Parcel paramAnonymousParcel)
    {
      return Query.EMPTY.fromParcel(paramAnonymousParcel);
    }
    
    public Query[] newArray(int paramAnonymousInt)
    {
      return new Query[paramAnonymousInt];
    }
  };
  public static final Query EMPTY;
  private static final ThreadLocal<Builder> sBuilder = new ThreadLocal();
  private static AtomicLong sLatestCommitId = new AtomicLong();
  @Nullable
  private final Bundle mAssistContext;
  @Nullable
  private final String mAssistPackage;
  private final long mCommitId;
  private final String mCorpusId;
  @Nullable
  private final Bundle mExtras;
  private final int mFlags;
  @Nullable
  private final Location mLocationOverride;
  @Nullable
  private final ImmutableList<CharSequence> mOtherHypotheses;
  @Nonnull
  private final ImmutableMap<String, String> mPersistCgiParameters;
  private final CharSequence mQueryChars;
  @Nullable
  private final Uri mRecordedAudioUri;
  private final int mResultIndex;
  @Nullable
  private final SearchBoxStats mSearchBoxStats;
  private final int mSelectionEnd;
  private final int mSelectionStart;
  @Nullable
  private final Serializable mSentinelData;
  
  static
  {
    EMPTY = new Query(0, "", null, 0, 0, "web", 0, null, 0L, null, null, null, null, null, null, null);
  }
  
  Query()
  {
    this(0, "", null, 0, 0, "web", 0, null, 0L, null, null, null, null, null, null, null);
  }
  
  private Query(int paramInt1, CharSequence paramCharSequence, ImmutableList<CharSequence> paramImmutableList, int paramInt2, int paramInt3, String paramString1, int paramInt4, Map<String, String> paramMap, long paramLong, SearchBoxStats paramSearchBoxStats, Location paramLocation, Serializable paramSerializable, Bundle paramBundle1, String paramString2, Bundle paramBundle2, Uri paramUri)
  {
    this.mFlags = paramInt1;
    if (paramCharSequence != null)
    {
      this.mQueryChars = paramCharSequence;
      this.mOtherHypotheses = paramImmutableList;
      this.mSelectionStart = paramInt2;
      this.mSelectionEnd = paramInt3;
      this.mCorpusId = paramString1;
      this.mResultIndex = paramInt4;
      if (paramMap != null) {
        break label114;
      }
      this.mPersistCgiParameters = ImmutableMap.of();
    }
    for (;;)
    {
      this.mCommitId = paramLong;
      this.mSearchBoxStats = paramSearchBoxStats;
      this.mLocationOverride = paramLocation;
      this.mSentinelData = paramSerializable;
      this.mExtras = paramBundle1;
      this.mAssistPackage = paramString2;
      this.mAssistContext = paramBundle2;
      this.mRecordedAudioUri = paramUri;
      return;
      paramCharSequence = "";
      break;
      label114:
      if ((paramMap instanceof ImmutableMap)) {
        this.mPersistCgiParameters = ((ImmutableMap)paramMap);
      } else {
        this.mPersistCgiParameters = ImmutableMap.copyOf(paramMap);
      }
    }
  }
  
  private Builder buildUpon()
  {
    Builder localBuilder = (Builder)sBuilder.get();
    if (localBuilder == null)
    {
      localBuilder = new Builder(null);
      sBuilder.set(localBuilder);
    }
    return localBuilder.buildUpon(this);
  }
  
  public static boolean equivalentForSearchDisregardingParams(Query paramQuery1, Query paramQuery2)
  {
    return (equivalentForSearchDisregardingParamsCorpusAndIndex(paramQuery1, paramQuery2)) && (paramQuery1.getResultIndex() == paramQuery2.getResultIndex()) && (TextUtils.equals(paramQuery1.getCorpusId(), paramQuery2.getCorpusId()));
  }
  
  public static boolean equivalentForSearchDisregardingParamsCorpusAndIndex(Query paramQuery1, Query paramQuery2)
  {
    return (Util.equalsIgnoreCase(paramQuery1.getQueryStringForSearch(), paramQuery2.getQueryStringForSearch())) && (paramQuery1.shouldRefreshAuthTokens() == paramQuery2.shouldRefreshAuthTokens()) && (paramQuery1.shouldExcludeAuthTokens() == paramQuery2.shouldExcludeAuthTokens()) && (Objects.equal(paramQuery1.mLocationOverride, paramQuery2.mLocationOverride));
  }
  
  public static boolean equivalentForSuggest(Query paramQuery1, Query paramQuery2)
  {
    return equivalentForSuggest(paramQuery1.getQueryStringForSuggest(), paramQuery2.getQueryStringForSuggest());
  }
  
  public static boolean equivalentForSuggest(String paramString1, String paramString2)
  {
    return Util.equalsIgnoreCase(paramString1, paramString2);
  }
  
  private Query fromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    CharSequence localCharSequence = SpannedCharSequences.readFromParcel(paramParcel);
    ImmutableList localImmutableList = SpannedCharSequences.readListFromParcel(paramParcel);
    int j = paramParcel.readInt();
    int k = paramParcel.readInt();
    String str = paramParcel.readString();
    ImmutableMap localImmutableMap = ImmutableMap.copyOf(Util.bundleToStringMap(paramParcel.readBundle()));
    int m = paramParcel.readInt();
    long l = paramParcel.readLong();
    sLatestCommitId.set(Math.max(sLatestCommitId.get(), l));
    return new Query(i, localCharSequence, localImmutableList, j, k, str, m, localImmutableMap, l, (SearchBoxStats)paramParcel.readParcelable(SearchBoxStats.class.getClassLoader()), (Location)paramParcel.readParcelable(Location.class.getClassLoader()), paramParcel.readSerializable(), paramParcel.readBundle(Query.class.getClassLoader()), paramParcel.readString(), paramParcel.readBundle(), (Uri)paramParcel.readParcelable(Uri.class.getClassLoader()));
  }
  
  private String getSelectionString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case -1: 
      return "unchanged";
    }
    return "end";
  }
  
  private int getTrigger()
  {
    return 0xF0 & this.mFlags;
  }
  
  private String getTriggerString()
  {
    int i = getTrigger();
    switch (i)
    {
    default: 
      return "unknown(" + i + ")";
    case 0: 
      return "user";
    case 16: 
      return "intent";
    case 32: 
      return "prefetch";
    case 48: 
      return "predictive";
    case 64: 
      return "webview";
    case 80: 
      return "hotword";
    case 96: 
      return "bthandsfree";
    case 112: 
      return "wiredheadset";
    case 128: 
      return "follow-on";
    }
    return "wave";
  }
  
  private int getType()
  {
    return 0xF & this.mFlags;
  }
  
  private String getTypeString()
  {
    int i = getType();
    switch (i)
    {
    default: 
      return "unknown(" + i + ")";
    case 0: 
      return "text";
    case 1: 
      return "voice";
    case 2: 
      return "music";
    case 3: 
      return "tv";
    case 4: 
      return "sentinel[" + getSentinelData() + "]";
    case 5: 
      return "externalActivitySentinel";
    }
    return "notificationAnnouncement";
  }
  
  private static boolean isValidSelectionValue(CharSequence paramCharSequence, int paramInt)
  {
    switch (paramInt)
    {
    default: 
      if ((paramInt < 0) || (paramInt > paramCharSequence.length())) {
        break;
      }
    case -2: 
    case -1: 
      return true;
    }
    return false;
  }
  
  private Query voiceSearchWithTrigger(int paramInt, boolean paramBoolean)
  {
    return voiceSearchWithTrigger(paramInt, paramBoolean, false);
  }
  
  private Query voiceSearchWithTrigger(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    Builder localBuilder1 = buildUpon().setType(1).setFlag(1024).setTrigger(paramInt).clearFlag(512).setSelection(0);
    if (paramBoolean1) {}
    for (int i = 16384;; i = 0)
    {
      Builder localBuilder2 = localBuilder1.clearAndSetFlags(16384, i);
      int j = 0;
      if (paramBoolean2) {
        j = 262144;
      }
      return localBuilder2.clearAndSetFlags(262144, j).setRecordedAudioUri(null).build();
    }
  }
  
  public boolean canCommit()
  {
    return (!isTextSearch()) || (!getQueryStringForSearch().isEmpty());
  }
  
  public Query clearCommit()
  {
    return buildUpon().newCommitId().setSelection(-1).build();
  }
  
  public Query clearQuery()
  {
    if (isSummonsCorpus()) {}
    for (String str = this.mCorpusId;; str = "web") {
      return buildUpon().setTrigger(0).setType(0).setQueryChars("").setSelection(0).setCorpus(str, true).build();
    }
  }
  
  public Query committed()
  {
    return buildUpon().clearNonTextQuery().clearFlag(32768).clearFlag(65536).newCommitId().build();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Query externalActivitySentinel(Bundle paramBundle)
  {
    return buildUpon().setType(5).setExtras(paramBundle).build();
  }
  
  public Query forceReload()
  {
    boolean bool1 = true;
    boolean bool2;
    boolean bool3;
    if (!isMusicSearch())
    {
      bool2 = bool1;
      Preconditions.checkState(bool2);
      if (isTvSearch()) {
        break label66;
      }
      bool3 = bool1;
      label24:
      Preconditions.checkState(bool3);
      if (getQueryStringForSearch().isEmpty()) {
        break label71;
      }
    }
    for (;;)
    {
      Preconditions.checkState(bool1);
      return buildUpon().setSearchBoxStats(null).setType(0).newCommitId().build();
      bool2 = false;
      break;
      label66:
      bool3 = false;
      break label24;
      label71:
      bool1 = false;
    }
  }
  
  public Query fromAuthFailure()
  {
    if (!shouldExcludeAuthTokens()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Received an auth failure for request without tokens.");
      if (!shouldRefreshAuthTokens()) {
        break;
      }
      return buildUpon().clearFlag(32768).setFlag(65536).newCommitId().build();
    }
    return buildUpon().setFlag(32768).newCommitId().build();
  }
  
  public Query fromBackStack()
  {
    return buildUpon().setFlag(131072).setSearchBoxStats(null).clearFlag(1024).build();
  }
  
  public Query fromHistoryRefresh()
  {
    return buildUpon().setTrigger(144).build();
  }
  
  public Query fromPredictive(String paramString, Location paramLocation)
  {
    return buildUpon().setQueryChars(paramString).setSelection(paramString.length()).setType(0).setTrigger(48).setLocationOverride(paramLocation).build();
  }
  
  public Query fromPredictiveToWeb(String paramString, Location paramLocation)
  {
    return fromPredictive(paramString, paramLocation).buildUpon().setCorpus("web", false).build();
  }
  
  public Query fromPrefetch(String paramString)
  {
    return buildUpon().setQueryChars(paramString).setTrigger(32).newCommitId().build();
  }
  
  public Query fromSearchIntent(String paramString, boolean paramBoolean)
  {
    Builder localBuilder = buildUpon().setQueryChars(paramString);
    if (paramBoolean) {}
    for (int i = 0;; i = -2) {
      return localBuilder.setSelection(i, -2).setTrigger(16).setType(0).build();
    }
  }
  
  public Query fromWebView()
  {
    return buildUpon().setTrigger(64).build();
  }
  
  @Nullable
  public Bundle getAssistContext()
  {
    return this.mAssistContext;
  }
  
  @Nullable
  public String getAssistPackage()
  {
    return this.mAssistPackage;
  }
  
  public long getCommitId()
  {
    return this.mCommitId;
  }
  
  public String getCorpusId()
  {
    return this.mCorpusId;
  }
  
  public Bundle getExtras()
  {
    return this.mExtras;
  }
  
  public Location getLocationOverride()
  {
    return this.mLocationOverride;
  }
  
  @Nullable
  public ImmutableList<CharSequence> getOtherHypotheses()
  {
    return this.mOtherHypotheses;
  }
  
  @Nonnull
  public ImmutableMap<String, String> getPersistCgiParameters()
  {
    return this.mPersistCgiParameters;
  }
  
  public CharSequence getQueryChars()
  {
    return this.mQueryChars;
  }
  
  public String getQueryString()
  {
    return this.mQueryChars.toString();
  }
  
  public String getQueryStringForSearch()
  {
    if (this.mQueryChars.length() == 0) {
      return this.mQueryChars.toString();
    }
    return Whitespace.trimAndCollapseFrom(this.mQueryChars, ' ');
  }
  
  public String getQueryStringForSuggest()
  {
    if (this.mQueryChars.length() == 0) {
      return this.mQueryChars.toString();
    }
    return Whitespace.trimLeadingAndCollapseFrom(this.mQueryChars, ' ');
  }
  
  @Nullable
  public Uri getRecordedAudioUri()
  {
    return this.mRecordedAudioUri;
  }
  
  public int getResultIndex()
  {
    return this.mResultIndex;
  }
  
  @Nullable
  public SearchBoxStats getSearchBoxStats()
  {
    return this.mSearchBoxStats;
  }
  
  public int getSelectionEnd()
  {
    return this.mSelectionEnd;
  }
  
  public int getSelectionStart()
  {
    return this.mSelectionStart;
  }
  
  public Serializable getSentinelData()
  {
    Preconditions.checkState(isSentinel());
    return this.mSentinelData;
  }
  
  public boolean hasSpans(Class<?> paramClass)
  {
    return ((this.mQueryChars instanceof Spanned)) && (((Spanned)this.mQueryChars).getSpans(0, this.mQueryChars.length(), paramClass).length > 0);
  }
  
  public boolean isCarMode()
  {
    return (isEyesFree()) && ((0x40000 & this.mFlags) != 0);
  }
  
  public boolean isEmptySuggestQuery()
  {
    return TextUtils.isEmpty(getQueryStringForSuggest());
  }
  
  public boolean isExternalActivitySentinel()
  {
    return getType() == 5;
  }
  
  public boolean isEyesFree()
  {
    return (0x4000 & this.mFlags) != 0;
  }
  
  public boolean isFollowOn()
  {
    return getTrigger() == 128;
  }
  
  public boolean isFromBackStack()
  {
    return (0x20000 & this.mFlags) != 0;
  }
  
  public boolean isFromPredictive()
  {
    return getTrigger() == 48;
  }
  
  public boolean isHistoryRefreshQuery()
  {
    return getTrigger() == 144;
  }
  
  public boolean isIntentQuery()
  {
    return getTrigger() == 16;
  }
  
  public boolean isMusicSearch()
  {
    return getType() == 2;
  }
  
  public boolean isNotificationAnnouncement()
  {
    return getType() == 6;
  }
  
  public boolean isPredictiveTvSearch()
  {
    return (getType() == 3) && (getTrigger() == 48);
  }
  
  public boolean isPrefetch()
  {
    return getTrigger() == 32;
  }
  
  public boolean isQueryTextFromVoice()
  {
    return (0x800 & this.mFlags) != 0;
  }
  
  public boolean isRestoredState()
  {
    return (0x2000 & this.mFlags) != 0;
  }
  
  public boolean isRewritten()
  {
    return (0x80000 & this.mFlags) != 0;
  }
  
  public boolean isSecondarySearchQuery()
  {
    return (0x1000 & this.mFlags) != 0;
  }
  
  public boolean isSentinel()
  {
    return getType() == 4;
  }
  
  public boolean isSummonsCorpus()
  {
    return this.mCorpusId.equals("summons");
  }
  
  public boolean isTextOrVoiceWebSearchWithQueryChars()
  {
    return (!getQueryStringForSearch().isEmpty()) && ((isTextSearch()) || (isVoiceSearch())) && (!isSummonsCorpus());
  }
  
  public boolean isTextSearch()
  {
    return getType() == 0;
  }
  
  public boolean isTriggeredFromBluetoothHandsfree()
  {
    return getTrigger() == 96;
  }
  
  public boolean isTriggeredFromHotword()
  {
    return getTrigger() == 80;
  }
  
  public boolean isTriggeredFromWiredHeadset()
  {
    return getTrigger() == 112;
  }
  
  public boolean isTvSearch()
  {
    return getType() == 3;
  }
  
  public boolean isUserQuery()
  {
    return getTrigger() == 0;
  }
  
  public boolean isValidSearch()
  {
    return (isMusicSearch()) || (isTvSearch()) || (isVoiceSearch()) || ((isTextSearch()) && (!getQueryStringForSearch().isEmpty()));
  }
  
  public boolean isVoiceSearch()
  {
    return getType() == 1;
  }
  
  public boolean isWebCorpus()
  {
    return this.mCorpusId.equals("web");
  }
  
  public boolean isWebViewQuery()
  {
    return getTrigger() == 64;
  }
  
  public Query musicSearchFromAction()
  {
    return buildUpon().setType(2).build();
  }
  
  public Query musicSearchFromIntent()
  {
    return buildUpon().setType(2).setTrigger(16).build();
  }
  
  public Query musicSearchFromPromotedQuery()
  {
    return buildUpon().setQueryChars("").setSelection(0).setType(2).clearFlag(1024).build();
  }
  
  public boolean needBrowserDimensions()
  {
    return this.mCorpusId.equals("web.isch");
  }
  
  public boolean needVoiceCorrection()
  {
    return (0x100000 & this.mFlags) != 0;
  }
  
  public Query notificationAnnouncement(String paramString1, String paramString2)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("notification-message", paramString1);
    localBundle.putString("notification-sender", paramString2);
    return buildUpon().setType(6).setFlag(16384).setFlag(262144).setFlag(1024).setExtras(localBundle).build();
  }
  
  public Query predictiveTvSearch()
  {
    return buildUpon().setQueryChars("").setSelection(0).setType(3).setTrigger(48).clearFlag(1024).build();
  }
  
  public Query restoredState()
  {
    return buildUpon().setSearchBoxStats(null).setFlag(8192).build();
  }
  
  public Query sentinel(Serializable paramSerializable, Bundle paramBundle)
  {
    return buildUpon().setSentinel(paramSerializable, paramBundle).build();
  }
  
  public boolean shouldExcludeAuthTokens()
  {
    return (0x10000 & this.mFlags) != 0;
  }
  
  public boolean shouldPlayTts()
  {
    return (0x400 & this.mFlags) != 0;
  }
  
  public boolean shouldRefreshAuthTokens()
  {
    return (0x8000 & this.mFlags) != 0;
  }
  
  public boolean shouldResendLastRecording()
  {
    return (0x200 & this.mFlags) != 0;
  }
  
  public boolean shouldShowCards()
  {
    return (isWebCorpus()) && (this.mResultIndex == 0);
  }
  
  public Query text()
  {
    return buildUpon().setType(0).build();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("Query[").append(getTypeString()).append(" from ").append(getTriggerString()).append(": \"").append(this.mQueryChars).append("\"").append("/").append(this.mCorpusId).append("@").append(this.mResultIndex).append(" CID=").append(this.mCommitId).append(", sel-").append(getSelectionString(getSelectionStart())).append(":").append(getSelectionString(getSelectionEnd()));
    String str1;
    String str2;
    label155:
    String str3;
    label175:
    String str4;
    label196:
    String str5;
    label237:
    String str6;
    label258:
    String str7;
    label299:
    String str8;
    label366:
    String str9;
    label410:
    StringBuilder localStringBuilder10;
    if (shouldPlayTts())
    {
      str1 = ", play-tts";
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1);
      if (!isEyesFree()) {
        break label474;
      }
      str2 = ", eyes-free";
      StringBuilder localStringBuilder3 = localStringBuilder2.append(str2);
      if (!isCarMode()) {
        break label481;
      }
      str3 = ", car-mode";
      StringBuilder localStringBuilder4 = localStringBuilder3.append(str3);
      if (!shouldResendLastRecording()) {
        break label488;
      }
      str4 = ", resend-last-recording";
      StringBuilder localStringBuilder5 = localStringBuilder4.append(str4);
      if (this.mSearchBoxStats == null) {
        break label495;
      }
      str5 = ", client-stats:" + this.mSearchBoxStats;
      StringBuilder localStringBuilder6 = localStringBuilder5.append(str5);
      if (!isFromBackStack()) {
        break label502;
      }
      str6 = ", from-back-stack";
      StringBuilder localStringBuilder7 = localStringBuilder6.append(str6);
      if (this.mLocationOverride == null) {
        break label509;
      }
      str7 = ", location-override:" + this.mLocationOverride;
      StringBuilder localStringBuilder8 = localStringBuilder7.append(str7).append(", persist-cgi-parameters: " + this.mPersistCgiParameters);
      if (this.mAssistPackage == null) {
        break label516;
      }
      str8 = ", assist-package:" + this.mAssistPackage;
      StringBuilder localStringBuilder9 = localStringBuilder8.append(str8);
      if (this.mAssistContext == null) {
        break label523;
      }
      str9 = ", assist-context:" + Util.bundleToString(this.mAssistContext);
      localStringBuilder10 = localStringBuilder9.append(str9);
      if (this.mRecordedAudioUri == null) {
        break label530;
      }
    }
    label516:
    label523:
    label530:
    for (String str10 = ", recording-uri:" + this.mRecordedAudioUri;; str10 = "")
    {
      return str10 + "]";
      str1 = "";
      break;
      label474:
      str2 = "";
      break label155;
      label481:
      str3 = "";
      break label175;
      label488:
      str4 = "";
      break label196;
      label495:
      str5 = "";
      break label237;
      label502:
      str6 = "";
      break label258;
      label509:
      str7 = "";
      break label299;
      str8 = "";
      break label366;
      str9 = "";
      break label410;
    }
  }
  
  public Query tvSearchFromAction()
  {
    return buildUpon().setType(3).setTrigger(0).build();
  }
  
  public Query voiceSearchFollowOn()
  {
    return voiceSearchWithTrigger(128, isEyesFree(), isCarMode());
  }
  
  public Query voiceSearchFromBluetoothHeadsetButton()
  {
    return voiceSearchWithTrigger(96, true, isCarMode());
  }
  
  public Query voiceSearchFromGui()
  {
    return voiceSearchWithTrigger(0, false);
  }
  
  public Query voiceSearchFromHotword(boolean paramBoolean)
  {
    return voiceSearchWithTrigger(80, paramBoolean, false);
  }
  
  public Query voiceSearchFromWaveGesture()
  {
    return voiceSearchWithTrigger(160, true, isCarMode());
  }
  
  public Query voiceSearchFromWiredHeadsetButton()
  {
    return voiceSearchWithTrigger(112, true, false);
  }
  
  public Query voiceSearchWithLastRecording()
  {
    return buildUpon().setType(1).setFlag(512).setRecordedAudioUri(null).build();
  }
  
  Query voiceSearchWithRecognizedQuery(CharSequence paramCharSequence)
  {
    return buildUpon().setType(1).setQueryChars(paramCharSequence).build();
  }
  
  public Query voiceSearchWithRecordedAudio(Uri paramUri)
  {
    return buildUpon().setType(1).clearFlag(512).setRecordedAudioUri(paramUri).build();
  }
  
  public Query withAssistContext(String paramString, Bundle paramBundle)
  {
    return buildUpon().setAssistContext(paramString, paramBundle).build();
  }
  
  public Query withCarMode(boolean paramBoolean)
  {
    Builder localBuilder = buildUpon();
    if (paramBoolean) {}
    for (int i = 262144;; i = 0) {
      return localBuilder.clearAndSetFlags(262144, i).build();
    }
  }
  
  public Query withCommitIdFrom(Query paramQuery)
  {
    return buildUpon().setCommitId(paramQuery.getCommitId()).build();
  }
  
  public Query withCorpus(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    return buildUpon().setCorpus(paramString, true).build();
  }
  
  public Query withNewQueryChars(CharSequence paramCharSequence)
  {
    return buildUpon().setQueryChars(paramCharSequence).setSelection(0).build();
  }
  
  public Query withPersistCgiParameters(Map<String, String> paramMap)
  {
    return buildUpon().setPersistCgiParameters(paramMap).build();
  }
  
  public Query withQueryChars(CharSequence paramCharSequence)
  {
    if (SpannedCharSequences.charSequencesEqual(getQueryChars(), paramCharSequence)) {
      return this;
    }
    return buildUpon().setTrigger(0).setType(0).setQueryChars(paramCharSequence).setSelection(paramCharSequence.length()).build();
  }
  
  public Query withQueryCharsAndSelection(CharSequence paramCharSequence, int paramInt)
  {
    return withQueryCharsAndSelection(paramCharSequence, paramInt, paramInt);
  }
  
  public Query withQueryCharsAndSelection(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    Preconditions.checkArgument(isValidSelectionValue(paramCharSequence, paramInt1));
    Preconditions.checkArgument(isValidSelectionValue(paramCharSequence, paramInt2));
    if ((SpannedCharSequences.charSequencesEqual(getQueryChars(), paramCharSequence)) && (paramInt1 == getSelectionStart()) && (paramInt2 == getSelectionEnd())) {
      return this;
    }
    return buildUpon().setTrigger(0).setType(0).setQueryChars(paramCharSequence).setSelection(paramInt1, paramInt2).build();
  }
  
  public Query withQueryStringAndIndex(String paramString, int paramInt)
  {
    return buildUpon().setType(0).setTrigger(0).setQueryChars(paramString).setSelection(paramString.length()).setResultIndex(paramInt).build();
  }
  
  public Query withQueryStringCorpusIndexAndPersistCgiParameters(String paramString1, String paramString2, int paramInt, @Nonnull Map<String, String> paramMap)
  {
    return buildUpon().setType(0).setQueryChars(paramString1).setSelection(paramString1.length()).setCorpus(paramString2, true).setResultIndex(paramInt).setPersistCgiParameters(paramMap).build();
  }
  
  public Query withRecognizedText(CharSequence paramCharSequence, @Nullable ImmutableList<CharSequence> paramImmutableList, boolean paramBoolean)
  {
    Builder localBuilder = buildUpon().setQueryChars(paramCharSequence).setOtherHypotheses(paramImmutableList).setSelection(0);
    int i = 0;
    if (paramBoolean) {
      i = 1048576;
    }
    return localBuilder.setFlag(i | 0x800).setSearchBoxStats(getSearchBoxStats()).build();
  }
  
  public Query withReplacedSelection(CharSequence paramCharSequence)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this.mQueryChars.subSequence(0, this.mSelectionStart));
    localStringBuilder.append(paramCharSequence);
    localStringBuilder.append(this.mQueryChars.subSequence(this.mSelectionEnd, this.mQueryChars.length()));
    SpannableString localSpannableString;
    if ((this.mQueryChars instanceof Spanned))
    {
      Spanned localSpanned = (Spanned)this.mQueryChars;
      localSpannableString = SpannableString.valueOf(localStringBuilder);
      SpannedCharSequences.copySpansFrom(localSpanned, 0, this.mSelectionStart, SuggestionSpan.class, localSpannableString, 0);
      SpannedCharSequences.copySpansFrom(localSpanned, 0, this.mSelectionStart, CorrectionSpan.class, localSpannableString, 0);
      SpannedCharSequences.copySpansFrom(localSpanned, 0, this.mSelectionStart, VoiceCorrectionSpan.class, localSpannableString, 0);
      if ((paramCharSequence instanceof Spanned))
      {
        SpannedCharSequences.copySpansFrom((Spanned)paramCharSequence, 0, paramCharSequence.length(), SuggestionSpan.class, localSpannableString, this.mSelectionStart);
        SpannedCharSequences.copySpansFrom((Spanned)paramCharSequence, 0, paramCharSequence.length(), CorrectionSpan.class, localSpannableString, this.mSelectionStart);
        SpannedCharSequences.copySpansFrom((Spanned)paramCharSequence, 0, paramCharSequence.length(), VoiceCorrectionSpan.class, localSpannableString, this.mSelectionStart);
      }
      int i = this.mSelectionStart + paramCharSequence.length();
      SpannedCharSequences.copySpansFrom(localSpanned, this.mSelectionEnd, localSpanned.length(), SuggestionSpan.class, localSpannableString, i);
      SpannedCharSequences.copySpansFrom(localSpanned, this.mSelectionEnd, localSpanned.length(), CorrectionSpan.class, localSpannableString, i);
      SpannedCharSequences.copySpansFrom(localSpanned, this.mSelectionEnd, localSpanned.length(), VoiceCorrectionSpan.class, localSpannableString, i);
    }
    for (Object localObject = localSpannableString;; localObject = localStringBuilder.toString()) {
      return buildUpon().setQueryChars((CharSequence)localObject).setSelection(0).build();
    }
  }
  
  public Query withRewrittenQuery(CharSequence paramCharSequence)
  {
    return buildUpon().setQueryChars(paramCharSequence).setFlag(524288).build();
  }
  
  public Query withSearchBoxStats(SearchBoxStats paramSearchBoxStats)
  {
    return buildUpon().setSearchBoxStats(paramSearchBoxStats).build();
  }
  
  public Query withSecondarySearchQueryString(String paramString)
  {
    return buildUpon().setQueryChars(paramString).setSelection(paramString.length()).setFlag(4096).build();
  }
  
  public Query withoutVoiceCorrection()
  {
    return buildUpon().clearFlag(1048576).build();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.mFlags);
    SpannedCharSequences.writeToParcel(this.mQueryChars, paramParcel, paramInt);
    SpannedCharSequences.writeAllToParcel(this.mOtherHypotheses, paramParcel, paramInt);
    paramParcel.writeInt(this.mSelectionStart);
    paramParcel.writeInt(this.mSelectionEnd);
    paramParcel.writeString(this.mCorpusId);
    paramParcel.writeBundle(Util.stringMapToBundle(this.mPersistCgiParameters));
    paramParcel.writeInt(this.mResultIndex);
    paramParcel.writeLong(this.mCommitId);
    paramParcel.writeParcelable(this.mSearchBoxStats, 0);
    paramParcel.writeParcelable(this.mLocationOverride, 0);
    paramParcel.writeSerializable(this.mSentinelData);
    paramParcel.writeBundle(this.mExtras);
    paramParcel.writeString(this.mAssistPackage);
    paramParcel.writeBundle(this.mAssistContext);
    paramParcel.writeParcelable(this.mRecordedAudioUri, 0);
  }
  
  private static class Builder
  {
    private Bundle mAssistContext;
    private String mAssistPackage;
    private boolean mChanged;
    private long mCommitId;
    private String mCorpusId;
    private Bundle mExtras;
    private int mFlags;
    private Location mLocationOverride;
    private ImmutableList<CharSequence> mOtherHypotheses;
    @Nonnull
    private Map<String, String> mPersistCgiParameters;
    private Query mQuery;
    private CharSequence mQueryChars;
    private Uri mRecordedAudioUri;
    private int mResultIndex;
    private SearchBoxStats mSearchBoxStats;
    private int mSelectionEnd;
    private int mSelectionStart;
    private Serializable mSentinelData;
    
    private Builder clearAndSetFlags(int paramInt1, int paramInt2)
    {
      int i = paramInt2 | this.mFlags & (paramInt1 ^ 0xFFFFFFFF);
      boolean bool1 = this.mChanged;
      if (i != this.mFlags) {}
      for (boolean bool2 = true;; bool2 = false)
      {
        this.mChanged = (bool2 | bool1);
        this.mFlags = i;
        return this;
      }
    }
    
    private void clearStateOnQueryChange()
    {
      clearAndSetFlags(765952, 0);
      this.mSelectionStart = -2;
      this.mSelectionEnd = -2;
      this.mResultIndex = 0;
      this.mLocationOverride = null;
      this.mPersistCgiParameters = null;
      this.mOtherHypotheses = null;
      this.mSearchBoxStats = null;
    }
    
    private void validate()
    {
      boolean bool1 = Query.isValidSelectionValue(this.mQueryChars, this.mSelectionStart);
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = Integer.valueOf(this.mQueryChars.length());
      arrayOfObject1[1] = Integer.valueOf(this.mSelectionStart);
      Preconditions.checkState(bool1, "Query has length %s but selection start is %s", arrayOfObject1);
      boolean bool2 = Query.isValidSelectionValue(this.mQueryChars, this.mSelectionEnd);
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = Integer.valueOf(this.mQueryChars.length());
      arrayOfObject2[1] = Integer.valueOf(this.mSelectionEnd);
      Preconditions.checkState(bool2, "Query has length %s but selection end is %s", arrayOfObject2);
    }
    
    Query build()
    {
      if (this.mChanged)
      {
        validate();
        return new Query(this.mFlags, this.mQueryChars, this.mOtherHypotheses, this.mSelectionStart, this.mSelectionEnd, this.mCorpusId, this.mResultIndex, this.mPersistCgiParameters, this.mCommitId, this.mSearchBoxStats, this.mLocationOverride, this.mSentinelData, this.mExtras, this.mAssistPackage, this.mAssistContext, this.mRecordedAudioUri, null);
      }
      return this.mQuery;
    }
    
    Builder buildUpon(Query paramQuery)
    {
      this.mQuery = paramQuery;
      this.mFlags = paramQuery.mFlags;
      this.mQueryChars = paramQuery.mQueryChars;
      this.mOtherHypotheses = paramQuery.mOtherHypotheses;
      this.mSelectionStart = paramQuery.mSelectionStart;
      this.mSelectionEnd = paramQuery.mSelectionEnd;
      this.mCorpusId = paramQuery.mCorpusId;
      this.mCommitId = paramQuery.mCommitId;
      this.mResultIndex = paramQuery.mResultIndex;
      this.mPersistCgiParameters = paramQuery.mPersistCgiParameters;
      this.mSearchBoxStats = paramQuery.mSearchBoxStats;
      this.mLocationOverride = paramQuery.mLocationOverride;
      this.mSentinelData = paramQuery.mSentinelData;
      this.mExtras = paramQuery.mExtras;
      this.mAssistPackage = paramQuery.mAssistPackage;
      this.mAssistContext = paramQuery.mAssistContext;
      this.mRecordedAudioUri = paramQuery.mRecordedAudioUri;
      this.mChanged = false;
      return this;
    }
    
    Builder clearFlag(int paramInt)
    {
      return clearAndSetFlags(paramInt, 0);
    }
    
    Builder clearNonTextQuery()
    {
      if ((0xF & this.mFlags) != 0)
      {
        this.mQueryChars = "";
        this.mSelectionStart = 0;
        this.mSelectionEnd = 0;
      }
      return this;
    }
    
    Builder newCommitId()
    {
      this.mCommitId = Query.sLatestCommitId.incrementAndGet();
      this.mChanged = true;
      return this;
    }
    
    Builder setAssistContext(String paramString, Bundle paramBundle)
    {
      boolean bool1 = this.mChanged;
      if ((!TextUtils.equals(paramString, this.mAssistPackage)) || (paramBundle != this.mAssistContext)) {}
      for (boolean bool2 = true;; bool2 = false)
      {
        this.mChanged = (bool2 | bool1);
        this.mAssistPackage = paramString;
        this.mAssistContext = paramBundle;
        return this;
      }
    }
    
    Builder setCommitId(long paramLong)
    {
      if (this.mCommitId != paramLong)
      {
        this.mCommitId = paramLong;
        this.mChanged = true;
      }
      return this;
    }
    
    Builder setCorpus(String paramString, boolean paramBoolean)
    {
      if (!TextUtils.equals(this.mCorpusId, paramString))
      {
        this.mChanged = true;
        clearStateOnQueryChange();
        if (paramBoolean) {
          setType(0);
        }
      }
      this.mCorpusId = paramString;
      return this;
    }
    
    Builder setExtras(Bundle paramBundle)
    {
      boolean bool1 = this.mChanged;
      if (this.mExtras != paramBundle) {}
      for (boolean bool2 = true;; bool2 = false)
      {
        this.mChanged = (bool2 | bool1);
        this.mExtras = paramBundle;
        return this;
      }
    }
    
    Builder setFlag(int paramInt)
    {
      return clearAndSetFlags(0, paramInt);
    }
    
    Builder setLocationOverride(Location paramLocation)
    {
      boolean bool1 = this.mChanged;
      if (!Objects.equal(paramLocation, this.mLocationOverride)) {}
      for (boolean bool2 = true;; bool2 = false)
      {
        this.mChanged = (bool2 | bool1);
        this.mLocationOverride = paramLocation;
        return this;
      }
    }
    
    Builder setOtherHypotheses(ImmutableList<CharSequence> paramImmutableList)
    {
      if (!Objects.equal(paramImmutableList, this.mOtherHypotheses))
      {
        this.mOtherHypotheses = paramImmutableList;
        this.mChanged = true;
      }
      return this;
    }
    
    Builder setPersistCgiParameters(@Nonnull Map<String, String> paramMap)
    {
      boolean bool1 = this.mChanged;
      if (!paramMap.equals(this.mPersistCgiParameters)) {}
      for (boolean bool2 = true;; bool2 = false)
      {
        this.mChanged = (bool2 | bool1);
        this.mPersistCgiParameters = paramMap;
        return this;
      }
    }
    
    Builder setQueryChars(CharSequence paramCharSequence)
    {
      if (!SpannedCharSequences.charSequencesEqual(paramCharSequence, this.mQueryChars)) {}
      for (int i = 1;; i = 0)
      {
        this.mChanged = (i | this.mChanged);
        if (i != 0)
        {
          if ((0xF & this.mFlags) != 1) {
            clearAndSetFlags(2048, 0);
          }
          clearStateOnQueryChange();
        }
        this.mQueryChars = SpannedCharSequences.immutableValueOf(paramCharSequence);
        return this;
      }
    }
    
    public Builder setRecordedAudioUri(@Nullable Uri paramUri)
    {
      boolean bool1 = this.mChanged;
      if (!Objects.equal(paramUri, this.mRecordedAudioUri)) {}
      for (boolean bool2 = true;; bool2 = false)
      {
        this.mChanged = (bool2 | bool1);
        this.mRecordedAudioUri = paramUri;
        return this;
      }
    }
    
    Builder setResultIndex(int paramInt)
    {
      if (paramInt != this.mResultIndex)
      {
        clearStateOnQueryChange();
        setType(0);
        this.mResultIndex = paramInt;
        this.mChanged = true;
      }
      return this;
    }
    
    Builder setSearchBoxStats(SearchBoxStats paramSearchBoxStats)
    {
      boolean bool1 = this.mChanged;
      if (this.mSearchBoxStats != paramSearchBoxStats) {}
      for (boolean bool2 = true;; bool2 = false)
      {
        this.mChanged = (bool2 | bool1);
        this.mSearchBoxStats = paramSearchBoxStats;
        return this;
      }
    }
    
    Builder setSelection(int paramInt)
    {
      return setSelection(paramInt, paramInt);
    }
    
    Builder setSelection(int paramInt1, int paramInt2)
    {
      if ((paramInt1 != this.mSelectionStart) || (paramInt2 != this.mSelectionEnd))
      {
        this.mSelectionStart = paramInt1;
        this.mSelectionEnd = paramInt2;
        this.mChanged = true;
      }
      return this;
    }
    
    Builder setSentinel(Serializable paramSerializable, Bundle paramBundle)
    {
      clearAndSetFlags(15, 4);
      boolean bool1 = this.mChanged;
      if ((this.mSentinelData != paramSerializable) || (this.mExtras != paramBundle)) {}
      for (boolean bool2 = true;; bool2 = false)
      {
        this.mChanged = (bool2 | bool1);
        this.mSentinelData = paramSerializable;
        this.mExtras = paramBundle;
        return this;
      }
    }
    
    Builder setTrigger(int paramInt)
    {
      return clearAndSetFlags(240, paramInt);
    }
    
    Builder setType(int paramInt)
    {
      if (paramInt != (0xF & this.mFlags))
      {
        clearAndSetFlags(8719, paramInt);
        if (paramInt != 1) {
          break label67;
        }
        setFlag(1024);
      }
      for (;;)
      {
        if ((paramInt == 1) || (paramInt == 2) || (paramInt == 3))
        {
          this.mCorpusId = "web";
          this.mPersistCgiParameters = null;
        }
        this.mSentinelData = null;
        return this;
        label67:
        if (paramInt == 0) {
          clearFlag(1024);
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.api.Query
 * JD-Core Version:    0.7.0.1
 */