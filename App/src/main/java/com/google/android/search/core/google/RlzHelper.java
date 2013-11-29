package com.google.android.search.core.google;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.google.android.search.core.SearchConfig;
import com.google.android.velvet.ActivityLifecycleNotifier;
import com.google.android.velvet.ActivityLifecycleObserver;
import java.util.concurrent.Executor;

public class RlzHelper
  implements ActivityLifecycleObserver
{
  private final Uri mBaseUri;
  private final Executor mBgThread;
  private boolean mContentObserverRegistered;
  private final Context mContext;
  private final Runnable mGetRlzTask = new Runnable()
  {
    public void run()
    {
      RlzHelper.this.getRlz(false);
    }
  };
  private final Runnable mPeekRlzTask = new Runnable()
  {
    public void run()
    {
      RlzHelper.this.getRlz(true);
    }
  };
  private volatile String mRlz;
  private ContentObserver mRlzObserver;
  
  public RlzHelper(ActivityLifecycleNotifier paramActivityLifecycleNotifier, Executor paramExecutor, Context paramContext, SearchConfig paramSearchConfig)
  {
    paramActivityLifecycleNotifier.addActivityLifecycleObserver(this);
    this.mBgThread = paramExecutor;
    this.mContext = paramContext;
    this.mBaseUri = Uri.withAppendedPath(paramSearchConfig.getRlzProviderUri(), paramContext.getResources().getString(2131361998));
    this.mBgThread.execute(this.mPeekRlzTask);
  }
  
  protected void getRlz(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (Uri localUri = Uri.withAppendedPath(this.mBaseUri, "peek");; localUri = this.mBaseUri)
    {
      localCursor = null;
      try
      {
        localCursor = this.mContext.getContentResolver().query(localUri, null, null, null, null);
        localObject2 = null;
        if (localCursor != null)
        {
          boolean bool1 = localCursor.moveToFirst();
          localObject2 = null;
          if (bool1)
          {
            boolean bool2 = localCursor.isNull(0);
            localObject2 = null;
            if (!bool2)
            {
              String str = localCursor.getString(0);
              localObject2 = str;
            }
          }
        }
      }
      catch (RuntimeException localRuntimeException)
      {
        for (;;)
        {
          Log.w("Velvet.RlzHelper", "Could not get RLZ: ", localRuntimeException);
          Object localObject2 = null;
          if (localCursor != null)
          {
            localCursor.close();
            localObject2 = null;
          }
        }
      }
      finally
      {
        if (localCursor == null) {
          break;
        }
        localCursor.close();
      }
      this.mRlz = localObject2;
      return;
    }
  }
  
  public String getRlzForSearch()
  {
    this.mBgThread.execute(this.mGetRlzTask);
    return this.mRlz;
  }
  
  public void onActivityStart()
  {
    if (!this.mContentObserverRegistered)
    {
      this.mRlzObserver = new ContentObserver(new Handler())
      {
        public void onChange(boolean paramAnonymousBoolean)
        {
          RlzHelper.this.mBgThread.execute(RlzHelper.this.mPeekRlzTask);
        }
      };
      this.mContext.getContentResolver().registerContentObserver(this.mBaseUri, false, this.mRlzObserver);
      this.mContentObserverRegistered = true;
    }
  }
  
  public void onActivityStop()
  {
    if (this.mContentObserverRegistered)
    {
      this.mContext.getContentResolver().unregisterContentObserver(this.mRlzObserver);
      this.mContentObserverRegistered = false;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.RlzHelper
 * JD-Core Version:    0.7.0.1
 */