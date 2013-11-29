package com.google.android.sidekick.main.contextprovider;

import android.os.Bundle;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.HelpContext;
import com.google.android.velvet.Help;

public class HelpContextProvider
{
  private final Help mHelp;
  
  public HelpContextProvider(Help paramHelp)
  {
    this.mHelp = paramHelp;
  }
  
  public void addHelpUri(CardRenderingContext paramCardRenderingContext, String paramString)
  {
    ((Bundle)paramCardRenderingContext.putSpecificRenderingContextIfAbsent(HelpContext.BUNDLE_KEY, new Bundle())).putParcelable(paramString, this.mHelp.getHelpUrl(paramString));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.HelpContextProvider
 * JD-Core Version:    0.7.0.1
 */