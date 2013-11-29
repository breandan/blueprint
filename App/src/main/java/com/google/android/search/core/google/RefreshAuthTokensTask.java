package com.google.android.search.core.google;

import android.text.TextUtils;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;

public class RefreshAuthTokensTask
  implements Callable<Void>
{
  private final SearchConfig mConfig;
  private final LoginHelper mLoginHelper;
  
  public RefreshAuthTokensTask(LoginHelper paramLoginHelper, SearchConfig paramSearchConfig)
  {
    this.mLoginHelper = paramLoginHelper;
    this.mConfig = paramSearchConfig;
  }
  
  private Collection<String> getUsedTokenTypes()
  {
    HashSet localHashSet = Sets.newHashSet();
    String str = this.mConfig.getTextSearchTokenType();
    if (!TextUtils.isEmpty(str)) {
      localHashSet.add(str);
    }
    localHashSet.add(this.mConfig.getVoiceSearchTokenType());
    localHashSet.add("oauth2:https://www.googleapis.com/auth/googlenow");
    localHashSet.add("mobilepersonalfeeds");
    return localHashSet;
  }
  
  public Void call()
  {
    Iterator localIterator = getUsedTokenTypes().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      Collection localCollection = this.mLoginHelper.blockingGetAllTokens(str, 5000L);
      this.mLoginHelper.invalidateTokens(localCollection);
      this.mLoginHelper.blockingGetAllTokens(str, 5000L);
    }
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.RefreshAuthTokensTask
 * JD-Core Version:    0.7.0.1
 */