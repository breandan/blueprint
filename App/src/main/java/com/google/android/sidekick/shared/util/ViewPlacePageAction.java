package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.shared.util.IntentUtils;

public class ViewPlacePageAction
  implements EntryAction
{
  private final ActivityHelper mActivityHelper;
  private final long mCid;
  private final Context mContext;
  private final IntentUtils mIntentUtils;
  
  public ViewPlacePageAction(Context paramContext, long paramLong, IntentUtils paramIntentUtils, ActivityHelper paramActivityHelper)
  {
    this.mContext = paramContext;
    this.mCid = paramLong;
    this.mIntentUtils = paramIntentUtils;
    this.mActivityHelper = paramActivityHelper;
  }
  
  Uri buildPlacePageUri()
  {
    return Uri.parse("http://maps.google.com/maps/place?cid=" + this.mCid);
  }
  
  Intent getPlacePageIntent()
  {
    Uri localUri = buildPlacePageUri();
    Intent localIntent = new Intent("android.intent.action.VIEW", localUri).setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
    if (!this.mIntentUtils.isIntentHandled(this.mContext, localIntent)) {
      localIntent = new Intent("android.intent.action.VIEW", localUri);
    }
    return localIntent;
  }
  
  public void run()
  {
    this.mActivityHelper.safeStartActivity(this.mContext, getPlacePageIntent());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.ViewPlacePageAction
 * JD-Core Version:    0.7.0.1
 */