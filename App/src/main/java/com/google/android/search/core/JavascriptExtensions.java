package com.google.android.search.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.shared.util.SimpleIntentStarter;
import com.google.android.velvet.ui.InAppWebPageActivity;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.util.List;
import javax.annotation.Nullable;

public final class JavascriptExtensions
  implements AgsaExtJavascriptInterface
{
  private static final boolean DBG = false;
  private static final String TAG = "Velvet.JavascriptExtensions";
  private final Context mApplicationContext;
  private final SimpleIntentStarter mIntentStarter;
  private final PackageManager mPackageManager;
  @Nullable
  private final PageEventListener mPageEventListener;
  private final SearchUrlHelper mSearchUrlHelper;
  private final TrustPolicy mTrustPolicy;
  
  public JavascriptExtensions(Context paramContext, SimpleIntentStarter paramSimpleIntentStarter, SearchUrlHelper paramSearchUrlHelper, TrustPolicy paramTrustPolicy)
  {
    this.mApplicationContext = ((Context)Preconditions.checkNotNull(paramContext));
    this.mIntentStarter = ((SimpleIntentStarter)Preconditions.checkNotNull(paramSimpleIntentStarter));
    this.mTrustPolicy = ((TrustPolicy)Preconditions.checkNotNull(paramTrustPolicy));
    this.mSearchUrlHelper = ((SearchUrlHelper)Preconditions.checkNotNull(paramSearchUrlHelper));
    this.mPackageManager = ((PackageManager)Preconditions.checkNotNull(paramContext.getPackageManager()));
    this.mPageEventListener = new DefaultPageEventListener(null);
  }
  
  public JavascriptExtensions(Context paramContext, SimpleIntentStarter paramSimpleIntentStarter, SearchUrlHelper paramSearchUrlHelper, TrustPolicy paramTrustPolicy, PageEventListener paramPageEventListener)
  {
    this.mApplicationContext = ((Context)Preconditions.checkNotNull(paramContext));
    this.mIntentStarter = ((SimpleIntentStarter)Preconditions.checkNotNull(paramSimpleIntentStarter));
    this.mTrustPolicy = ((TrustPolicy)Preconditions.checkNotNull(paramTrustPolicy));
    this.mSearchUrlHelper = ((SearchUrlHelper)Preconditions.checkNotNull(paramSearchUrlHelper));
    this.mPackageManager = ((PackageManager)Preconditions.checkNotNull(paramContext.getPackageManager()));
    this.mPageEventListener = ((PageEventListener)Preconditions.checkNotNull(paramPageEventListener));
  }
  
  private Intent createAppLaunchIntent(ComponentName paramComponentName)
  {
    return new Intent("android.intent.action.MAIN").setFlags(268435456).addCategory("android.intent.category.LAUNCHER").setComponent(paramComponentName);
  }
  
  private Intent createAppLaunchIntent(String paramString)
  {
    return new Intent("android.intent.action.MAIN").setFlags(268435456).addCategory("android.intent.category.LAUNCHER").setPackage(paramString);
  }
  
  private Intent createPackageSpecificUriIntent(String paramString1, String paramString2)
  {
    return new Intent("android.intent.action.VIEW", Uri.parse(paramString1)).setPackage(paramString2).addCategory("android.intent.category.BROWSABLE");
  }
  
  private Intent getResolvedAppLaunchIntent(String paramString)
  {
    ComponentName localComponentName = ComponentName.unflattenFromString(paramString);
    if (localComponentName != null) {
      return getResolvedAppLaunchIntentForComponentName(localComponentName);
    }
    return getResolvedAppLaunchIntentForPackageName(paramString);
  }
  
  private Intent getResolvedAppLaunchIntentForComponentName(ComponentName paramComponentName)
  {
    Intent localIntent = createAppLaunchIntent(paramComponentName);
    if (this.mPackageManager.queryIntentActivities(localIntent, 0).isEmpty()) {
      localIntent = null;
    }
    return localIntent;
  }
  
  private Intent getResolvedAppLaunchIntentForPackageName(String paramString)
  {
    Intent localIntent1 = createAppLaunchIntent(paramString);
    Intent localIntent2 = createAppLaunchIntent(paramString).addCategory("android.intent.category.DEFAULT");
    List localList = this.mPackageManager.queryIntentActivityOptions(null, new Intent[] { localIntent2 }, localIntent1, 0);
    if (localList.isEmpty()) {
      return null;
    }
    ResolveInfo localResolveInfo = (ResolveInfo)localList.get(0);
    return createAppLaunchIntent(new ComponentName(localResolveInfo.activityInfo.packageName, localResolveInfo.activityInfo.name));
  }
  
  public static TrustPolicy permissiveTrustPolicy()
  {
    new TrustPolicy()
    {
      public boolean isTrusted()
      {
        return true;
      }
    };
  }
  
  public static TrustPolicy searchResultsTrustPolicy(SearchUrlHelper paramSearchUrlHelper)
  {
    new TrustPolicy()
    {
      public boolean isTrusted()
      {
        return this.val$searchUrlHelper.isSearchDomainSchemeSecure();
      }
    };
  }
  
  @JavascriptInterface
  public void addInAppUrlPattern(String paramString)
  {
    try
    {
      if (this.mPageEventListener != null) {
        this.mPageEventListener.addInAppUrlPattern(paramString);
      }
      return;
    }
    catch (Throwable localThrowable)
    {
      Throwables.propagate(localThrowable);
    }
  }
  
  @JavascriptInterface
  public void addOptionsMenuItem(String paramString1, int paramInt, String paramString2, boolean paramBoolean)
  {
    try
    {
      if (this.mPageEventListener != null) {
        this.mPageEventListener.addOptionsMenuItem(paramString1, paramInt, paramString2, paramBoolean);
      }
      return;
    }
    catch (Throwable localThrowable)
    {
      Throwables.propagate(localThrowable);
    }
  }
  
  @JavascriptInterface
  public boolean canLaunchApp(String paramString)
  {
    try
    {
      if (!this.mTrustPolicy.isTrusted()) {
        return false;
      }
      Intent localIntent = getResolvedAppLaunchIntent(paramString);
      if (localIntent != null) {
        return true;
      }
    }
    catch (Throwable localThrowable)
    {
      throw Throwables.propagate(localThrowable);
    }
    return false;
  }
  
  @JavascriptInterface
  public boolean canUriBeHandledByPackage(String paramString1, String paramString2)
  {
    try
    {
      if (!this.mTrustPolicy.isTrusted()) {
        return false;
      }
      Intent localIntent = createPackageSpecificUriIntent(paramString1, paramString2);
      boolean bool1 = this.mPackageManager.queryIntentActivities(localIntent, 0).isEmpty();
      boolean bool2 = false;
      if (!bool1) {
        bool2 = true;
      }
      return bool2;
    }
    catch (Throwable localThrowable)
    {
      throw Throwables.propagate(localThrowable);
    }
  }
  
  @JavascriptInterface
  public void delayedPageLoad()
  {
    try
    {
      if (this.mPageEventListener != null) {
        this.mPageEventListener.delayedPageLoad();
      }
      return;
    }
    catch (Throwable localThrowable)
    {
      throw Throwables.propagate(localThrowable);
    }
  }
  
  @JavascriptInterface
  public boolean isTrusted()
  {
    try
    {
      boolean bool = this.mTrustPolicy.isTrusted();
      return bool;
    }
    catch (Throwable localThrowable)
    {
      throw Throwables.propagate(localThrowable);
    }
  }
  
  @JavascriptInterface
  public boolean launchApp(String paramString)
  {
    try
    {
      if (!this.mTrustPolicy.isTrusted()) {
        return false;
      }
      Intent localIntent = getResolvedAppLaunchIntent(paramString);
      if (localIntent != null)
      {
        boolean bool = this.mIntentStarter.startActivity(new Intent[] { localIntent });
        return bool;
      }
    }
    catch (Throwable localThrowable)
    {
      throw Throwables.propagate(localThrowable);
    }
    return false;
  }
  
  @JavascriptInterface
  public boolean openInApp(String paramString)
  {
    try
    {
      Uri localUri = Uri.parse(paramString);
      if (!this.mTrustPolicy.isTrusted()) {
        return false;
      }
      if (this.mSearchUrlHelper.isSecureGoogleUri(localUri))
      {
        this.mPageEventListener.loadUriInApp(localUri);
        return true;
      }
    }
    catch (Throwable localThrowable)
    {
      throw Throwables.propagate(localThrowable);
    }
    return false;
  }
  
  @JavascriptInterface
  public boolean openWithPackage(String paramString1, String paramString2)
  {
    try
    {
      if (!this.mTrustPolicy.isTrusted()) {
        return false;
      }
      Intent localIntent = createPackageSpecificUriIntent(paramString1, paramString2);
      boolean bool = this.mIntentStarter.startActivity(new Intent[] { localIntent });
      return bool;
    }
    catch (Throwable localThrowable)
    {
      throw Throwables.propagate(localThrowable);
    }
  }
  
  @JavascriptInterface
  public void pageReady()
  {
    try
    {
      if (this.mPageEventListener != null) {
        this.mPageEventListener.pageReady();
      }
      return;
    }
    catch (Throwable localThrowable)
    {
      throw Throwables.propagate(localThrowable);
    }
  }
  
  @JavascriptInterface
  public void prefetch(String paramString) {}
  
  private class DefaultPageEventListener
    implements JavascriptExtensions.PageEventListener
  {
    private DefaultPageEventListener() {}
    
    public void addInAppUrlPattern(String paramString) {}
    
    public void addOptionsMenuItem(String paramString1, int paramInt, String paramString2, boolean paramBoolean) {}
    
    public void delayedPageLoad() {}
    
    public void loadUriInApp(Uri paramUri)
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", paramUri).setClass(JavascriptExtensions.this.mApplicationContext, InAppWebPageActivity.class);
      JavascriptExtensions.this.mIntentStarter.startActivity(new Intent[] { localIntent });
    }
    
    public void pageReady() {}
  }
  
  public static abstract interface PageEventListener
  {
    public abstract void addInAppUrlPattern(String paramString);
    
    public abstract void addOptionsMenuItem(String paramString1, int paramInt, String paramString2, boolean paramBoolean);
    
    public abstract void delayedPageLoad();
    
    public abstract void loadUriInApp(Uri paramUri);
    
    public abstract void pageReady();
  }
  
  public static abstract interface TrustPolicy
  {
    public abstract boolean isTrusted();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.JavascriptExtensions
 * JD-Core Version:    0.7.0.1
 */