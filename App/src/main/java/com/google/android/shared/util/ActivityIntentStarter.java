package com.google.android.shared.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ActivityIntentStarter
  implements IntentStarter
{
  static final String KEY_CALLBACKS = "velvet:activity_intent_starter:callbacks";
  private final Activity mActivity;
  final Map<Integer, IntentStarter.ResultCallback> mCallbacks;
  private final AtomicInteger mNextRequestCode;
  
  public ActivityIntentStarter(Activity paramActivity, int paramInt)
  {
    this.mActivity = paramActivity;
    this.mNextRequestCode = new AtomicInteger(paramInt);
    this.mCallbacks = Maps.newHashMap();
  }
  
  protected void logSecurityException(Intent paramIntent, SecurityException paramSecurityException)
  {
    Log.e("IntentStarter", "Cannot start activity: " + paramIntent);
  }
  
  public void onActivityResultDelegate(int paramInt1, int paramInt2, Intent paramIntent)
  {
    IntentStarter.ResultCallback localResultCallback = (IntentStarter.ResultCallback)this.mCallbacks.remove(Integer.valueOf(paramInt1));
    if (localResultCallback == null)
    {
      Log.w("IntentStarter", "Got result callback with request code: " + paramInt1 + " with no callback in this object, could belong to someone else");
      return;
    }
    localResultCallback.onResult(paramInt2, paramIntent, this.mActivity);
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    Bundle localBundle = new Bundle();
    Iterator localIterator = this.mCallbacks.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      IntentStarter.ResultCallback localResultCallback = (IntentStarter.ResultCallback)localEntry.getValue();
      if ((localResultCallback instanceof Parcelable)) {
        localBundle.putParcelable(String.valueOf(localEntry.getKey()), (Parcelable)localResultCallback);
      }
    }
    if (!localBundle.isEmpty()) {
      paramBundle.putBundle("velvet:activity_intent_starter:callbacks", localBundle);
    }
  }
  
  public boolean resolveIntent(Intent paramIntent)
  {
    return this.mActivity.getPackageManager().resolveActivity(paramIntent, 65536) != null;
  }
  
  public void restoreInstanceState(Bundle paramBundle)
  {
    if ((paramBundle != null) && (paramBundle.containsKey("velvet:activity_intent_starter:callbacks")))
    {
      Bundle localBundle = paramBundle.getBundle("velvet:activity_intent_starter:callbacks");
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        this.mCallbacks.put(Integer.valueOf(str), (IntentStarter.ResultCallback)localBundle.getParcelable(str));
      }
    }
  }
  
  public boolean startActivity(Intent... paramVarArgs)
  {
    int i = paramVarArgs.length;
    int j = 0;
    for (;;)
    {
      if (j < i)
      {
        Intent localIntent = paramVarArgs[j];
        try
        {
          this.mActivity.startActivity(localIntent);
          return true;
        }
        catch (ActivityNotFoundException localActivityNotFoundException)
        {
          Log.i("IntentStarter", "No activity found for " + localIntent);
          j++;
        }
        catch (SecurityException localSecurityException)
        {
          for (;;)
          {
            logSecurityException(localIntent, localSecurityException);
          }
        }
      }
    }
    Log.e("IntentStarter", "No activity found for any of the " + paramVarArgs.length + " intents");
    return false;
  }
  
  public boolean startActivityForResult(Intent paramIntent, IntentStarter.ResultCallback paramResultCallback)
  {
    try
    {
      Preconditions.checkNotNull(paramResultCallback);
      int i = this.mNextRequestCode.getAndIncrement();
      this.mCallbacks.put(Integer.valueOf(i), paramResultCallback);
      this.mActivity.startActivityForResult(paramIntent, i);
      return true;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      Log.e("IntentStarter", "No activity found for " + paramIntent);
      return false;
    }
    catch (SecurityException localSecurityException)
    {
      logSecurityException(paramIntent, localSecurityException);
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.ActivityIntentStarter
 * JD-Core Version:    0.7.0.1
 */