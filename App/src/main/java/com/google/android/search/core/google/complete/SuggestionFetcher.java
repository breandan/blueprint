package com.google.android.search.core.google.complete;

import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.google.gaia.LoginHelper.AuthToken;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.search.core.util.HttpHelper.HttpException;
import com.google.android.search.shared.api.Query;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class SuggestionFetcher
{
  @Nonnull
  protected final SearchConfig mConfig;
  @Nonnull
  protected final HttpHelper mHttpHelper;
  @Nullable
  private final LoginHelper mLoginHelper;
  
  SuggestionFetcher(SearchConfig paramSearchConfig, HttpHelper paramHttpHelper, LoginHelper paramLoginHelper)
  {
    this.mConfig = ((SearchConfig)Preconditions.checkNotNull(paramSearchConfig));
    this.mHttpHelper = ((HttpHelper)Preconditions.checkNotNull(paramHttpHelper));
    this.mLoginHelper = paramLoginHelper;
  }
  
  protected LoginHelper.AuthToken addAuthHeader(Map<String, String> paramMap)
  {
    LoginHelper.AuthToken localAuthToken = getAuthToken();
    if (localAuthToken != null) {
      paramMap.put("Authorization", "GoogleLogin auth=" + localAuthToken.getToken());
    }
    return localAuthToken;
  }
  
  protected Uri.Builder buildBaseUri(String paramString, boolean paramBoolean)
  {
    Uri.Builder localBuilder = new Uri.Builder();
    if (paramBoolean) {}
    for (String str = "https";; str = "http") {
      return localBuilder.scheme(str).authority(this.mConfig.getCompleteServerDomainName()).path(paramString).appendQueryParameter("client", this.mConfig.getCompleteServerClientId());
    }
  }
  
  @Nullable
  public abstract SuggestionResponse fetch(Query paramQuery, boolean paramBoolean);
  
  protected String getAccountName()
  {
    if (this.mLoginHelper == null) {
      return null;
    }
    return this.mLoginHelper.getAccountName();
  }
  
  protected LoginHelper.AuthToken getAuthToken()
  {
    return getAuthToken("mobilepersonalfeeds");
  }
  
  protected LoginHelper.AuthToken getAuthToken(String paramString)
  {
    LoginHelper.AuthToken localAuthToken;
    if (this.mLoginHelper == null) {
      localAuthToken = null;
    }
    do
    {
      return localAuthToken;
      localAuthToken = this.mLoginHelper.blockingGetAuthTokenForAccount(this.mLoginHelper.getAccount(), paramString, 1000L);
    } while (localAuthToken != null);
    return null;
  }
  
  public boolean removeFromHistory(String paramString)
  {
    Uri.Builder localBuilder = buildBaseUri(this.mConfig.getRemoveHistoryPath(), true);
    localBuilder.appendQueryParameter("delq", paramString);
    localBuilder.appendQueryParameter("callback", "google.sbox.d0");
    HashMap localHashMap = Maps.newHashMap();
    if (addAuthHeader(localHashMap) == null) {
      return false;
    }
    try
    {
      HttpHelper.GetRequest localGetRequest = new HttpHelper.GetRequest(localBuilder.build().toString(), localHashMap);
      localGetRequest.setUseCaches(false);
      this.mHttpHelper.get(localGetRequest, 2);
      return true;
    }
    catch (IOException localIOException)
    {
      return false;
    }
    catch (HttpHelper.HttpException localHttpException) {}
    return false;
  }
  
  public static class SuggestionResponse
  {
    @Nullable
    public final String mAccountUsed;
    @Nonnull
    public final String mEventId;
    public String mJson;
    
    public SuggestionResponse(String paramString1, String paramString2, String paramString3)
    {
      this.mEventId = paramString1;
      this.mJson = paramString2;
      this.mAccountUsed = paramString3;
    }
    
    public String toString()
    {
      return "SuggestionResponse{mEventId:" + this.mEventId + ", account:" + this.mAccountUsed + ", json:" + this.mJson + "}";
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.complete.SuggestionFetcher
 * JD-Core Version:    0.7.0.1
 */