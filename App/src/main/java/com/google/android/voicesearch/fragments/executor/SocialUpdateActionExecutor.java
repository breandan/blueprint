package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.shared.util.IntentStarter;
import com.google.android.voicesearch.fragments.action.SocialUpdateAction;
import com.google.android.voicesearch.fragments.action.SocialUpdateAction.SocialNetwork;
import javax.annotation.Nullable;

public class SocialUpdateActionExecutor
  extends IntentActionExecutor<SocialUpdateAction>
{
  public SocialUpdateActionExecutor(IntentStarter paramIntentStarter)
  {
    super(paramIntentStarter);
  }
  
  private Intent createPlayStoreIntent(String paramString)
  {
    return new Intent("android.intent.action.VIEW", Uri.parse(String.format("http://play.google.com/store/apps/details?id=%s", new Object[] { paramString })));
  }
  
  private Intent createPostIntent(String paramString1, @Nullable String paramString2)
  {
    Intent localIntent = new Intent().setAction("android.intent.action.SEND").setPackage(paramString1).setType("text/plain");
    if (!TextUtils.isEmpty(paramString2)) {
      localIntent.putExtra("android.intent.extra.TEXT", paramString2);
    }
    return localIntent;
  }
  
  protected Intent[] getExecuteIntents(SocialUpdateAction paramSocialUpdateAction)
  {
    return null;
  }
  
  protected Intent[] getOpenExternalAppIntents(SocialUpdateAction paramSocialUpdateAction)
  {
    String str = paramSocialUpdateAction.getSocialNetwork().getPkg();
    Intent[] arrayOfIntent = new Intent[2];
    arrayOfIntent[0] = createPostIntent(str, paramSocialUpdateAction.getMessage());
    arrayOfIntent[1] = createPlayStoreIntent(str);
    return arrayOfIntent;
  }
  
  protected Intent[] getProberIntents(SocialUpdateAction paramSocialUpdateAction)
  {
    Intent[] arrayOfIntent = new Intent[1];
    arrayOfIntent[0] = createPostIntent(paramSocialUpdateAction.getSocialNetwork().getPkg(), null);
    return arrayOfIntent;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.SocialUpdateActionExecutor
 * JD-Core Version:    0.7.0.1
 */