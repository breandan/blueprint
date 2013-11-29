package com.google.android.sidekick.shared.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import java.util.concurrent.Executor;

public class ActivityHelper
{
  private static final String TAG = Tag.getTag(ActivityHelper.class);
  private final Executor mUiThreadExecutor;
  
  public ActivityHelper(Executor paramExecutor)
  {
    this.mUiThreadExecutor = paramExecutor;
  }
  
  private void showToastOnUiThread(final Context paramContext, final int paramInt)
  {
    this.mUiThreadExecutor.execute(new Runnable()
    {
      public void run()
      {
        Toast.makeText(paramContext, paramInt, 0).show();
      }
    });
  }
  
  public boolean safeStartActivity(Context paramContext, Intent paramIntent)
  {
    return safeStartActivityWithMessage(paramContext, paramIntent, 2131363306);
  }
  
  public boolean safeStartActivityWithMessage(Context paramContext, Intent paramIntent, int paramInt)
  {
    try
    {
      paramContext.startActivity(paramIntent);
      return true;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      Log.w(TAG, "no handler for intent: " + paramIntent, localActivityNotFoundException);
      showToastOnUiThread(paramContext, paramInt);
    }
    return false;
  }
  
  public boolean safeViewUri(Context paramContext, Uri paramUri, boolean paramBoolean)
  {
    return safeViewUriWithMessage(paramContext, paramUri, paramBoolean, 2131363215);
  }
  
  public boolean safeViewUriWithMessage(Context paramContext, Uri paramUri, boolean paramBoolean, int paramInt)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", paramUri);
    if (paramBoolean) {
      localIntent.setFlags(268435456);
    }
    return safeStartActivityWithMessage(paramContext, localIntent, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.ActivityHelper
 * JD-Core Version:    0.7.0.1
 */