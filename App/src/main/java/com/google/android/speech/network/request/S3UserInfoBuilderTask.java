package com.google.android.speech.network.request;

import android.location.Location;
import android.util.Log;
import com.google.android.search.core.google.XGeoEncoder;
import com.google.android.speech.SpeechSettings;
import com.google.android.speech.helper.AuthTokenHelper;
import com.google.android.speech.helper.SpeechLocationHelper;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.speech.s3.S3.AuthToken;
import com.google.speech.s3.S3.Locale;
import com.google.speech.s3.S3.S3UserInfo;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class S3UserInfoBuilderTask
  extends BaseRequestBuilderTask<S3.S3UserInfo>
{
  private final AuthTokenHelper mAuthTokenHelper;
  private final SpeechLocationHelper mLocationHelper;
  private final Location mLocationOverride;
  private final S3.S3UserInfo mS3UserInfo;
  private final SpeechSettings mSpeechSettings;
  private final String mSpokenLocale;
  
  S3UserInfoBuilderTask(AuthTokenHelper paramAuthTokenHelper, SpeechSettings paramSpeechSettings, SpeechLocationHelper paramSpeechLocationHelper, String paramString, Location paramLocation, S3.S3UserInfo paramS3UserInfo)
  {
    super("S3UserInfoBuilderTask");
    this.mAuthTokenHelper = paramAuthTokenHelper;
    this.mSpeechSettings = paramSpeechSettings;
    this.mLocationHelper = paramSpeechLocationHelper;
    this.mSpokenLocale = paramString;
    this.mLocationOverride = paramLocation;
    this.mS3UserInfo = paramS3UserInfo;
  }
  
  private void addAuthTokens(String paramString, S3.S3UserInfo paramS3UserInfo)
  {
    Collection localCollection = this.mAuthTokenHelper.blockingGetAllTokens(paramString, 1000L);
    if (localCollection != null)
    {
      Iterator localIterator = localCollection.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        paramS3UserInfo.addAuthToken(new S3.AuthToken().setName(maybeStripOAuthPrefix(paramString)).setToken(str));
      }
    }
    Log.w("VS.S3UserInfoBuilderTask", "Failed fetching auth.");
  }
  
  private S3.S3UserInfo buildNewS3UserInfo()
  {
    S3.S3UserInfo localS3UserInfo = new S3.S3UserInfo().setInstallId(this.mSpeechSettings.getInstallId()).setUserLocale(new S3.Locale().setLocale(Locale.getDefault().toString()).setFormat(2));
    addAuthTokens(this.mSpeechSettings.getVoiceSearchTokenType(), localS3UserInfo);
    String str;
    if (this.mLocationHelper.shouldSendLocation())
    {
      str = this.mLocationHelper.getXGeoLocation();
      if (this.mLocationOverride != null) {
        localS3UserInfo.setXGeoLocation(XGeoEncoder.createHeader(false, this.mLocationOverride, null));
      }
    }
    for (;;)
    {
      localS3UserInfo.setSpokenLanguage(new S3.Locale().setLocale(this.mSpokenLocale).setFormat(1));
      return localS3UserInfo;
      if (str != null)
      {
        localS3UserInfo.setXGeoLocation(str);
      }
      else
      {
        localS3UserInfo.setXGeoLocation("w ");
        continue;
        localS3UserInfo.setUsePreciseGeolocation(false);
      }
    }
  }
  
  public static Callable<S3.S3UserInfo> getAuthTokenRefreshingBuilder(@Nonnull AuthTokenHelper paramAuthTokenHelper, @Nonnull S3.S3UserInfo paramS3UserInfo, @Nonnull SpeechSettings paramSpeechSettings)
  {
    return new S3UserInfoBuilderTask(paramAuthTokenHelper, paramSpeechSettings, null, null, null, paramS3UserInfo);
  }
  
  public static Callable<S3.S3UserInfo> getBuilder(@Nonnull AuthTokenHelper paramAuthTokenHelper, @Nonnull SpeechSettings paramSpeechSettings, @Nonnull SpeechLocationHelper paramSpeechLocationHelper, @Nonnull String paramString, @Nullable Location paramLocation)
  {
    return new S3UserInfoBuilderTask(paramAuthTokenHelper, paramSpeechSettings, paramSpeechLocationHelper, paramString, paramLocation, null);
  }
  
  private static String maybeStripOAuthPrefix(String paramString)
  {
    if (paramString.startsWith("oauth2:")) {
      paramString = paramString.substring("oauth2:".length());
    }
    return paramString;
  }
  
  private S3.S3UserInfo refreshS3UserInfo()
  {
    Iterator localIterator = this.mS3UserInfo.getAuthTokenList().iterator();
    while (localIterator.hasNext())
    {
      S3.AuthToken localAuthToken = (S3.AuthToken)localIterator.next();
      this.mAuthTokenHelper.invalidateToken(localAuthToken.getToken());
    }
    S3.S3UserInfo localS3UserInfo = new S3.S3UserInfo();
    try
    {
      localS3UserInfo.mergeFrom(this.mS3UserInfo.toByteArray());
      localS3UserInfo.clearAuthToken();
      addAuthTokens(this.mSpeechSettings.getVoiceSearchTokenType(), localS3UserInfo);
      return localS3UserInfo;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      for (;;)
      {
        Log.e("VS.S3UserInfoBuilderTask", "Invalid s3UserInfo: " + localInvalidProtocolBufferMicroException.getMessage());
        localS3UserInfo.setUserLocale(new S3.Locale().setLocale(Locale.getDefault().toString()).setFormat(2));
        localS3UserInfo.setUsePreciseGeolocation(false);
      }
    }
  }
  
  protected S3.S3UserInfo build()
  {
    if (this.mS3UserInfo == null) {
      return buildNewS3UserInfo();
    }
    return refreshS3UserInfo();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.network.request.S3UserInfoBuilderTask
 * JD-Core Version:    0.7.0.1
 */