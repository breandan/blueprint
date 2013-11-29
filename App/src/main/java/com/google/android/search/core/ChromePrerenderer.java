package com.google.android.search.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.search.core.util.JsonUtf8Reader;
import com.google.android.shared.util.Util;
import com.google.common.collect.ImmutableList;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChromePrerenderer
{
  private final AtomicBoolean mAttached = new AtomicBoolean();
  private final Executor mBackgroundExecutor;
  private final Context mContext;
  private final SearchConfig mSearchConfig;
  private final ServiceCommunicator mServiceCommunicator;
  
  public ChromePrerenderer(Context paramContext, SearchConfig paramSearchConfig, Executor paramExecutor)
  {
    this(paramContext, paramSearchConfig, paramExecutor, new ServiceCommunicator(paramContext));
  }
  
  ChromePrerenderer(Context paramContext, SearchConfig paramSearchConfig, Executor paramExecutor, ServiceCommunicator paramServiceCommunicator)
  {
    this.mContext = paramContext;
    this.mSearchConfig = paramSearchConfig;
    this.mBackgroundExecutor = paramExecutor;
    this.mServiceCommunicator = paramServiceCommunicator;
  }
  
  private String getDefaultWebIntentsApp()
  {
    return getUriHandledPackageName("http://nonexistingurl.google.com");
  }
  
  private String getUriHandledPackageName(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
    ResolveInfo localResolveInfo = this.mContext.getPackageManager().resolveActivity(localIntent, 65536);
    if ((localResolveInfo == null) || (localResolveInfo.activityInfo == null)) {
      return null;
    }
    return localResolveInfo.activityInfo.packageName;
  }
  
  private boolean isChromePackageName(String paramString)
  {
    return ("com.android.chrome".equals(paramString)) || ("com.google.android.apps.chrome_dev".equals(paramString)) || ("com.chrome.beta".equals(paramString));
  }
  
  private boolean isPrerenderEnabled()
  {
    return (Feature.PRERENDER_IN_CHROME.isEnabled()) || (this.mSearchConfig.shouldUseChromePrerender());
  }
  
  private boolean isUriHandledByChrome(String paramString)
  {
    return isChromePackageName(getUriHandledPackageName(paramString));
  }
  
  private List<String> parseJsonUrls(String paramString)
  {
    if ("[]".equals(paramString)) {
      return ImmutableList.of();
    }
    ArrayList localArrayList = new ArrayList();
    JsonUtf8Reader localJsonUtf8Reader = new JsonUtf8Reader(new ByteArrayInputStream(paramString.getBytes(Util.UTF_8)));
    try
    {
      localJsonUtf8Reader.beginArray();
      while (localJsonUtf8Reader.hasNext()) {
        localArrayList.add(localJsonUtf8Reader.nextString());
      }
      localJsonUtf8Reader.endArray();
    }
    catch (IOException localIOException)
    {
      Log.e("ChromePrerenderer", "Unable to parse the prerender hints");
      return ImmutableList.of();
    }
    return localArrayList;
  }
  
  private void prerenderUrl(String paramString)
  {
    if (!isUriHandledByChrome(paramString)) {
      return;
    }
    try
    {
      this.mServiceCommunicator.sendMessage(paramString);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("ChromePrerenderer", "Unable to send prerender request to chrome");
    }
  }
  
  public void attach()
  {
    this.mAttached.set(true);
    this.mBackgroundExecutor.execute(new Runnable()
    {
      public void run()
      {
        if ((ChromePrerenderer.this.mAttached.get()) && (ChromePrerenderer.this.isPrerenderEnabled()))
        {
          String str = ChromePrerenderer.this.getDefaultWebIntentsApp();
          if (ChromePrerenderer.this.isChromePackageName(str)) {
            ChromePrerenderer.this.mServiceCommunicator.bindService(str);
          }
        }
      }
    });
  }
  
  public void detach()
  {
    this.mAttached.set(false);
    this.mBackgroundExecutor.execute(new Runnable()
    {
      public void run()
      {
        if (!ChromePrerenderer.this.mAttached.get()) {
          ChromePrerenderer.this.mServiceCommunicator.unBindService();
        }
      }
    });
  }
  
  public void prerenderJsonUrls(final String paramString)
  {
    this.mBackgroundExecutor.execute(new Runnable()
    {
      public void run()
      {
        if (ChromePrerenderer.this.mAttached.get())
        {
          Iterator localIterator = ChromePrerenderer.this.parseJsonUrls(paramString).iterator();
          while (localIterator.hasNext())
          {
            String str = (String)localIterator.next();
            ChromePrerenderer.this.prerenderUrl(str);
          }
        }
      }
    });
  }
  
  public static class ServiceCommunicator
  {
    private final AtomicBoolean mBound = new AtomicBoolean();
    private final ServiceConnection mConnection = new ServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        ChromePrerenderer.ServiceCommunicator.access$702(ChromePrerenderer.ServiceCommunicator.this, new Messenger(paramAnonymousIBinder));
        ChromePrerenderer.ServiceCommunicator.this.mBound.set(true);
      }
      
      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
        ChromePrerenderer.ServiceCommunicator.access$702(ChromePrerenderer.ServiceCommunicator.this, null);
        ChromePrerenderer.ServiceCommunicator.this.mBound.set(false);
      }
    };
    private final Context mContext;
    private Messenger mService = null;
    
    public ServiceCommunicator(Context paramContext)
    {
      this.mContext = paramContext;
    }
    
    public void bindService(String paramString)
    {
      Intent localIntent;
      if (!this.mBound.get())
      {
        localIntent = new Intent();
        localIntent.setClassName(paramString, "com.google.android.apps.chrome.ChromeBrowserPrerenderService");
      }
      try
      {
        boolean bool2 = this.mContext.bindService(localIntent, this.mConnection, 1);
        bool1 = bool2;
      }
      catch (SecurityException localSecurityException)
      {
        for (;;)
        {
          boolean bool1 = false;
        }
      }
      if (!bool1) {}
    }
    
    public void sendMessage(String paramString)
      throws RemoteException
    {
      if (this.mBound.get())
      {
        Bundle localBundle = new Bundle();
        localBundle.putString("url_to_preprender", paramString);
        Message localMessage = Message.obtain(null, 1, 0, 0);
        localMessage.setData(localBundle);
        this.mService.send(localMessage);
      }
    }
    
    public void unBindService()
    {
      if (this.mBound.get())
      {
        this.mContext.unbindService(this.mConnection);
        this.mBound.set(false);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.ChromePrerenderer
 * JD-Core Version:    0.7.0.1
 */