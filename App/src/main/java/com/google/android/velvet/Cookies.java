package com.google.android.velvet;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cookies
{
  private final Context mContext;
  private CookieManager mCookieMan;
  private final Lock mInitializeLock = new ReentrantLock();
  private CookieSyncManager mSyncMan;
  
  private Cookies(Context paramContext)
  {
    this.mContext = paramContext;
  }
  
  public static Cookies create(Context paramContext)
  {
    return new Cookies(paramContext);
  }
  
  private boolean maybeInitialize()
  {
    
    try
    {
      boolean bool = this.mInitializeLock.tryLock(10L, TimeUnit.SECONDS);
      if (bool) {
        try
        {
          if (this.mSyncMan == null)
          {
            CookieSyncManager.createInstance(this.mContext);
            this.mSyncMan = CookieSyncManager.getInstance();
            this.mCookieMan = CookieManager.getInstance();
          }
          return true;
        }
        finally
        {
          this.mInitializeLock.unlock();
        }
      }
      BugLogger.record(10362986);
    }
    catch (InterruptedException localInterruptedException)
    {
      Thread.currentThread().interrupt();
      return false;
    }
    Log.w("Cookies", "Omitting cookies because initialize lock timed out");
    return false;
  }
  
  public String getCookie(String paramString)
  {
    if (maybeInitialize()) {
      return this.mCookieMan.getCookie(paramString);
    }
    return "";
  }
  
  public void removeAllCookies()
  {
    if (maybeInitialize()) {
      this.mCookieMan.removeAllCookie();
    }
  }
  
  public void setCookiesFromHeaders(String paramString, Map<String, List<String>> paramMap)
  {
    Preconditions.checkNotNull(paramString);
    Preconditions.checkNotNull(paramMap);
    List localList = (List)paramMap.get("Set-Cookie");
    if ((localList != null) && (maybeInitialize()))
    {
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        this.mCookieMan.setCookie(paramString, str);
      }
    }
  }
  
  public void sync()
  {
    maybeInitialize();
    this.mSyncMan.sync();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.Cookies
 * JD-Core Version:    0.7.0.1
 */