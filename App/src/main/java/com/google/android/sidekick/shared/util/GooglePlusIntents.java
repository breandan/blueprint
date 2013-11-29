package com.google.android.sidekick.shared.util;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.widget.Toast;
import com.google.android.shared.util.IntentStarter.ResultCallback;
import com.google.android.shared.util.IntentUtils;
import com.google.common.base.Optional;
import java.util.List;
import java.util.concurrent.Executor;

public class GooglePlusIntents
{
  static final String PLUS_ONE_PACKAGE = "com.google.android.apps.plus";
  private static final String TAG = Tag.getTag(GooglePlusIntents.class);
  
  public static boolean canSendBirthdayIntent(Context paramContext, IntentUtils paramIntentUtils)
  {
    return paramIntentUtils.isIntentHandled(paramContext, createBirthdayIntentWithNoExtras());
  }
  
  public static boolean canSendManageLocationSharingIntent(Context paramContext)
  {
    int i = 1;
    Intent localIntent = createManageLocationSharingIntentWithNoExtras();
    PackageManager localPackageManager = paramContext.getPackageManager();
    List localList = localPackageManager.queryIntentActivities(localIntent, 0);
    if (localList.size() != i) {
      return false;
    }
    ResolveInfo localResolveInfo = (ResolveInfo)localList.get(0);
    if (localPackageManager.checkSignatures(paramContext.getPackageName(), localResolveInfo.activityInfo.applicationInfo.packageName) == 0) {}
    for (;;)
    {
      return i;
      int j = 0;
    }
  }
  
  private static Intent createBirthdayIntentWithNoExtras()
  {
    return new Intent("com.google.android.apps.plus.GOOGLE_BIRTHDAY_POST");
  }
  
  private static Intent createManageLocationSharingIntentWithNoExtras()
  {
    Intent localIntent = new Intent("com.google.android.apps.plus.LOCATION_PLUS_SETTINGS");
    localIntent.addCategory("android.intent.category.DEFAULT");
    localIntent.setPackage("com.google.android.apps.plus");
    return localIntent;
  }
  
  public static Intent getBirthdayIntent(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    Intent localIntent = createBirthdayIntentWithNoExtras();
    localIntent.putExtra("com.google.android.apps.plus.SENDER_ID", paramString1);
    localIntent.putExtra("com.google.android.apps.plus.RECIPIENT_ID", paramString2);
    localIntent.putExtra("com.google.android.apps.plus.RECIPIENT_NAME", paramString3);
    localIntent.putExtra("android.intent.extra.TEXT", paramString4);
    localIntent.putExtra("com.google.android.apps.plus.BIRTHDAY_YEAR", 2012);
    return localIntent;
  }
  
  public static Intent getManageLocationSharingIntent(Account paramAccount)
  {
    Intent localIntent = createManageLocationSharingIntentWithNoExtras();
    localIntent.putExtra("account", paramAccount);
    localIntent.putExtra("version", 1);
    return localIntent;
  }
  
  public static final class ToastGooglePlusError
    implements IntentStarter.ResultCallback
  {
    private final Context mContext;
    private final Optional<IntentStarter.ResultCallback> mDelegate;
    private final Executor mUiThreadExecutor;
    
    public ToastGooglePlusError(Context paramContext, Executor paramExecutor)
    {
      this(paramContext, paramExecutor, Optional.absent());
    }
    
    public ToastGooglePlusError(Context paramContext, Executor paramExecutor, Optional<IntentStarter.ResultCallback> paramOptional)
    {
      this.mContext = paramContext;
      this.mUiThreadExecutor = paramExecutor;
      this.mDelegate = paramOptional;
    }
    
    private void showToastOnUiThread(final Context paramContext, final int paramInt)
    {
      this.mUiThreadExecutor.execute(new Runnable()
      {
        public void run()
        {
          Toast.makeText(paramContext, paramInt, 1).show();
        }
      });
    }
    
    public void onResult(int paramInt, Intent paramIntent, Context paramContext)
    {
      switch (paramInt)
      {
      default: 
        Log.w(GooglePlusIntents.TAG, "Google+ location settings Intent failed: " + paramInt);
      }
      for (;;)
      {
        if (this.mDelegate.isPresent()) {
          ((IntentStarter.ResultCallback)this.mDelegate.get()).onResult(paramInt, paramIntent, paramContext);
        }
        return;
        showToastOnUiThread(this.mContext, 2131362206);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.GooglePlusIntents
 * JD-Core Version:    0.7.0.1
 */