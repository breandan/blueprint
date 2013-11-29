package com.google.android.search.core.google.gaia;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.UserRecoverableNotifiedException;
import com.google.android.search.core.SearchConfig;
import com.google.android.shared.util.Clock;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class CachingGoogleAuthAdapter
  implements GoogleAuthAdapter
{
  private final TokenCache mTokenCache;
  private final GoogleAuthAdapter mWrapped;
  
  public CachingGoogleAuthAdapter(SearchConfig paramSearchConfig, Clock paramClock, GoogleAuthAdapter paramGoogleAuthAdapter)
  {
    this.mWrapped = paramGoogleAuthAdapter;
    this.mTokenCache = new TokenCache(paramSearchConfig, paramClock);
  }
  
  public String getTokenWithNotification(Context paramContext, String paramString1, String paramString2, Bundle paramBundle)
    throws UserRecoverableNotifiedException, IOException, GoogleAuthException
  {
    if (!paramString2.startsWith("weblogin:"))
    {
      String str2 = this.mTokenCache.get(paramString2, paramString1);
      if (str2 != null) {
        return str2;
      }
    }
    try
    {
      String str1 = this.mWrapped.getTokenWithNotification(paramContext, paramString1, paramString2, paramBundle);
      if (!paramString2.startsWith("weblogin:")) {
        this.mTokenCache.put(paramString2, paramString1, str1);
      }
      return str1;
    }
    catch (UserRecoverableNotifiedException localUserRecoverableNotifiedException)
    {
      if (!paramString2.startsWith("weblogin:")) {
        this.mTokenCache.put(paramString2, paramString1, localUserRecoverableNotifiedException);
      }
      throw localUserRecoverableNotifiedException;
    }
  }
  
  public void invalidateToken(Context paramContext, String paramString)
  {
    this.mTokenCache.remove(paramString);
    this.mWrapped.invalidateToken(paramContext, paramString);
  }
  
  public void phoneCredentialsUpdated()
  {
    this.mTokenCache.clearCache();
  }
  
  private static class TokenCache
  {
    private final Clock mClock;
    private final SearchConfig mConfig;
    private final Object mLock = new Object();
    private final Map<String, Token> mTokenValuesLookup;
    private final Map<String, Map<String, Token>> mTokensCache;
    
    public TokenCache(SearchConfig paramSearchConfig, Clock paramClock)
    {
      this.mConfig = paramSearchConfig;
      this.mClock = paramClock;
      this.mTokensCache = Maps.newHashMap();
      this.mTokenValuesLookup = Maps.newHashMap();
    }
    
    private void clearCache()
    {
      synchronized (this.mLock)
      {
        this.mTokensCache.clear();
        this.mTokenValuesLookup.clear();
        return;
      }
    }
    
    private long getTokenTtl(Token paramToken, long paramLong)
    {
      return paramToken.getTimeStamp() + TimeUnit.SECONDS.toMillis(this.mConfig.getAuthTokenCacheTtl()) - paramLong;
    }
    
    public String get(String paramString1, String paramString2)
      throws UserRecoverableNotifiedException
    {
      synchronized (this.mLock)
      {
        Map localMap = (Map)this.mTokensCache.get(paramString1);
        if (localMap != null)
        {
          Token localToken = (Token)localMap.get(paramString2);
          if ((localToken != null) && (getTokenTtl(localToken, this.mClock.uptimeMillis()) > 0L))
          {
            String str = localToken.getTokenValue();
            return str;
          }
        }
        return null;
      }
    }
    
    public void put(String paramString1, String paramString2, UserRecoverableNotifiedException paramUserRecoverableNotifiedException)
    {
      Token localToken = new Token(paramString2, paramString1, this.mClock.uptimeMillis(), paramUserRecoverableNotifiedException);
      synchronized (this.mLock)
      {
        Object localObject3 = (Map)this.mTokensCache.get(paramString1);
        if (localObject3 == null)
        {
          localObject3 = Maps.newHashMap();
          this.mTokensCache.put(paramString1, localObject3);
        }
        ((Map)localObject3).put(paramString2, localToken);
        return;
      }
    }
    
    public void put(String paramString1, String paramString2, String paramString3)
    {
      Token localToken = new Token(paramString2, paramString1, this.mClock.uptimeMillis(), paramString3);
      synchronized (this.mLock)
      {
        Object localObject3 = (Map)this.mTokensCache.get(paramString1);
        if (localObject3 == null)
        {
          localObject3 = Maps.newHashMap();
          this.mTokensCache.put(paramString1, localObject3);
        }
        ((Map)localObject3).put(paramString2, localToken);
        this.mTokenValuesLookup.put(paramString3, localToken);
        return;
      }
    }
    
    public void remove(String paramString)
    {
      synchronized (this.mLock)
      {
        Token localToken = (Token)this.mTokenValuesLookup.get(paramString);
        if (localToken != null)
        {
          Map localMap = (Map)this.mTokensCache.get(localToken.getScope());
          if (localMap != null) {
            localMap.remove(localToken.getAccountName());
          }
        }
        return;
      }
    }
    
    private static class Token
    {
      private final String mAccountName;
      private final String mScope;
      private final long mTimeStamp;
      @Nullable
      private final UserRecoverableNotifiedException mTokenError;
      @Nullable
      private final String mTokenValue;
      
      Token(String paramString1, String paramString2, long paramLong, UserRecoverableNotifiedException paramUserRecoverableNotifiedException)
      {
        this.mAccountName = paramString1;
        this.mScope = paramString2;
        this.mTimeStamp = paramLong;
        this.mTokenValue = null;
        this.mTokenError = paramUserRecoverableNotifiedException;
      }
      
      Token(String paramString1, String paramString2, long paramLong, String paramString3)
      {
        this.mAccountName = paramString1;
        this.mScope = paramString2;
        this.mTimeStamp = paramLong;
        this.mTokenValue = paramString3;
        this.mTokenError = null;
      }
      
      String getAccountName()
      {
        return this.mAccountName;
      }
      
      String getScope()
      {
        return this.mScope;
      }
      
      long getTimeStamp()
      {
        return this.mTimeStamp;
      }
      
      String getTokenValue()
        throws UserRecoverableNotifiedException
      {
        if (this.mTokenError != null) {
          throw this.mTokenError;
        }
        return this.mTokenValue;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.gaia.CachingGoogleAuthAdapter
 * JD-Core Version:    0.7.0.1
 */